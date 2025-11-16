package rvsim

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalatest.BeforeAndAfterAll
import java.nio.file.{Files, Paths}
import Types._

import scala.jdk.CollectionConverters._


// Include testsuite from https://github.com/TheAIBot/RISC-V_Sim/tree/master/RISC-V_Sim/InstructionTests
class TheAIBotSpec extends AnyFunSpec with Matchers with BeforeAndAfterAll {
  // make resource paths and result collectors available to the whole suite
  val base = Paths.get("src/test/resources/theaitbot")
  val binDir = base.resolve("bin")
  val resDir = base.resolve("res")
  val asmDir = base.resolve("asm")

  // TODO: Enable printing asm

  // shared collections to track succeeded/failed tests
  // use the test's simple name (bin filename without extension)
  val failedTests = scala.collection.mutable.ListBuffer.empty[String]
  val succeededTests = scala.collection.mutable.ListBuffer.empty[String]

  describe("TheAIBot tests") {
    // collect bin files and close the stream to avoid resource leak
    val stream = Files.list(binDir)
    val binFiles =
      try {
        stream
          .iterator()
          .asScala
          .toList
          .filter(p => p.toString.endsWith(".bin"))
          .sortBy(_.getFileName.toString)
      } finally {
        stream.close()
      }

    // create one test per bin
    for (binPath <- binFiles) {
      val name = binPath.getFileName.toString.stripSuffix(".bin")
      it(s"$name should match expected registers") {
        info(s"Running test: $name")

        // Read program bytes
        val program = Files.readAllBytes(binPath)

        // Run VM
        val vm = new VM()
        vm.loadProgram(program)
        vm.run()

        // Read expected registers (.res contains 32 little-endian u32 values)
        val resPath = resDir.resolve(s"$name.res")
        if (!Files.exists(resPath)) {
          fail(s"Missing .res file for $name at $resPath")
        }
        val resBytes = Files.readAllBytes(resPath)
        if (resBytes.length != 32 * 4) {
          fail(s"Unexpected .res length for $name: ${resBytes.length}")
        }
        val expected: IndexedSeq[UINT_32] = (0 until 32).map { i =>
          val offset = i * 4
          val v = (resBytes(offset) & 0xff) | ((resBytes(
            offset + 1
          ) & 0xff) << 8) | ((resBytes(offset + 2) & 0xff) << 16) | ((resBytes(
            offset + 3
          ) & 0xff) << 24)
          UINT_32(v & 0xffffffffL)
        }

        // Compare registers but collect mismatches so we can record success/failure
        val regs = vm.getRegisters()
        val regList = Reg.values.toList
        val mismatches = scala.collection.mutable.ListBuffer.empty[String]
        for (i <- 0 until 32) {
          val reg = regList(i)
          val regName = reg.toString
          val actual = regs(reg).toLong()
          val expect = expected(i).toLong()
          if (actual != expect) {
            mismatches += s"register $regName expected=0x${expect.toHexString} actual=0x${actual.toHexString}"
          }
        }

        if (mismatches.nonEmpty) {
          // record failed test name and fail with collected clues
          failedTests.synchronized { failedTests += name }
          val msg =
            (s"$name: ${mismatches.size} register(s) mismatched:\n" + mismatches
              .mkString("\n")).trim
          fail(msg)
        } else {
          succeededTests.synchronized { succeededTests += name }
        }
      }
    }

  }

  override protected def afterAll(): Unit = {
    // Analyze asm files for failed vs succeeded tests and print common failed instructions
    try {
      val asmBase = asmDir

      def findAsmPath(testName: String): Option[java.nio.file.Path] = {
        val exact = asmBase.resolve(testName + ".s")
        if (Files.exists(exact)) Some(exact)
        else {
          // try any file that starts with the testName
          val stream = Files.list(asmBase)
          try {
            stream
              .iterator()
              .asScala
              .find(p => p.getFileName.toString.startsWith(testName + "."))
          } finally stream.close()
        }
      }

      def extractInstructions(path: java.nio.file.Path): Set[String] = {
        val lines = Files.readAllLines(path).asScala.toList
        val instrs = lines
          .map(_.trim)
          .map { l =>
            // drop everything starting with a letter
            l.takeWhile(c => c.isLetter || c == '_')
              // drop everything with a ':' (labels)
              .dropWhile(c => c == ':')
              .trim
          }
          .filter(l =>
            l.nonEmpty && !l.startsWith(".") && !l.contains("{") && !l.contains(
              "}"
            ) && !l.endsWith(";")
          )
        instrs.toSet
      }

      // gather instruction presence per failed test
      val failedInstrCounts =
        scala.collection.mutable.Map.empty[String, Int].withDefaultValue(0)
      val succeededInstrs = scala.collection.mutable.Set.empty[String]

      // process succeeded tests first
      for (s <- succeededTests) {
        findAsmPath(s) match {
          case Some(p) => succeededInstrs ++= extractInstructions(p)
          case None    => // ignore
        }
      }

      // for failed tests increment per-test presence (count tests that contain instr)
      for (f <- failedTests) {
        findAsmPath(f) match {
          case Some(p) =>
            val instrs = extractInstructions(p)
            for (instr <- instrs) {
              failedInstrCounts(instr) = failedInstrCounts(instr) + 1
            }
          case None => // ignore
        }
      }

      // find instructions that appear in failed tests and not in any succeeded test
      val commonFailed = failedInstrCounts.toList
        .filter { case (instr, count) =>
          !succeededInstrs.contains(instr)
        }
        .sortBy(-_._2)

      if (commonFailed.nonEmpty) {
        println(
          "\n==== ASM Analysis: common instructions in FAILED tests (not seen in succeeded tests) ===="
        )
        for ((instr, count) <- commonFailed) {
          // find which tests include this instruction
          val testsWith = failedTests.filter { f =>
            findAsmPath(f).exists(p => extractInstructions(p).contains(instr))
          }
          println(s"[$count tests] $instr")
          println(s"  tests: ${testsWith.mkString(", ")}")
        }
        println("==== end ASM Analysis ====")
      } else {
        println(
          "\n==== ASM Analysis: no common failed instructions found===="
        )
      }
    } finally {
      super.afterAll()
    }
  }

}

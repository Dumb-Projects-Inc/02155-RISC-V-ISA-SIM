package rvsim

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import java.nio.file.{Files, Paths}
import Types._

import scala.jdk.CollectionConverters._

class CAESSpec extends AnyFunSpec with Matchers {
  describe("CAE tests") {
    val base = Paths.get("src/test/resources/cae")
    val binDir = base.resolve("bin")
    val resDir = base.resolve("res")
    val asmDir = base.resolve("asm")

    // TODO: Enable printing asm

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

        // Compare registers
        val regs = vm.getRegisters()
        val regList = Reg.values.toList
        for (i <- 0 until 32) {
          val reg = regList(i)
          val regName = reg.toString
          val actual = regs(reg).toLong()
          val expect = expected(i).toLong()
          withClue(s"$name: register $regName mismatch: ") {
            actual shouldEqual expect
          }
        }
      }
    }
  }

}

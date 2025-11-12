package rvsim

import rvsim.Types._
import java.nio.{ByteBuffer, ByteOrder}
import java.nio.file.{Files, Paths}

// Helper functions for debugging
object Debug {
  def printRegisters(regs: Registers, cols: Int = 4): Unit = {
    val names = Reg.values.toArray.map(_.toString.padTo(4, ' '))
    val values =
      Reg.values.toArray.map(r => f"0x${regs(r).toInt}%08x")

    val rows = Math.ceil(names.length.toDouble / cols).toInt
    for (r <- 0 until rows) {
      val line = (0 until cols)
        .flatMap { c =>
          val idx = r + c * rows
          if (idx < names.length)
            Some(f"${names(idx)}: ${values(idx)}")
          else None
        }
        .mkString(" | ")
      println(line)
    }
  }

  // Dump registers as a binary file (x0 to x31) for comparison with .res files
  def dumpRegisterToFile(regs: Registers): Unit = {
    val byteArray = Array.fill[UINT_32](32)(UINT_32.zero)
    for (i <- 0 until 32) {
      val reg = Reg(i)
      byteArray(i) = regs(reg)
    }
    val byteBuffer = java.nio.ByteBuffer.allocate(32 * 4)
    for (regValue <- byteArray) {
      byteBuffer.putInt(regValue.toInt())
    }
    // Ensure little-endian output by creating a little-endian buffer and writing ints into it,
    // then write the raw bytes directly to the file.
    val binBuffer = ByteBuffer.allocate(32 * 4).order(ByteOrder.LITTLE_ENDIAN)
    for (regValue <- byteArray) {
      binBuffer.putInt(regValue.toInt)
    }
    Files.write(Paths.get("registers_dump.res"), binBuffer.array())
  }
}

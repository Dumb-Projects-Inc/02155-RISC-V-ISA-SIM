package example

import java.nio.file.{Files, Paths}

object Types {
  type INT_8 = Byte
  type INT_16 = Short
  type INT_32 = Int
  type INT_64 = Long
}
import Types._

object Reg extends Enumeration {
  type Reg = Value
  val ZERO, RA, SP, GP, TP, T0, T1, T2, S0, S1, A0, A1, A2, A3, A4, A5, A6, A7,
      S2, S3, S4, S5, S6, S7, S8, S9, S10, S11, T3, T4, T5, T6 = Value
}

class Registers() {
  private val regs: Array[INT_32] = Array.ofDim[INT_32](32)

  // Overload access operators
  def apply(reg: Reg.Value): INT_32 = regs(reg.id)
  def update(reg: Reg.Value, value: INT_32): Unit = {
    if (reg != Reg.ZERO) regs(reg.id) = value
  }
}

class Memory() {
  private val memory: Array[INT_8] =
    Array.ofDim[INT_8](1e6) // 1MB of memory allocated

  def read(addr: INT_32): INT_32 = {
    memory(wordIndex(addr))
  }

  def write(addr: INT_32, value: INT_32): Unit = {
    memory(wordIndex(addr)) = value
  }
  def load_program(bin_path: String): Unit = {
    // load file
    val bin_bytes: Array[Int] =
      Files.readAllBytes(Paths.get(bin_path)).map(_ & 0xff)
    // load into 32-bit little-endian
    val word = bin_bytes.grouped(4).toArray
    val little_endian = word.map { bytes =>
      (bytes(0) & 0xff) |
        ((bytes(1) & 0xff) << 8) |
        ((bytes(2) & 0xff) << 16) |
        ((bytes(3) & 0xff) << 24)
    }
    // write to memory
    Array.copy(little_endian, 0, memory, 0, little_endian.length)

  }
}

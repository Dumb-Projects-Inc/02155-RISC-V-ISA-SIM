package rvsim

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

class Memory(mem_size: Int = 1e6.toInt) {
  private def to_little_endian(in: INT_32): Array[INT_8] = {
    Array(
      (in & 0xff).toByte,
      ((in >> 8) & 0xff).toByte,
      ((in >> 16) & 0xff).toByte,
      ((in >> 24) & 0xff).toByte
    )
  }
  private def from_little_endian(bytes: Array[INT_8]): INT_32 = {
    (bytes(0) & 0xff) |
      ((bytes(1) & 0xff) << 8) |
      ((bytes(2) & 0xff) << 16) |
      ((bytes(3) & 0xff) << 24)
  }

  private val memory: Array[INT_8] =
    Array.ofDim[INT_8](mem_size) // 1MB of memory allocated

  def readByte(addr: INT_32): INT_8 = memory(addr)
  def writeByte(addr: INT_32, value: INT_8): Unit = memory(addr) = value

  def checkWordAligned(addr: INT_32): Unit = {
    if (addr % 4 != 0)
      throw new Exception(
        s"Unaligned memory access at address 0x${addr.toHexString}"
      )
  }

  def readWord(addr: INT_32): INT_32 = {
    checkWordAligned(addr)
    val bytes = Array(
      memory(addr),
      memory(addr + 1),
      memory(addr + 2),
      memory(addr + 3)
    )
    from_little_endian(bytes)
  }
  def writeWord(addr: INT_32, value: INT_32): Unit = {
    checkWordAligned(addr)
    // Little-endian
    val bytes = to_little_endian(value)
    memory(addr) = bytes(0)
    memory(addr + 1) = bytes(1)
    memory(addr + 2) = bytes(2)
    memory(addr + 3) = bytes(3)
  }
}

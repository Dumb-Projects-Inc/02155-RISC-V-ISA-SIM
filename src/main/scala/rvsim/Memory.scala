package rvsim

import Types._

object Reg extends Enumeration {
  type Reg = Value
  val ZERO, RA, SP, GP, TP, T0, T1, T2, S0, S1, A0, A1, A2, A3, A4, A5, A6, A7,
      S2, S3, S4, S5, S6, S7, S8, S9, S10, S11, T3, T4, T5, T6 = Value
}

class Registers() {
  private val regs: Array[UINT_32] = Array.fill(32)(UINT_32.zero)
  // Overload access operators
  def apply(reg: Reg.Value): UINT_32 = regs(reg.id)
  def update(reg: Reg.Value, value: UINT_32): Unit = {
    if (reg != Reg.ZERO) regs(reg.id) = value
  }
}

class Memory(mem_size: Int = 1e6.toInt) {
  private def to_little_endian(in: UINT_32): Array[INT_8] = {
    val inLong = in.toLong()
    Array(
      (inLong & 0xff).toByte,
      ((inLong >> 8) & 0xff).toByte,
      ((inLong >> 16) & 0xff).toByte,
      ((inLong >> 24) & 0xff).toByte
    )
  }
  private def from_little_endian(bytes: Array[INT_8]): UINT_32 = {
    ((bytes(0) & 0xff) |
      ((bytes(1) & 0xff) << 8) |
      ((bytes(2) & 0xff) << 16) |
      ((bytes(3) & 0xff) << 24)).u32
  }

  private val memory: Array[INT_8] =
    Array.ofDim[INT_8](mem_size) // 1MB of memory allocated

  private def idx(addr: UINT_32): Int =
    (addr.raw & 0xffffffffL).toInt // This cannot index full 32-bit space but scala arrays cannot either

  def readByte(addr: UINT_32): INT_8 = memory(idx(addr))
  def writeByte(addr: UINT_32, value: INT_8): Unit = memory(idx(addr)) = value

  def checkWordAligned(addr: UINT_32): Unit = {
    if (addr % 4 != 0)
      throw new Exception(
        s"Unaligned memory access at address 0x${addr.toHexString}"
      )
  }

  def readWord(addr: UINT_32): UINT_32 = {
    checkWordAligned(addr)
    val addrInt = idx(addr)
    val bytes = Array(
      memory(addrInt),
      memory(addrInt + 1),
      memory(addrInt + 2),
      memory(addrInt + 3)
    )
    from_little_endian(bytes)
  }
  def writeWord(addr: UINT_32, value: UINT_32): Unit = {
    checkWordAligned(addr)
    // Little-endian
    val bytes = to_little_endian(value)
    val addrInt = idx(addr)
    memory(addrInt) = bytes(0)
    memory(addrInt + 1) = bytes(1)
    memory(addrInt + 2) = bytes(2)
    memory(addrInt + 3) = bytes(3)
  }
}

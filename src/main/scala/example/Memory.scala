package example

object Types {
  type UINT_8 = Byte
  type UINT_16 = Short
  type UINT_32 = Int
  type UINT_64 = Long
}
import Types._

object Reg extends Enumeration {
  type Reg = Value
  val ZERO, RA, SP, GP, TP, T0, T1, T2, S0, S1, A0, A1, A2, A3, A4, A5, A6, A7,
      S2, S3, S4, S5, S6, S7, S8, S9, S10, S11, T3, T4, T5, T6 = Value
}
class Registers() {
  private val regs: Array[UINT_32] = Array.ofDim[UINT_32](32)

  // Overload access operators
  def apply(reg: Reg.Value): UINT_32 = regs(reg.id)
  def update(reg: Reg.Value, value: UINT_32): Unit = {
    if (reg != Reg.ZERO) regs(reg.id) = value
  }
}
class Memory() {
  private val memory: Array[UINT_32] = Array.ofDim[UINT_32](1 << 16)

  def read(addr: UINT_32): UINT_32 = memory((addr >> 2).toInt)
  def write(addr: UINT_32, value: UINT_32): Unit = {
    memory((addr >> 2).toInt) = value
  }
}

package example

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
}

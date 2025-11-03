package rvsim

object Fields {
  def rd(inst: Int): Int = (inst >> 7) & 0x1f
  def rs1(inst: Int): Int = (inst >> 15) & 0x1f
  def rs2(inst: Int): Int = (inst >> 20) & 0x1f
  def funct3(inst: Int): Int = (inst >> 12) & 0x7
  def funct7(inst: Int): Int = (inst >> 25) & 0x7f
  def opcode(inst: Int): Int = inst & 0x7f

  def decode(inst: Int): {}
}

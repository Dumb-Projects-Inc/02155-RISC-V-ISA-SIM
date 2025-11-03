package example

import Types._

sealed trait OP

object OP {
  case object LUI extends OP
  case object AUIPC extends OP
  case object ADDI extends OP
  case object SLTI extends OP
  case object SLTIU extends OP
  case object XORI extends OP
  case object ORI extends OP
  case object ANDI extends OP
  case object SLLI extends OP
  case object SRLI extends OP
  case object SRAI extends OP
  case object ADD extends OP
  case object SUB extends OP
  case object SLL extends OP
  case object SLT extends OP
  case object SLTU extends OP
  case object XOR extends OP
  case object SRL extends OP
  case object SRA extends OP
  case object OR extends OP
  case object AND extends OP
  // case object FENCE extends OP
  // case object FENCE_I extends OP
  // case object CSRRW extends OP
  // case object CSRRS extends OP
  // case object CSRRC extends OP
  // case object CSRRWI extends OP
  // case object CSRRSI extends OP
  // case object CSRRCI extends OP
  case object ECALL extends OP
  // case object EBREAK extends OP
  case object URET extends OP
  case object SRET extends OP
  case object MRET extends OP
  case object WFI extends OP
  // case object SFENCE_VMA extends OP
  case object LB extends OP
  case object LH extends OP
  case object LW extends OP
  case object LBU extends OP
  case object LHU extends OP
  case object SB extends OP
  case object SH extends OP
  case object SW extends OP
  case object JAL extends OP
  case object JALR extends OP
  case object BEQ extends OP
  case object BNE extends OP
  case object BLT extends OP
  case object BGE extends OP
  case object BLTU extends OP
  case object BGEU extends OP
}

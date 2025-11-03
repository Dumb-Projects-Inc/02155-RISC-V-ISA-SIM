package example

import Types._

sealed trait OP

case class LUI(rd: Reg.value, imm: Int) extends OP
case class AUIPC extends OP
case class ADDI extends OP
case class SLTI extends OP
case class SLTIU extends OP
case class XORI extends OP
case class ORI extends OP
case class ANDI extends OP
case class SLLI extends OP
case class SRLI extends OP
case class SRAI extends OP
case class ADD extends OP
case class SUB extends OP
case class SLL extends OP
case class SLT extends OP
case class SLTU extends OP
case class XOR extends OP
case class SRL extends OP
case class SRA extends OP
case class OR extends OP
case class AND extends OP
// case class FENCE extends OP
// case class FENCE_I extends OP
// case class CSRRW extends OP
// case class CSRRS extends OP
// case class CSRRC extends OP
// case class CSRRWI extends OP
// case class CSRRSI extends OP
// case class CSRRCI extends OP
case class ECALL extends OP
// case class EBREAK extends OP
case class URET extends OP
case class SRET extends OP
case class MRET extends OP
case class WFI extends OP
// case class SFENCE_VMA extends OP
case class LB extends OP
case class LH extends OP
case class LW extends OP
case class LBU extends OP
case class LHU extends OP
case class SB extends OP
case class SH extends OP
case class SW extends OP
case class JAL extends OP
case class JALR extends OP
case class BEQ extends OP
case class BNE extends OP
case class BLT extends OP
case class BGE extends OP
case class BLTU extends OP
case class BGEU extends OP


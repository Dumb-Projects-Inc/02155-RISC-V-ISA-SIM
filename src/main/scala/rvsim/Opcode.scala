package rvsim

import Types._

sealed trait OP

case class LUI(rd: Reg.Value, imm: Int) extends OP
case class AUIPC(rd: Reg.Value, imm: Int) extends OP
case class ADDI(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends OP
case class SLTI(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends OP
case class SLTIU(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends OP
case class XORI(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends OP
case class ORI(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends OP
case class ANDI(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends OP
case class SLLI(rd: Reg.Value, rs1: Reg.Value, shamt: Int) extends OP
case class SRLI(rd: Reg.Value, rs1: Reg.Value, shamt: Int) extends OP
case class SRAI(rd: Reg.Value, rs1: Reg.Value, shamt: Int) extends OP
case class ADD(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value) extends OP
case class SUB(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value) extends OP
case class SLL(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value) extends OP
case class SLT(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value) extends OP
case class SLTU(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value) extends OP
case class XOR(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value) extends OP
case class SRL(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value) extends OP
case class SRA(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value) extends OP
case class OR(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value) extends OP
case class AND(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value) extends OP
// case class FENCE extends OP
// case class FENCE_I extends OP
// case class CSRRW extends OP
// case class CSRRS extends OP
// case class CSRRC extends OP
// case class CSRRWI extends OP
// case class CSRRSI extends OP
// case class CSRRCI extends OP
case class ECALL() extends OP
// case class EBREAK extends OP
case class URET() extends OP
case class SRET() extends OP
case class MRET() extends OP
case class WFI() extends OP
// case class SFENCE_VMA extends OP
case class LB(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends OP
case class LH(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends OP
case class LW(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends OP
case class LBU(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends OP
case class LHU(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends OP
case class SB(rs1: Reg.Value, rs2: Reg.Value, imm: Int) extends OP
case class SH(rs1: Reg.Value, rs2: Reg.Value, imm: Int) extends OP
case class SW(rs1: Reg.Value, rs2: Reg.Value, imm: Int) extends OP
case class JAL(rd: Reg.Value, imm: Int) extends OP
case class JALR(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends OP
case class BEQ(rs1: Reg.Value, rs2: Reg.Value, imm: Int) extends OP
case class BNE(rs1: Reg.Value, rs2: Reg.Value, imm: Int) extends OP
case class BLT(rs1: Reg.Value, rs2: Reg.Value, imm: Int) extends OP
case class BGE(rs1: Reg.Value, rs2: Reg.Value, imm: Int) extends OP
case class BLTU(rs1: Reg.Value, rs2: Reg.Value, imm: Int) extends OP
case class BGEU(rs1: Reg.Value, rs2: Reg.Value, imm: Int) extends OP

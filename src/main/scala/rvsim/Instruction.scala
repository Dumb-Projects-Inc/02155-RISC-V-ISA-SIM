package rvsim

import rvsim.Types._
import rvsim.Reg

sealed trait Instruction

case class LUI(rd: Reg.Value, imm: Int) extends Instruction
case class AUIPC(rd: Reg.Value, imm: Int) extends Instruction
case class ADDI(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends Instruction
case class SLTI(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends Instruction
case class SLTIU(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends Instruction
case class XORI(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends Instruction
case class ORI(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends Instruction
case class ANDI(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends Instruction
case class SLLI(rd: Reg.Value, rs1: Reg.Value, shamt: Int) extends Instruction
case class SRLI(rd: Reg.Value, rs1: Reg.Value, shamt: Int) extends Instruction
case class SRAI(rd: Reg.Value, rs1: Reg.Value, shamt: Int) extends Instruction
case class ADD(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value)
    extends Instruction
case class SUB(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value)
    extends Instruction
case class SLL(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value)
    extends Instruction
case class SLT(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value)
    extends Instruction
case class SLTU(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value)
    extends Instruction
case class XOR(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value)
    extends Instruction
case class SRL(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value)
    extends Instruction
case class SRA(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value)
    extends Instruction
case class OR(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value) extends Instruction
case class AND(rd: Reg.Value, rs1: Reg.Value, rs2: Reg.Value)
    extends Instruction
// case class FENCE extends Instruction
// case class FENCE_I extends Instruction
// case class CSRRW extends Instruction
// case class CSRRS extends Instruction
// case class CSRRC extends Instruction
// case class CSRRWI extends Instruction
// case class CSRRSI extends Instruction
// case class CSRRCI extends Instruction
case class ECALL() extends Instruction
// case class EBREAK extends Instruction
//case class URET() extends Instruction TODO: FIND OUT IF THESE EXIST
//case class SRET() extends Instruction TODO: FIND OUT IF THESE EXIST
//case class MRET() extends Instruction TODO: FIND OUT IF THESE EXIST
//case class WFI() extends Instruction TODO: FIND OUT IF THESE EXIST
// case class SFENCE_VMA extends Instruction
case class LB(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends Instruction
case class LH(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends Instruction
case class LW(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends Instruction
case class LBU(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends Instruction
case class LHU(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends Instruction
case class SB(rs1: Reg.Value, rs2: Reg.Value, imm: Int) extends Instruction
case class SH(rs1: Reg.Value, rs2: Reg.Value, imm: Int) extends Instruction
case class SW(rs1: Reg.Value, rs2: Reg.Value, imm: Int) extends Instruction
case class JAL(rd: Reg.Value, imm: Int) extends Instruction
case class JALR(rd: Reg.Value, rs1: Reg.Value, imm: Int) extends Instruction
case class BEQ(rs1: Reg.Value, rs2: Reg.Value, imm: Int) extends Instruction
case class BNE(rs1: Reg.Value, rs2: Reg.Value, imm: Int) extends Instruction
case class BLT(rs1: Reg.Value, rs2: Reg.Value, imm: Int) extends Instruction
case class BGE(rs1: Reg.Value, rs2: Reg.Value, imm: Int) extends Instruction
case class BLTU(rs1: Reg.Value, rs2: Reg.Value, imm: Int) extends Instruction
case class BGEU(rs1: Reg.Value, rs2: Reg.Value, imm: Int) extends Instruction

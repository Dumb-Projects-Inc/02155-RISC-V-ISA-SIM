//Reference: https://github.com/mortbopet/Ripes/blob/06cc83a48392613b0b769c6e8248baf0af087c16/src/processors/RISC-V/rv_decode.h

package rvsim

//TODO: Find instructions where these are used
object helper {
  def toUnsigned32(value: Int): Long = value.toLong & 0xFFFFFFFFL
}


object Fields {
  def rd(inst: Int): Int = (inst >> 7) & 0x1f
  def rs1(inst: Int): Int = (inst >> 15) & 0x1f
  def rs2(inst: Int): Int = (inst >> 20) & 0x1f
  def funct3(inst: Int): Int = (inst >> 12) & 0x7
  def funct7(inst: Int): Int = (inst >> 25) & 0x7f
  def opcode(inst: Int): Int = inst & 0x7f

  def decode(inst: Int): Instruction = {
    val opcode = Fields.opcode(inst)

    opcode match {
      case Opcode.LUI => {
        val rd = Fields.rd(inst)
        val imm = inst & 0xfffff000
        LUI(Reg(rd), imm)
      }
      case Opcode.AUIPC => {
        val rd = Fields.rd(inst)
        val imm = inst & 0xfffff000
        AUIPC(Reg(rd), imm)
      }

      case Opcode.JAL => {
        val rd = Fields.rd(inst)
        val imm = ((inst >> 12) & 0xff000) |
          ((inst >> 20) & 0x7fe) |
          ((inst << 11) & 0x800) |
          ((inst >> 31) & 0x100000)
        JAL(Reg(rd), imm)
      }

      case Opcode.JALR => {
        val rd = Fields.rd(inst)
        val rs1 = Fields.rs1(inst)
        val imm = (inst >> 20) & 0xfff
        JALR(Reg(rd), Reg(rs1), imm)
      }

      case Opcode.SYSTEM => {
        val funct3 = Fields.funct3(inst)
        funct3 match {
          case 0b000 => {
            val imm = (inst >> 20) & 0xfff
            imm match {
              case 0 => ECALL()
              case 1 => throw new Exception("EBREAK not implemented")
              case _ => throw new Exception(s"Unknown SYSTEM instruction with imm $imm")
            }
          }
          case _ => throw new Exception(s"Unknown SYSTEM instruction with funct3 $funct3")
        }
      }
      case Opcode.OPIMM => {
        val funct3 = Fields.funct3(inst)
        val rd = Fields.rd(inst)
        val rs1 = Fields.rs1(inst)
        val imm = (inst >> 20) & 0xfff

        funct3 match {
          case 0b000 => ADDI(Reg(rd), Reg(rs1), imm)
          case 0b010 => SLTI(Reg(rd), Reg(rs1), imm)
          case 0b011 => SLTIU(Reg(rd), Reg(rs1), imm)
          case 0b100 => XORI(Reg(rd), Reg(rs1), imm)
          case 0b110 => ORI(Reg(rd), Reg(rs1), imm)
          case 0b111 => ANDI(Reg(rd), Reg(rs1), imm)
          case 0b001 => {
            val shamt = Fields.rs2(inst)
            SLLI(Reg(rd), Reg(rs1), shamt)
          }
          case 0b101 => {
            val shamt = Fields.rs2(inst)
            val funct7 = Fields.funct7(inst)
            funct7 match {
              case 0b0000000 => SRLI(Reg(rd), Reg(rs1), shamt)
              case 0b0100000 => SRAI(Reg(rd), Reg(rs1), shamt)
            }
          }
        }
      }


      case Opcode.R_TYPE => {
        val funct7 = Fields.funct7(inst)
        val funct3 = Fields.funct3(inst)
        val rs2 = Fields.rs2(inst)
        val rs1 = Fields.rs1(inst)
        val rd = Fields.rd(inst)

        funct3 match {
          case 0b000 => {
            funct7 match {
              case 0b0000000 => ADD(Reg(rd), Reg(rs1), Reg(rs2))
              case 0b0100000 => SUB(Reg(rd), Reg(rs1), Reg(rs2))
            }
          }
          case 0b001 => SLL(Reg(rd), Reg(rs1), Reg(rs2))
          case 0b010 => SLT(Reg(rd), Reg(rs1), Reg(rs2))
          case 0b011 => SLTU(Reg(rd), Reg(rs1), Reg(rs2))
          case 0b100 => XOR(Reg(rd), Reg(rs1), Reg(rs2))
          case 0b101 => {
            funct7 match {
              case 0b0000000 => SRL(Reg(rd), Reg(rs1), Reg(rs2))
              case 0b0100000 => SRA(Reg(rd), Reg(rs1), Reg(rs2))
            }
          }
          case 0b110 => OR(Reg(rd), Reg(rs1), Reg(rs2))
          case 0b111 => AND(Reg(rd), Reg(rs1), Reg(rs2))
        }
      }

      case Opcode.LOAD => {
        val funct3 = Fields.funct3(inst)
        val rd = Fields.rd(inst)
        val rs1 = Fields.rs1(inst)
        val imm = (inst >> 20) & 0xfff

        funct3 match {
          case 0b000 => LB(Reg(rd), Reg(rs1), imm)
          case 0b001 => LH(Reg(rd), Reg(rs1), imm)
          case 0b010 => LW(Reg(rd), Reg(rs1), imm)
          case 0b100 => LBU(Reg(rd), Reg(rs1), imm)
          case 0b101 => LHU(Reg(rd), Reg(rs1), imm)
        }
      }

      case Opcode.STORE => {
        val funct3 = Fields.funct3(inst)
        val rs1 = Fields.rs1(inst)
        val rs2 = Fields.rs2(inst)
        val imm = ((inst >> 7) & 0x1f) | ((inst >> 25) & 0xfe0)

        funct3 match {
          case 0b000 => SB(Reg(rs1), Reg(rs2), imm)
          case 0b001 => SH(Reg(rs1), Reg(rs2), imm)
          case 0b010 => SW(Reg(rs1), Reg(rs2), imm)
        }
      }

      case Opcode.B_TYPE => {
        val funct3 = Fields.funct3(inst)
        val rs1 = Fields.rs1(inst)
        val rs2 = Fields.rs2(inst)
        val imm = ((inst >> 7) & 0x1e) |
          ((inst >> 20) & 0x7e0) |
          ((inst << 4) & 0x800) |
          ((inst >> 31) & 0x1000)

        funct3 match {
          case 0b000 => BEQ(Reg(rs1), Reg(rs2), imm)
          case 0b001 => BNE(Reg(rs1), Reg(rs2), imm)
          case 0b100 => BLT(Reg(rs1), Reg(rs2), imm)
          case 0b101 => BGE(Reg(rs1), Reg(rs2), imm)
          case 0b110 => BLTU(Reg(rs1), Reg(rs2), imm)
          case 0b111 => BGEU(Reg(rs1), Reg(rs2), imm)
        }
      }


    }
  }
}

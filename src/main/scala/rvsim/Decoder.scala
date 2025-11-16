//Reference: https://github.com/mortbopet/Ripes/blob/06cc83a48392613b0b769c6e8248baf0af087c16/src/processors/RISC-V/rv_decode.h

package rvsim
import rvsim.Types.UINT_32


object Fields {

  def extract(data: Int, end: Int, start: Int): Int = {
    val length = end - start + 1
    val mask = (1 << length) - 1
    (data >> start) & mask
  }

  def rd(inst: Int): Int = extract(inst, 11, 7)
  def rs1(inst: Int): Int = extract(inst, 19, 15)
  def rs2(inst: Int): Int = extract(inst, 24,20)
  def funct3(inst: Int): Int = extract(inst, 14, 12)
  def funct7(inst: Int): Int = extract(inst, 31,25)
  def opcode(inst: Int): Int = extract(inst, 6, 0)

  def decode(instrVal: UINT_32): Instruction = {
    val instr = instrVal.toInt()
    val opcode = Fields.opcode(instr)

    opcode match {

      case Opcode.LUI => {
        val rd = Fields.rd(instr)
        val imm = extract(instr, 31,12) << 12
        LUI(Reg(rd), imm)
      }

      case Opcode.AUIPC => {
        val rd = Fields.rd(instr)
        val imm = extract(instr, 31,12) << 12
        AUIPC(Reg(rd), imm)
      }

      case Opcode.JAL => {
        val rd = Fields.rd(instr)
        val imm = (extract(instr, 31,31)  << 20)  | // imm[20]
                  (extract(instr, 30,21)  << 1)   | // imm[10:1]
                  (extract(instr, 20, 20) << 11)  | // imm[11]
                  (extract(instr, 19,12)   << 12)   // imm[19:12]
        // sign-extend: moves sign bit to msb position, then arithmetic shifts it back resulting in sign extensions
        val signExtImm = (imm << 11) >> 11
        JAL(Reg(rd), signExtImm)
      }

      case Opcode.JALR => {
        val rd = Fields.rd(instr)
        val rs1 = Fields.rs1(instr)
        val imm = extract(instr, 31, 20) << 20 >> 20 // Sign-extend so that imm is signed 12-bit
        JALR(Reg(rd), Reg(rs1), imm)
      }

      case Opcode.SYSTEM => {
        val funct3 = Fields.funct3(instr)
        funct3 match {
          case 0b000 => {
            val imm = (instr >> 20) & 0xfff // No need to sign-extend as only ecall and ebreak exist
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
        val funct3 = Fields.funct3(instr)
        val rd = Fields.rd(instr)
        val rs1 = Fields.rs1(instr)
        val imm = ((instr >> 20) << 20) >> 20 // Sign-extend so that imm is signed 12-bit

        funct3 match {
          case 0b000 => ADDI(Reg(rd), Reg(rs1), imm)
          case 0b010 => SLTI(Reg(rd), Reg(rs1), imm)
          case 0b011 => SLTIU(Reg(rd), Reg(rs1), imm)
          case 0b100 => XORI(Reg(rd), Reg(rs1), imm)
          case 0b110 => ORI(Reg(rd), Reg(rs1), imm)
          case 0b111 => ANDI(Reg(rd), Reg(rs1), imm)
          case 0b001 => {
            val shamt = Fields.rs2(instr)
            SLLI(Reg(rd), Reg(rs1), shamt)
          }
          case 0b101 => {
            val shamt = Fields.rs2(instr)
            val funct7 = Fields.funct7(instr)
            funct7 match {
              case 0b0000000 => SRLI(Reg(rd), Reg(rs1), shamt)
              case 0b0100000 => SRAI(Reg(rd), Reg(rs1), shamt)
            }
          }
        }
      }


      case Opcode.R_TYPE => {
        val funct7 = Fields.funct7(instr)
        val funct3 = Fields.funct3(instr)
        val rs2 = Fields.rs2(instr)
        val rs1 = Fields.rs1(instr)
        val rd = Fields.rd(instr)

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
        val funct3 = Fields.funct3(instr)
        val rd = Fields.rd(instr)
        val rs1 = Fields.rs1(instr)
        val imm = ((instr >> 20) & 0xfff) // imm[11:0]
        val signExtImm = (imm << 20) >> 20 // sign-extend

        funct3 match {
          case 0b000 => LB(Reg(rd), Reg(rs1), signExtImm)
          case 0b001 => LH(Reg(rd), Reg(rs1), signExtImm)
          case 0b010 => LW(Reg(rd), Reg(rs1), signExtImm)
          case 0b100 => LBU(Reg(rd), Reg(rs1), signExtImm)
          case 0b101 => LHU(Reg(rd), Reg(rs1), signExtImm)
        }
      }

      case Opcode.STORE => {
        val funct3 = Fields.funct3(instr)
        val rs1 = Fields.rs1(instr)
        val rs2 = Fields.rs2(instr)
        val imm12   = (((instr >>> 25) & 0x7f) << 5) | ((instr >>> 7) & 0x1f)
        val imm     = (imm12 << 20) >> 20  // sign-extend

        funct3 match {
          case 0b000 => SB(Reg(rs1), Reg(rs2), imm)
          case 0b001 => SH(Reg(rs1), Reg(rs2), imm)
          case 0b010 => SW(Reg(rs1), Reg(rs2), imm)
        }
      }

      case Opcode.B_TYPE => {
        val funct3 = Fields.funct3(instr)
        val rs1 = Fields.rs1(instr)
        val rs2 = Fields.rs2(instr)
        val imm = (((instr >>> 31) & 0x1)  << 12) | // imm[12]
                  (((instr >>> 7)  & 0x1)  << 11) | // imm[11]
                  (((instr >>> 25) & 0x3f) << 5 ) | // imm[10:5]
                  (((instr >>> 8)  & 0xf)  << 1 )   // imm[4:1]
        
        // sign-extend imm
        val imm_se = (imm << 19) >> 19 // Sign-extend to 32 bits


        funct3 match {
          case 0b000 => BEQ(Reg(rs1), Reg(rs2), imm_se)
          case 0b001 => BNE(Reg(rs1), Reg(rs2), imm_se)
          case 0b100 => BLT(Reg(rs1), Reg(rs2), imm_se)
          case 0b101 => BGE(Reg(rs1), Reg(rs2), imm_se)
          case 0b110 => BLTU(Reg(rs1), Reg(rs2), imm_se)
          case 0b111 => BGEU(Reg(rs1), Reg(rs2), imm_se)
        }
      }


    }
  }
}

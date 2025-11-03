package rvsim

object Opcode {
  val R_TYPE = 0b0110011 
}


object Fields {
  def rd(inst: Int): Int = (inst >> 7) & 0x1f
  def rs1(inst: Int): Int = (inst >> 15) & 0x1f
  def rs2(inst: Int): Int = (inst >> 20) & 0x1f
  def funct3(inst: Int): Int = (inst >> 12) & 0x7
  def funct7(inst: Int): Int = (inst >> 25) & 0x7f
  def opcode(inst: Int): Int = inst & 0x7f

  def decode(inst: Int): OP = {
    val opcode = Fields.opcode(inst)

    opcode match {
      case Opcode.R_TYPE => {
        val funct7 = Fields.funct7(inst)
        val funct3 = Fields.funct3(inst)
        val rs2 = Fields.rs2(inst)
        val rs1 = Fields.rs1(inst)
        val rd = Fields.rd(inst)

        funct7 match {
          case 0b000000 => {

            funct3 match {
              case 0b001 => {
                SLLI(rd, rs1, rs2)
              }
            }
          }
          case 0b0100000 => {}
        }

      }
    }
  }
}

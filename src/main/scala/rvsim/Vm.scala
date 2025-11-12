package rvsim

import Types._

class VM() {
  private val mem = new Memory()
  private val regs = new Registers()
  private var pc: UINT_32 = 0x0000_0000.u32

  def loadProgram(
      program: Array[INT_8],
      startAddr: UINT_32 = 0x0000_0000.u32
  ): Unit = {
    for (i <- program.indices) {
      mem.writeByte(startAddr + i.u32, program(i))
    }
    pc = startAddr
  }

  def step(): Boolean = {
    // Fetch instruction
    val instr = mem.readWord(pc)

    // Default increment PC by 4 JAL and JALR will modify it later
    pc += 4.u32

    // Decode and execute instruction
    val instruction = Fields.decode(instr)

    // Execute instruction
    instruction match {
      case LUI(rd, imm) => {
        val value = imm.u32
        regs(rd) = value
      }

      case AUIPC(rd, imm) => {
        val value = pc + (imm << 12).u32 - 4.u32
        regs(rd) = value
      }
      case ADDI(rd, rs1, imm) => {
        val result = regs(rs1) + imm.u32
        regs(rd) = result
      }

      case SLTI(rd, rs1, imm) => {
        // TODO: Implement
      }

      case SLTIU(rd, rs1, imm) => {
        // TODO: Implement
      }

      case XORI(rd, rs1, imm) => {
        // TODO: Implement
      }

      case ORI(rd, rs1, imm) => {
        val result = regs(rs1) | imm.u32
        regs(rd) = result
      }

      case ANDI(rd, rs1, imm) => {
        val result = regs(rs1) & imm.u32
        regs(rd) = result
      }

      case SLLI(rd, rs1, shamt) => {
        // TODO: Implement
      }

      case SRLI(rd, rs1, shamt) => {
        // TODO: Implement
      }

      case SRAI(rd, rs1, shamt) => {
        // TODO: Implement
      }

      case ADD(rd, rs1, rs2) => {
        val result = regs(rs1) + regs(rs2)
        regs(rd) = result
      }

      case SUB(rd, rs1, rs2) => {
        val result = regs(rs1) - regs(rs2)
        regs(rd) = result
      }

      case SLL(rd, rs1, rs2) => {
        // TODO: Implement
      }

      case SLT(rd, rs1, rs2) => {
        // TODO: Implement
      }

      case SLTU(rd, rs1, rs2) => {
        // TODO: Implement
      }

      case XOR(rd, rs1, rs2) => {
        val result = regs(rs1) ^ regs(rs2)
        regs(rd) = result
      }

      case SRL(rd, rs1, rs2) => {
        // TODO: Implement
      }

      case SRA(rd, rs1, rs2) => {
        // TODO: Implement
      }

      case OR(rd, rs1, rs2) => {
        val result = regs(rs1) | regs(rs2)
        regs(rd) = result
      }

      case AND(rd, rs1, rs2) => {
        val result = regs(rs1) & regs(rs2)
        regs(rd) = result
      }

      case ECALL() => {
        return handleEcall(regs, mem) // May halt the VM
      }

      case LB(rd, rs1, imm) => {
        val addr = (regs(rs1).toInt + imm).u32
        val byte = mem.readByte(addr)
        regs(rd) = byte.u32
      }

      case LH(rd, rs1, imm) => {
        // TODO: Implement
      }

      case LW(rd, rs1, imm) => {
        val addr = (regs(rs1).toInt + imm).u32
        val word = mem.readWord(addr)
        regs(rd) = word
      }
      case LBU(rd, rs1, imm) => {
        val addr = (regs(rs1).toInt + imm).u32
        val byte = mem.readByte(addr)
        regs(rd) = (byte & 0xff).u32
      }
      case LHU(rd, rs1, imm) => {
        val addr = (regs(rs1).toInt + imm).u32
        val halfWord =
          (mem
            .readByte(addr) & 0xff) | ((mem.readByte(addr + 1.u32) & 0xff) << 8)
        regs(rd) = halfWord.u32
      }
      case SB(rs1, rs2, imm) => {
        val addr = (regs(rs1).toInt + imm).u32
        val value = regs(rs2) & 0xff.u32
        mem.writeByte(addr, value.toByte())
      }
      case SH(rs1, rs2, imm) => {
        // TODO: Implement
      }
      case SW(rs1, rs2, imm) => {
        val addr = (regs(rs1).toInt + imm).u32
        val value = regs(rs2)
        mem.writeWord(addr, value)
      }
      case JAL(rd, imm) => {
        // TODO: Implement
      }

      case JALR(rd, rs1, imm) => {
        // TODO: Implement
      }

      case BEQ(rs1, rs2, imm) => {
        // TODO: Implement
      }

      case BNE(rs1, rs2, imm) => {
        // TODO: Implement
      }

      case BLT(rs1, rs2, imm) => {
        // TODO: Implement
      }

      case BGE(rs1, rs2, imm) => {
        // TODO: Implement
      }

      case BLTU(rs1, rs2, imm) => {
        // TODO: Implement
      }

      case BGEU(rs1, rs2, imm) => {
        // TODO: Implement
      }

      case _ => {
        throw new Exception(s"Instruction $instruction not implemented")
      }
    }

    true
  }

  def run(): Unit = {
    while (step()) {
      // Print registers non -zero registers for debugging
      // for (reg <- Reg.values if reg != Reg.ZERO) {
      //  val value = regs(reg)
      //  if (value != 0) {
      //    println(s"${reg.toString}: 0x${value.toHexString}")
      //  }
      // }
      // println(s"PC: 0x${pc.toHexString}")
    }

  }

  // Expose registers for testing and inspection
  def getRegisters(): Registers = regs

  def handleEcall(regs: Registers, mem: Memory): Boolean = {
    val syscallNum = regs(Reg.A7).toInt
    val arg = regs(Reg.A0)
    syscallNum match {
      case SysCall.printInt => {
        println(arg.toInt)
      }
      case SysCall.printString => {
        val strAddr = arg
        var str = ""
        var offset = 0
        var char = mem.readByte(strAddr + offset.u32)
        while (char != 0) {
          str += char.toChar
          offset += 1
          char = mem.readByte(strAddr + offset.u32)
        }
        println(str)
      }
      case SysCall.exit => {
        println("Syscall exit: Halting VM")

        // assignment tells us to print all registers on exit.
        println("Final register state:")
        Debug.printRegisters(regs)

        return false // Indicate to halt the VM
      }
      case _ => {
        throw new Exception(s"Unknown syscall number: $syscallNum")
      }

    }
    true
  }
}

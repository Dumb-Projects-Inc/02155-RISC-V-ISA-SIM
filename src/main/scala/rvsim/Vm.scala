package rvsim

import Types._

class VM() {
  private val mem = new Memory()
  private val regs = new Registers()
  private var pc: UINT_32 = 0x0000_0000.u32
  private var lastInstruction: Instruction = _

  def loadProgram(
      program: Array[INT_8],
      startAddr: UINT_32 = 0x0000_0000.u32
  ): Unit = {
    for (i <- program.indices) {
      mem.writeByte(startAddr + i.u32, program(i))
    }
    pc = startAddr
  }
  private def calculateBranchTarget(
      pc: UINT_32,
      imm: INT_32
  ): UINT_32 = {
    val currentPc = pc - 4.u32
    val target = currentPc + imm.u32

    if (target == currentPc) {
      throw new Exception(
        s"Infinite loop detected at pc=0x${pc.toHexString} lastInstr = ${lastInstruction.toString}"
      )
    }
    target
  }

  def step(): Boolean = {
    // Fetch instruction
    val instr = mem.readWord(pc)

    // Default increment PC by 4 JAL and JALR will modify it later
    pc += 4.u32

    // Decode and execute instruction
    // println(
    //  f"pc=0x${pc.toInt}%x inst=0x${instr.toInt}%08x opcode=${instr.toInt & 0x7f}%02x funct3=${(instr.toInt >>> 12) & 7}%d rd=${(instr.toInt >>> 7) & 0x1f}%d"
    // )

    val instruction = Fields.decode(instr)
    lastInstruction = instruction
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
        regs(rd) = if (regs(rs1).toInt < imm) 1.u32 else 0.u32
      }

      case SLTIU(rd, rs1, imm) => {
        regs(rd) = if (regs(rs1) < imm.u32) 1.u32 else 0.u32
      }

      case XORI(rd, rs1, imm) => {
        val result = regs(rs1) ^ imm.u32
        regs(rd) = result
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
        val result = regs(rs1) << shamt
        regs(rd) = result
      }

      case SRLI(rd, rs1, shamt) => {
        val result = regs(rs1) >> shamt
        regs(rd) = result
      }

      case SRAI(rd, rs1, shamt) => {
        val result = (regs(rs1).toInt >> shamt).u32 // preserve sign bit
        regs(rd) = result
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
        val low5 = (regs(rs2) & 0x1f.u32).toInt
        val result = regs(rs1) << low5
        regs(rd) = result
      }

      case SLT(rd, rs1, rs2) => {
        regs(rd) = if (regs(rs1).toInt < regs(rs2).toInt) 1.u32 else 0.u32
      }

      case SLTU(rd, rs1, rs2) => {
        regs(rd) = if (regs(rs1) < regs(rs2)) 1.u32 else 0.u32
      }

      case XOR(rd, rs1, rs2) => {
        val result = regs(rs1) ^ regs(rs2)
        regs(rd) = result
      }

      case SRL(rd, rs1, rs2) => {
        val low5 = (regs(rs2) & 0x1f.u32).toInt
        val result = regs(rs1) >> low5
        regs(rd) = result
      }

      case SRA(rd, rs1, rs2) => {
        val low5 = (regs(rs2) & 0x1f.u32).toInt
        val result = (regs(rs1).toInt >> low5).u32 // preserve sign bit
        regs(rd) = result
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
        val signExtended = (byte.toInt << 24) >> 24
        regs(rd) = signExtended.u32
      }

      case LH(rd, rs1, imm) => {
        val addr = (regs(rs1).toInt + imm).u32
        val high = mem.readByte(addr + 1.u32) & 0xff
        val low = mem.readByte(addr) & 0xff
        val halfword = (((high << 8) | low) << 16) >> 16 // sign-extend

        regs(rd) = halfword.u32
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
        val addr = (regs(rs1).toInt + imm).u32
        val value = regs(rs2)
        println(f"SH value=0x${value.toInt}%04x at addr=0x${addr.toInt}%08x")
        mem.writeByte(addr, (value & 0xff).toByte())
        mem.writeByte(addr + 1.u32, ((value.toInt >>> 8) & 0xff).toByte)
      }
      case SW(rs1, rs2, imm) => {
        val addr = (regs(rs1).toInt + imm).u32
        val value = regs(rs2)
        mem.writeWord(addr, value)
      }
      case JAL(rd, imm) => {
        regs(rd) = pc // No need to worry about x0 since it's not writable
        pc = calculateBranchTarget(pc, imm)

      }

      case JALR(rd, rs1, imm) => {
        val target = (regs(rs1).toInt + imm).u32 & 0xfffffffe.u32
        val returnAddr = pc
        regs(rd) = returnAddr
        pc = target

      }

      case BEQ(rs1, rs2, imm) => {
        if (regs(rs1) == regs(rs2)) {
          pc = calculateBranchTarget(pc, imm)
        }
      }

      case BNE(rs1, rs2, imm) => {
        if (regs(rs1) != regs(rs2)) {
          pc = calculateBranchTarget(pc, imm)
        }
      }

      case BLT(rs1, rs2, imm) => {
        if (regs(rs1).toInt < regs(rs2).toInt) {
          pc = calculateBranchTarget(pc, imm)
        }
      }

      case BGE(rs1, rs2, imm) => {
        if (regs(rs1).toInt >= regs(rs2).toInt) {
          pc = calculateBranchTarget(pc, imm)
        }
      }

      case BLTU(rs1, rs2, imm) => {
        if (regs(rs1) < regs(rs2)) {
          pc = calculateBranchTarget(pc, imm)
        }
      }

      case BGEU(rs1, rs2, imm) => {
        if (regs(rs1) >= regs(rs2)) {
          pc = calculateBranchTarget(pc, imm)
        }
      }

      case _ => {
        throw new Exception(s"Instruction $instruction not implemented")
      }
    }

    true
  }

  def run(): Unit = {
    println("Starting VM execution...")
    while (step()) {
      // Print registers non -zero registers for debugging
      // for (reg <- Reg.values if reg != Reg.ZERO) {
      //  val value = regs(reg)
      //  if (value != 0) {
      //    println(s"${reg.toString}: 0x${value.toHexString}")
      //  }
      // }
      // println( // add registers used if in instruction
      //  s"Instruction executed = ${lastInstruction.toString}, PC=0x${pc.toHexString}: "
      // )
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

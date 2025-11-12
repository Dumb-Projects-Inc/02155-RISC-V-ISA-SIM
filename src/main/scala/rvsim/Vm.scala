package rvsim

import Types._

class VM() {
  private val mem = new Memory()
  private val regs = new Registers()
  private var pc: INT_32 = 0x0000_0000

  def loadProgram(
      program: Array[INT_8],
      startAddr: INT_32 = 0x0000_0000
  ): Unit = {
    for (i <- program.indices) {
      mem.writeByte(startAddr + i, program(i))
    }
    pc = startAddr
  }

  def step(): Boolean = {
    // Fetch instruction
    val instr = mem.readWord(pc)

    // Default increment PC by 4 JAL and JALR will modify it later
    pc += 4

    // Decode and execute instruction
    val instruction = Fields.decode(instr)

    // Execute instruction
    instruction match {
      case ADDI(rd, rs1, imm) => {
        val result = regs(rs1) + imm
        regs(rd) = result
      }
      case ADD(rd, rs1, rs2) => {
        val result = regs(rs1) + regs(rs2)
        regs(rd) = result
      }
      case ECALL() => {
        return handleEcall(regs, mem) // May halt the VM
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

  def handleEcall(regs: Registers, mem: Memory): Boolean = {
    val syscallNum = regs(Reg.A7)
    val arg = regs(Reg.A0)
    syscallNum match {
      case SysCall.printInt => {
        val intToPrint = arg
        println(intToPrint)
      }
      case SysCall.printString => {
        val strAddr = arg
        var str = ""
        var offset = 0
        var char = mem.readByte(strAddr + offset)
        while (char != 0) {
          str += char.toChar
          offset += 1
          char = mem.readByte(strAddr + offset)
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

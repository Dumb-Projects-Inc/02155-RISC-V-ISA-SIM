package rvsim

import Types._

class VM() {
  private val mem = new Memory()
  private val regs = new Registers()
  private var pc: INT_32 = 0x0000_0000

  def step(): Boolean = {
    // Fetch instruction
    val instr = mem.readWord(pc)
    // Decode and execute instruction
    // TODO: Decoder and extract fields

    // TODO: Execute instruction logic

    // TODO: Return false if halt instruction encountered

    // Increment Program counter
    pc += 4
    true
  }

  def run(): Unit = {
    while (step()) { /* Add debug output for vm here */ }

  }
}

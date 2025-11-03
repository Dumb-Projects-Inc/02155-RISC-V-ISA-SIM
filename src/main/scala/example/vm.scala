package example

import Types._

class VM() {
  private val mem = new Memory()
  private val regs = new Registers()
  private var pc: UINT_32 = 0x0000_0000

  def step(): Boolean = {
    // Fetch instruction
    val instr = mem.read(pc)
    // Decode and execute instruction
    // TODO: Decoder and extract fields
    // TODO: Execute instruction
    pc += 4
    true
  }

  def run(): Unit = {
    while step() do ()
  }
}

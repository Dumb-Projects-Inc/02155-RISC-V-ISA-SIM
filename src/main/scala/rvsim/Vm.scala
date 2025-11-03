package rvsim

import Types._

class VM() {
  private val mem = new Memory()
  private val regs = new Registers()
  private var pc: UINT_32 = 0x0000_0000

  def step(): Unit = {
    // Fetch instruction
    val instr = mem.read(pc)
    // Decode and execute instruction
    // TODO: Decoder and extract fields
  }

  def run(): Unit = {
    while step() do ()
  }
}

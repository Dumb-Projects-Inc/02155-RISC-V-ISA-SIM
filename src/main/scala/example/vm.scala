package example

import Types._

class VM() {
  private val mem = new Memory()
  private val regs = new Registers()
  private var pc: UINT_32 = 0x0000_0000
}

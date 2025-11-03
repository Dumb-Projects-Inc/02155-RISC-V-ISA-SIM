class VM:
    private val mem = Memory()
    private val regs = Registers()
    private var pc: UINT_32 = 0x0000_0000

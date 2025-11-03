enum Reg:
    case ZERO, RA, SP, GP, TP, T0, T1, T2,
         S0, S1, A0, A1, A2, A3, A4, A5,
         A6, A7, S2, S3, S4, S5, S6, S7,
         S8, S9, S10, S11, T3, T4, T5, T6

object Registers:
    def create_Registers(): Array[UInt32] = Array.ofDim[UINT_32](Reg.T6.ordinal + 1)
    val regs: Array[UINT_32] = create_Registers()

    //Overload access operators
    def [](reg: Reg): UINT_32 = regs(reg.ordinal)
    def []=(reg: Reg, value: UINT_32): Unit =
        if reg != Reg.ZERO then regs(reg.ordinal) = value

object Memory:
    val memorySize: Int = 1 << 32 // 32 bit system - max
    val memory: Array[UInt32] = Array.fill(memorySize)(0) // 32 bit addressable memory with 32 bit words

    def pos_addr(addr: UINT_32): Int = addr & 0xFFFF_FFFF
    def mem_write(addr: UINT_32, value: UINT_32): Unit = Memory.memory(pos_addr(addr)) = value
    def mem_read(addr: UINT_32): UINT_32 = Memory.memory(pos_addr(addr))
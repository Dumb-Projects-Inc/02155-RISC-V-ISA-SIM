
enum OP:
    case LUI,
    AUIPC,
    ADDI,
    SLTI,
    SLTIU,
    XORI,
    ORI,
    ANDI,
    SLLI,
    SRLI,
    SRAI,
    ADD,
    SUB,
    SLL,
    SLT,
    SLTU,
    XOR,
    SRL,
    SRA,
    OR,
    AND,
    //FENCE,
    //FENCE_I,
    //CSRRW,
    //CSRRS,
    //CSRRC,
    //CSRRWI,
    //CSRRSI,
    //CSRRCI,
    ECALL,
    //EBREAK,
    URET,
    SRET,
    MRET,
    WFI,
    //SFENCE_VMA,
    LB,
    LH,
    LW,
    LBU,
    LHU,
    SB,
    SH,
    SW,
    JAL,
    JALR,
    BEQ,
    BNE,
    BLT,
    BGE,
    BLTU,
    BGEU

object OP:
    //TODO: Pointer to registers and memory

    def LUI(instr: UINT_32): Unit = {}
    def AUIPC(instr: UINT_32): Unit = {}
    def ADDI(instr: UINT_32): Unit = {}
    def SLTI(instr: UINT_32): Unit = {}
    def SLTIU(instr: UINT_32): Unit = {}
    def XORI(instr: UINT_32): Unit = {}
    def ORI(instr: UINT_32): Unit = {}
    def ANDI(instr: UINT_32): Unit = {}
    def SLLI(instr: UINT_32): Unit = {}
    def SRLI(instr: UINT_32): Unit = {}
    def SRAI(instr: UINT_32): Unit = {}
    def ADD(instr: UINT_32): Unit = {}
    def SUB(instr: UINT_32): Unit = {}
    def SLL(instr: UINT_32): Unit = {}
    def SLT(instr: UINT_32): Unit = {}
    def SLTU(instr: UINT_32): Unit = {}
    def XOR(instr: UINT_32): Unit = {}
    def SRL(instr: UINT_32): Unit = {}
    def SRA(instr: UINT_32): Unit = {}
    def OR(instr: UINT_32): Unit = {}
    def AND(instr: UINT_32): Unit = {}
    // Fence functions skipped
    def ECALL(instr: UINT_32): Unit = {}
    def URET(instr: UINT_32): Unit = {}
    def SRET(instr: UINT_32): Unit = {}
    def MRET(instr: UINT_32): Unit = {}
    def WFI(instr: UINT_32): Unit = {}
    def LB(instr: UINT_32): Unit = {}
    def LH(instr: UINT_32): Unit = {}
    def LW(instr: UINT_32): Unit = {}
    def LBU(instr: UINT_32): Unit = {}
    def LHU(instr: UINT_32): Unit = {}
    def SB(instr: UINT_32): Unit = {}
    def SH(instr: UINT_32): Unit = {}
    def SW(instr: UINT_32): Unit = {}
    def JAL(instr: UINT_32): Unit = {}
    def JALR(instr: UINT_32): Unit = {}
    def BEQ(instr: UINT_32): Unit = {}
    def BNE(instr: UINT_32): Unit = {}
    def BLT(instr: UINT_32): Unit = {}
    def BGE(instr: UINT_32): Unit = {}
    def BLTU(instr: UINT_32): Unit = {}
    def BGEU(instr: UINT_32): Unit = {}
package rvsim

// Same syscalls as Ripes
object SysCall {
  val None = 0
  val printInt = 1
  //val printFloat = 2 // Not implemented
  val printString = 4
  val exit = 10
}

object Opcode {
  val R_TYPE = 0b0110011
  //val I_TYPE = 0b0010011 //LOAD, JALR, and Op-IMM
  val OPIMM = 0b0010011
  val LOAD = 0b0000011
  val STORE = 0b0100011
  
  //val S_TYPE = 0b0100011 // Same as STORE
  val B_TYPE = 0b1100011 // Branch instructions


  //val J_TYPE = 0b1101111 //only JAL we also have JALR in I_TYPE
  val JAL = 0b1101111
  val JALR = 0b1100111
  //val U_TYPE = 0b0110111 //only LUI and AUIPC
  val LUI = 0b0110111
  val AUIPC = 0b0010111
  val SYSTEM = 0b1110011
}
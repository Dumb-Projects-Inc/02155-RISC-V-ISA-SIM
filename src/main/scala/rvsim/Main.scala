package rvsim

import java.nio.file.{Files, Paths}

object Main {

  def main(args: Array[String]): Unit = {
    println("Hello, RISC-V ISA Simulator!")
    // make sure program.bin exists
    val progPath = if (args.length > 0) args(0) else ""

    // init vm
    val vm = new VM()

    var program: Array[Byte] = Array()
    if (progPath.nonEmpty) {
      program = Files.readAllBytes(Paths.get(progPath))
    } else {
      // load sample program that adds two numbers
      program = Array(
        0x13, 0x05, 0x50, 0x00, // ADDI x10, x0, 5
        0x13, 0x06, 0x60, 0x00, // ADDI x12, x0, 6
        0x33, 0x07, 0xa6, 0x00, // ADD x14, x12, x10
        0x93, 0x08, 0x10, 0x00, // ADDI x17, x0, 1
        0x13, 0x05, 0x07, 0x00, // ADDI x10, x14, 0   (copy result to a0)
        0x73, 0x00, 0x00, 0x00, // ECALL
        0x93, 0x08, 0xa0, 0x00, // ADDI x17,x0,10
        0x73, 0x00, 0x00, 0x00 // ECALL

      ).map(_.toByte)
    }
    vm.loadProgram(program)
    vm.run()
  }
}

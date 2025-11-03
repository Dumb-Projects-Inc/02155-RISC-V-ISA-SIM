package rvsim

import Types._

object Main {
  def main(args: Array[String]): Unit = {
    println("Hello, RISC-V ISA Simulator!")
    // make sure program.bin exists
    val progPath = if (args.length > 0) args(0) else "bin/program.bin"

    // init vm
    val vm = new VM()
  }
}

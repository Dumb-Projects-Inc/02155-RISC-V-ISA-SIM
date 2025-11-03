

def main(args: Array[String]): Unit = {
  println("Hello, RISC-V ISA Simulator!")
  // make sure program.bin exists
  val progPath = if args.length > 0 then args(0) else "bin/program.bin"

  // init vm
  VM vm = new VM()
}
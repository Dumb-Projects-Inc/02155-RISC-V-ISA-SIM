package rvsim

// Helper functions for debugging
object Debug {
  def printRegisters(regs: Registers, cols: Int = 4): Unit = {
    val names = Reg.values.toArray.map(_.toString.padTo(4, ' '))
    val values =
      Reg.values.toArray.map(r => f"0x${regs(r).toInt}%08x")

    val rows = Math.ceil(names.length.toDouble / cols).toInt
    for (r <- 0 until rows) {
      val line = (0 until cols)
        .flatMap { c =>
          val idx = r + c * rows
          if (idx < names.length)
            Some(f"${names(idx)}: ${values(idx)}")
          else None
        }
        .mkString(" | ")
      println(line)
    }
  }
}

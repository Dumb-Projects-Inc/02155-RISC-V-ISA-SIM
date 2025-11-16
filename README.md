# RISC-V Instruction Set Simulator (RV32I)

A Scala-based simulator for the RISC-V 32-bit Integer (RV32I) instruction set

## Usage

### Run a Simulation

You can run the simulator by passing the path to a binary (`.bin`) file

```bash
sbt "run <path_to_binary_file>"
```

### Run Tests

The project includes a comprehensive test suite designed by the supervisors of course 02155

```bash
sbt test
```

## Project Structure

  * `src/main/scala/rvsim/`
      * `Main.scala`: Entry point. Reads the binary and initializes the VM.
      * `Vm.scala`: Core simulation loop (Fetch, Decode, Execute).
      * `Decoder.scala`: Decodes 32-bit instructions into Scala case classes.
      * `Memory.scala`: Manages memory and register file (x0-x31).
      * `Instruction.scala`: Case classes defining the supported ISA.
  * `src/test/resources/cae/`: Test suite

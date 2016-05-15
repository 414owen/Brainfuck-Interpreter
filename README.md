## Brainfuck Interpreter
An uncompromising brainfuck interpreter in exactly 100 lines of scala.

The only limitation on the amount of available cells is your RAM.

Cells are 1 Byte in size.

Internally, everything is immutable and each machine state is shared with the previous/next state.


### How to test:
    mkdir bytecode
	scalac -d bytecode brainfuck.scala
	cd bytecode
	scala Brainfuck ../examples/hanoi.bf		# Or the path of another brainfuck program

### TODO
* ~~Fix crashing after terminating program~~
* ~~Fix Stack Overflow when matching brackets are super far apart.~~

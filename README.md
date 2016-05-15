## Brainfuck Interpreter
### How to test:
    mkdir bytecode
	scalac -d bytecode brainfuck.scala
	cd bytecode
	scala Brainfuck ../examples/hanoi.bf		# Or the path of another brainfuck program

### TODO
* ~~Fix crashing after terminating program~~
* ~~Fix Stack Overflow when matching brackets are super far apart.~~

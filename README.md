## Brainfuck Interpreter
### How to test:
	scalac -d bytecode brainfuck.scala
	cd bytecode
	scala Brainfuck ../hello.bf		# Or other brainfuck program

### TODO
* ~~Fix crashing after terminating program~~
* Fix Stack Overflow when matching brackets are super far apart.

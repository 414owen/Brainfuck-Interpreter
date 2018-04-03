## Brainfuck Interpreter

An uncompromising brainfuck interpreter in 80 lines of scala. The amount of
available cells is limited only by the RAM available. Cells are 1 Byte in size.
Internally, the machine's state is never mutated, every state shares the
majority of its structure with the previous / next states.

### How to use:

```bash
mkdir bytecode
scalac -d bytecode brainfuck.scala 
cd bytecode
scala Brainfuck ../examples/hanoi.bf		# Or the path of another brainfuck program
```

### Unresolvable bugs?
* Fix input requiring a newline character to register.  (This is difficult, as
  reading input one character at a time requires switching  the console to
  'raw' mode, as opposed to 'cooked' (line-by line) mode. There is  no
  cross-platform way of doing this, hence it is not supported by Java or Scala,
  and I won't be implementing it for brainfuck.)  

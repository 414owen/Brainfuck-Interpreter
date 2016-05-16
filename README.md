## Brainfuck Interpreter

An uncompromising brainfuck interpreter in under 100 lines of scala. The only  
limitation on the amount of available cells is your RAM. Cells are 1 Byte in  
size. Internally, everything is immutable and each machine state is shared with  
the previous/next state

### How to use:
    mkdir bytecode
    scalac -d bytecode brainfuck.scala 
    cd bytecode scala Brainfuck ../examples/hanoi.bf		# Or the path of another brainfuck program

### TODO
* ~~Fix crashing after terminating program~~  
* ~~Fix Stack Overflow when matching brackets are super far apart.~~

### Unresolvable?
* Fix input requiring a newline character to register.

(This is difficult, as reading input one character at a time requires switching  
the console to 'raw'mode, as opposed to 'cooked' (line-by line) mode. There is  
no cross-platform way of doing this, hence it is not supported by Java or Scala,  
and I won't be implementing it for brainfuck.)  

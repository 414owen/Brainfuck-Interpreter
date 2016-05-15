object Brainfuck {
    class Machine (ml: List[Byte], mr: List[Byte],
                   mc: Byte, prog: String, at:Int) {
        val left = ml
        val right = mr
        val current = mc
        val program = prog
        val instruction = at

        def memLeft(): Machine = {
            val curr: Byte = left match {
                case x :: rest => x
                case nil => 0
            }
            val newLeft: List[Byte] = left match {
                case x :: rest => rest
                case nil => List(0)
            }
            new Machine(newLeft, current::right, 
                curr, program, instruction)
        }

        def memRight(): Machine = {
            val curr: Byte = right match {
                case x :: rest => x
                case nil => 0
            }
            val newRight: List[Byte] = right match {
                case x :: rest => rest
                case nil => List(0)
            }
            new Machine(current::left, newRight,
                curr, program, instruction)
            }

        def codeRight(): Machine = new Machine(left, 
                right, current, program, instruction + 1)

        def editMem(f: Byte => Byte): Machine = new Machine(left, 
            right, f(current), program, instruction)

        final def matchBracket(brac: Char, depth: Int = 0): Machine = {
            val otherBrac = if (brac == '[') ']' else '['
            val inc = if (brac == '[') 1 else -1
            val machNext = new Machine(left, right, 
                current, program, instruction + inc)
            machNext.program(machNext.instruction) match {
                case `brac` => machNext.matchBracket(brac, depth - inc)
                case `otherBrac` => if (depth + inc == inc) machNext
                        else machNext.matchBracket(brac, depth + inc)
                case _ => machNext.matchBracket(brac, depth)
            }
        }

        def printChar(): Machine = {
            print(current.toChar)
            this
        }

        def strChar(): Machine = {
            editMem(i => Console.in.read.toChar.toByte)
        }

        def nextState(): Option[Machine] = {
            val machNext: Machine = program(instruction) match {
                case '[' => if (current == 0) 
                                matchBracket('[').codeRight
                            else codeRight
                case ']' => if (current == 0) codeRight
                            else matchBracket(']').codeRight
                case '<' => memLeft.codeRight
                case '>' => memRight.codeRight
                case '+' => editMem(i => (i + 1).toByte).codeRight
                case '-' => editMem(i => (i - 1).toByte).codeRight
                case '.' => printChar.codeRight
                case ',' => strChar.codeRight
                case _ => codeRight
            }
            if (machNext.instruction <= program.length - 1 
                && machNext.instruction >= 0) 
                    Some(machNext)
            else None
        }
    }

    def main(args: Array[String]) {
        def mutateToEnd(mach: Machine): Option[Machine] = {
            mach.nextState match {
                case None => None
                case Some(m) => mutateToEnd(m)
            }
        }

        val program = scala.io.Source.fromFile(args(0))
            .mkString.filter(a => "[]<>+-.,".contains(a))
        mutateToEnd(new Machine(List(), List(), 0.toByte, program, 0))
    }
}

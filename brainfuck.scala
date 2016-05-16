object Brainfuck {
    case class Machine (left: List[Byte], right: List[Byte],
                   current: Byte, program: String, 
                   instruction: Int) {

        def memLeft(): Machine = {
            val newCurrent: Byte = left match {
                case x :: rest => x
                case nil => 0
            }
            val newLeft: List[Byte] = left match {
                case x :: rest => rest
                case nil => List(0)
            }
            Machine(newLeft, current::right, 
                newCurrent, program, instruction)
        }

        def memRight(): Machine = {
            val newCurrent: Byte = right match {
                case x :: rest => x
                case nil => 0
            }
            val newRight: List[Byte] = right match {
                case x :: rest => rest
                case nil => List(0)
            }
            Machine(current::left, newRight,
                newCurrent, program, instruction)
        }

        def codeRight(): Machine = copy(instruction = instruction + 1)

        def editMem(f: Byte => Byte): Machine = Machine(left, 
            right, f(current), program, instruction)

        final def matchBracket(brac: Char, depth: Int = 0): Machine = {
            val otherBrac = if (brac == '[') ']' else '['
            val inc = if (brac == '[') 1 else -1
            val machNext = Machine(
                left, right,
                current, program,
                instruction + inc)
            program(machNext.instruction) match {
                case `brac` => machNext.matchBracket(brac, depth - inc)
                case `otherBrac` => if (depth == 0) machNext
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
                                matchBracket('[')
                            else this
                case ']' => if (current == 0) this
                            else matchBracket(']')
                case '<' => memLeft
                case '>' => memRight
                case '+' => editMem(i => (i + 1).toByte)
                case '-' => editMem(i => (i - 1).toByte)
                case '.' => printChar
                case ',' => strChar
                case _ => this
            }
            if (machNext.instruction < program.length - 1 
                && machNext.instruction >= 0) 
                    Some(machNext.codeRight)
            else None
        }
    }

    def main(args: Array[String]) {
        def mutateToEnd(mach: Machine): Option[Machine] = 
            mach.nextState match {
                case Some(m) => mutateToEnd(m)
                case None => None
        }

        val program = scala.io.Source.fromFile(args(0))
            .mkString.filter("[]<>+-.,".contains(_))
        mutateToEnd(Machine(List(), List(), 0.toByte, program, 0))
    }
}

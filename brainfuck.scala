import scala.annotation.tailrec

object Brainfuck {
    class Machine (ml: List[Byte], mr: List[Byte],
                   mc: Byte, prog: String,
                   at:Int) {
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

        @tailrec
        final def matchBracket(find: Char, counter: Char, dir: Int => Int, depth: Int = 0): Machine = {
            val machNext = new Machine(left, right, 
                current, program, dir(instruction))
            val machNextChar = machNext.program(machNext.instruction)
            machNextChar match {
                case `find` => if (depth == 0) machNext
                             else machNext.matchBracket(find, counter, dir, depth - 1)
                case `counter` => machNext.matchBracket(find, counter, dir, depth + 1)
                case _ => machNext.matchBracket(find, counter, dir, depth)
            }
        }

        def printChar(): Machine = {
            print(current.toChar)
            this
        }

        def strChar(): Machine = {
            val c = Console.in.read.toChar.toByte
            editMem(i => c)
        }
    }

    def main(args: Array[String]) {
        def nextState(mach: Machine): Option[Machine] = {
            mach.program(mach.instruction) match {
                case '[' => if (mach.current == 0) 
                                Some(mach.matchBracket(']', '[', i => i + 1).codeRight)
                            else Some(mach.codeRight)
                case ']' => Some(mach.matchBracket('[', ']', i => i - 1))
                case _ => if (mach.instruction != mach.program.length - 1)
                              mach.program(mach.instruction) match {
                                  case '<' => Some(mach.memLeft.codeRight)
                                  case '>' => Some(mach.memRight.codeRight)
                                  case '+' => Some(mach.editMem(i => (i + 1).toByte).codeRight)
                                  case '-' => Some(mach.editMem(i => (i - 1).toByte).codeRight)
                                  case '.' => Some(mach.printChar.codeRight)
                                  case ',' => Some(mach.strChar.codeRight)
                                  case _ => Some(mach.codeRight)
                              }
                          else None
            }
        }

        def mutateToEnd(mach: Machine): Option[Machine] = {
            nextState(mach) match {
                case None => None
                case Some(m) => mutateToEnd(m)
            }
        }

        val program = scala.io.Source.fromFile(args(0)).mkString
        mutateToEnd(new Machine(List(0), List(0), 0.toByte, program, 0))
    }
}

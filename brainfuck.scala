object Brainfuck {
  def headOrZero(list: List[Byte]): Byte = list match {
    case x :: xs => x
    case _ => 0
  }

  def tailOrSame(list: List[Byte]): List[Byte] = list match {
    case x :: xs => xs
    case same => same
  }
  
  def mutateToEnd(mach: Machine): Option[Machine] = 
    mach.nextState match {
      case Some(m) => mutateToEnd(m)
      case None => None
    }

  def main(args: Array[String]) =
    run(scala.io.Source.fromFile(args(0))
        .mkString.filter("[]<>+-.,".contains(_)))

  def run(program: String): Unit = 
    mutateToEnd(Machine(Nil, Nil, 0.toByte, program, 0))

  case class Machine (left: List[Byte], right: List[Byte],
                      current: Byte, program: String,
                      instruction: Int) {

    def memLeft(): Machine = copy(left = tailOrSame(left), 
        right = current::right, current = headOrZero(left))

    def memRight(): Machine = copy(left = current::left, 
        right =tailOrSame(right), current = headOrZero(right))

    def codeRight(): Machine = copy(instruction = instruction + 1)

    def editMem(f: Byte => Byte): Machine = copy(current = f(current))

    final def matchBracket(brac: Char, depth: Int = 0): Machine = {
      val otherBrac = if (brac == '[') ']' else '['
      val inc = if (brac == '[') 1 else -1
      val machNext = copy(instruction = instruction + inc)
      program(machNext.instruction) match {
        case `brac` => machNext.matchBracket(brac, depth - inc)
        case `otherBrac` => if (depth == 0) machNext
                            else machNext .matchBracket(brac, depth + inc)
        case _ => machNext.matchBracket(brac, depth)
      }
    }

    def printChar(): Machine = {
      val toPrint = if (current < 0) (current + 256) % 257
                    else current
      print(toPrint.toChar)
      this
    }

    def strChar(): Machine = editMem(i => Console.in.read.toChar.toByte)

    def nextState(): Option[Machine] = {
      val machNext: Machine = program(instruction) match {
        case '[' => if (current == 0) matchBracket('[')
                    else this
        case ']' => if (current == 0) this
                    else matchBracket(']')
        case '<' => memLeft
        case '>' => memRight
        case '+' => editMem(i => (i + 1).toByte)
        case '-' => editMem(i => (i - 1).toByte)
        case '.' => printChar
        case ',' => strChar
      }
      if (machNext.instruction < program.length - 1 
          && machNext.instruction >= 0) 
        Some(machNext.codeRight)
        else None
    }
  }
}

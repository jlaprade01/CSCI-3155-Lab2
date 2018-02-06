package jsy.student

import jsy.lab2.Lab2Like

object Lab2 extends jsy.util.JsyApplication with Lab2Like {
  import jsy.lab2.Parser
  import jsy.lab2.ast._

  /*
   * CSCI 3155: Lab 2
   * <Your Name>
   * 
   * Partner: <Your Partner's Name>
   * Collaborators: <Any Collaborators>
   */

  /*
   * Fill in the appropriate portions above by replacing things delimited
   * by '<'... '>'.
   * 
   * Replace the '???' expression with  your code in each function.
   * 
   * Do not make other modifications to this template, such as
   * - adding "extends App" or "extends Application" to your Lab object,
   * - adding a "main" method, and
   * - leaving any failing asserts.
   * 
   * Your lab will not be graded if it does not compile.
   * 
   * This template compiles without error. Before you submit comment out any
   * code that does not compile or causes a failing assert. Simply put in a
   * '???' as needed to get something  that compiles without error. The '???'
   * is a Scala expression that throws the exception scala.NotImplementedError.
   *
   */

  /* We represent a variable environment as a map from a string of the
   * variable name to the value to which it is bound.
   * 
   * You may use the following provided helper functions to manipulate
   * environments, which are just thin wrappers around the Map type
   * in the Scala standard library.  You can use the Scala standard
   * library directly, but these are the only interfaces that you
   * need.
   */



  /* Some useful Scala methods for working with Scala values include:
   * - Double.NaN
   * - s.toDouble (for s: String)
   * - n.isNaN (for n: Double)
   * - n.isWhole (for n: Double)
   * - s (for n: Double)
   * - s format n (for s: String [a format string like for printf], n: Double)
   *
   * You can catch an exception in Scala using:
   * try ... catch { case ... => ... }
   */

  def toNumber(v: Expr): Double = {
    require(isValue(v))
    (v: @unchecked) match {
      case N(n) => n
      case B(true) => 1.0
      case B(false) => 0.0
      case S(s) => try s.toDouble catch {case _ => Double.NaN}
      case Undefined => Double.NaN
      case _ => Double.NaN
    }
  }

  def toBoolean(v: Expr): Boolean = {
    require(isValue(v))
    (v: @unchecked) match {
      case B(b) => b
      case N(n) => if(n==0.0 || n==Double.NaN) false else true
      case S(s) => if(s=="") false else true
      case Undefined => false
      case _ => false
    }
  }

  def toStr(v: Expr): String = {
    require(isValue(v))
    (v: @unchecked) match {
      case S(s) => s
      case Undefined => "undefined"
      case B(b) => b.toString()
      case N(n) => n.toString()
      case _ => ""
    }
  }

  def eval(env: Env, e: Expr): Expr = {
    e match {
      /* Base Cases */
      case Undefined => Undefined
      case N(n) => N(n)
      case S(s) => S(s)
      case B(b) => B(b)
      case Unary(uop, e1) => {
        uop match {
          case Neg => N(0 - toNumber(eval(env,e1)))
          case Not => B(!toBoolean(eval(env,e1)))
        }
      }
      case Binary(bop, e1, e2) => {
        bop match {
          case Plus  => N(toNumber(eval(env,e1)) + toNumber(eval(env,e2)))
          case Minus => N(toNumber(eval(env,e1)) - toNumber(eval(env,e2)))
          case Times => N(toNumber(eval(env,e1)) * toNumber(eval(env,e2)))
          case Div   => N(toNumber(eval(env,e1)) / toNumber(eval(env,e2)))
          case Eq    => B(eval(env,e1) == eval(env,e2))
          case Ne    => B(eval(env,e1) != eval(env,e2))
          case Lt    => B(toNumber(eval(env,e1)) < toNumber(eval(env,e2)))
          case Le    => B(toNumber(eval(env,e1)) <= toNumber(eval(env,e2)))
          case Gt    => B(toNumber(eval(env,e1)) > toNumber(eval(env,e2)))
          case Ge    => B(toNumber(eval(env,e1)) >= toNumber(eval(env,e2)))
          case And => ???
          case Or => ???
          case Seq => ???
        }
      }
      /* Inductive Cases */
      case Print(e1) => println(pretty(eval(env, e1))); Undefined
      case If(e1, e2, e3) => ???

      case _ => ???
    }
  }



  /* Interface to run your interpreter from the command-line.  You can ignore what's below. */
  def processFile(file: java.io.File) {
    if (debug) { println("Parsing ...") }

    val expr = Parser.parseFile(file)

    if (debug) {
      println("\nExpression AST:\n  " + expr)
      println("------------------------------------------------------------")
    }

    if (debug) { println("Evaluating ...") }

    val v = eval(expr)

     println(pretty(v))
  }

}

import mill._
import $ivy.`com.lihaoyi::mill-contrib-playlib:`,  mill.playlib._

object playlearning extends RootModule with PlayModule {

  def scalaVersion = "3.3.5"
  def playVersion = "3.0.7"
  def twirlVersion = "2.0.1"

  object test extends PlayTests
}

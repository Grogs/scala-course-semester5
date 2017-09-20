package fss

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("App")
class App extends JSApp {

  @JSExport
  def main(): Unit = {
    println("Hello from Scala.js")
  }

}

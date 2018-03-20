package fss

import org.scalajs.dom.{Event, document}
import org.scalajs.dom.html.{Button, Input}
import services.hotels.HotelsService
import autowire._

import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

@JSExportTopLevel("App")
class App extends JSApp {

  def hotelsTables() = document.querySelector("table")
  def destination() = document.getElementById("destination").asInstanceOf[Input]
  def distance() = document.getElementById("distance").asInstanceOf[Input]
  def searchButton() = document.getElementById("load-hotels").asInstanceOf[Button]






  def notMain() = {

    hotelsTables().innerHTML = ""

    val res = Client[HotelsService].search("London", 5).call()
    val res2 = res.foreach( hotels =>
      println(hotels)
    )

    //Render table
    views.html.hotelsTable(Nil).body


    destination().onchange = (e: Event) => {}

    destination().addEventListener("<type>", (e: Event) => {
      destination().value
      //callService
      //renderTable
      //updateTable
    })
  }














































  @JSExport
  def main(): Unit = {

    //EXERCISE 1
    def reload(destination: String, distance: Double) = {
      for {
        hotels <- Client[HotelsService].search(destination, distance).call() //Note the .call()
        table = views.html.hotelsTable(hotels).body //Yay, reused code across frontend and backend!
      } hotelsTables().outerHTML = table
    }

    def handleChange(e: Event) = reload(destination().value, distance().value.toDouble)

    //Key up for when the user changes the text
    destination().onkeyup = handleChange _
    distance().onkeyup = handleChange _
    distance().onchange = _ => reload(destination().value, distance().value.toDouble) //to show you can use a lambda instead

    searchButton().style.display = "none"


    //For exercise 3, add `if hotels.nonEmpty` to the above for-comprehension in the reload function.

    //EXERCISE 2
    new Autocomplete(
      destination(),
      Seq("London", "Paris", "Bath"),
      _ => handleChange(null)
    )

  }

}

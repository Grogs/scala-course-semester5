package lesson5

import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.scalatest.matchers.{MatchResult, Matcher}
import org.scalatestplus.play._
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Helpers._
import play.api.test._

import scala.collection.JavaConverters._

class HotelsControllerSpec extends PlaySpec with GuiceOneAppPerTest {
  lazy val page = Jsoup.parse(route(app, FakeRequest(GET, "/hotels/search?destination=london&distance=1.2")).map(contentAsString).get)
  lazy val body = page.body.html

  "Basic search page" should {

    "send 200 on valid request" in {

      withClue("You need to add an entry to your routes file, and start implementing HotelsController.search\n") {

        route(app, FakeRequest(GET, "/hotels/search?destination=london&distance=5"))
          .map(status) mustBe Some(OK)
      }
    }

    "include distance and destination in the title" in {
      withClue("You can pass the endpoint's parameters to your view\n") {
        page.title must include("london")
        page.title must (include("1.2km") or include("1.2 kilometers"))
      }
    }

    "output the titles of the hotels found" in {

      withClue("Now you need to output your results. Use the search") {
        body must include("Park Plaza Westminster Bridge London")
        body must include("Club Quarters, Trafalgar Square")
      }
    }

    "send 400 on invalid distance, with a descriptive error message" in {

      withClue("Validate the distance in the Controller. You'll need to use play.api.mvc.Results.BadRequest.\n") {

        route(app, FakeRequest(GET, "/hotels/search?destination=london&distance=-5")).map(status) mustBe Some(BAD_REQUEST)

        route(app, FakeRequest(GET, "/hotels/search?destination=london&distance=-5")).map(contentAsString) mustBe Some("Invalid distance")
      }
    }

  }

  "Make the search page look nice with Bootstrap" should {
    lazy val tableColumns = page.body().select("th").asScala.map(_.text).toList

    "display results in a table" in {

      body must include("Park Plaza Westminster Bridge London")

      withClue("The hotels should be inside an HTML table element. See https://www.w3schools.com/html/html_tables.asp") {
        page.body must haveA ("table")
      }
      withClue("We need to add the `table` class to the table of hotels for bootstrap to make it look nice. See http://getbootstrap.com/docs/3.3/css/#tables") {
        page.body must haveA ("table.table")
      }
      withClue("The table should have a header. See https://www.w3schools.com/html/html_tables.asp") {
        tableColumns must not be empty
      }
    }

    "display images in the table" in {
      withClue("Your table must include an images column. Docs for HTML tables: https://www.w3schools.com/tags/tag_table.asp") {
        tableColumns must contain("Images")
      }

      withClue("Put the images for each hotel in the column you created using <img> tags: https://www.w3schools.com/tags/tag_img.asp") {
        val tableHtml = page.select("table").html
        tableHtml must include("http://exp.cdn-hotels.com/hotels/4000000/3120000/3113100/3113039/3113039_31_y.jpg")
      }
    }

    "include a link to the hotel on Google Maps" in {

      withClue("Add a new column for a link to the hotel on google maps") {
        tableColumns must contain ("Location")
      }

      withClue("Add a link using the <a> tag. Look at the test to see how to create the link.") {
        val googleMapsSearchQuery = "Park Plaza Westminster Bridge London near 51.501108,-0.117331"
        body must include(s"""<a href="http://maps.google.com/?q=$googleMapsSearchQuery">""")
      }
    }
  }

  "Let the user change their search" should {
    lazy val form = page.select("form").last()

    "add a form to allow the user to change their search" in {
      withClue("Add a form element. See bootstrap docs for forms: http://getbootstrap.com/docs/3.3/css/#forms") {
        page.body must haveA ("form")
      }

      withClue("See HTML form example: https://www.w3schools.com/tags/att_form_method.asp. ") {
        withClue("Your form should include an input for the destination and distance.") {
          form must haveA ("input[name=destination]")
          form must haveA ("input[name=distance]")
        }

        withClue("The inputs should contain the values the user searched with: https://www.w3schools.com/tags/att_input_value.asp") {
          form must haveA ("input[value=london]")
          form must haveA ("input[value=1.2]")
        }

        withClue("There should be a search button that submits the form") {
          form must haveA ("button[type=submit]")
        }

        withClue("Make sure your form submits a GET when the user clicks the submit button.") {
          form.attr("method") mustBe "get"
        }
      }
    }
  }

  def haveA(selector: String) = new Matcher[Element] {
    def apply(e: Element) = {
      MatchResult(
        ! e.select(selector).isEmpty,
        s"Your ${e.tagName} element did not include the required element. Here's what you have:\n${e.html}",
        s"Your ${e.tagName} included an element matching $selector"
      )
    }
  }
}

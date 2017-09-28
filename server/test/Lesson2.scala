import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.FakeRequest
import play.api.test.Helpers._

class Lesson2 extends PlaySpec with GuiceOneAppPerTest {

  "HomeController" should {

    "have an endpoint to return the time" in {
      val time = route(app, FakeRequest(GET, "/time")).get

      withClue(
        "You will need to add a new entry to the routes file, and a method to a Controller") {
        status(time) mustBe OK
      }
    }

    "actually return the time" in {
      val time = route(app, FakeRequest(GET, "/time")).get

      withClue("You should return the current time. You can get this from System.currentTimeMillis") {
        contentAsString(time) must include(System.currentTimeMillis().toString.take(8))
      }
    }

    "return the time as HTML" in {
      val time = route(app, FakeRequest(GET, "/time")).get

      withClue("Use a view to render this page") {
        contentType(time) mustBe Some("text/html")
      }
    }

  }

}

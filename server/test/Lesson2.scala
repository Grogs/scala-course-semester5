import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.FakeRequest
import play.api.test.Helpers._

class Lesson2 extends PlaySpec with GuiceOneAppPerTest {

  "HomeController" should {

    "have an endpoint to return the time" in {
      val time = route(app, FakeRequest(GET, "/time")).get

      withClue("You will need to add a new entry to the routes file, and a method to a Controller: https://www.playframework.com/documentation/2.6.x/ScalaRouting") {

        status(time) mustBe OK
      }
    }

    "actually return the time" in {
      val time = route(app, FakeRequest(GET, "/time")).get

      withClue("You should return the current time. You can get this from System.currentTimeMillis") {

        status(time) mustBe OK
        contentAsString(time) must include(System.currentTimeMillis().toString.take(8))
      }
    }

    "return the time as HTML" in {
      val time = route(app, FakeRequest(GET, "/time")).get

      withClue("Create a view for this page which takes the time and outputs it. See Twirl template guide: https://www.playframework.com/documentation/2.6.x/ScalaTemplates") {

        contentType(time) mustBe Some("text/html")
      }
    }

  }

}

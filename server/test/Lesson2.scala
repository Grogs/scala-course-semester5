import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.FakeRequest
import play.api.test.Helpers._

class Lesson2 extends PlaySpec with GuiceOneAppPerTest {

  "HomeController" should {

    "have an endpoint to return the time" in {
      val time = route(app, FakeRequest(GET, "/time")).get

      status(time) mustBe OK
    }

    "actually return the time" in {
      val time = route(app, FakeRequest(GET, "/time")).get

      status(time) mustBe OK
      contentAsString(time) must include (System.currentTimeMillis().toString.take(8))
    }

    "return the time as HTML" in {
      val time = route(app, FakeRequest(GET, "/time")).get

      status(time) mustBe OK
      contentType(time) mustBe Some("text/html")
    }

  }

}

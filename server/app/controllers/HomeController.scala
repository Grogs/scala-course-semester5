package controllers

import javax.inject._

import play.api.libs.ws.WSClient
import play.api.mvc._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()  (ws: WSClient) extends InjectedController {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index = Action {
    Ok(views.html.index("Welcome to Full Stack Scala"))
  }

  def time = Action {
    Ok(views.html.index(System.currentTimeMillis().toString))
  }

}

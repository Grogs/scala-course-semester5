package controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{Action, InjectedController}
import services.hotels.HotelsServiceImpl

@Singleton
class HotelsController @Inject()(hotelsService: HotelsServiceImpl, webJarAssets: WebJarAssets) extends InjectedController {

  def search(destination: String, radius: String) = Action {

    val distance = radius.toDouble

    if (distance > 0) {
      Ok(
        views.html.search(
          destination, radius,
          hotelsService.search(destination, distance),
          webJarAssets
        )
      )
    } else {
      BadRequest("Invalid distance")
    }
  }

}

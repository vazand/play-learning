package controllers

import javax.inject._
import play.api.mvc.ControllerComponents
import play.api.mvc.AbstractController
import play.api.Configuration

@Singleton
class LearnConfigController @Inject() (
    cc: ControllerComponents,
    conf: Configuration
) extends AbstractController(cc) {

  // get a string using configuration
  def getHorlicks() = Action {
    val horlicks = conf.get[String]("HORLICKS")
    Ok(horlicks)
  }

  //get list of string using configuration
  def getAllAvailableGroceryItems() = Action {
    val available_items = conf.get[Seq[String]]("store.grocery.available")
    Ok(s"All available Grocery items in the store : ${available_items.mkString(",")}")
  }

}

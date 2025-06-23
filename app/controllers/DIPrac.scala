package controllers

import play.api.libs.json.{JsArray, JsString}
import play.api.libs.json.Json._

import javax.inject._
import play.api.mvc._

//guice

@Singleton
class DIPrac @Inject()(cc: ControllerComponents, gp: GroceryProducts) extends AbstractController(cc) {


  def get(): Action[AnyContent] = Action{
    Ok(gp.getAll).as("application/json")
  }

}

class GroceryProducts @Inject() {
  def getAll: JsArray = arr(obj("name"->"milk","quantity"->2),obj("name"->"coffee powder","quantity"->5))
}



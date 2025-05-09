package controllers

import javax.inject._
import play.api.mvc._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json.Json
import play.api.libs.json.Format
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsError

@Singleton
class ActionsPractices @Inject() (controllerComponents: ControllerComponents)
    extends AbstractController(controllerComponents) {
  // Actions
  // Base Controller
  // Abstract Controller

  // val addOne: Int => Int = x => x+1
  // an action takes a requet and generats a respose

  def welcome() = Action.async {
    Future(
      Ok("welcome to the store of evverything")
    )
  }

  def Electronics() = Action { implicit request: Request[AnyContent] =>
    {
      val userState = request.cookies.get("knownUser")
      userState match {
        case None => {
          val store = List("Grocery", "Electronics", "Furniture")
          Ok(s" This is ${store(1)} section")
            .withCookies(Cookie("knownUser", "yes"))
        }
        case Some(value) => {
          val items = List("Mobile", "Laptop", "Computers")
          Ok(s"Available electronics ${items.mkString(",")}")
        }
      }

    }
  }

  def Furniture() = Action {
    // NotFound("Not found the page you are looking for")
    // BadRequest("Bad Request")
    InternalServerError("Oops!")
  }
  // Cookie, Session, Flash msg
  // def Furniture() = TODO

  // a post request
  // check the item available or not

  def checkItemExists() = Action(parse.json).async { implicit req =>
    {
      Future {
        val data = req.body.validate[ElectronicItem]
        data match{
          case JsSuccess(value, _) => {
            println(s"value: $value")
            val items = List("Mobile", "Laptop", "Computers")
            if (items.contains(value.name)) {
              Ok(s"found ${value.name} in the electronic section")
            } else {
              NotFound("Item not found!")
            }
            
          }
          case JsError(errors) => BadRequest("check the Json Body")
        }

      }
    }

  }

}

case class ElectronicItem(name: String)
object ElectronicItem {
  implicit val format: Format[ElectronicItem] = Json.format[ElectronicItem]
}

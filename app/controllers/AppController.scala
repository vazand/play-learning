package controllers

import javax.inject.Inject
import play.api.mvc.ControllerComponents
import play.api.mvc.AbstractController
import play.api.mvc.Request
import play.api.mvc.AnyContent
import play.api.libs.json.Format
import play.api.libs.json.Json

@javax.inject.Singleton
class AppController @Inject() (cc: ControllerComponents)
    extends AbstractController(cc) {

  implicit val format: Format[User] = Json.format[User]
  def users() = Action { implicit request: Request[AnyContent] =>
    {
      val title = "Mobile users"
      val list = List(User("Luffy"), User("Zoro"), User("Sanji")).map(user =>
        Json.toJson(user).toString
      )
      Ok(views.html.userhtml(title, list))
    }
  }

}

case class User(name: String)

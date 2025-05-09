package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

@Singleton
class MyFormController @Inject()(cc: MessagesControllerComponents)
    extends MessagesAbstractController(cc) {
      
  val userForm: Form[UserData] = Form(
    mapping(
      "name" -> text,
      "age" -> number
    )(UserData.apply)(UserData.unapply)
  )
  def showForm() = Action { implicit rquest :MessagesRequest[AnyContent] =>
    val postUrl:Call = routes.MyFormController.userPost()
    println(postUrl.absoluteURL())
    Ok(views.html.userFormTwirl(userForm,postUrl))
  }

  def userPost() = Action { implicit request:MessagesRequest[AnyContent] =>
    {
      userForm.bindFromRequest().fold(
      formWithErrors => {
        BadRequest("WrongData")
      },
      userData => {
        Ok(s"Hello ${userData.name}")
      }
      )
    }
  }

}
case class UserData(name: String, age: Int)
object UserData {
  def unapply(u: UserData): Option[(String, Int)] = Some((u.name, u.age))
}


package controllers
import javax.inject._
import play.api.mvc._
import scala.concurrent.ExecutionContext
import models.UserRepository
import play.api.libs.json.Json
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.util.ArrayList
import play.api.libs.json.JsArray


@Singleton
class UserController @Inject()(cc: ControllerComponents, userRepo: UserRepository)
    extends AbstractController(cc) {

  implicit val ec: ExecutionContext = cc.executionContext

  def listUsers = Action {
    val data = (Await.result(userRepo.all(),Duration.Inf)).map(x => Json.toJson(x))
    val jsArr = JsArray(data)
    Ok(jsArr)
  }

  def getUser(id: Long) = Action.async {
    userRepo.getById(id).map {
      case Some(user) => Ok(Json.toJson(user))
      case None       => NotFound(Json.obj("error" -> "User not found"))
    }
  }

  def createUser = Action(parse.json).async { request =>
    (request.body \ "name").asOpt[String] match {
      case Some(name) =>
        userRepo.create(name).map(user => Created(Json.toJson(user)))
      case None =>
        Future.successful(BadRequest(Json.obj("error" -> "Missing name")))
    }
  }

  def updateUser(id: Long) = Action(parse.json).async { request =>
    (request.body \ "name").asOpt[String] match {
      case Some(newName) =>
        userRepo.update(id, newName).map {
          case 0 => NotFound(Json.obj("error" -> "User not found"))
          case _ => Ok(Json.obj("status" -> "updated"))
        }
      case None =>
        Future.successful(BadRequest(Json.obj("error" -> "Missing name")))
    }
  }

  def deleteUser(id: Long) = Action.async {
    userRepo.delete(id).map {
      case 0 => NotFound(Json.obj("error" -> "User not found"))
      case _ => Ok(Json.obj("status" -> "deleted"))
    }
  }
}

package controllers

import javax.inject._
import play.api.mvc._
import actors.UserActor

import scala.concurrent.ExecutionContext
import models.UserRepository
import org.apache.pekko.actor.{ActorRef, ActorSystem}
import org.apache.pekko.event.slf4j.SLF4JLogging
import play.api.libs.json.Json

import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import java.util.ArrayList
import play.api.libs.json.JsArray
import org.apache.pekko.pattern.ask
import org.apache.pekko.util.Timeout

import scala.concurrent.duration._

@Singleton
class UserController @Inject()(cc:       ControllerComponents,
                               userRepo: UserRepository,
                               system:   ActorSystem
                              )(implicit ec: ExecutionContext)
  extends AbstractController(cc) with SLF4JLogging{


  //  def listUsers = Action {
  //    val data = (Await.result(userRepo.all(),Duration.Inf)).map(x => Json.toJson(x))
  //    val jsArr = JsArray(data)
  //    Ok(jsArr)
  //  }


  private val userActor: ActorRef = system.actorOf(UserActor.props(userRepo), "user-actor")
  implicit val to: Timeout = Timeout(20.seconds)

  def listUsers = Action.async {
    (userActor ? UserActor.GetAll).map {
      case ud: Seq[models.User] => {
        ud.map(x => Json.toJson(x))
        Ok(Json.toJson(ud))
      }
    }.recover {
      case ex: Exception => InternalServerError(s"Error occurred: ${ex.getMessage}")
    }
  }

  def getUser(id: Long) = Action.async {
    userRepo.getById(id).map {
      case Some(user) => Ok(Json.toJson(user))
      case None =>{
        log.error(s"Data not found for this id $id")
        NotFound(Json.obj("error" -> "User not found"))
      }
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

  def oldUpdateUser(id: Long) = Action(parse.json).async { request =>
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

  def updateUser(id: Long) = Action(parse.json).async { request =>
    (request.body \ "name").asOpt[String] match {
      case Some(newName) =>
        val user = models.User(id = id, name = newName)
        (userActor ? UserActor.Update(user)).map{
          case 0 => NotFound(Json.obj("error" -> "User not found"))
          case _ => Ok(Json.obj("status" -> "updated"))
        }
      case None =>
        Future.successful(BadRequest(Json.obj("error" -> "Missing name")))
    }
  }

  def deleteUser(id: Long) = Action.async {
    (userActor ? UserActor.Delete(id.toInt)).map {
      case dr: Int => Ok(Json.obj("status"->"deleted"))
    }.recover {
      case ex: Exception => InternalServerError(s"Error occurred: ${ex.getMessage}")
    }

//
//    userRepo.delete(id).map {
//      case 0 => NotFound(Json.obj("error" -> "User not found"))
//      case _ => Ok(Json.obj("status" -> "deleted"))
//    }
  }
}




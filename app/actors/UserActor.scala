package actors

import models.{User, UserRepository}
import org.apache.pekko.actor.{Actor, ActorLogging, Props}
import org.apache.pekko.pattern.pipe

import scala.concurrent.ExecutionContext.Implicits.global

class UserActor(userRepo: UserRepository) extends Actor with ActorLogging {

  import UserActor._

  override def receive: Receive = {
    case GetAll => {
      val replyTo = sender()
      //userRepo.all().pipeTo(replyTo)
      val usersData = userRepo.all()
      for {
        users <- usersData
      } yield{
        log.info("got users {}",users)
        replyTo  ! users
      }
    }
    case Delete(id) => {
      val senderRef = sender()
      for{
        res <-  userRepo.delete(id)
      } yield {
        log.debug(s"res $res")
        senderRef ! res
      }
    }
    case Update(user) => {
      val senderRef = sender()
      val process = userRepo.update(user.id,user.name)
      for{
        res <- process
      } yield{
        senderRef ! res
      }

    }
  }
}
object UserActor {
  case object GetAll
  case class Delete(id:Int)
  case class Update(user:User)
  def props(userRepo: UserRepository): Props = Props(new UserActor(userRepo))
}




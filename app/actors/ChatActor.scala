package actors

import org.apache.pekko.pattern._
import org.apache.pekko.actor._

class ChatActor(out: ActorRef, room: ActorRef) extends Actor {

  import ChatActor._

  override def receive: Receive = {
    case msg: String => {
      room ! ChatRoom.Talk(msg)
    }
    case ChatRoom.Message(msg) => {
      out ! msg
    }
  }

  override def preStart(): Unit = {
    room ! ChatRoom.Join(self)
  }

  override def postStop(): Unit = room ! ChatRoom.Leave(self)

}

object ChatActor {
  def props(out: ActorRef, room: ActorRef): Props = Props[ChatActor](new ChatActor(out, room))

  def chatRoom: Props = Props[ChatRoom]
}


class ChatRoom extends Actor with ActorLogging {

  import ChatRoom._

  var members: Set[ActorRef] = Set.empty

  override def receive: Receive = {
    case Join(user) => {

      members += user
      println(s"members $members")
      log.info(s"members $members")
    }
    case Leave(user) => {
      members -= user
    }
    case Talk(msg) => {
      members.foreach(_ ! Message(msg))
    }
  }
}

object ChatRoom {
  case class Join(user: ActorRef)

  case class Leave(user: ActorRef)

  case class Talk(msg: String)

  case class Message(msg: String)

}

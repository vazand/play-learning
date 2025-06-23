package actors

import org.apache.pekko.actor.{Actor, ActorRef, Props}

class MyActor(out:ActorRef) extends Actor{
  override def receive: Receive = {

    case msg: String => {
      //val senderRef = sender()
      println(msg)
      out ! s"received: $msg"
    }
  }

}
object MyActor {
  def props(out: ActorRef) = Props(new MyActor(out))
}
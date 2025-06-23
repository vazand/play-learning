package controllers

import actors.{ChatActor, MyActor}
import org.apache.pekko.actor.ActorSystem
import org.apache.pekko.stream.{Materializer, OverflowStrategy}
import org.apache.pekko.stream.scaladsl._
import play.api.Play.materializer
import play.api.libs.streams.ActorFlow

import javax.inject._
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class WSController @Inject()(cc: ControllerComponents)(implicit system: ActorSystem, mat: Materializer) extends
  AbstractController(cc) {

  def ws(): WebSocket = WebSocket.accept[String, String] {
    request => {
      //Sink
      val in = Sink.foreach[String](println)
      //Source
      val out = Source.single(s" I don't care about your request").concat(Source.maybe)

      Flow.fromSinkAndSource(in, out)
    }
  }

  def gws(): WebSocket = WebSocket.accept[String, String] {
    request => {
      // Create a Source.queue to push messages to the client
      val (queue, source): (SourceQueueWithComplete[String], Source[String, _]) =
        Source.queue[String](bufferSize = 10, OverflowStrategy.dropHead).preMaterialize()

      // Define a Sink that pushes each incoming message to the queue (source)
      val sink: Sink[String, _] = Sink.foreach[String] { msg =>
        println(s"Received from client: $msg")
        queue.offer(s"Server echo: $msg") // echo back the message
      }
      Flow.fromSinkAndSource(sink, source)
    }

  }

  //  def ws() : WebSocket = WebSocket.accept[String, String] {
  //    request => {
  //      ActorFlow.actorRef{
  //        out => {
  //          MyActor.props(out)
  //        }
  //      }
  //    }
  //  }


  //  def ws(): WebSocket = WebSocket.accept[String, String] {
  //    //Sink
  //    request =>
  //      val in = Sink.foreach[String](message => {
  //        println(s"received $message")
  //      })
  //
  //      val out = Source.single("the message has been received by the server")
  //
  //      Flow.fromSinkAndSource(in, out)
  //  }


  private val chatRoom = system.actorOf(ChatActor.chatRoom, "chat-room")

  def chatSocket: WebSocket = WebSocket.accept[String, String] {
    request =>
      ActorFlow.actorRef(out => ChatActor.props(out, chatRoom))
  }

}

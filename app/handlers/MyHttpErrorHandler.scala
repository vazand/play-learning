package handlers

import javax.inject._
import play.api.mvc._
import play.api.http.HttpErrorHandler
import play.api.libs.json.Json._
import play.api.http._
import play.api.http.Status._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class MyHttpErrorHandler @Inject()(implicit ec: ExecutionContext) extends HttpErrorHandler {

// 400
  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    statusCode match {
      case BAD_REQUEST =>
        Future {
          val jsData = obj("message" -> "check the data - BadRequest")
          Results.Status(statusCode)(jsData)
        }


      case NOT_FOUND =>
        Future {
          val jsData = obj("message" -> "client side error - page not found")
          Results.Status(statusCode)(jsData)
        }
      case _ => Future {
        val jsData = obj("message" -> "client side error")
        Results.Status(statusCode)(jsData)
      }
    }
  }

//500
  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    Future{
      val jsData = obj("message"->"there is an internal server error")
      Results.InternalServerError(jsData)
    }
  }
}


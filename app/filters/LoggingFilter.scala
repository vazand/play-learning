package filters

import org.apache.pekko.stream.Materializer
import play.api.Logger
import play.api.mvc.{Filter, RequestHeader, Result}

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

class LoggingFilter @Inject()(implicit val mat: Materializer, ec: ExecutionContext)
  extends Filter {
  private val logger = Logger(this.getClass)
  private val startTime = System.currentTimeMillis()

  logger.info(s"logger name ${logger.underlyingLogger.getClass}")
  override def apply(next: RequestHeader => Future[Result])(request: RequestHeader): Future[Result] = {
    next(request).map {
      result => {
        val endTime = System.currentTimeMillis()
        val requestTime = endTime - startTime
        logger.info(s"Method: ${request.method}  Path: ${request.uri} statusCode: ${result.header.status} " +
                    s"RequestTime: ${requestTime}")
        result
      }

    }(ec)
  }


}


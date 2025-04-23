package controllers

import javax.inject.Inject
import play.api.mvc.ControllerComponents
import play.api.mvc.AbstractController
import play.api.mvc.Request
import play.api.mvc.AnyContent
import play.api.libs.json.Json

@javax.inject.Singleton
class RoutesPractices @Inject()(cc:ControllerComponents) extends AbstractController(cc){
  

    def users(userName:String) = Action{
        implicit request:Request[AnyContent] => {
            val js = Json.obj("name"->userName)
            Ok(js)
        }
    }

    def user(userName:String,age:Int) = Action{
        implicit request:Request[AnyContent] => {
            val fromQuery = request.getQueryString("age").get.toInt
            val requestHeaderInfo = request.headers.toString
            println(fromQuery)
            val js = Json.obj("name"->userName, "age"->age)
            Ok(requestHeaderInfo)
        }
    }
}

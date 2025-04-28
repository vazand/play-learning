package controllers

import javax.inject._ 
import play.api.mvc.ControllerComponents
import play.api.mvc.AbstractController

@Singleton
class AdminController @Inject()(cc:ControllerComponents) extends AbstractController(cc) {
  
    def info() = Action{
        Ok("I'm admin")
    }
}

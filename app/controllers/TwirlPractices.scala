package controllers


import javax.inject._
import play.api.mvc._
import play.twirl.api.Html

@Singleton
class TwirlPractices @Inject() (cc: ControllerComponents)
    extends AbstractController(cc) {

  def mobiles() = Action {
    val mobiles = List(
      Mobile("Apple 15", 1500000),
      Mobile("Redmi 14", 120000),
      Mobile("Samsung S24", 140000)
    )
    Ok(views.html.mobiles(mobiles.empty){ color =>
    Html(s"""<p style="color:$color">This message came from the controller</p>""")
  })
  }
}
case class Mobile(name: String, price: Int)

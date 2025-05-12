package models

import slick.jdbc.PostgresProfile.api._
import play.api.libs.json.Format
import play.api.libs.json.Json
import scala.concurrent.Future

case class User(id: Long, name: String)
object User{
  implicit val formatter: Format[User] = Json.format[User]
}

// FRM mapping
class UsersTable(tag: Tag) extends Table[User](tag, "users") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")

  def * = (id, name) <> ((User.apply _).tupled, User.unapply)


}


/* 
* Create DB in psql
* CREATE DATABASE store
* CREATE TABLE users (
*    id SERIAL PRIMARY KEY,
*    name VARCHAR NOT NULL
*);
*/
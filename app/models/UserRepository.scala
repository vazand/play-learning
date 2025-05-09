package models
import javax.inject._
import play.api.db.slick.HasDatabaseConfigProvider
import play.api.db.slick.DatabaseConfigProvider
import scala.concurrent.Future
import slick.jdbc.PostgresProfile

@Singleton
class UserRepository @Inject()(protected val dbConfigProvider: DatabaseConfigProvider)
  extends HasDatabaseConfigProvider[PostgresProfile] {

  import profile.api._
  val users = TableQuery[UsersTable]

  def all(): Future[Seq[User]] = db.run(users.result)
  
  def getById(id: Long): Future[Option[User]] =
    db.run(users.filter(_.id === id).result.headOption)

  def create(name: String): Future[User] = {
    val insertQuery = (users returning users.map(_.id)
      into ((user, id) => user.copy(id = id))) += User(0, name)
    db.run(insertQuery)
  }

  def update(id: Long, newName: String): Future[Int] =
    db.run(users.filter(_.id === id).map(_.name).update(newName))

  def delete(id: Long): Future[Int] =
    db.run(users.filter(_.id === id).delete)
}

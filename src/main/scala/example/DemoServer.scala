package example

import cats.effect._
import monix.eval.Task
import monix.execution.Scheduler
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.blaze.BlazeServerBuilder

object DemoServer extends Http4sDsl[Task] {
  implicit val scheduler: Scheduler = Scheduler.Implicits.global
  implicit val timer: Timer[Task]   = Task.timer(scheduler)
  val port: Int                     = 9010
  val hostname: String              = "localhost"
  val url: String                   = s"http://$hostname:$port/"
  val myService: HttpRoutes[Task]   = HttpRoutes.of[Task] { case GET -> Root => Ok() }

  def run(): Task[Unit] = {
    val services = myService
    val httpApp  = Router("/" -> services).orNotFound

    BlazeServerBuilder[Task]
      .bindHttp(port, hostname)
      .withHttpApp(httpApp)
      .serve
      .compile
      .drain
  }
}

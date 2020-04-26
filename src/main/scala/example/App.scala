package example

import cats.effect.{ExitCode, IO, IOApp}
import monix.execution.Scheduler
import cats.implicits._
import monix.eval.Task

object App extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    implicit val scheduler: Scheduler = Scheduler.traced
    Task.gather(List(
      DemoServer.run(),
      DemoClient.run()
    )).as(ExitCode.Success).to[IO]
  }
}

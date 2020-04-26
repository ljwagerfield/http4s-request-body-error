package example

import cats.implicits._
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import org.http4s.client.Client
import org.http4s.client.asynchttpclient.AsyncHttpClient
import org.http4s.{Method, Request, Uri}

object DemoClient {
  def run(): Task[Unit] =
    runTestsWithNewClient()
      .map { _ =>
        println("Re-run me a few  times, and hopefully you'll see the error!")
      }

  private def runTestsWithNewClient(): Task[Unit] =
    AsyncHttpClient.resource[Task]().use(runTests)

  private def runTests(client: Client[Task]): Task[Unit] = {
    val url = DemoServer.url
    val requests: List[Request[Task]] = List(
      Request(
        Method.GET,
        Uri.unsafeFromString(url)
      ),
      Request(
        Method.GET,
        Uri.unsafeFromString(url)
      ),
      Request(
        Method.DELETE,
        Uri.unsafeFromString(url)
      ),
      Request(
        Method.DELETE,
        Uri.unsafeFromString(url)
      ),
      Request(
        Method.POST,
        Uri.unsafeFromString(url),
      ).withEntity(
        "BANG!"
      ),
      Request(
        Method.GET,
        Uri.unsafeFromString(url)
      ),
    )

    requests.traverse_ { request =>
      client
        .fetch(request)(_ => Task {
          println(s"Success: ${request.method} ${request.uri}")
        })
    }
  }
}

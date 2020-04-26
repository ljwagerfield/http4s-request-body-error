package example

import monix.execution.Scheduler.Implicits.global
import monix.eval.Task
import org.http4s.client.Client
import org.http4s.client.asynchttpclient.AsyncHttpClient
import cats.implicits._
import org.http4s
import org.http4s.headers.{`Content-Length`, `Content-Type`}
import org.http4s.{Headers, MediaType, Method, Request, Uri}

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
      ), {
        val payload       = 2.toString
        val contentType   = `Content-Type`(MediaType.unsafeParse("text/plain"), http4s.DefaultCharset)
        val bytes         = payload.getBytes(contentType.charset.getOrElse(http4s.DefaultCharset).nioCharset)
        val contentLength = `Content-Length`.unsafeFromLong(bytes.length.toLong)
        val stream        = fs2.Stream.emits(bytes)
        Request(
          Method.POST,
          Uri.unsafeFromString(url),
          body    = stream,
          headers = Headers(List(contentType, contentLength))
        )
      },
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

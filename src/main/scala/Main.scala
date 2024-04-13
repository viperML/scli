import cats.effect.IO
import epollcat.EpollApp
import cats.effect.Sync
import cats.implicits._

import scala.scalanative.unsafe._
import org.http4s.ember.client.EmberClientBuilder

import scribe.cats.io

@extern
object libc {
  def unshare(size: CInt): CInt = extern
}

object Main extends EpollApp.Simple {
  def run: IO[Unit] = {
    val x = scribe.cats.io.info("Hello, World!")
    val y = IO.println("Goodbye!")

    val client = EmberClientBuilder.default[IO].build

    val z = client.use { client =>
      {
        for {
          res <- client.expect[String]("https://httpbin.org")
          _ <- io.info(res)
        } yield ()
      }
    }

    x *> y *> z
  }
}

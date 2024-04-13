import cats.effect.IO
import epollcat.EpollApp
import cats.effect.Sync
import cats.implicits._

import scala.scalanative.unsafe._

@extern
object libc {
  def unshare(size: CInt): CInt = extern
}

object Main extends EpollApp.Simple {
  def run: IO[Unit] = {
    val x = scribe.cats.io.info("Hello, World!")
    val y = IO.println("Goodbye!")

    x *> y
  }
}

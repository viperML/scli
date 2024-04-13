// object Main {
//   def main(args: Array[String]): Unit =
//     println("Hello, world!")
// }

// import cats.effect.IOApp
import cats.effect.IO
import epollcat.EpollApp

object Main extends EpollApp.Simple {
  val run = IO.println("Hello, World!")
}


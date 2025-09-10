import cats.effect.{IO, IOApp}
import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.Logger

import org.slf4j.LoggerFactory

object PlainSlf4jTest extends App {
  val logger = LoggerFactory.getLogger("PlainSlf4jTest")
  logger.info("Hello from SLF4J")
}


object TestLogging extends IOApp.Simple {
  given Logger[IO] = Slf4jLogger.getLogger[IO]

  def run: IO[Unit] = for {
    _ <- Logger[IO].info("Hello INFO")
    _ <- Logger[IO].warn("Hello WARN")
    _ <- Logger[IO].error("Hello ERROR")
    _ <- Logger[IO].debug("Hello DEBUG")
  } yield ()
}

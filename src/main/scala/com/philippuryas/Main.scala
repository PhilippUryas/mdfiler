package com.philippuryas

import cats.effect.{IO, IOApp}
import cats.implicits.toSemigroupKOps
import com.comcast.ip4s.*
import com.philippuryas.controller.MdFileController
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.server.middleware.Logger


object Main extends IOApp.Simple {

  private val httpAppWithLogs =
    Logger.httpApp(true, true)(MdFileController.unauthorizedRoutes.orNotFound)

  val run: IO[Unit] = for {
    _ <- EmberServerBuilder.default[IO]
      .withHost(host"0.0.0.0")
      .withPort(port"8082")
      .withHttpApp(httpAppWithLogs)
      .build
      .use(_ => IO.never)
  } yield ()
}
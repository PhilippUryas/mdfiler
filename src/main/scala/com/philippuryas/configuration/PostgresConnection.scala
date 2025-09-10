package com.philippuryas.configuration

import cats.*
import cats.effect.*
import doobie.*
import doobie.util.log.LogEvent
import doobie.util.transactor.Transactor.Aux

object PostgresConnection {
  //ToDo: configuration
  val dbUrl = "jdbc:postgresql://localhost:5432/mdfiler"
  val dbUser = "admin"
  val dbPassword = "admin"


  //ToDo: better logging
  private val printSqlLogHandler: LogHandler[IO] = (logEvent: LogEvent) => IO {
    println(logEvent.sql)
  }

  val connection: Aux[IO, Unit] =
    Transactor.fromDriverManager[IO](
      driver = "org.postgresql.Driver",
      url = dbUrl,
      user = dbUser,
      password = dbPassword,
      logHandler = Some(printSqlLogHandler)
    )
}

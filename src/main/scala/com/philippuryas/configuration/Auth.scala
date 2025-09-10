package com.philippuryas.configuration


import cats.data.{Kleisli, OptionT}
import cats.effect.IO
import cats.implicits.*
import org.http4s.*
import org.http4s.server.*

case class User(id: Long, name: String)

object Auth {
  private val users = Map(
    "token123" -> User(1, "Philipp"),
    "token456" -> User(2, "Bob")
  )

  private type OptionIO[A] = OptionT[IO, A]

  private val authUser: Kleisli[OptionIO, Request[IO], User] =
    Kleisli { req =>
      OptionT {
        IO {
          req.headers
            .get[headers.Authorization]
            .collect {
              case headers.Authorization(Credentials.Token(_, token)) => users.get(token)
            }
            .flatten
        }
      }
    }

  val middleware: AuthMiddleware[IO, User] =
    AuthMiddleware(authUser)

}
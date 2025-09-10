package com.philippuryas.controller

import cats.effect.IO
import com.philippuryas.configuration.{Auth, User}
import com.philippuryas.dto.MdFileDto
import com.philippuryas.entity.MDEntity
import com.philippuryas.service.MdFileService
import io.circe.generic.auto.*
import io.circe.syntax.*
import io.circe.{Decoder, Encoder}
import org.http4s.*
import org.http4s.circe.*
import org.http4s.dsl.io.*
import org.typelevel.log4cats.*
import org.typelevel.log4cats.slf4j.Slf4jLogger


object MdFileController {

  implicit val mdDecoder: EntityDecoder[IO, MDEntity] = jsonOf[IO, MDEntity]
  implicit val mdDtoDecoder: EntityDecoder[IO, MdFileDto] = jsonOf[IO, MdFileDto]

  given Logger[IO] = Slf4jLogger.getLogger[IO]

  val unauthorizedRoutes: HttpRoutes[IO] = HttpRoutes.of {
    case GET -> Root / "index.html" =>
      StaticFile
        .fromResource("/static/index.html", Some(org.http4s.Request[IO]()))
        .getOrElseF(NotFound())

    case GET -> Root / "hello" =>
      Ok("Hello from MdFileController!")

    case GET -> Root / "file" / LongVar(id) =>
      for {
        md <- MdFileService.get(id)
        resp <- md match
          case Right(data) => data match {
            case Some(someData) => Ok(someData.asJson)
            case None => NotFound()
          }
          case Left(ex) => {
            Logger[IO].error(ex.getMessage)
            InternalServerError(ex.getMessage)
          }
      } yield resp
    case GET -> Root / "all" / LongVar(id) =>
      for {
        listMd <- MdFileService.getAll(1)
        resp <- listMd match {
          case Right(data) => Ok(data.asJson)
          case Left(ex) => {
            Logger[IO].error(ex.getMessage)
            InternalServerError(ex.getMessage)
          }
        }
      } yield resp

    case req@POST -> Root / "file" =>
      for {
        _ <- Logger[IO].info(s"POST /file with body: ${req}")
        md <- req.as[MdFileDto]
        dbResp <- MdFileService.save(1, md)
        resp <- dbResp match
          case Right(_) => Ok()
          case Left(ex) => {
            Logger[IO].error("exception")
            NotAcceptable()
          }

      } yield resp

  }
}

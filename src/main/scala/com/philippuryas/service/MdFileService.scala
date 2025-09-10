package com.philippuryas.service

import cats.effect.IO
import com.philippuryas.configuration.PostgresConnection
import com.philippuryas.dto.MdFileDto
import com.philippuryas.entity.{MDEntity, MDSaveEntity}
import com.philippuryas.repository.MdRepository
import doobie.implicits.*
import doobie.util.transactor.Transactor.Aux

object MdFileService {

  val connection: Aux[IO, Unit] = PostgresConnection.connection

  def save(userId: Long, mdFileDto: MdFileDto): IO[Either[Throwable, Int]] =
    MdRepository
      .save(
        MDSaveEntity(
          userId = userId,
        fileName = mdFileDto.fileName,
        fileData = mdFileDto.fileData)
      )

  def get(id: Long): IO[Either[Throwable, Option[MDEntity]]] =
    MdRepository
      .get(id)


  def getAll(userId: Long): IO[Either[Throwable, List[MDEntity]]] =
    MdRepository
      .getAllByUserId(userId)

  def deleteFile(fileName: String, userId: Long): IO[Either[Throwable, Int]] =
    MdRepository.deleteFile(fileName, userId)

}

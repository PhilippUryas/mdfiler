package com.philippuryas.repository

import cats.effect.IO
import com.philippuryas.configuration.PostgresConnection
import com.philippuryas.entity.{MDEntity, MDSaveEntity}
import doobie.Write
import doobie.implicits.*
import doobie.util.transactor.Transactor.Aux

given Write[MDEntity] =
  Write[(Long, Long, String, String)]
    .contramap(c => (c.fileId, c.userId, c.fileName, c.fileData))

given Write[MDSaveEntity] =
  Write[(Long, String, String)]
    .contramap(c => (c.userId, c.fileName, c.fileData))

object MdRepository {

  val connection: Aux[IO, Unit] = PostgresConnection.connection

  def save(md: MDSaveEntity): IO[Either[Throwable, Int]] =
    sql"""INSERT INTO md_files (user_id, file_name, file_data)
         VALUES (${md});"""
      .update
      .run
      .transact(connection)
      .attempt


  def get(id: Long): IO[Either[Throwable, Option[MDEntity]]] =
    sql"""SELECT id, user_id, file_name, file_data FROM md_files WHERE id = ${id}"""
      .query[MDEntity]
      .option
      .transact(connection)
      .attempt


  def getAllByUserId(user_id: Long): IO[Either[Throwable, List[MDEntity]]] =
    sql"""SELECT *
      FROM (
        SELECT *,
           ROW_NUMBER() OVER (PARTITION BY file_name ORDER BY insert_date DESC) AS rn
        FROM md_files
      ) sub
      WHERE rn = 1 and user_id = ${user_id};
      """
      .query[MDEntity]
      .to[List]
      .transact(connection)
      .attempt

  def getFilesByFileNameAndUserId(file_name: String, user_id: Long): IO[Either[Throwable, List[MDEntity]]] =
    sql"""SELECT
      |    file_name,
      |    file_hash,
      |    prev_hash,
      |    insert_date,
      |    ROW_NUMBER() OVER (PARTITION BY file_name ORDER BY insert_date ASC) AS version_number
      |FROM md_files
      |WHERE file_name = '${file_name}' and user_id = ${user_id}
      |ORDER BY version_number;
      |"""
      .query[MDEntity]
      .to[List]
      .transact(connection)
      .attempt
  //  def getPrevFile(curHash: String) : IO[Either[Throwable, MDEntity]] =
  //    sql"""SELECT user_id, file_name, file_data FROM md_files WHERE """

}

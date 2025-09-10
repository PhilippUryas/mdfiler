package com.philippuryas.entity

import io.circe.generic.auto.*

case class MDEntity(fileId: Long, userId: Long, fileName: String, fileData: String)

case class MDSaveEntity(userId: Long, fileName: String, fileData: String)

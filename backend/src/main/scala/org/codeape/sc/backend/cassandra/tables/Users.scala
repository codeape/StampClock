package org.codeape.sc.backend.cassandra.tables

import java.util.UUID

import com.outworkers.phantom.dsl._

import scala.concurrent.Future

case class User(
  id: UUID,
  hash: String,
  salt: String,
  name: String,
  email: String
)

abstract class Users extends Table[Users, User] {

  object id extends  UUIDColumn with PartitionKey
  object hash extends StringColumn
  object salt extends StringColumn
  object name extends StringColumn
  object email extends StringColumn

  def createUsers(): Future[Seq[ResultSet]] = {
    this.create.ifNotExists().future()
  }

}

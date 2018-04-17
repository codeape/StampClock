package org.codeape.sc.backend.cassandra

import com.outworkers.phantom.database.Database
import com.outworkers.phantom.connectors.CassandraConnection
import org.codeape.sc.backend.cassandra.tables.Users

class StampClockDatabase(override val connector: CassandraConnection)
  extends Database[StampClockDatabase](connector)
{

  object users extends Users with Connector

  def createTables(): Unit = {
    users.createUsers()
  }

}

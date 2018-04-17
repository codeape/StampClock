package org.codeape.sc.backend.config

import com.typesafe.config.Config
import scala.collection.JavaConverters._
import configs.syntax._

case class CassandraConf(hosts: Seq[String], port: Int, keySpace: String)

class StampClockConfig(config: Config) {

  val cassandra: CassandraConf = CassandraConf(
    hosts = config.getStringList("cassandra.hosts").asScala,
    port = config.get[Int]("cassandra.port").value,
    keySpace = config.get[String]("cassandra.key-space").value
  )

}

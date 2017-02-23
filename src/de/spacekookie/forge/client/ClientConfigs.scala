package de.spacekookie.forge.client

import play.api.libs.json._

/** Define base models for data storage from json objects  */
case class Server(url: String, port: Int, authToken: Option[String])
case class ClientConf(host: String, server: Option[Server], key: Option[String], twoWay: Boolean)

object Server {
  implicit val serverFormats = Json.format[Server]

  def write(server: Option[Server]) = Json.toJson(server)
  def read(json: JsValue): Option[Server] = Option(json.as[Server])
}

object ClientConf {
  implicit val clientFormats = Json.format[ClientConf]

  def write(conf: ClientConf) = {

    JsObject(Seq(
      "hostname" -> JsString(conf.host),
      "two_way" -> JsBoolean(conf.twoWay),
      "server" -> Json.toJson(conf.server),

      /** Gracefully handle non existant key */
      "git_key" -> JsString(
        conf.key match {
          case None => ""
          case _ => conf.key.get
        }) /* end */ ))
  }

  def read(json: JsValue): ClientConf = {
    val host = (json \ "hostname").as[String]
    val server = (json \ "server").as[Server]
    val key = (json \ "git_key").as[String]
    val twoWay = (json \ "two_way").as[Boolean]

    return ClientConf(host, Option(server), Option(key), twoWay)
  }
}
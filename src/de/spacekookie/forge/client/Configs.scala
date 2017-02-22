package de.spacekookie.forge.client

import play.api.libs.json._

/** Define base models for data storage from json objects  */
case class Server(url: String, port: Int, authToken: Option[String])
case class ClientConf(host: String, server: Server, gitKey: String, twoWay: Boolean)

object Server {
  implicit val serverFormats = Json.format[Server]

  def write(server: Server) = Json.toJson(server)
  def read(jsonServer: JsValue): Server = jsonServer.as[Server]
}

object ClientConf {

  def write(conf: ClientConf) = {
    JsObject(Seq(
      "hostname" -> JsString(conf.host),
      "server" -> Json.toJson(conf.server),
      "git_key" -> JsString(conf.gitKey),
      "two_way" -> JsBoolean(conf.twoWay)))
  }

  def read(json: JsValue): ClientConf = {
    val host = (json \ "hostname").as[String]
    val server = (json \ "server").as[Server]
    val gitKey = (json \ "git_key").as[String]
    val twoWay = (json \ "two_way").as[Boolean]

    return ClientConf(host, server, gitKey, twoWay)
  }
}
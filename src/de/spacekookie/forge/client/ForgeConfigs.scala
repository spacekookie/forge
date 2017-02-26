package de.spacekookie.forge.client

import play.api.libs.json.{ JsValue, Json }
import play.api.libs.json.JsObject

/**
 *  Allowing us to parse file entries in the "fileset" array
 */
case class FileEntry(id: String, path: String, _type: String, flags: Array[String])
object FileEntry {
  implicit val entryFormats = Json.format[FileEntry]

  def write(entry: FileEntry) = Json.toJson(entry)
  def read(json: JsValue): FileEntry = json.as[FileEntry]
}

/**
 *  Allowing us to parse entire forge configs
 */
case class ForgeConf(fileset: Array[FileEntry], exclude: JsObject, exclusive: JsObject)
object ForgeConf {
  implicit val forgeFormats = Json.format[ForgeConf]

  def write(conf: ForgeConf) = Json.toJson(conf)
  def read(json: JsValue): ForgeConf = json.as[ForgeConf]
}
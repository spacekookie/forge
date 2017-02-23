package de.spacekookie.forge.client

import play.api.libs.json.{ JsValue, Json }

/**
 *  Allowing us to parse file entries in the "fileset" array
 */
case class FileEntry(id: String, path: String, _type: String, flags: Array[String])
object FileEntry {
  implicit val entryFormats = Json.format[FileEntry]

  def write(entry: FileEntry) = Json.toJson(entry)
  def read(json: JsValue): FileEntry = json.as[FileEntry]
}
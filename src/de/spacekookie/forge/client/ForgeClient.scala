package de.spacekookie.forge.client

// This enables operators without dot notation
import language.postfixOps

import java.nio.file.{ Paths, Files }
import play.api.libs.json.{ JsValue, Json }

import sys.process._
import scala.io.Source
import java.io.{ File, FileWriter, PrintWriter }


object ForgeClient {
  val home: String = System.getProperty("user.home")
  val cache: String = home + "/.local/share/forge/"
  val config: String = home + "/.config/forge/"

  /** Place to store future configuration safel */
  var clientConf: Option[ClientConf] = null

  def main(args: Array[String]): Unit = {
    println("Starting forge client...")
    println(s"Config directory '$config'")
    println(s"Cache directory '$cache'")

    /** Build the config path and check for existance */
    val clientConf = config + "client.conf"

    /** Either load config or create new one */
    exists(clientConf) match {
      case true => checkConfig(clientConf)
      case false => createConfig(clientConf)
    }
    
    /** Validate cache directory */
    // FIXME: Do this with built-in API
    if(!exists(cache)) s"mkdir -p ${cache}" !
    
    /** Update the cached repo */
    // FIXME: Get the repo address from server
    getLatestRepo("https://github.com/spacekookie/forge")
  }

  private def checkConfig(path: String) {
    println("Loading existing config")

    /** Read the config file carefully */
    val source = Source.fromFile(path)
    val lines: String = try source.mkString finally source.close()
    val json: JsValue = Json.parse(lines)

    /** Then create a new ClientConf from data */
    val cc: ClientConf = ClientConf.read(json)
    clientConf = Option(cc)
  }

  private def createConfig(path: String) {
    println("Creating new config")

    /** First make sure our directory exists */
    new File(path).createNewFile()

    /** Then load data about system */
    val source = Source.fromFile("/etc/hostname")

    /** Hostname and twoway sync are always present  */
    val host: String = try source.mkString.replace("\n", "") finally source.close()
    val server: Server = Server("forge.spacekookie.de", 443, None)
    val twoWay: Boolean = false

    /** Create a new client config with some empty fields */
    val cc: ClientConf = ClientConf(host, Option(server), None, twoWay)
    clientConf = Option(cc)

    /** Encode config to json and write to disk */
    updateConfig(path, cc)
  }

  /** Write a client conf to disk for future use */
  private def updateConfig(path: String, conf: ClientConf) = {
    val json: String = ClientConf.write(conf).toString();
    val writer = new PrintWriter(new File(path))
    writer.write(json)
    writer.close()
    println("Writing config complete")
  }

  /** A function that only returns if a path exists or not */
  private def exists(str: String): Boolean = Files.exists(Paths.get(str))
  
  /** Function that either clones repo or updates the existing one that's cached */
  private def getLatestRepo(repo: String) = exists(cache + "/repo") match {
    case true => {
      println("Updating existing repository")
      s"git -C ${cache}repo pull" !
    }
    case false => {
      println("Cloning repository for the first time")
      s"git clone $repo ${cache}repo" !
    }
  }

}
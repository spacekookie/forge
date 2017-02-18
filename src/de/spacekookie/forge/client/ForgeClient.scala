package de.spacekookie.forge.client

import com.typesafe.config._

object ForgeClient {
  val home: String = System.getProperty("user.home")
  val cache: String = home + "/.local/share/forge/"
  val config: String = home + "/.config/forge/"
  
  def main(args: Array[String]): Unit = {
    println("Starting forge client...")
    println(s"Config directory '$config'")
    println(s"Cache directory '$cache'")
    
    /** Load the client configuration from config directory */
    val clientConf = config + "client.conf"
    var conf = ConfigFactory.load();
    
  }
}
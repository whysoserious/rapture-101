package org.zy.rapture101

import scala.annotation.tailrec

import rapture._
import core._
import io._
import net._
import http._
import uri._
import json._
import codec._
import encodings.system._
import jsonBackends.jackson._
import jsonInterop._
import formatters.humanReadable._

object HelloWorld extends App {

  implicit val httpQueryExtractor = Json.extractor[String].map(HttpQuery.parse)
  case class Links(self: HttpQuery, next: Option[HttpQuery])
  case class TopGames(_total: Int, _links: Links, top: Json)

  case class GameData(name: String, viewers: Int, channels: Int)

  case class GistFile(content: String)
  case class Gist(public: Boolean, files: Map[String, GistFile])

  @tailrec
  def fetchTopGames(url: HttpUrl, offset: Int, limit: Int, acc: Seq[GameData]): Seq[GameData] = {
    println(s"Fetching list of top games from Twitch. Offset: $offset")

    // This is a HttpQuery object ready to be sent
    val httpQuery: HttpQuery = url.query(Map("limit" -> limit, "offset" -> offset))

    // This is a body of a HTTP response converted to a String
    val response: String = httpQuery.slurp[Char]

    // Convert body of a response to a Json object
    val topGames: TopGames = (Json.parse(response)).as[TopGames]

    // Second method of parsing JSON thanks to Dynamic trait
    val games = topGames.top.as[Seq[Json]].map(g => GameData(g.game.name.as[String], g.viewers.as[Int], g.channels.as[Int]))

    games match {
      case Nil =>
        println(s"Total number of games on Twitch: ${topGames._total}")
        acc
      case _ => fetchTopGames(url, offset + limit, limit, acc ++ games)
    }
  }

  // fetch list of top games recursively
  val games = fetchTopGames("https://api.twitch.tv/kraken/games/top".as[HttpUrl], 0, 100, Seq())

  // Prepare Gist data
  val gistData = Gist(true, Map("twitch-games.json" -> GistFile(Json.format(Json(games)))))

  // Convert it to JSON
  val json: Json = Json(gistData)

  // POST it
  val response: String = uri"https://api.github.com/gists".httpPost[Json](json).slurp[Char]

  // Convert back to JSON and extract HTTP URL
  val gistUrl: String = Json.parse(response).html_url.as[String]

  println(s"New gist is available here: $gistUrl")

}

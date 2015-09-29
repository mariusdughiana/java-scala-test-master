package com.sky.assignment.clients

import java.net.ConnectException

import org.json4s.DefaultFormats
import org.json4s.JsonAST._
import org.json4s.Xml.toJson
import org.json4s.native.Serialization.write
import org.xml.sax.SAXParseException

import scala.collection.mutable.HashMap
import scala.xml.XML


object RecsEngineClient extends HttpClient {

  private val recsEngineBaseURL = "http://localhost:8080/"

  def getRecsForSubscriber(subscriber: String, startingTime: Long): String = {

    try {
      val oneHour = 60 * 60 * 1000
      val recSslot1 = getRecsForTimeSlot(5, startingTime, startingTime + oneHour, subscriber)
      val recSslot2 = getRecsForTimeSlot(5, startingTime + oneHour, startingTime + 2 * oneHour, subscriber)
      val recSslot3 = getRecsForTimeSlot(5, startingTime + 2 * oneHour, startingTime + 3 * oneHour, subscriber)

      "[ " + recSslot1 + "," + recSslot2 + "," + recSslot3 + "]"
    } catch {
      case e @ (_ : SAXParseException | _ : ConnectException| _ : HttpClientException)  => "{\"error\": \"Wrong result from RescEngine." + e.getMessage + "\"}"
    }
  }


  private def getRecsForTimeSlot(num: Int, start: Long, end: Long, subscriber: String): String = {
    val params = new HashMap[String, String]
    params.put("num", num.toString);
    params.put("start", start.toString);
    params.put("end", end.toString);
    params.put("subscriber", subscriber);

    val content = getRestContent(buildUrl(recsEngineBaseURL + "/recs/personalised", params))

    val recsAsXml = XML.loadString(content)
    assert(recsAsXml.isInstanceOf[scala.xml.Elem])

    val recsAsJson = toJson(recsAsXml) transformField {
      case ("recommendations", x: JObject) => ("recommendations", x.children.apply(0))
      case x => x
    }


    implicit val formats = DefaultFormats
    val jsonOutput = recsAsJson merge JObject(JField("expiry", JInt(end)) :: Nil)
    write(jsonOutput)
  }

}

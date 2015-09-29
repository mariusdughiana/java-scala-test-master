package com.sky.assignment.clients

import java.net.ConnectException

import org.json4s.DefaultFormats
import org.json4s.JsonAST._
import org.json4s.Xml.toJson
import org.json4s.native.Serialization.write
import org.xml.sax.SAXParseException

import scala.collection.mutable.MutableList
import scala.collection.mutable.HashMap
import scala.xml.XML


object RecsEngineClient extends HttpClient {

  private val recsEngineBaseURL = "http://localhost:8080/"
  private implicit val formats = DefaultFormats

  def getRecsForSubscriber(subscriber: String, startingTime: Long,
  recs: Int = 5, slots: Int = 3, slotLength: Long = 3600000): String = {

    try {
      val recsList = MutableList[JValue]()
      for (i <- 1 to slots) {
        val slotEnd = startingTime + i*slotLength;
        val slotStart = slotEnd - slotLength;
        val recsPerSlot = getRecsForTimeSlot(recs, slotStart, slotEnd, subscriber)
        recsList += recsPerSlot;
      }
      write(recsList)
    } catch {
      case e@(_: SAXParseException | _: ConnectException | _: HttpClientException) => "{\"error\": \"Wrong result from RescEngine." + e.getMessage + "\"}"
    }
  }


  private def getRecsForTimeSlot(num: Int, start: Long, end: Long, subscriber: String): JValue = {
    val params = new HashMap[String, String]
    params.put("num", num.toString);
    params.put("start", start.toString);
    params.put("end", end.toString);
    params.put("subscriber", subscriber);

    val content = getRestContent(buildUrl(recsEngineBaseURL + "/recs/personalised", params))

    val recsAsXml = XML.loadString(content)

    val recsAsJson = toJson(recsAsXml) transformField {
      case ("recommendations", x: JObject) => ("recommendations", x.children.apply(0))
      case x => x
    }
    recsAsJson merge JObject(JField("expiry", JInt(end)) :: Nil)
  }
}

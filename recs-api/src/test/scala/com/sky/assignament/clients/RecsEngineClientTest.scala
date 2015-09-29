package com.sky.assignament.clients

import java.util.Date

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock._
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import com.sky.assignment.clients.RecsEngineClient
import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

class RecsEngineClientTest extends FlatSpec with Matchers with BeforeAndAfterEach {

  val Port = 8080
  val Host = "localhost"
  val wireMockServer = new WireMockServer(wireMockConfig().port(Port))

  override def beforeEach {
    wireMockServer.start()
    WireMock.configureFor(Host, Port)
  }

  override def afterEach {
    wireMockServer.stop()
  }


  "RecsEngineClient" should "return a JSON with recommendations" in {

    val currentTime = new Date().getTime;
    val currentTimePlus1Hr = currentTime + 60 * 60 * 1000
    val currentTimePlus2Hr = currentTime + 2 * 60 * 60 * 1000
    val currentTimePlus3Hr = currentTime + 3 * 60 * 60 * 1000
    stubFor(get(urlEqualTo("/recs/personalised?subscriber=subscriber1&num=5&end=" + currentTimePlus1Hr + "&start=" + currentTime))
      .willReturn(aResponse.withHeader("Content-Type", "application/xml").
      withBody("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
      "\n<recommendations>" +
      "\n<recommendations>" +
      "\n<uuid>1f18536a-e86f-4781-9819-7b3e7d385908</uuid>" +
      "\n<start>1415288463203</start>" +
      "\n<end>1415289998492</end>" +
      "\n</recommendations>" +
      "\n<recommendations>" +
      "\n<uuid>c3a6ac38-16b1-41da-83f8-077aff4841d6</uuid>" +
      "\n<start>1415284772998</start>" +
      "\n<end>1415289302319</end>" +
      "\n</recommendations>" +
      "\n<recommendations>" +
      "\n<uuid>42d34321-3283-467c-89dd-36983d8e4f4e</uuid>" +
      "\n<start>1415290983863</start>" +
      "\n<end>1415294905557</end>" +
      "\n</recommendations>" +
      "\n<recommendations>" +
      "\n<uuid>b5408d7c-688a-48f5-ae04-7d78765c3f3f</uuid>" +
      "\n<start>1415289589667</start>" +
      "\n<end>1415290372753</end>" +
      "\n</recommendations>" +
      "\n<recommendations>" +
      "\n<uuid>4d6bf32d-11d8-4b82-9b2b-07bc612a6060</uuid>" +
      "\n<start>1415290982236</start>" +
      "\n<end>1415292219539</end>" +
      "\n</recommendations>" +
      "\n</recommendations>")))

    stubFor(get(urlEqualTo("/recs/personalised?subscriber=subscriber1&num=5&end=" + currentTimePlus2Hr + "&start=" +
      currentTimePlus1Hr))
      .willReturn(aResponse.withHeader("Content-Type", "application/xml").
      withBody("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
      "\n<recommendations>" +
      "\n<recommendations>" +
      "\n<uuid>1f18536a-e86f-4781-9819-7b3e7d385908</uuid>" +
      "\n<start>1415288463203</start>" +
      "\n<end>1415289998492</end>" +
      "\n</recommendations>" +
      "\n</recommendations>")))

    stubFor(get(urlEqualTo("/recs/personalised?subscriber=subscriber1&num=5&end=" + currentTimePlus3Hr + "&start=" +
      currentTimePlus2Hr))
      .willReturn(aResponse.withHeader("Content-Type", "application/xml").
      withBody("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
      "\n<recommendations>" +
      "\n<recommendations>" +
      "\n<uuid>4d6bf32d-11d8-4b82-9b2b-07bc612a6060</uuid>" +
      "\n<start>1415290982236</start>" +
      "\n<end>1415292219539</end>" +
      "\n</recommendations>" +
      "\n</recommendations>")))


    val recs = RecsEngineClient.getRecsForSubscriber("subscriber1", currentTime)
    assert(recs == "[ {\"recommendations\":[{\"uuid\":\"1f18536a-e86f-4781-9819-7b3e7d385908\"," +
      "\"start\":\"1415288463203\",\"end\":\"1415289998492\"},{\"uuid\":\"c3a6ac38-16b1-41da-83f8-077aff4841d6\"," +
      "\"start\":\"1415284772998\",\"end\":\"1415289302319\"},{\"uuid\":\"42d34321-3283-467c-89dd-36983d8e4f4e\"," +
      "\"start\":\"1415290983863\",\"end\":\"1415294905557\"},{\"uuid\":\"b5408d7c-688a-48f5-ae04-7d78765c3f3f\"," +
      "\"start\":\"1415289589667\",\"end\":\"1415290372753\"},{\"uuid\":\"4d6bf32d-11d8-4b82-9b2b-07bc612a6060\"," +
      "\"start\":\"1415290982236\",\"end\":\"1415292219539\"}],\"expiry\":" + currentTimePlus1Hr + "}," +
      "{\"recommendations\":\"1f18536a-e86f-4781-9819-7b3e7d385908\",\"expiry\":" + currentTimePlus2Hr + "}," +
      "{\"recommendations\":\"4d6bf32d-11d8-4b82-9b2b-07bc612a6060\",\"expiry\":" + currentTimePlus3Hr + "}]")
  }


  "RecsEngineClient when gets invalid xml" should "return an error" in {

    val currentTime = new Date().getTime;
    val currentTimePlus1Hr = currentTime + 60 * 60 * 1000
    val currentTimePlus2Hr = currentTime + 2 * 60 * 60 * 1000
    val currentTimePlus3Hr = currentTime + 3 * 60 * 60 * 1000
    stubFor(get(urlEqualTo("/recs/personalised?subscriber=subscriber1&num=5&end=" + currentTimePlus1Hr + "&start=" + currentTime))
      .willReturn(aResponse.withHeader("Content-Type", "html/txt").
      withBody("some error")))




    val recs = RecsEngineClient.getRecsForSubscriber("subscriber1", currentTime)
    assert(recs == "{\"error\": \"Wrong result from RescEngine.Content is not allowed in prolog.\"}")
  }


  "RecsEngineClient when RecsEngine is down" should "return an error" in {

    val currentTime = new Date().getTime;
      val currentTimePlus1Hr = currentTime + 1 * 60 * 60 * 1000
    val recs = RecsEngineClient.getRecsForSubscriber("subscriber1", currentTime)
    assert(recs == "{\"error\": \"Wrong result from RescEngine" +
      ".http://localhost:8080//recs/personalised?subscriber=subscriber1&num=5&end="+currentTimePlus1Hr+"&start="+currentTime+"" +
      " failed : HTTP/1.1 404 Not Found\"}")
  }

}

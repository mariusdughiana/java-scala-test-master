package com.sky.assignment.clients

import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.DefaultHttpClient

import scala.collection.mutable.HashMap

trait HttpClient {

  protected def getRestContent(url:String): String = {
    val httpClient = new DefaultHttpClient()
    val httpResponse = httpClient.execute(new HttpGet(url))
    if (httpResponse.getStatusLine.getStatusCode != 200) {
      throw new HttpClientException(url + " failed : " + httpResponse.getStatusLine)
    }
    val entity = httpResponse.getEntity()
    var content = ""
    if (entity != null) {
      val inputStream = entity.getContent()
      content = io.Source.fromInputStream(inputStream).getLines.mkString
      inputStream.close
    }
    httpClient.getConnectionManager().shutdown()
    return content
  }

  protected def buildUrl(baseUrl:String, parameters: HashMap[String, String]): String = {
    var url = baseUrl;
    if (!parameters.isEmpty) {
      url += "?";
      parameters.foreach{ case (k,v) => url+= k + "=" + v + "&"}
      url = url.dropRight(1);
    }
    return url;
  }

}

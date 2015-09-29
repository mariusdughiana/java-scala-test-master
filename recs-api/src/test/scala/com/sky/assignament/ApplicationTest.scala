package com.sky.assignament

import java.util.Date

import com.sky.assignment.Application
import com.sky.assignment.clients.RecsEngineClient
import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest

class ApplicationTest extends Specification with Specs2RouteTest {
  val actorRefFactory = system

  "return recs for subscriber1" in {
    Get("/personalised/subscriber1") ~> Application.route ~> check {
      responseAs[String] === RecsEngineClient.getRecsForSubscriber("subscriber1", new Date().getTime)
    }
  }




}

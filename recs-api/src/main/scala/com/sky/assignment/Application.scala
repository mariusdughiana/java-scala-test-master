package com.sky.assignment

import java.util.Date

import akka.actor.ActorSystem
import com.sky.assignment.clients.RecsEngineClient
import spray.routing.SimpleRoutingApp

object Application extends App with SimpleRoutingApp {

  implicit val system = ActorSystem("recs")

  def route = path("personalised" / Segment) { subscriber =>
    get {
      complete {
        RecsEngineClient.getRecsForSubscriber(subscriber, new Date().getTime)
      }
    }
  }

   startServer(interface = "localhost", port = 8090) {
    route
  }
}

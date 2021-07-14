package uk.gov.hmrc.agentsregfrontend.models

import play.api.libs.json.{Json, OFormat}

case class Agent(arn: String)

object Agent {
  implicit val format: OFormat[Agent] = Json.format[Agent]
}



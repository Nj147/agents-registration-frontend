package uk.gov.hmrc.agentsregfrontend.models

import play.api.data.Form
import play.api.data.Forms.{list, mapping, text}

case class Correspondence(modes: List[String]) {
  def encode: String = modes.mkString(",")
}

object Correspondence {
  def decode(string: String): List[String] = string.split(",").toList

  val correspondenceForm: Form[Correspondence] = Form(
    mapping(
      "modes" -> list(text)
    )(Correspondence.apply)(Correspondence.unapply))
}

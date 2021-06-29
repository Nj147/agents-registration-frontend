package uk.gov.hmrc.agentsregfrontend.models

import play.api.libs.json.{Json, OFormat}

import play.api.data.Form
import play.api.data.Forms.{email, mapping}

case class Agent(arn: String)

case class RegisteringUser(password: String, businessName: String, email: String, mobileNumber: Int, moc: String, addresslineOne: String, addressLineTwo: String, city: String, postcode: String)

object RegisteringUser{
  implicit val format: OFormat[RegisteringUser] = Json.format[RegisteringUser]
}

case class Email(email: String)

object Email {
  val emailForm: Form[Email] =
    Form(
      mapping(
        "email" -> email
      )(Email.apply)(Email.unapply)
    )
}

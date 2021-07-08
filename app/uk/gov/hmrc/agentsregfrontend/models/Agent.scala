/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.agentsregfrontend.models

import play.api.libs.json.{Json, OFormat}
import play.api.data.Form
import play.api.data.Forms.{email, list, mapping, nonEmptyText, text}
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}

import scala.util.matching.Regex

case class Agent(arn: String)

object Agent {
  implicit val format: OFormat[Agent] = Json.format[Agent]
}


case class RegisteringUser(password: String, businessName: String, email: String, mobileNumber: Long, moc: Seq[String], propertyNumber: String, postcode: String)

object RegisteringUser {
  implicit val format: OFormat[RegisteringUser] = Json.format[RegisteringUser]
}

case class BusinessName(businessName: String)

object BusinessName {
  val form: Form[BusinessName] =
    Form(
      mapping(
        "businessName" -> nonEmptyText
      )(BusinessName.apply)(BusinessName.unapply)
    )
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

case class ContactNumber(number: String)

object ContactNumber {
  val validNumber: Regex = """\d{11}""".r

  val valid: Constraint[String] = Constraint("constraints.number")({ plainText =>
    val errors = plainText match {
      case validNumber() => Nil
      case _ => Seq(ValidationError("Please Enter an 11 digit, UK Phone Number e.g 07123456789"))
    }
    if (errors.isEmpty) {
      Valid
    } else {
      Invalid(errors)
    }
  })

  val contactForm: Form[ContactNumber] =
    Form(
      mapping(
        "number" -> text.verifying(valid)
      )(ContactNumber.apply)(ContactNumber.unapply))
}

case class Password(password: String, passwordCheck: String)

object Password {
  val regex: Regex = """^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$""".r

  val passwordCheckConstraint: Constraint[String] = Constraint("constraints.passwordcheck")({ plainText =>
    val errors = plainText match {
      case regex() => Nil
      case _ => Seq(ValidationError("Password is invalid"))
    }
    if (errors.isEmpty) {
      Valid
    } else {
      Invalid(errors)
    }
  })


  val passwordForm: Form[Password] = {
    Form(
      mapping(
        "password" -> text.verifying(passwordCheckConstraint),
        "passwordCheck" -> text
      )(Password.apply)(Password.unapply))

  }
}

case class Address(propertyNumber: String, postcode: String) {
  val encode: String = propertyNumber + "/" + postcode
}

object Address {

  val regex: Regex = """(?:[A-Za-z]\d ?\d[A-Za-z]{2})|(?:[A-Za-z][A-Za-z\d]\d ?\d[A-Za-z]{2})|(?:[A-Za-z]{2}\d{2} ?\d[A-Za-z]{2})|(?:[A-Za-z]\d[A-Za-z] ?\d[A-Za-z]{2})|(?:[A-Za-z]{2}\d[A-Za-z] ?\d[A-Za-z]{2})""".stripMargin.r

  val postcodeCheckConstraint: Constraint[String] = Constraint("constraints.postcodecheck")({ plainText =>
    val errors = plainText match {
      case regex() => Nil
      case _ => Seq(ValidationError("Input is not a valid postcode"))
    }
    if (errors.isEmpty) {
      Valid
    } else {
      Invalid(errors)
    }
  })

  val addressForm: Form[Address] =
    Form(
      mapping(
        "propertyNumber" -> nonEmptyText,
        "postcode" -> text.verifying(postcodeCheckConstraint)
      )(Address.apply)(Address.unapply))

  def decode(string: String): Address = {
    val (n, p): (String, String) = string.split("/").toList match {
      case h :: t :: _ => h -> t
      case _ => ("", "")
    }
    Address(n, p)
  }
}

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


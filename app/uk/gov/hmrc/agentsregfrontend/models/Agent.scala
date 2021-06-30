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
import play.api.libs.json.{Json, OFormat}
import play.api.data.Form
import play.api.data.Forms.{email, mapping, nonEmptyText, number}

case class Agent(arn: String)

case class RegisteringUser(password: String, businessName: String, email: String, mobileNumber: Int, moc: String, addresslineOne: String, addressLineTwo: String, city: String, postcode: String)

object RegisteringUser{
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

case class ContactNumber(number: Int)

object ContactNumber {
  val contactForm: Form[ContactNumber] =
    Form(
      mapping(
        "number" -> number
      )(ContactNumber.apply)(ContactNumber.unapply))
}

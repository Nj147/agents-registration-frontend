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

import play.api.data.Form
import play.api.data.Forms.{mapping, text}
import play.api.data.validation.{Constraint, Invalid, Valid, ValidationError}
import scala.util.matching.Regex

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

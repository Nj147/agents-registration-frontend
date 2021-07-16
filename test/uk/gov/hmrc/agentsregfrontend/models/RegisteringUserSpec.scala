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

import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.{JsSuccess, JsValue, Json}

class RegisteringUserSpec extends AnyWordSpec with BeforeAndAfter with Matchers {

  val moc: List[String] = List("Text message")
  val regUser: RegisteringUser = RegisteringUser("pa55w0rd", "Business Ltd", "john@gmail.com", 74134323, moc, "21", "SE12 1BU")

  val regUserJs: JsValue = Json.parse(
    """{
      |"password": "pa55w0rd",
      |"businessName": "Business Ltd",
      |"email": "john@gmail.com",
      |"contactNumber": 74134323,
      |"moc" : [
      |    "Text message"
      |],
      |"propertyNumber": "21",
      |"postcode": "SE12 1BU"
      |}""".stripMargin
  )

  "A RegisteringUser object" can {
    "be formatted to json" in {
      Json.toJson(regUser) shouldBe regUserJs
    }
  }

  "A RegisteringUser JsValue" can {
    "be formatted to a RegisteringUser object" in {
      Json.fromJson[RegisteringUser](regUserJs) shouldBe JsSuccess(regUser)
    }
  }


}

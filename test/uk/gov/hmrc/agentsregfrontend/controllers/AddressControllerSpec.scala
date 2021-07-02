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

package uk.gov.hmrc.agentsregfrontend.controllers

import org.jsoup.Jsoup
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.http.Status
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeRequest
import play.api.test.Helpers.{charset, contentAsString, contentType, defaultAwaitTimeout, session, status}
import uk.gov.hmrc.agentsregfrontend.models.Address

class AddressControllerSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {
  override def fakeApplication(): Application =
    new GuiceApplicationBuilder()
      .configure(
        "metrics.jvm"     -> false,
        "metrics.enabled" -> false
      )
      .build()

  private val fakeRequest = FakeRequest("GET", "/")
  private val testAddress = Address("1 New Address", "A01 2BC")
  private val controller = app.injector.instanceOf[AddressController]

  "GET /address" should {
    "return 200" in {
      val result = controller.displayAddressPage(isUpdate = false).apply(fakeRequest)
      status(result) shouldBe Status.OK
    }
    "return HTML" in {
      val result = controller.displayAddressPage(isUpdate = false).apply(fakeRequest)
      contentType(result) shouldBe Some("text/html")
      charset(result)     shouldBe Some("utf-8")
    }
    "return a page with 2 inputs" in {
      val result = controller.displayAddressPage(isUpdate = false).apply(fakeRequest)
      Jsoup.parse(contentAsString(result)).getElementsByClass("govuk-input--width-10").size shouldBe 2
    }
    "redirect if the user is logged in" in {
      val result = controller.displayAddressPage(isUpdate = false).apply(fakeRequest.withSession("arn" -> "ARN0000001"))
      status(result) shouldBe Status.SEE_OTHER
    }
  }

  "POST /address" should {
    "return a bad request if one or more invalid value input" in {
      val result = controller.processAddress(isUpdate = false).apply(fakeRequest.withFormUrlEncodedBody("propertyNumber" -> "1 New Street", "postcode" -> ""))
      status(result) shouldBe 400
    }
    "redirect if valid value input with new sessions data" in {
      val result = controller.processAddress(isUpdate = false).apply(fakeRequest.withFormUrlEncodedBody("propertyNumber" -> testAddress.propertyNumber, "postcode" -> testAddress.postcode))
      status(result) shouldBe 303
      Address.decode(session(result).get("address").get) shouldBe testAddress
    }
    "send to Summary page with OK status if update" in {
      val result = controller.processAddress(isUpdate = true).apply(fakeRequest.withFormUrlEncodedBody("propertyNumber" -> testAddress.propertyNumber, "postcode" -> testAddress.postcode))
      status(result) shouldBe Status.OK
    }
  }

}
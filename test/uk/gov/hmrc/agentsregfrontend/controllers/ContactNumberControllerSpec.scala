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
import play.api.http.Status._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeRequest
import play.api.test.Helpers.{charset, contentAsString, contentType, defaultAwaitTimeout, redirectLocation, session, status}

class ContactNumberControllerSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {
  override def fakeApplication(): Application =
    new GuiceApplicationBuilder()
      .configure(
        "metrics.jvm"     -> false,
        "metrics.enabled" -> false
      )
      .build()

  private val fakeRequest = FakeRequest("GET", "/")

  private val controller = app.injector.instanceOf[ContactNumberController]

  "GET /contactNumber" should {
    "return 200" in {
      val result = controller.displayContactPage(isUpdate = false).apply(fakeRequest)
      status(result) shouldBe OK
    }
    "return HTML" in {
      val result = controller.displayContactPage(isUpdate = false).apply(fakeRequest)
      contentType(result) shouldBe Some("text/html")
      charset(result)     shouldBe Some("utf-8")
    }
    "return a page with 1 input" in {
      val result = controller.displayContactPage(isUpdate = false).apply(fakeRequest)
      Jsoup.parse(contentAsString(result)).getElementsByClass("govuk-input--width-10").size shouldBe 1
    }
    "redirect if the user is logged in" in {
      val result = controller.displayContactPage(isUpdate = false).apply(fakeRequest.withSession("arn" -> "ARN0000001"))
      status(result) shouldBe SEE_OTHER
    }
  }

  "POST /contactNumber" should {
    "return a bad request if an invalid contact number is input" in {
      val result = controller.processContactNumber(isUpdate = false).apply(fakeRequest.withFormUrlEncodedBody("number" -> ""))
      status(result) shouldBe BAD_REQUEST
    }
    "redirect to the next form page when given a valid form value, and add to the session data" in {
      val result = controller.processContactNumber(isUpdate = false).apply(fakeRequest.withFormUrlEncodedBody("number" -> "01234567890"))
      status(result) shouldBe SEE_OTHER
      session(result).get("contactNumber").get shouldBe "01234567890"
      redirectLocation(result) shouldBe Some(s"${routes.AddressController.displayAddressPage(false)}")
    }
    "redirected to Summary page status if update" in {
      val result = controller.processContactNumber(isUpdate = true).apply(fakeRequest.withFormUrlEncodedBody("number" -> "01234567890").withSession("address" -> "blah/DED2"))
      status(result) shouldBe Status.SEE_OTHER
      redirectLocation(result) shouldBe Some(s"${routes.SummaryController.summary()}")
    }
  }

}

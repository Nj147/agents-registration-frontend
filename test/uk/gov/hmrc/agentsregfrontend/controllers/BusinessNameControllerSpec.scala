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
import uk.gov.hmrc.agentsregfrontend.models.Address

class BusinessNameControllerSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {

  override def fakeApplication(): Application =
      new GuiceApplicationBuilder()
        .configure(
          "metrics.jvm" -> false,
          "metrics.enabled" -> false
        )
        .build()

    private val getfakeRequest = FakeRequest("GET", "/")
    private val postfakeRequest = FakeRequest("POST", "/")

    private val testAddress = Address("1 New Address", "A01 2BC")

    private val controller = app.injector.instanceOf[BusinessNameController]

    "GET /businessName" should {
      "return 200" in {
        val result = controller.displayBusinessNamePage(isUpdate = false).apply(getfakeRequest)
        status(result) shouldBe 200
      }
      "return HTML" in {
        val result = controller.displayBusinessNamePage(isUpdate = false).apply(getfakeRequest)
        contentType(result) shouldBe Some("text/html")
        charset(result)     shouldBe Some("utf-8")
      }
      "return a page with 1 input" in {
        val result = controller.displayBusinessNamePage(isUpdate = false).apply(getfakeRequest)
        Jsoup.parse(contentAsString(result)).getElementsByClass("govuk-input--width-10").size shouldBe 1
      }
      "have a pre-populated input field if already entered once" in {
        val result = controller.displayBusinessNamePage(isUpdate = false).apply(getfakeRequest.withSession("businessName" -> "Test Business Name"))
        Jsoup.parse(contentAsString(result)).getElementById("businessName").`val`() shouldBe "Test Business Name"
      }
      "redirect if the user is logged in" in {
        val result = controller.displayBusinessNamePage(isUpdate = false).apply(getfakeRequest.withSession("arn" -> "ARN0000001"))
        status(result) shouldBe SEE_OTHER
      }
    }

    "POST /businessName" should {
      "redirect to the next form page with added session values if valid values added" in {
        val result = controller.processBusinessName(isUpdate = false).apply(postfakeRequest.withFormUrlEncodedBody("businessName" -> "testBusinessName"))
        status(result) shouldBe SEE_OTHER
        session(result).get("businessName") shouldBe Some("testBusinessName")
        redirectLocation(result) shouldBe Some(s"${routes.EmailController.displayEmailPage(false)}")
      }
      "return bad request with nothing in session when form is left empty" in {
        val result = controller.processBusinessName(isUpdate = false).apply(postfakeRequest.withFormUrlEncodedBody("businessName" -> ""))
        session(result).isEmpty
        status(result) shouldBe BAD_REQUEST
      }
      "redirect to the summary page with added session values if updating and valid values added" in {
        val result = controller.processBusinessName(isUpdate = true).apply(postfakeRequest.withFormUrlEncodedBody("businessName" -> "testBusinessName"))
        status(result) shouldBe Status.SEE_OTHER
        session(result).get("businessName") shouldBe Some("testBusinessName")
        redirectLocation(result) shouldBe Some(s"${routes.SummaryController.summary()}")
      }
    }
  }
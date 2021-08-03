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

class EmailControllerSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {
  override def fakeApplication(): Application =
    new GuiceApplicationBuilder()
      .configure(
        "metrics.jvm" -> false,
        "metrics.enabled" -> false
      )
      .build()

  private val fakeRequest = FakeRequest("GET", "/")

  private val controller = app.injector.instanceOf[EmailController]

  "GET /email" should {
    "return 200" in {
      val result = controller.displayEmailPage(isUpdate = false).apply(fakeRequest)
      status(result) shouldBe OK
    }
    "return HTML" in {
      val result = controller.displayEmailPage(isUpdate = false).apply(fakeRequest)
      contentType(result) shouldBe Some("text/html")
      charset(result) shouldBe Some("utf-8")
    }
    "return a page with 1 input" in {
      val result = controller.displayEmailPage(isUpdate = false).apply(fakeRequest)
      Jsoup.parse(contentAsString(result)).getElementsByClass("govuk-input").size shouldBe 1
      Jsoup.parse(contentAsString(result)).getElementsByTag("title").text() shouldBe "Email"
    }
    "have a pre-populated input field if already entered once" in {
      val result = controller.displayEmailPage(isUpdate = false).apply(fakeRequest.withSession("email" -> "test@test.com"))
      Jsoup.parse(contentAsString(result)).getElementById("email").`val`() shouldBe "test@test.com"
    }
    "redirect if the user is logged in" in {
      val result = controller.displayEmailPage(isUpdate = false).apply(fakeRequest.withSession("arn" -> "ARN0000001"))
      status(result) shouldBe SEE_OTHER
    }
  }

  "POST /email" should {
    "return a bad request if invalid value input" in {
      val result = controller.processEmail(isUpdate = false).apply(fakeRequest.withFormUrlEncodedBody("email" -> ""))
      status(result) shouldBe BAD_REQUEST
    }
    "redirect to the next form page with added if valid value input with session data" in {
      val result = controller.processEmail(isUpdate = false).apply(fakeRequest.withFormUrlEncodedBody("email" -> "test@test.com"))
      status(result) shouldBe SEE_OTHER
      session(result).get("email").get shouldBe "test@test.com"
      redirectLocation(result) shouldBe Some(s"${routes.ContactNumberController.displayContactPage(false)}")
    }
    "redirect to the Summary page with updated session values when updating" in {
      val result = controller.processEmail(isUpdate = true).apply(fakeRequest.withFormUrlEncodedBody("email" -> "test1@test2.com").withSession("address" -> "blah/DED2"))
      status(result) shouldBe Status.SEE_OTHER
      session(result).get("email").get shouldBe "test1@test2.com"
      redirectLocation(result) shouldBe Some(s"${routes.SummaryController.summary()}")
    }
  }

}


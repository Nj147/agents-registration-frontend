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
import play.api.test.{FakeRequest, Helpers}
import play.api.test.Helpers.{contentAsString, contentType, defaultAwaitTimeout, session, status}

class CorrespondenceControllerSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite{

  override def fakeApplication(): Application =
    new GuiceApplicationBuilder()
      .configure(
        "metrics.jvm"     -> false,
        "metrics.enabled" -> false
      )
      .build()

  private val getfakeRequest = FakeRequest("GET", "/")
  private val postfakeRequest = FakeRequest("POST", "/")

  private val controller = app.injector.instanceOf[CorrespondenceController]

  "GET /correspondence" should {
    "return 200" in {
      val result = controller.displayCorrespondencePage(isUpdate = false).apply(getfakeRequest)
      status(result) shouldBe 200
    }
    "return HTML" in {
      val result = controller.displayCorrespondencePage(isUpdate = false).apply(getfakeRequest)
      contentType(result) shouldBe Some("text/html")
      Helpers.charset(result)     shouldBe Some("utf-8")
    }
    "return a page with 1 input" in {
      val result = controller.displayCorrespondencePage(isUpdate = false).apply(getfakeRequest)
      Jsoup.parse(contentAsString(result)).getElementsByClass("govuk-checkboxes__item").size shouldBe 4
    }
    "redirect if the user is logged in" in {
      val result = controller.displayCorrespondencePage(isUpdate = false).apply(getfakeRequest.withSession("arn" -> "ARN0000001"))
      status(result) shouldBe SEE_OTHER
    }
  }

  "POST /correspondence" should {
    "return 303 redirect with added session values" in {
      val result = controller.processCorrespondence(isUpdate = false).apply(postfakeRequest.withFormUrlEncodedBody("modes[]" -> "call"))
      status(result) shouldBe SEE_OTHER
      session(result).get("modes") shouldBe Some("call")
    }
    "return bad request when form is left empty" in {
      val result = controller.processCorrespondence(isUpdate = false).apply(postfakeRequest.withFormUrlEncodedBody())
      session(result).isEmpty
      status(result) shouldBe BAD_REQUEST
    }
    "send to Summary page with OK status if update" in {
      val result = controller.processCorrespondence(isUpdate = true).apply(postfakeRequest.withFormUrlEncodedBody("modes[]" -> "call,text").withSession("address" -> "blah/DED2"))
      status(result) shouldBe Status.OK
    }
  }
}

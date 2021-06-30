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

class EmailControllerSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {
  override def fakeApplication(): Application =
    new GuiceApplicationBuilder()
      .configure(
        "metrics.jvm"     -> false,
        "metrics.enabled" -> false
      )
      .build()

  private val fakeRequest = FakeRequest("GET", "/")

  private val controller = app.injector.instanceOf[EmailController]

  "GET /email" should {
    "return 200" in {
      val result = controller.displayEmailPage(fakeRequest)
      status(result) shouldBe Status.OK
    }
    "return HTML" in {
      val result = controller.displayEmailPage(fakeRequest)
      contentType(result) shouldBe Some("text/html")
      charset(result)     shouldBe Some("utf-8")
    }
    "return a page with 1 input" in {
      val result = controller.displayEmailPage(fakeRequest)
      Jsoup.parse(contentAsString(result)).getElementsByClass("govuk-input--width-10").size shouldBe 1
    }
  }

  "POST /email" should {
    "return a bad request if invalid value input" in {
      val result = controller.processEmail(fakeRequest.withFormUrlEncodedBody("email" -> ""))
      status(result) shouldBe 400
    }
    "redirect if valid value input with session data" in {
      val result = controller.processEmail(fakeRequest.withFormUrlEncodedBody("email" -> "test@test.com"))
      status(result) shouldBe 303
      session(result).get("address").get shouldBe "test@test.com"
    }
  }

}


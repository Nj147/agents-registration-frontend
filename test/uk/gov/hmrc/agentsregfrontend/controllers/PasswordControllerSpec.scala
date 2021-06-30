package uk.gov.hmrc.agentsregfrontend.controllers

import org.jsoup.Jsoup
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeRequest
import play.api.test.Helpers.{charset, contentAsString, contentType, defaultAwaitTimeout, session, status}

class PasswordControllerSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite {

  override def fakeApplication(): Application =
    new GuiceApplicationBuilder()
      .configure(
        "metrics.jvm" -> false,
        "metrics.enabled" -> false
      )
      .build()

  private val getfakeRequest = FakeRequest("GET", "/")
  private val postfakeRequest = FakeRequest("POST", "/")

  private val controller = app.injector.instanceOf[PasswordController]

  "GET /password" should {
    "return 200" in {
      val result = controller.displayPasswordPage(getfakeRequest)
      status(result) shouldBe 200
    }
    "return HTML" in {
      val result = controller.displayPasswordPage(getfakeRequest)
      contentType(result) shouldBe Some("text/html")
      charset(result)     shouldBe Some("utf-8")
    }
    "return a page with 2 input" in {
      val result = controller.displayPasswordPage(getfakeRequest)
      Jsoup.parse(contentAsString(result)).getElementsByClass("govuk-input--width-10").size shouldBe 2
    }
  }

  "POST /password" should {
    "return 400 when passwords dont match" in {
      val result = controller.processPassword(postfakeRequest.withFormUrlEncodedBody("password" -> "testPassword", "passwordCheck" -> "testPasswordCheck"))
      status(result) shouldBe 400
    }
    "return 303 when passwords do match" in {
      val result = controller.processPassword(postfakeRequest.withFormUrlEncodedBody("password" -> "testPassword", "passwordCheck" -> "testPassword"))
      status(result) shouldBe 303
    }
    "return session variables" in {
      val result = controller.processPassword(postfakeRequest.withFormUrlEncodedBody("password" -> "testPassword", "passwordCheck" -> "testPassword"))
      session(result).get("password") shouldBe Some("testPassword")
    }
    "return nothing in session when form is left empty" in {
      val result = controller.processPassword(postfakeRequest.withFormUrlEncodedBody("password" -> "", "passwordCheck" -> ""))
      session(result).isEmpty
    }
    "return 400" in {
      val result = controller.processPassword(postfakeRequest.withFormUrlEncodedBody("password" -> "", "passwordCheck" -> ""))
      status(result) shouldBe 400
    }
  }

}

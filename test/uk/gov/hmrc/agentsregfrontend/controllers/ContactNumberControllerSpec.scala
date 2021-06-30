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
      val result = controller.displayContactPage(fakeRequest)
      status(result) shouldBe Status.OK
    }
    "return HTML" in {
      val result = controller.displayContactPage(fakeRequest)
      contentType(result) shouldBe Some("text/html")
      charset(result)     shouldBe Some("utf-8")
    }
    "return a page with 1 input" in {
      val result = controller.displayContactPage(fakeRequest)
      Jsoup.parse(contentAsString(result)).getElementsByClass("govuk-input--width-10").size shouldBe 1
    }
  }

  "POST /contactNumber" should {
    "return a bad request if an invalid contact number is input" in {
      val result = controller.processContactNumber(fakeRequest.withFormUrlEncodedBody("number" -> ""))
      status(result) shouldBe Status.BAD_REQUEST
    }
    "redirect when given a valid form value, and add to the session data" in {
      val result = controller.processContactNumber(fakeRequest.withFormUrlEncodedBody("number" -> "012345678"))
      status(result) shouldBe 303
      session(result).get("contactNumber").get shouldBe "12345678"
    }
  }

}

package uk.gov.hmrc.agentsregfrontend.controllers

import org.jsoup.Jsoup
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.FakeRequest
import play.api.test.Helpers.{charset, contentAsString, contentType, defaultAwaitTimeout, session, status}

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

    private val controller = app.injector.instanceOf[BusinessNameController]

    "GET /businessName" should {
      "return 200" in {
        val result = controller.displayBusinessNamePage(getfakeRequest)
        status(result) shouldBe 200
      }
      "return HTML" in {
        val result = controller.displayBusinessNamePage(getfakeRequest)
        contentType(result) shouldBe Some("text/html")
        charset(result)     shouldBe Some("utf-8")
      }
      "return a page with 1 input" in {
        val result = controller.displayBusinessNamePage(getfakeRequest)
        Jsoup.parse(contentAsString(result)).getElementsByClass("govuk-input--width-10").size shouldBe 1
      }
    }

    "POST /businessName" should {
      "return 303 redirect" in {
        val result = controller.processBusinessName(postfakeRequest.withFormUrlEncodedBody("businessName" -> "testBusinessName"))
        status(result) shouldBe 303
      }
      "return session variables" in {
        val result = controller.processBusinessName(postfakeRequest.withFormUrlEncodedBody("businessName" -> "testBusinessName"))
        session(result).get("businessName") shouldBe Some("testBusinessName")
      }
      "return nothing in session when form is left empty" in {
        val result = controller.processBusinessName(postfakeRequest.withFormUrlEncodedBody("businessName" -> ""))
        session(result).isEmpty
      }
      "return 400" in {
        val result = controller.processBusinessName(postfakeRequest.withFormUrlEncodedBody("businessName" -> ""))
        status(result) shouldBe 400
      }
    }
  }
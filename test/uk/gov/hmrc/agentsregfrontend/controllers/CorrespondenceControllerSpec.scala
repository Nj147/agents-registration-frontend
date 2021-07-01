package uk.gov.hmrc.agentsregfrontend.controllers

import org.jsoup.Jsoup
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
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
      val result = controller.displayCorrespondencePage(getfakeRequest)
      status(result) shouldBe 200
    }
    "return HTML" in {
      val result = controller.displayCorrespondencePage(getfakeRequest)
      contentType(result) shouldBe Some("text/html")
      Helpers.charset(result)     shouldBe Some("utf-8")
    }
    "return a page with 1 input" in {
      val result = controller.displayCorrespondencePage(getfakeRequest)
      Jsoup.parse(contentAsString(result)).getElementsByClass("govuk-checkboxes__item").size shouldBe 4
    }
    "redirect if the user is logged in" in {
      val result = controller.displayCorrespondencePage(getfakeRequest.withSession("arn" -> "ARN0000001"))
      status(result) shouldBe SEE_OTHER
    }
  }

  "POST /correspondence" should {
    "return 303 redirect with added session values" in {
      val result = controller.processCorrespondence(postfakeRequest.withFormUrlEncodedBody("modes[]" -> "call"))
      status(result) shouldBe SEE_OTHER
      session(result).get("modes") shouldBe Some("call,text")
    }
    "return session variables" in {
      val result = controller.processCorrespondence(postfakeRequest.withFormUrlEncodedBody("modes[]" -> "call,text"))

    }
    "return bad request when form is left empty" in {
      val result = controller.processCorrespondence(postfakeRequest.withFormUrlEncodedBody("modes[]" -> ""))
      session(result).isEmpty
      status(result) shouldBe BAD_REQUEST
    }
  }

}

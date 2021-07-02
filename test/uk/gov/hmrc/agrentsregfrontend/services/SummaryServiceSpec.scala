package uk.gov.hmrc.agrentsregfrontend.services

import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{mock, when}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import uk.gov.hmrc.agentsregfrontend.connectors.AgentConnector
import uk.gov.hmrc.agentsregfrontend.models.{Agent, RegisteringUser}
import uk.gov.hmrc.agentsregfrontend.services.SummaryService

import scala.concurrent.Future

class SummaryServiceSpec extends AnyWordSpec with Matchers with GuiceOneAppPerSuite{

  override def fakeApplication(): Application =
    new GuiceApplicationBuilder()
      .configure(
        "metrics.jvm"     -> false,
        "metrics.enabled" -> false
      )
      .build()

  val connector: AgentConnector = mock(classOf[AgentConnector])
  val service = new SummaryService(connector)
  val user = RegisteringUser("password", "businessName", "email", "0456".toInt, List("gg"), "propertyNumber", "postcode")

  "agentDetails" should {
    "return the ARN value" in {
      when(connector.createAgent(any())) thenReturn(Future.successful(Some(Agent("ARN0000005"))))
      val result = service.agentDetails(user)
      await(result) shouldBe ("ARN0000005")
    }
  }

}

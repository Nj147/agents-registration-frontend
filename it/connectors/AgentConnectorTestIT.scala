package connectors

import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.libs.json.Json
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import play.api.test.Helpers.baseApplicationBuilder.injector
import uk.gov.hmrc.agentsregfrontend.connectors.AgentConnector
import uk.gov.hmrc.agentsregfrontend.models.RegisteringUser

class AgentConnectorTestIT extends AnyWordSpec with Matchers with GuiceOneServerPerSuite with WireMockHelper with BeforeAndAfterEach{
  lazy val connector: AgentConnector = injector.instanceOf[AgentConnector]

  val obj = RegisteringUser("password", "business", "email", 1234, List("call", "text message"), "addressline1", "postcode")

  override def beforeEach(): Unit = startWireMock()

  override def afterEach(): Unit = stopWireMock()

  "POST /removeClient" should {
    "return true when accepted response returned" in {
      stubPost("/registerAgent",201, "")
      val result = connector.createAgent(obj)
      await(result) shouldBe true
    }
    "return false when bad request response «returned" in {
      stubPost("/registerAgent",500, "")
      val result = connector.createAgent(obj)
      await(result) shouldBe false
    }
  }
}



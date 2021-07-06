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

package connectors

import org.scalatest.BeforeAndAfterEach
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.test.Helpers.{await, defaultAwaitTimeout}
import play.api.test.Helpers.baseApplicationBuilder.injector
import uk.gov.hmrc.agentsregfrontend.connectors.AgentConnector
import uk.gov.hmrc.agentsregfrontend.models.RegisteringUser

class AgentConnectorTestIT extends AnyWordSpec with Matchers with GuiceOneServerPerSuite with WireMockHelper with BeforeAndAfterEach{
  lazy val connector: AgentConnector = injector.instanceOf[AgentConnector]

  val obj = RegisteringUser("password", "business", "email", 1234, List("call", "text message"), "addressline1", "postcode")

  override def beforeEach(): Unit = startWireMock()

  override def afterEach(): Unit = stopWireMock()

  "POST /register" should {
    "return true when accepted response returned" in {
      stubPost("/registerAgent",201, "ARN150009")
      val result = connector.createAgent(obj)
      await(result).get.arn should be ("ARN150009")
    }
    "return false when bad request response «returned" in {
      stubPost("/registerAgent",500, "")
      val result = connector.createAgent(obj)
      await(result).get.arn shouldBe empty
    }
  }
}



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

package uk.gov.hmrc.agentsregfrontend.services

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
  val user: RegisteringUser = RegisteringUser("password", "businessName", "email", "0456".toInt, List("gg"), "propertyNumber", "postcode")

  "agentDetails" should {
    "return the ARN value" in {
      when(connector.createAgent(any())) thenReturn(Future.successful(Some(Agent("ARN0000005"))))
      val result = service.agentDetails(user)
      await(result) shouldBe Some(Agent("ARN0000005"))
    }
  }

}

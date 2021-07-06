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

package uk.gov.hmrc.agentsregfrontend.connectors

import play.api.libs.json.Json
import play.api.libs.ws.WSClient
import uk.gov.hmrc.agentsregfrontend.models.{Agent, RegisteringUser}
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AgentConnector @Inject()(ws: WSClient) {

  def createAgent(agent: RegisteringUser): Future[Option[Agent]] = {
    ws.url(s"http://localhost:9009/registerAgent").post(Json.obj("password" -> agent.password, "businessName" -> agent.businessName, "email" -> agent.email, "mobileNumber" -> agent.mobileNumber, "moc" -> agent.moc, "propertyNumber" -> agent.propertyNumber, "postcode" -> agent.postcode))
      .map { response =>
        response.status match {
          case 201 => Some(Agent((response.json \ "arn").as[String]))
          case 500 => None
        }
      }
  }
}
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

package uk.gov.hmrc.agentsregfrontend.controllers

import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import uk.gov.hmrc.agentsregfrontend.connectors.AgentConnector
import uk.gov.hmrc.agentsregfrontend.controllers.predicates.LoginChecker
import uk.gov.hmrc.agentsregfrontend.models.RegisteringUser
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import uk.gov.hmrc.agentsregfrontend.views.html.{ARNFailurePage, ARNSuccessPage, SummaryPage}
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SummaryController @Inject()(mcc: MessagesControllerComponents,
                                  connector: AgentConnector,
                                  loginChecker: LoginChecker,
                                  summarypage: SummaryPage,
                                  arnSuccess: ARNSuccessPage,
                                  arnFailure: ARNFailurePage) extends FrontendController(mcc) {

  def summary: Action[AnyContent] = Action async { implicit request =>
    loginChecker.authSession(data => {
      data.isComplete match {
        case true => Future.successful(Ok(summarypage(data)))
        case false => Future.successful(BadRequest("Registration failed"))
      }
    })
  }

  def getArn: Action[AnyContent] = Action async { implicit request =>
    loginChecker.authSession(data => {
      (request.session.get("password").isDefined, data.isComplete) match {
        case (true, true) => connector.createAgent(RegisteringUser(request.session.get("password").get, data.businessName.get, data.email.get, data.contactNumber.get, data.correspondence.get, data.address.get.propertyNumber, data.address.get.postcode)).map {
          case Some(x) => Ok(arnSuccess(x)).withSession("arn" -> x.arn)
          case None => BadRequest(arnFailure())
        }
        case _ => Future.successful(BadRequest(arnFailure()))
      }
    })
  }

}

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
import uk.gov.hmrc.agentsregfrontend.models._
import uk.gov.hmrc.agentsregfrontend.services.SummaryService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import uk.gov.hmrc.agentsregfrontend.views.html.{ARNFailurePage, ARNSuccessPage, SummaryPage}
import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global

class SummaryController @Inject()(mcc: MessagesControllerComponents, service: SummaryService, summarypage: SummaryPage, arnSuccess: ARNSuccessPage, arnFailure: ARNFailurePage) extends FrontendController(mcc) {

  def summary: Action[AnyContent] = Action { implicit request =>
    val businessName = request.session.get("businessName").get
    val email = request.session.get("email").get
    val contactNumber = request.session.get("contactNumber").get
    val address = Address.decode(request.session.get("address").get)
    val correspondence = Correspondence.decode(request.session.get("modes").get)
    val password = request.session.get("password").get
    val user = RegisteringUser(password, businessName, email, contactNumber, correspondence, address.propertyNumber, address.postcode)
    Ok(summarypage(user))
  }

  def getArn: Action[AnyContent] = Action async { implicit request =>
    val businessName = request.session.get("businessName").get
    val email = request.session.get("email").get
    val contactNumber = request.session.get("contactNumber").get
    val address = Address.decode(request.session.get("address").get)
    val correspondence = Correspondence.decode(request.session.get("modes").get)
    val password = request.session.get("password").get
    val user = RegisteringUser(password, businessName, email, contactNumber, correspondence, address.propertyNumber, address.postcode)
    service.agentDetails(user).map {
      case Some(x) => Ok(arnSuccess(x)).withSession("arn" -> x.arn)
      case None => BadRequest(arnFailure())
    }
  }

}

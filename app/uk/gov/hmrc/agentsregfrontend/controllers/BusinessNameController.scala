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
import uk.gov.hmrc.agentsregfrontend.views.html.{BusinessNamePage, SummaryPage}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import javax.inject.Inject

class BusinessNameController @Inject()(mcc: MessagesControllerComponents, businessNamePage: BusinessNamePage, summaryPage: SummaryPage)
  extends FrontendController(mcc) {

  def displayBusinessNamePage(isUpdate: Boolean): Action[AnyContent] = Action { implicit request =>
    request.session.get("arn") match {
      case Some(_) => Redirect("http://localhost:9005/agents-frontend/dashboard")
      case None => Ok(businessNamePage(BusinessName.form, isUpdate))
    }
  }

  def processBusinessName(isUpdate: Boolean): Action[AnyContent] = Action { implicit request =>
    BusinessName.form.bindFromRequest().fold(
      formWithErrors => BadRequest(businessNamePage(formWithErrors, isUpdate)),
      response =>
        if (isUpdate) {
          val updatedRegUser = RegisteringUser(
            request.session.get("password").getOrElse("NOT FOUND"),
            response.businessName,
            request.session.get("email").getOrElse("NOT FOUND"),
            request.session.get("mobileNumber").getOrElse("000").toInt,
            Correspondence.decode(request.session.get("modes").getOrElse("NOT FOUND")),
            Address.decode(request.session.get("address").get).propertyNumber,
            Address.decode(request.session.get("address").get).postcode
          )
          Ok(summaryPage(updatedRegUser))
        } else {
          Redirect(routes.EmailController.displayEmailPage(isUpdate = false)).withSession(request.session + ("businessName" -> response.businessName))
        }
    )
  }
}

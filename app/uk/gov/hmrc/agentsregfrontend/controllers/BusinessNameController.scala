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
import uk.gov.hmrc.agentsregfrontend.views.html.BusinessNamePage
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import javax.inject.Inject

class BusinessNameController @Inject()(mcc: MessagesControllerComponents, businessNamePage: BusinessNamePage)
  extends FrontendController(mcc) {

  def displayBusinessNamePage(isUpdate: Boolean): Action[AnyContent] = Action { implicit request =>
    request.session.get("arn") match {
      case Some(_) => Redirect("http://localhost:9005/agents-frontend/dashboard")
      case None => request.session.get("businessName").fold(
        Ok(businessNamePage(BusinessName.form.fill(BusinessName(businessName = "")), isUpdate))
      )
      { bName => Ok(businessNamePage(BusinessName.form.fill(BusinessName(businessName = bName)), isUpdate)) }
    }
  }

  def processBusinessName(isUpdate: Boolean): Action[AnyContent] = Action { implicit request =>
    BusinessName.form.bindFromRequest().fold(
      formWithErrors => BadRequest(businessNamePage(formWithErrors, isUpdate)),
      response =>
        if (isUpdate) {
          Redirect(routes.SummaryController.summary()).withSession(request.session + ("businessName" -> response.businessName))
        } else {
          Redirect(routes.EmailController.displayEmailPage(isUpdate = false)).withSession(request.session + ("businessName" -> response.businessName))
        }
    )
  }
}

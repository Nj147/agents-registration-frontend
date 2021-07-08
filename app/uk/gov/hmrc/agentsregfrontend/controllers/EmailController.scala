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
import uk.gov.hmrc.agentsregfrontend.models.{Address, Correspondence, Email, RegisteringUser}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import javax.inject.Inject
import uk.gov.hmrc.agentsregfrontend.views.html.{EmailPage, SummaryPage}

class EmailController @Inject()(  mcc: MessagesControllerComponents,
                                  emailPage: EmailPage,
                                  summaryPage: SummaryPage
                               )
  extends FrontendController(mcc) {

  def displayEmailPage(isUpdate: Boolean): Action[AnyContent] = Action { implicit request =>
    request.session.get("arn") match {
      case Some(_) => Redirect("http://localhost:9005/agents-frontend/dashboard")
      case None => Ok(emailPage(Email.emailForm.fill(Email(email="")), isUpdate))
    }
  }

  def processEmail(isUpdate: Boolean): Action[AnyContent] = Action { implicit request =>
    Email.emailForm.bindFromRequest().fold(
      formWithErrors => BadRequest(emailPage(formWithErrors, false)),
      response =>
        if(isUpdate) {
          Redirect(routes.SummaryController.summary()).withSession("email" -> response.email)
        } else {
          Redirect(routes.ContactNumberController.displayContactPage(isUpdate = false)).withSession(request.session + ("email" -> response.email))
        }
    )
  }
}
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
import uk.gov.hmrc.agentsregfrontend.controllers.predicates.LoginChecker
import uk.gov.hmrc.agentsregfrontend.models.Email
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import javax.inject.Inject
import uk.gov.hmrc.agentsregfrontend.views.html.EmailPage
import scala.concurrent.Future

class EmailController @Inject()(mcc: MessagesControllerComponents,
                                loginChecker: LoginChecker,
                                emailPage: EmailPage) extends FrontendController(mcc) {

  def displayEmailPage(isUpdate: Boolean): Action[AnyContent] = Action async { implicit request =>
    loginChecker.authSession(_.email.fold(
      Future.successful(Ok(emailPage(Email.emailForm.fill(Email(email = "")), isUpdate)))
    ) { email => Future.successful(Ok(emailPage(Email.emailForm.fill(Email(email)), isUpdate))) })
  }

  def processEmail(isUpdate: Boolean): Action[AnyContent] = Action async { implicit request =>
    loginChecker.authSession(_ =>
      Email.emailForm.bindFromRequest().fold(
        formWithErrors => Future.successful(BadRequest(emailPage(formWithErrors, isUpdate = false))),
        response => isUpdate match {
          case true => Future.successful(Redirect(routes.SummaryController.summary()).withSession(request.session + ("email" -> response.email)))
          case false => Future.successful(Redirect(routes.ContactNumberController.displayContactPage(isUpdate = false)).withSession(request.session + ("email" -> response.email)))
        }
      )
    )
  }
}
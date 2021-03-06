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
import uk.gov.hmrc.agentsregfrontend.models.Correspondence
import uk.gov.hmrc.agentsregfrontend.views.html.CorrespondencePage
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import javax.inject.Inject
import scala.concurrent.Future

class CorrespondenceController @Inject()(mcc: MessagesControllerComponents,
                                         loginChecker: LoginChecker,
                                         page: CorrespondencePage) extends FrontendController(mcc) {

  def displayCorrespondencePage(isUpdate: Boolean): Action[AnyContent] = Action async { implicit request =>
    loginChecker.authSession(_ => Future.successful(Ok(page(Correspondence.correspondenceForm, isUpdate))))
  }

  def processCorrespondence(isUpdate: Boolean): Action[AnyContent] = Action async { implicit request =>
    loginChecker.authSession(_ => {
      val response = Correspondence.correspondenceForm.bindFromRequest.get
      response.modes.size match {
        case 0 => Future.successful(BadRequest(page(Correspondence.correspondenceForm.withError("modes", "Please select at least one method of correspondence"), isUpdate = false)))
        case _ => isUpdate match {
          case true => Future.successful(Redirect(routes.SummaryController.summary()).withSession(request.session + ("modes" -> response.encode)))
          case false => Future.successful(Redirect(routes.PasswordController.displayPasswordPage()).withSession(request.session + ("modes" -> response.encode)))
        }
      }
    })
  }

}


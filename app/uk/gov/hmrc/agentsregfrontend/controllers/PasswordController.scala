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
import uk.gov.hmrc.agentsregfrontend.models.Password
import uk.gov.hmrc.agentsregfrontend.views.html.PasswordPage
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import javax.inject.Inject
import scala.concurrent.Future

class PasswordController @Inject()(mcc: MessagesControllerComponents,
                                   loginChecker: LoginChecker,
                                   passwordPage: PasswordPage) extends FrontendController(mcc) {

  val displayPasswordPage: Action[AnyContent] = Action async { implicit request =>
    loginChecker.authSession(_ => Future.successful(Ok(passwordPage(Password.passwordForm))))
  }

  def processPassword: Action[AnyContent] = Action async { implicit request =>
    loginChecker.authSession(_ =>
      Password.passwordForm.bindFromRequest.fold(
        formWithErrors => { Future.successful(BadRequest(passwordPage(formWithErrors)))
        }, formData => {
          formData.password match {
            case formData.passwordCheck => Future.successful(Redirect(routes.SummaryController.summary()).withSession(request.session + ("password" -> formData.password)))
            case _ => Future.successful(BadRequest(passwordPage(Password.passwordForm.withError("passwordCheck", "Password does not match"))))
          }
        }
      )
    )
  }


}

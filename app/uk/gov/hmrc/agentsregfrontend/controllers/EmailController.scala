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

import play.api.mvc.MessagesControllerComponents
import uk.gov.hmrc.agentsregfrontend.models.Email
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import javax.inject.Inject
import uk.gov.hmrc.agentsregfrontend.views.html.EmailPage

class EmailController @Inject()(mcc: MessagesControllerComponents,
                                 emailPage: EmailPage)
  extends FrontendController(mcc) {

  val displayEmailPage = Action { implicit request =>
    Ok(emailPage(Email.emailForm))
  }

  val processEmail = Action { implicit request =>
    Email.emailForm.bindFromRequest().fold(
      formWithErrors => BadRequest(emailPage(formWithErrors))
      ,email => Redirect(routes.EmailController.displayEmailPage()).withSession(request.session + ("email" -> email.email))
    )
  }
}
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
import uk.gov.hmrc.agentsregfrontend.models.ContactNumber
import uk.gov.hmrc.agentsregfrontend.views.html.ContactNumberPage
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import javax.inject.Inject
import scala.concurrent.Future

class ContactNumberController @Inject()(mcc: MessagesControllerComponents,
                                        loginChecker: LoginChecker,
                                        cnPage: ContactNumberPage) extends FrontendController(mcc) {

  def displayContactPage(isUpdate: Boolean): Action[AnyContent] = Action async { implicit request =>
    loginChecker.authSession(_.contactNumber.fold(
      Future.successful(Ok(cnPage(ContactNumber.contactForm.fill(ContactNumber("")), isUpdate)))
    ) { contactNumber => Future.successful(Ok(cnPage(ContactNumber.contactForm.fill(ContactNumber(contactNumber)), isUpdate))) }
    )
  }

  def processContactNumber(isUpdate: Boolean): Action[AnyContent] = Action async { implicit request =>
    loginChecker.authSession(_ =>
      ContactNumber.contactForm.bindFromRequest().fold(
        formWithErrors => Future.successful(BadRequest(cnPage(formWithErrors, isUpdate = false))),
        response => isUpdate match {
          case true => Future.successful(Redirect(routes.SummaryController.summary()).withSession(request.session + ("contactNumber" -> response.number)))
          case false => Future.successful(Redirect(routes.AddressController.displayAddressPage(isUpdate = false)).withSession(request.session + ("contactNumber" -> response.number)))
        }
      )
    )
  }
}
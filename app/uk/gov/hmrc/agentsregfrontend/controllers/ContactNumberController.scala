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
import uk.gov.hmrc.agentsregfrontend.models.{Address, ContactNumber, Correspondence, RegisteringUser}
import uk.gov.hmrc.agentsregfrontend.views.html.{ContactNumberPage, SummaryPage}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import javax.inject.Inject

class ContactNumberController @Inject()(mcc: MessagesControllerComponents,
                                        cnPage: ContactNumberPage,
                                        summaryPage: SummaryPage)
  extends FrontendController(mcc) {

  def displayContactPage(isUpdate: Boolean): Action[AnyContent] = Action { implicit request =>
    request.session.get("arn") match {
      case Some(_) => Redirect("http://localhost:9005/agents-frontend/dashboard")
      case None => Ok(cnPage(ContactNumber.contactForm, isUpdate))
    }
  }

  def processContactNumber(isUpdate: Boolean): Action[AnyContent] = Action { implicit request =>
    ContactNumber.contactForm.bindFromRequest().fold(
      formWithErrors => BadRequest(cnPage(formWithErrors, false)),
      response =>
        if(isUpdate) {
          val updatedRegUser = RegisteringUser(
            request.session.get("password").getOrElse("NOT FOUND"),
            request.session.get("businessName").getOrElse("NOT FOUND"),
            request.session.get("email").getOrElse("NOT FOUND"),
            response.number,
            Correspondence.decode(request.session.get("modes").getOrElse("NOT FOUND")),
            Address.decode(request.session.get("address").get).propertyNumber,
            Address.decode(request.session.get("address").get).postcode
          )
          Ok(summaryPage(updatedRegUser))
        } else {
          Redirect(routes.AddressController.displayAddressPage(isUpdate = false)).withSession(request.session + ("contactNumber" -> response.number.toString))
        }
    )
  }
}
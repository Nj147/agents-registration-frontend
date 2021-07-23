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
import uk.gov.hmrc.agentsregfrontend.models.Address
import uk.gov.hmrc.agentsregfrontend.views.html.AddressPage
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController
import javax.inject.Inject

class AddressController @Inject()(mcc: MessagesControllerComponents,
                                  addressPage: AddressPage,
                                  loginChecker: LoginChecker) extends FrontendController(mcc) {

  def displayAddressPage(isUpdate: Boolean): Action[AnyContent] = Action { implicit request =>
    loginChecker.isLoggedIn(_ => request.session.get("address").fold(
      Ok(addressPage(Address.addressForm.fill(Address(propertyNumber = "", postcode = "")), isUpdate))
    ) { address => Ok(addressPage(Address.addressForm.fill(Address.decode(address)), isUpdate)) })
  }

  def processAddress(isUpdate: Boolean): Action[AnyContent] = Action { implicit request =>
    loginChecker.isLoggedIn(_ =>
      Address.addressForm.bindFromRequest().fold(
        formWithErrors => BadRequest(addressPage(formWithErrors, isUpdate)),
        response =>
          if (isUpdate) {
            Redirect(routes.SummaryController.summary()).withSession(request.session + ("address" -> response.encode))
          } else {
            Redirect(routes.CorrespondenceController.displayCorrespondencePage(isUpdate = false)).withSession(request.session + ("address" -> response.encode))
          }
      ))
  }
}

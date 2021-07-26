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

package uk.gov.hmrc.agentsregfrontend.controllers.predicates

import play.api.mvc.{AnyContent, MessagesControllerComponents, MessagesRequest, Result}
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendController

import javax.inject.Inject
import scala.concurrent.Future

class LoginChecker @Inject()(mcc: MessagesControllerComponents) extends FrontendController(mcc) {

  def isLoggedIn(func: (Any) => Result)(implicit request: MessagesRequest[AnyContent]): Result = {
    request.session.get("arn") match {
      case Some(_) => Redirect("http://localhost:9005/agents-frontend/dashboard")
      case None => func()
    }
  }

  def isLoggedInAsync(func: (Any) => Future[Result])(implicit request: MessagesRequest[AnyContent]): Future[Result] = {
    request.session.get("arn") match {
      case Some(_) => Future.successful(Redirect("http://localhost:9005/agents-frontend/dashboard"))
      case None => func()
    }
  }

}

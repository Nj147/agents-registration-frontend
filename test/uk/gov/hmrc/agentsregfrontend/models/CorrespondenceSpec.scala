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

package uk.gov.hmrc.agentsregfrontend.models


import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CorrespondenceSpec extends AnyWordSpec with Matchers{
  val testCorrespondence: Correspondence = Correspondence(List("call", "text"))
  val encodedCorrespondence: String = "call,text"

  "Correspondence" should{
    "return an encoded string containing all members of the list" in {
      testCorrespondence.encode should include(testCorrespondence.modes.head)
      testCorrespondence.encode should include(testCorrespondence.modes(1))
      testCorrespondence.encode shouldBe encodedCorrespondence
    }
    "be able to decode a comma-separated list into a List of strings" in {
      Correspondence.decode(encodedCorrespondence) shouldBe testCorrespondence.modes
    }
  }
}

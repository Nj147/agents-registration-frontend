package uk.gov.hmrc.agentsregfrontend.models

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class AddressSpec extends AnyWordSpec with Matchers{
  val testAddress: Address = Address("2 New Street", "AB1 CD2")
  val encodedAddress: String = "2 New Street/AB1 CD2"

  "Address" should {
    "return an encoded string containing all variables" in {
      testAddress.encode shouldBe encodedAddress
    }
    "be able to decode a '/' seperated list into an Address object" in {
      Address.decode(encodedAddress) shouldBe testAddress
    }
  }
}

package uk.gov.hmrc.models

import org.scalatest.BeforeAndAfter
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.{JsSuccess, JsValue, Json}
import uk.gov.hmrc.agentsregfrontend.models.RegisteringUser

class RegisteringUserSpec extends AnyWordSpec with BeforeAndAfter with Matchers {

  val moc: List[String] = List("Text message")
  val regUser: RegisteringUser = RegisteringUser("pa55w0rd", "Business Ltd", "john@gmail.com", 74134323, moc, "21", "SE12 1BU")

  val regUserJs: JsValue = Json.parse(
    """{
      |"password": "pa55w0rd",
      |"businessName": "Business Ltd",
      |"email": "john@gmail.com",
      |"mobileNumber": 74134323,
      |"moc" : [
      |    "Text message"
      |],
      |"propertyNumber": "21",
      |"postcode": "SE12 1BU"
      |}""".stripMargin
  )

  "A RegisteringUser object" can {
    "be formatted to json" in {
      Json.toJson(regUser) shouldBe regUserJs
    }
  }

  "A RegisteringUser JsValue" can {
    "be formatted to a RegisteringUser object" in {
      Json.fromJson[RegisteringUser](regUserJs) shouldBe JsSuccess(regUser)
    }
  }


}

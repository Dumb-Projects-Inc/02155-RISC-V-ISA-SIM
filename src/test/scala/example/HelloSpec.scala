package example

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class HelloSpec extends AnyFunSpec with Matchers {
  
  describe("Hello") {
    it("should say hello") {
      Hello.greeting should be ("hello")
    }
  }
}

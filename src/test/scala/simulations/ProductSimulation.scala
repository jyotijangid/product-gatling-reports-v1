package simulations

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration.DurationInt


class ProductSimulation extends Simulation {

  //conf
  val value_conf = http.baseUrl("https://api.rvymart.com:443")
    .header("Accept",value="application/json")
    .header(name="content-type", value ="application/json")


  //scenario
  val scn = scenario("Get all Products")
    .exec(http("get all product details")
      .get("/ps/rvy/api/pms/v1/products")
      .check(status is 200)

     )
	 
    .exec(http("Get Product By Id")
      .get("/ps/rvy/api/pms/v1/product/118")
      .check(status is 200)
    )
	
	.exec(http("Post product")
      .post("/ps/rvy/api/pms/v1/product")
      .body(RawFileBody(filePath = "./src/test/resources/bodies/addProduct.json")).asJson
      .header(name="content-type",value = "application/json")
      .check(status is 200))

	.exec(http("Update product")
      .put("/ps/rvy/api/pms/v1/product")
      .body(RawFileBody(filePath = "./src/test/resources/bodies/updateProduct.json")).asJson
      .header(name="content-type",value = "application/json")
      .check(status is 200))

    

  //setup
//   setUp(scn.inject(atOnceUsers(users=100))).protocols(value_conf)
setUp(scn.inject(constantConcurrentUsers(50).during(30),
		rampConcurrentUsers(50).to(100).during(30))).protocols(value_conf)
	////      constantConcurrentUsers(200).during(2.minutes), // 1
////      rampConcurrentUsers(200).to(500).during(3.minutes)


}

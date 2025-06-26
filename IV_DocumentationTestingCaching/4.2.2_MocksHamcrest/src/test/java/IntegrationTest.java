
import org.example.Main;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;

//@SpringBootTest(classes = Main.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//public class IntegrationTest {
//
//    @LocalServerPort
//    private int port;
//
//    @BeforeAll
//    static void setupRestAssured() {
//        RestAssured.baseURI = "http://localhost";
//    }
//
//    @Test
//    void test() {
//        given()
//                .port(port)
//                .auth().basic("admin", "adminpass")
//                .when()
//                    .get("/download")
//                .then()
//                    .statusCode(200)
//                    .header("Content-Disposition", containsString("data.xlsx"))
//                    .contentType("application/vnd.openxmlformat-offecedocument.spreadsheetml.sheet")
//                    .body(notNullValue());
//
////        assertThat("Проверка идентификаторов", response.get().getId(), equalTo(id));
//    }
//}

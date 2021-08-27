import Specififactions.RequestSpecifications;
import Specififactions.ResponseSpecifications;
import model.comment;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class CommentTests extends Base{

    @Test(description = "Crear un comment nuevo")
    public void createCommentTest(){

        comment testcomment = new comment("some_name", "some_comment");

        given().spec(RequestSpecifications.userBasicAuthentication())
                .body(testcomment)
                .when()
                .post("/v1/comment/"+postId)
                .then()
                .log().all()
                .statusCode(200)
                .body("message", Matchers.equalTo("Comment created"));
    }

    @Test(description = "Caso negativo de la creacion de un nuevo comment")
    public void negativeCreateCommentTest(){

        comment testcomment = new comment("", "some_content");

        given().spec(RequestSpecifications.userBasicAuthentication())
                .body(testcomment)
                .when()
                .post("/v1/comment/"+postId)
                .then()
                .log().all()
                .statusCode(406)
                .body("message", Matchers.equalTo("Invalid form"));
    }

    @Test(description = "Obtener todos los comment")
    public void getAllCommentTest(){

        given().spec(RequestSpecifications.userBasicAuthentication())
                .when()
                .get("/v1/comments/"+postId)
                .then()
                .log().all()
                .statusCode(200)
                .body("results[0].data[0].id", Matchers.equalTo(CommentId));
    }

    @Test(description = "Caso negativo de Obtener todos los comment")
    public void negativeGetAllCommentTest(){

        given().spec(RequestSpecifications.userFakeBasicAuthentication())
                .when()
                .get("/v1/comments/"+postId)
                .then()
                .log().all()
                .statusCode(401)
                .body("message", Matchers.equalTo("Please login first"));
    }

    @Test(description = "Obtener un comment especifico")
    public void getOneCommentTest(){

        given().spec(RequestSpecifications.userBasicAuthentication())
                .when()
                .get("/v1/comment/" + postId + "/"+CommentId)
                .then()
                .log().all()
                .statusCode(200)
                .body("data.id", Matchers.equalTo(CommentId));
    }

    @Test(description = "caso negativo de Obtener un comment especifico")
    public void negativeGetOneCommentTest(){

        given().spec(RequestSpecifications.userBasicAuthentication())
                .when()
                .get("/v1/comment/" + CommentId + "/"+ postId)
                .then()
                .log().all()
                .statusCode(404)
                .body("Message", Matchers.equalTo("Comment not found"));
    }

    @Test(description = "actualizar un comment")
    public void updateCommentTest(){

        comment testcomment = new comment("some_Name", "Update");

        given().spec(RequestSpecifications.userBasicAuthentication())
                .body(testcomment)
                .when()
                .put("/v1/comment/" + postId + "/"+CommentId)
                .then()
                .log().all()
                .statusCode(200)
                .body("message", Matchers.equalTo("Comment updated"));
    }

    @Test(description = "caso negativo de actualizar un comment")
    public void negativeUpdateCommentTest(){

        comment testcomment = new comment("", "Update");

        given().spec(RequestSpecifications.userBasicAuthentication())
                .body(testcomment)
                .when()
                .put("/v1/comment/" + postId + "/"+CommentId)
                .then()
                .log().all()
                .statusCode(406)
                .body("message", Matchers.equalTo("Invalid form"));
    }

    @Test(description = "Eliminar un comment")
    public void deleteCommentTest(){

        given().spec(RequestSpecifications.userBasicAuthentication())
                .when()
                .delete("/v1/comment/" + postId + "/"+CommentId)
                .then()
                .log().all()
                .statusCode(200)
                .body("message", Matchers.equalTo("Comment deleted"));
    }

    @Test(description = "caso negativo de eliminar un comment")
    public void negativeDeletePostTest(){

        given().spec(RequestSpecifications.userBasicAuthentication())
                .when()
                .delete("/v1/comment/" + CommentId + "/"+postId)
                .then()
                .log().all()
                .statusCode(406)
                .body("message", Matchers.equalTo("Comment could not be deleted"));
    }
}
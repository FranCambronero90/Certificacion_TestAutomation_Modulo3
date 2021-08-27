import Specififactions.RequestSpecifications;
import Specififactions.ResponseSpecifications;
import io.restassured.response.Response;
import model.Post;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;

public class PostTests extends Base{

    @Test(description = "Crear un post nuevo")
    public void createPostTest(){

        Post testPost = new Post("some_title", "some_content");

        given().spec(RequestSpecifications.useJWTAuthentication())
                .body(testPost)
                .when()
                .post("/v1/post")
                .then()
                .log().all()
                .spec(ResponseSpecifications.validatePositiveResponse())
                .body("message", Matchers.equalTo("Post created"));
    }

    @Test(description = "Caso negativo de la creacion de un nuevo post")
    public void negativeCreatePostTest(){

        Post testPost = new Post("", "some_content");

        given().spec(RequestSpecifications.useJWTAuthentication())
                .body(testPost)
                .when()
                .post("/v1/post")
                .then()
                .log().all()
                .statusCode(406)
                .body("message", Matchers.equalTo("Invalid form"));
    }

    @Test(description = "Obtener todos los posts", groups = "usePost")
    public void getAllPostTest(){

        given().spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .get("/v1/posts")
                .then()
                .log().all()
                .statusCode(200)
                .body("results[0].data[0].id", Matchers.equalTo(postId));
    }

    @Test(description = "CAso negativo de Obtener todos los posts", groups = "usePost")
    public void negativeGetAllPostTest(){

        given().spec(RequestSpecifications.useFakeJWTAuthentication())
                .when()
                .get("/v1/posts")
                .then()
                .log().all()
                .statusCode(401)
                .body("message", Matchers.equalTo("Please login first"));
    }

    @Test(description = "Obtener un post especifico")
    public void getOnePostTest(){

        given().spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .get("/v1/post/" + postId)
                .then()
                .log().all()
                .statusCode(200)
                .body("data.id", Matchers.equalTo(postId));

    }

    @Test(description = "caso negativo de Obtener un post especifico")
    public void negativeGetOnePostTest(){

        given().spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .get("/v1/post/" + postId*36)
                .then()
                .log().all()
                .statusCode(404)
                .body("Message", Matchers.equalTo("Post not found"));

    }

    @Test(description = "actualizar un post")
    public void updatePostTest(){

        Post testPost = new Post("some_title", "Update");

        given().spec(RequestSpecifications.useJWTAuthentication())
                .body(testPost)
                .when()
                .put("/v1/post/" + postId)
                .then()
                .log().all()
                .statusCode(200)
                .body("message", Matchers.equalTo("Post updated"));
    }

    @Test(description = "caso negativo de actualizar un post")
    public void negativeUpdatePostTest(){

        Post testPost = new Post("", "Update");

        given().spec(RequestSpecifications.useJWTAuthentication())
                .body(testPost)
                .when()
                .put("/v1/post/" + postId)
                .then()
                .log().all()
                .statusCode(406)
                .body("message", Matchers.equalTo("Invalid form"));
    }

    @Test(description = "Eliminar un post")
    public void deletePostTest(){

        given().spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .delete("/v1/post/" + postId)
                .then()
                .log().all()
                .statusCode(200)
                .body("message", Matchers.equalTo("Post deleted"));
    }

    @Test(description = "caso negativo de eliminar un post")
    public void negativeDeletePostTest(){

        given().spec(RequestSpecifications.useJWTAuthentication())
                .when()
                .delete("/v1/post/" + postId*36)
                .then()
                .log().all()
                .statusCode(406)
                .body("message", Matchers.equalTo("Post could not be deleted"));
    }
}
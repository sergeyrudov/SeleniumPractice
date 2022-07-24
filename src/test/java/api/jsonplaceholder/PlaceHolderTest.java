package api.jsonplaceholder;

import api.jsonplaceholder.photos.Photo;
import api.jsonplaceholder.posts.Post;
import api.jsonplaceholder.users.UserResource;
import api.reqres.spec.Specifications;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class PlaceHolderTest {
    private final static String URL = "https://jsonplaceholder.typicode.com/";

    @Test
    public void test(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        List<UserResource> users = given()
                .when()
                .get("users")
                .then().log().all()
                .extract().body().jsonPath().getList(".",UserResource.class);

        List<String> phones = new ArrayList<>();

        users.stream().filter(x->x.getPhone().contains(".")).forEach(x->phones.add(x.getPhone()));

        Assertions.assertEquals(2, phones.size());
    }

    @Test
    public void test2(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        List<Photo> photos = given()
                .when()
                .get("photos")
                .then().log().all()
                .extract().body().jsonPath().getList(".",Photo.class);
        Assertions.assertEquals(photos.size(),5000);

        Set<Integer> albumIds = photos.stream().map(Photo::getAlbumId).collect(Collectors.toSet());
        Assertions.assertEquals(albumIds.size(),100);
    }


    @Test
    public void test3(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        List<Photo> photos = given()
                .when()
                .get("photos")
                .then().log().all()
                .extract().body().jsonPath().getList(".",Photo.class);
        String regex = "\\d{3}\\/";
        photos.forEach(x->Assertions.assertEquals(x.getThumbnailUrl().replaceAll(regex,""),
                x.getUrl().replaceAll(regex,"")));
    }


    @Test
    public void test4(){
        Specifications.installSpecification(Specifications.requestSpec(URL), Specifications.responseSpecOK200());
        List<Post> posts = given()
                .when()
                .get("posts")
                .then().log().all()
                .extract().body().jsonPath().getList(".",Post.class);
        int postIndex = 5;
        Assertions.assertEquals(postIndex,posts.get(postIndex).getId()-1);
    }
}

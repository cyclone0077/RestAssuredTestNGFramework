package com.spotify.oauth2.api;

import com.spotify.oauth2.pojo.Playlist;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;

import static com.spotify.oauth2.api.Route.*;
import static com.spotify.oauth2.api.SpecBuilder.*;
import static io.restassured.RestAssured.given;

public class RestResource {

    public static Response post(String path, String token, Object requestPlaylist){

        return given(getRequestSpec()).
                auth().oauth2(token).
                body(requestPlaylist).
        when().
                post(path).

        then().spec(getResponseSpec()).
                extract().
                response();
    }

    public static Response postSong( String path, String token, Object songTrackId){

        return given(getRequestSpec()).
                auth().oauth2(token).
                body(songTrackId).
                when().
                post(path + TRACKS).

        then().spec(getResponseSpec()).
                extract().
                response();
    }

    public static Response postAccount(HashMap<String, String> formParam){

        return given(getAccountRequestSpec()).
                formParams(formParam).
        when()
                .post(API + TOKEN).
        then().spec(getResponseSpec()).
                extract().
                response();
    }

    public static Response get(String path, String token){

        return given(getRequestSpec()).
                auth().oauth2(token).
        when().
                get(path).

        then().spec(getResponseSpec()).
                extract().
                response();
    }

    public static Response update(String path, String token, Object requestPlaylist){

         return given(getRequestSpec()).
                 auth().oauth2(token).
                 body(requestPlaylist).

         when().
                put(path).

         then().spec(getResponseSpec()).
                 extract().
                 response();
    }
}

package com.spotify.oauth2.api;

import com.spotify.oauth2.utils.ConfigLoader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.time.Instant;
import java.util.HashMap;

import static com.spotify.oauth2.api.SpecBuilder.getResponseSpec;
import static io.restassured.RestAssured.given;

public class TokenManager {
    private static String access_token;
    private static Instant expiry_time;

    public synchronized static String getToken(){
        try{
            if(access_token == null || Instant.now().isAfter(expiry_time)){
                System.out.println("Renewing token ...");
                Response response = renewToken();
                access_token = response.path("access_token");
                int expiryDurationInSeconds = response.path("expires_in");
                expiry_time = Instant.now().plusSeconds(expiryDurationInSeconds - 300);
            } else{
                System.out.println("Token is good to use");
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("ABORT!!! Failed to get token");
        }
        return access_token;
    }

    public static Response renewToken(){
        HashMap<String, String> formParam = new HashMap<String, String>();
        formParam.put("grant_type", ConfigLoader.getInstance().getGrantType());
        formParam.put("refresh_token",ConfigLoader.getInstance().getRefreshToken());
        formParam.put("client_id", ConfigLoader.getInstance().getClientId());
        formParam.put("client_secret", ConfigLoader.getInstance().getClientSecret());

        Response response = RestResource.postAccount(formParam);

        if(response.statusCode() != 200){
            throw new RuntimeException("Abort! Renew Token Failed");
        }
        return response;

    }


}

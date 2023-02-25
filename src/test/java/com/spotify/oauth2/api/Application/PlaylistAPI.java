package com.spotify.oauth2.api.Application;

import com.spotify.oauth2.api.RestResource;
import com.spotify.oauth2.pojo.AddSongInPlaylist;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.ConfigLoader;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.spotify.oauth2.api.Route.PLAYLISTS;
import static com.spotify.oauth2.api.Route.USERS;
import static com.spotify.oauth2.api.TokenManager.getToken;
import static io.restassured.RestAssured.given;

public class PlaylistAPI {
  //  static String access_token = "BQBHlmsMj0u-JSRIa93t-fBtYAd4slilJDymaFR4lCMJlHzAP4RI5hHVyfUw_wjivVUHp-aWbCENUyJj9DPKFoJDEy28UYCpKluMYBAZjoUXWr8A3y7S5cs3WO6TznUJr9LjYycclZ-9KvZowLDPd7E5WVf4gOnKi72Quje_p6qZONuNPdSQhRfZItFG6Pi3iBnknXhFEDK_Vx9FpVwow_kDisC_9JY9nqB_KmHa-oPsISFOKt2MeGTUwqu2QvRrK1exWIuM1exWvTBYqw";

    @Step
    public static Response post(Playlist requestPlaylist){
        return RestResource.post(USERS + ConfigLoader.getInstance().getUserId() + PLAYLISTS, getToken(), requestPlaylist);

    }

    public static Response postSong(String playlistID, AddSongInPlaylist songTrackId){
        return RestResource.postSong(PLAYLISTS + "/" + playlistID, getToken(), songTrackId);

    }

    public static Response post(String token, Playlist requestPlaylist){
        return RestResource.post(USERS + ConfigLoader.getInstance().getUserId() + PLAYLISTS, token, requestPlaylist);

    }



    public static Response get(String playlistId){
        return RestResource.get(PLAYLISTS + "/" + playlistId, getToken());

    }



    public static Response update(String playlistID, Playlist requestPlaylist){
        return RestResource.update(PLAYLISTS + "/" + playlistID, getToken(), requestPlaylist);

    }
}

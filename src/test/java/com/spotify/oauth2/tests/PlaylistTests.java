package com.spotify.oauth2.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spotify.oauth2.api.Application.PlaylistAPI;
import com.spotify.oauth2.api.StatusCode;
import com.spotify.oauth2.pojo.Error;
import com.spotify.oauth2.pojo.AddSongInPlaylist;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

import static com.spotify.oauth2.utils.FakeUtils.generatePlaylistDescription;
import static com.spotify.oauth2.utils.FakeUtils.generatePlaylistName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

@Epic("Spotify Oauth 2.0")
@Feature("Playlist API")

public class PlaylistTests extends BaseTest {
    public static final ObjectMapper mapper = new ObjectMapper();

    @Story("Create A Playlist Story")
    @Description("Here I am creating a Fresh Playlist, fetching the Playlist ID once it is created and then using the same ID for updating the Playlist name and Description")
    @Test(description = "User Should Be Able to Create A Playlist")
    public void ShouldBeAbleToCreateAPlaylist(){
        Playlist requestPlaylist = playlistBuilder(generatePlaylistName(), generatePlaylistDescription(), false);

        Response response = PlaylistAPI.post(requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_201.code);

        Playlist responsePlaylist = response.as(Playlist.class);
        assertPlaylistEqual(responsePlaylist, requestPlaylist);

        String StorePlaylistID = responsePlaylist.getId();
        System.out.println("The ID of the created Playlist is " + StorePlaylistID);
        PlaylistTests playlistTests= new PlaylistTests();
        playlistTests.getIdFromPost(StorePlaylistID);
        playlistTests.UpdatePlaylistGetFromPost(StorePlaylistID);
    }
    public void getIdFromPost(String StorePlaylistID){

        Response response = PlaylistAPI.get(StorePlaylistID);
        assertThat(response.statusCode(), equalTo(200));

    }
    public void UpdatePlaylistGetFromPost(String StorePlaylistID){

        Playlist requestPlaylist = playlistBuilder("Experiment Successful", "Operation Successful", false);

        Response response = PlaylistAPI.update(StorePlaylistID, requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_200.code);
    }


    @Description("Here I am adding a song item in a Playlist")
    @Test(description = "User Should Be Able to Add a Song in Playlist")
    public void postTrack(){

        AddSongInPlaylist songTrackId = expertBuilder(Collections.singletonList("spotify:track:4iV5W9uYEdYUVa79Axb7Rh"), 0);
        Response response = PlaylistAPI.postSong(DataLoader.getInstance().getSongId(),songTrackId);
        assertStatusCode(response.statusCode(), StatusCode.CODE_201.code);
    }

    @Description("Here I am fetching an already created Playlist and then comparing the fields of Playlist in Request and Response")
    @Test(description = "User Should be Able to Get A Playlist using the Playlist ID")
    public void ShouldBeAbleToGetAPlaylist(){

        Playlist requestPlaylist = playlistBuilder("Updated POJO Playlist", "Updated Description of POJO Playlist 4", false);

        Response response = PlaylistAPI.get(DataLoader.getInstance().getPlaylistId());
        assertStatusCode(response.statusCode(), StatusCode.CODE_200.code);
        Playlist responsePlaylist = response.as(Playlist.class);
        assertPlaylistEqual(responsePlaylist, requestPlaylist);

    }



    @Description("Here I am updating the name of Playlist which is already created and then comparing the Playlist's fields in Request and Response")
    @Test (description = "Should be Able to Update the Playlist's Parameters")
    public void ShouldBeAbleToUpdateAPlaylist(){

        Playlist requestPlaylist = playlistBuilder("Updated POJO Playlist", "Updated Description of POJO Playlist 4", false);

        Response response = PlaylistAPI.update(DataLoader.getInstance().getUpdatePlaylistId(), requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_200.code);
    }

    @Story("Create A Playlist Story")
    @Description("Here I should not be able to create a Playlist if the Playlist name is not provided")
    @Test(description = "User Should Not be Able to Create A Playlist When The Name is not provided in Name param")
    public void ShouldNotBeAbleToCreateAPlaylistWithoutName(){

        Playlist requestPlaylist = playlistBuilder("", "Updated Description of POJO Playlist 4", false);

       Response response = PlaylistAPI.post(requestPlaylist);
       assertStatusCode(response.statusCode(), StatusCode.CODE_400.code);

        Error error = response.as(Error.class);
        assertError(error, StatusCode.CODE_400.code, StatusCode.CODE_400.msg);

    }
    @Story("Create A Playlist Story")
    @Description("Here I should not be able to create a Playlist if the Token has expired")
    @Test(description = "Should Not be Able to Create a Playlist Using the Expired Token")
    public void ShouldNotBeAbleToCreateAPlaylistWithExpiredToken(){

        Playlist requestPlaylist = playlistBuilder("Playlist with Expired Token", "Updated Description of POJO Playlist", false);

        Response response = PlaylistAPI.post("BQCk7m7hCX5GXwdJlW5vgWdoXKSOfetipHBDgKeRTZQnPZ-0fwfh5mMQOK14B2bXxMOc4vijI1DpwVh16WaO2tns75T7cjPazvMFIRhpIir-YEXXdYu9WVP20CXso0RZdBBYG2sAdKKrFxPt3MX6VRLq2SasowpiKQFnRef0VTHAcxr0Vg5P9mPciPxhcROMATQUP4gN3-CRzSsnsJXeFYStEIsLOt31W1xlhsrSCu27uVBs8CfpiKpfdZ6THsebwv2YTS6e8EIKHN15HQ", requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_401.code);

        Error error = response.as(Error.class);

        assertError(error, StatusCode.CODE_401.code, StatusCode.CODE_401.msg);
    }
    @Step
    public Playlist playlistBuilder(String name, String description, boolean _public){
        return Playlist.builder().
                name(name).
                description(description).
                _public(_public).
                build();

    }
    @Step
    public AddSongInPlaylist expertBuilder(List<String> uris, int position){
        return AddSongInPlaylist.builder().
                uris(uris).
                position(position).
                build();
    }

    @Step
    public void assertPlaylistEqual(Playlist responsePlaylist, Playlist requestPlaylist){

        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.get_public(), equalTo(requestPlaylist.get_public()));
    }

    @Step
    public void assertStatusCode(int actualStatusCode, int expectedStatusCode){
        assertThat(actualStatusCode, equalTo(expectedStatusCode));
    }

    @Step
    public void assertError(Error error, int expectedStatusCode, String expectedMsg){

        assertThat(error.getError().getStatus(), equalTo(expectedStatusCode));
        assertThat(error.getError().getMessage(), equalTo(expectedMsg));
    }

}





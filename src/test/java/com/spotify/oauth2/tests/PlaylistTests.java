package com.spotify.oauth2.tests;

import com.spotify.oauth2.api.Application.PlaylistAPI;
import com.spotify.oauth2.api.StatusCode;
import com.spotify.oauth2.pojo.Error;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;
import io.qameta.allure.*;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static com.spotify.oauth2.utils.FakeUtils.generatePlaylistDescription;
import static com.spotify.oauth2.utils.FakeUtils.generatePlaylistName;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;

@Epic("Spotify Oauth 2.0")
@Feature("Playlist API")

public class PlaylistTests extends BaseTest {
    String ResponseGetId;
    @Story("Create A Playlist Story")
    @Description("Description: This is the Description of Test Case 1 i.e. ShouldBeAbleToCreateAPlaylist")
    @Test(description = "Should Be Able to Create A Playlist")
    public void ShouldBeAbleToCreateAPlaylist(){
//Builder Pattern with POJO classes
        Playlist requestPlaylist = playlistBuilder(generatePlaylistName(), generatePlaylistDescription(), false);

        Response response = PlaylistAPI.post(requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_201.code);

        Playlist responsePlaylist = response.as(Playlist.class);

        assertPlaylistEqual(responsePlaylist, requestPlaylist);

        System.out.println("The ID of the created Playlist is " + responsePlaylist.getId());
        ResponseGetId = responsePlaylist.getId();
        System.out.println(ResponseGetId);
    }

//    @Test
//    public void getIdFromPost(){
//
//        Playlist requestPlaylist = new Playlist().
//                setName("Updated POJO Playlist 4").
//                setDescription("Updated Description of POJO Playlist 4").
//                setPublic(false);
//
//        Response response = PlaylistAPI.get(DataLoader.getInstance().getPlaylistId());
//        assertThat(response.statusCode(), equalTo(200));
//
//        Playlist responsePlaylist = response.as(Playlist.class);
//        System.out.println("This is what " + ResponseGetId);
//
//        assertThat(responsePlaylist.getName(), equalTo(requestPlaylist.getName()));
//        assertThat(responsePlaylist.getDescription(), equalTo(requestPlaylist.getDescription()));
//        assertThat(responsePlaylist.getPublic(), equalTo(requestPlaylist.getPublic()));
//
//    }

    @Description("Description: This is the Description of Test Case 2 i.e. ShouldBeAbleToGetAPlaylist")
    @Test(description = "Should be Able to Get A Playlist using the Playlist ID")
    public void ShouldBeAbleToGetAPlaylist(){

        Playlist requestPlaylist = playlistBuilder("Updated POJO Playlist 4", "Updated Description of POJO Playlist 4", false);

        Response response = PlaylistAPI.get(DataLoader.getInstance().getPlaylistId());
        assertStatusCode(response.statusCode(), StatusCode.CODE_200.code);
        Playlist responsePlaylist = response.as(Playlist.class);
        assertPlaylistEqual(responsePlaylist, requestPlaylist);

    }
    @Description("Description: This is the Description of Test Case 3 i.e. ShouldBeAbleToUpdateAPlaylist")
    @Test (description = "Should be Able to Update the Playlist's Parameters")
    public void ShouldBeAbleToUpdateAPlaylist(){

        Playlist requestPlaylist = playlistBuilder("Updated POJO Playlist 4", "Updated Description of POJO Playlist 4", false);

        Response response = PlaylistAPI.update(DataLoader.getInstance().getUpdatePlaylistId(), requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_200.code);
    }

    @Story("Create A Playlist Story")
    @Description("Description: This is the Description of Test Case 4 i.e. ShouldNotBeAbleToCreateAPlaylistWithoutName")
    @Test(description = "Should Not be Able to Create A Playlist When The Name is not provided in Name param")
    public void ShouldNotBeAbleToCreateAPlaylistWithoutName(){

        Playlist requestPlaylist = playlistBuilder("", "Updated Description of POJO Playlist 4", false);

       Response response = PlaylistAPI.post(requestPlaylist);
       assertStatusCode(response.statusCode(), StatusCode.CODE_400.code);

        Error error = response.as(Error.class);
        asserError(error, StatusCode.CODE_400.code, StatusCode.CODE_400.msg);

    }
    @Story("Create A Playlist Story")
    @Description("Description: This is the Description of Test Case 5 i.e. ShouldNotBeAbleToCreateAPlaylistWithExpiredToken")
    @Test(description = "Should Not be Able to Create a Playlist Using the Expired Token")
    public void ShouldNotBeAbleToCreateAPlaylistWithExpiredToken(){

        Playlist requestPlaylist = playlistBuilder("Playlist with Expired Token", "Updated Description of POJO Playlist", false);

        Response response = PlaylistAPI.post("BQCk7m7hCX5GXwdJlW5vgWdoXKSOfetipHBDgKeRTZQnPZ-0fwfh5mMQOK14B2bXxMOc4vijI1DpwVh16WaO2tns75T7cjPazvMFIRhpIir-YEXXdYu9WVP20CXso0RZdBBYG2sAdKKrFxPt3MX6VRLq2SasowpiKQFnRef0VTHAcxr0Vg5P9mPciPxhcROMATQUP4gN3-CRzSsnsJXeFYStEIsLOt31W1xlhsrSCu27uVBs8CfpiKpfdZ6THsebwv2YTS6e8EIKHN15HQ", requestPlaylist);
        assertStatusCode(response.statusCode(), StatusCode.CODE_401.code);

        Error error = response.as(Error.class);

        asserError(error, StatusCode.CODE_401.code, StatusCode.CODE_401.msg);
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
    public void asserError(Error error, int expectedStatusCode, String expectedMsg){

        assertThat(error.getError().getStatus(), equalTo(expectedStatusCode));
        assertThat(error.getError().getMessage(), equalTo(expectedMsg));
    }

}



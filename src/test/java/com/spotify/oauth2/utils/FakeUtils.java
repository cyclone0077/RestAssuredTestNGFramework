package com.spotify.oauth2.utils;

import com.github.javafaker.Faker;

public class FakeUtils {

    public static String generatePlaylistName() {
        Faker faker = new Faker();
        return "Name " + faker.regexify("[A-Za-z0-9,_-]{10}");

    }

    public static String generatePlaylistDescription() {
        Faker faker = new Faker();
        return "Description " + faker.regexify("[A-Za-z0-9_@#&+,_-]{10}");

    }
}

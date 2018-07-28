package com.example.raju.demoBlog.Utils;

public class UrlUtils {
    // BUILDING BLOCKS
    public static final String BASE_URL = "https://www.googleapis.com/";
    private static final String TEST_BLOG_ID = "3865433669670337662";
    public static final String PATH = "/blogger/v3/blogs/" + TEST_BLOG_ID + "/posts";
    public static final String QUERY_API_KEY = "?key=AIzaSyB_fdeiLFL9PrFxJIqRjovHfdRtjigwSjw";

    // FETCH BODY Query params
    public static final String QUERY_FETCH_BODIES = "&fetchBodies=false";
}

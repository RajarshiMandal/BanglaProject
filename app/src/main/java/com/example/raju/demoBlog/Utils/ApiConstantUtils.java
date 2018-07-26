package com.example.raju.demoBlog.Utils;

public class ApiConstantUtils {
    public static final String BASE_URL = "https://www.googleapis.com/";

    private static final String API_KEY = "AIzaSyB_fdeiLFL9PrFxJIqRjovHfdRtjigwSjw";
    private static final String TEST_BLOG_ID = "3865433669670337662";
    // Query params
    private static final String FETCH_BODIES = "&fetchBodies=false";
    // Final url
    public static final String SERVICE_CALL_URL = "/blogger/v3/blogs/" + TEST_BLOG_ID +
            "/posts?key=" + API_KEY +
            FETCH_BODIES;
    private static final String FETCH_IMAGES = "&fetchImages=false";
    private static final String MAX_RESULTS = "&maxResults=15";
    private static final String ORDER_BY = "&orderBy=updated";
    private static final String STATUS = "&status=live";
//            FETCH_IMAGES +
////            MAX_RESULTS +
//            ORDER_BY;
//            STATUS;
}

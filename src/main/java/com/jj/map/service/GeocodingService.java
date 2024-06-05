package com.jj.map.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.HttpUrl;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class GeocodingService {

    private static final Logger LOGGER = Logger.getLogger(GeocodingService.class.getName());
    private final OkHttpClient httpClient = new OkHttpClient();

    public JSONObject search(String query) throws IOException {
        // Encode the query parameter
        String encodedQuery = HttpUrl.parse("https://nominatim.openstreetmap.org/search")
                .newBuilder()
                .addQueryParameter("format", "json")
                .addQueryParameter("q", query)
                .build()
                .toString();

        Request request = new Request.Builder()
                .url(encodedQuery)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                LOGGER.log(Level.SEVERE, "Unexpected response code: " + response.code());
                throw new IOException("Unexpected code " + response);
            }

            String responseBody = response.body().string();
            JSONArray jsonArray = new JSONArray(responseBody);

            if (jsonArray.length() > 0) {
                return jsonArray.getJSONObject(0);
            } else {
                LOGGER.log(Level.WARNING, "Empty response for query: " + query);
                return null;
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error occurred while making HTTP request", e);
            throw e;
        }
    }
}

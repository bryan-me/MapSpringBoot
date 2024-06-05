package com.jj.map.controller;

import com.jj.map.service.GeocodingService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
public class GeocodingController {

    @Autowired
    private GeocodingService geocodingService;

    @GetMapping("/search")
    @ResponseBody
    public ResponseEntity<?> search(@RequestParam("query") String query) {
        JSONObject location;
        try {
            location = geocodingService.search(query);
            if (location != null) {
                return ResponseEntity.ok(location.toString());
            } else {
                return ResponseEntity.status(404).body("Location not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error occurred while searching for location");
        }
    }
}

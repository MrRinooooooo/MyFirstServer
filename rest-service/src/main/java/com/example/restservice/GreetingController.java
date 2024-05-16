package com.example.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    @Autowired
    private ObjectMapper objectMapper;

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name){
        return new Greeting(counter.incrementAndGet(),String.format(template, name));
    }

    // GET endpoint
    @GetMapping("/data")
    public ResponseEntity<String> getData(@RequestHeader Map<String, String> headers) {
        headers.forEach((key, value) -> {
            System.out.println(String.format("Header '%s' = %s", key, value));
        });

        return new ResponseEntity<>("Headers received", HttpStatus.OK);
    }
    // POST endpoint
    @PostMapping("/data")
    public ResponseEntity<String> postData(@RequestHeader HttpHeaders headers, @RequestBody String body) {
        headers.forEach((key, value) -> {
            System.out.println(String.format("Header '%s' = %s", key, value));
        });

        System.out.println("Request Body: " + body);

        try {
            JsonNode jsonNode = objectMapper.readTree(body);
            String prettyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
            System.out.println("Formatted JSON Body: " + prettyJson);
            //jsonNode.get();

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid JSON format", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Headers and body received", HttpStatus.OK);
    }
}

package com.goffity.demo.fcm.push.controllers;

import com.goffity.demo.fcm.push.service.AndroidPushNotificationsService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class WebController {

    private static final Logger logger = LoggerFactory.getLogger(WebController.class);
    String TOPIC = "Goffity-topic";

    @Autowired
    AndroidPushNotificationsService androidPushNotificationsService;

    @RequestMapping(value = "/send", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> send() throws JSONException {

        JSONObject notification = new JSONObject();
        notification.put("title", "JSA Notification");
        notification.put("body", "Happy Message!");

        JSONObject message = new JSONObject();

        message.put("to", "fDROMn3hUNU:APA91bE206dsHFvfV_U2MmG43hOe6v887bQlqWNmY1LjDiYFL_2NTp8xH2U84hNCuWbUbLq3P7p6C8EJ0mO0Sx5M6O58LUPqOkeIJjOC4EWqt-0zVmUGUPhXOuBHvaQN5XOFZUPyrwXX");
        message.put("notification", notification);
        message.put("picture", "http://opsbug.com/static/google-io.jpg");

        logger.debug("Body: " + message.toString());

        HttpEntity<String> request = new HttpEntity<>(message.toString());

        CompletableFuture<String> pushNotification = androidPushNotificationsService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();

            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Error!!!", HttpStatus.BAD_REQUEST);
    }

}

package com.example.Mybackendintellij;

import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class FcmService {

    private static final String PROJECT_ID = "directmessa";

    private static final String FCM_URL =
            "https://fcm.googleapis.com/v1/projects/" + PROJECT_ID + "/messages:send";

    private GoogleCredentials getCredentials() throws IOException {

        InputStream serviceAccount =
                new FileInputStream("firebase-service-account.json");

        return GoogleCredentials.fromStream(serviceAccount)
                .createScoped(List.of("https://www.googleapis.com/auth/firebase.messaging"));
    }

    private String getAccessToken() throws IOException {
        GoogleCredentials credentials = getCredentials();
        credentials.refreshIfExpired();
        return credentials.getAccessToken().getTokenValue();
    }

    public void sendNotification(String fcmToken, String title, String body) {

        if (fcmToken == null || fcmToken.isBlank()) return;

        try {
            String accessToken = getAccessToken();

            String payload = """
            {
              "message": {
                "token": "%s",
                "notification": {
                  "title": "%s",
                  "body": "%s"
                }
              }
            }
            """.formatted(fcmToken, title, body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(FCM_URL))
                    .header("Authorization", "Bearer " + accessToken)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostConstruct
    public void testFirebaseFile() {
        try {
            GoogleCredentials credentials =
                    GoogleCredentials.getApplicationDefault()
                            .createScoped(List.of(
                                    "https://www.googleapis.com/auth/firebase.messaging"));

            credentials.refreshIfExpired();
            System.out.println("✅ FIREBASE FILE FOUND AND READABLE");

        } catch (Exception e) {
            System.out.println("❌ FIREBASE FILE NOT FOUND OR INVALID");
            e.printStackTrace();
        }
    }

}

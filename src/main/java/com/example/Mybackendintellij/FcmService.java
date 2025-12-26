package com.example.Mybackendintellij;

import com.google.auth.oauth2.GoogleCredentials;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
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

        String json = System.getenv("GOOGLE_APPLICATION_CREDENTIALS_JSON");

        if (json == null || json.isBlank()) {
            throw new RuntimeException("❌ Firebase credentials not found in env");
        }

        InputStream stream = new ByteArrayInputStream(json.getBytes());

        return GoogleCredentials.fromStream(stream)
                .createScoped(List.of("https://www.googleapis.com/auth/firebase.messaging"));
    }

    private String getAccessToken() throws IOException {
        GoogleCredentials credentials = getCredentials();
        credentials.refreshIfExpired();
        return credentials.getAccessToken().getTokenValue();
    }

    public void sendNotification(String fcmToken, String title, String body) {

        if (fcmToken == null || fcmToken.isBlank()) {
            System.out.println("❌ FCM token missing");
            return;
        }

        try {
            String accessToken = getAccessToken();

            String payload = """
            {
              "message": {
                "token": "%s",
                "data": {
                  "title": "%s",
                  "body": "%s",
                  "type": "chat"
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

            HttpResponse<String> response =
                    HttpClient.newHttpClient()
                            .send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("🔥 FCM STATUS = " + response.statusCode());
            System.out.println("🔥 FCM RESPONSE = " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

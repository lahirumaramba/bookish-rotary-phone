package com.example;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ListUsersPage;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import org.junit.jupiter.api.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class FirebaseAdminTest {

    private static FirebaseApp app;

    @BeforeAll
    public static void setup() {
        // Initialize the app via ADC
        if (FirebaseApp.getApps().isEmpty()) {
            try {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .setServiceAccountId("firebase-adminsdk-fbsvc@admin-sdk-wif.iam.gserviceaccount.com")
                        .setProjectId("admin-sdk-wif")
                        .build();
                app = FirebaseApp.initializeApp(options);
            } catch (IOException e) {
                throw new RuntimeException("Failed to initialize FirebaseApp with ADC credentials", e);
            }
        } else {
            app = FirebaseApp.getInstance();
        }
    }

    @Test
    public void test1InitializeApp() {
        assertNotNull(app);
        System.out.println("Firebase Admin SDK initialized successfully via ADC");
    }

    @Test
    public void test2AuthGetUsers() throws Exception {
        FirebaseAuth auth = FirebaseAuth.getInstance(app);
        assertNotNull(auth);

        try {
            ListUsersPage page = auth.listUsers(null, 1);
            int count = com.google.common.collect.Iterables.size(page.getValues());
            System.out.println("Successfully fetched " + count + " users.");
            assertNotNull(page.getValues());
        } catch (Exception e) {
            System.err.println("Auth test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void test3FirestoreRead() throws Exception {
        Firestore db = FirestoreClient.getFirestore(app);
        assertNotNull(db);

        try {
            DocumentReference docRef = db.collection("wif-demo").document("test-connection");

            DocumentSnapshot doc = docRef.get().get();
            assertTrue(doc.exists());
            assertEquals("Hello from GitHub Actions WIF!", doc.getString("message"));
            assertNotNull(doc.get("timestamp"));
            System.out.println("Firestore read successful.");
        } catch (Exception e) {
            System.err.println("Firestore test failed: " + e.getMessage());
            throw e;
        }
    }

    @Test
    public void test4CreateCustomToken() throws Exception {
        FirebaseAuth auth = FirebaseAuth.getInstance(app);
        String uid = "wif-demo-user-123";

        try {
            String customToken = auth.createCustomToken(uid);
            assertNotNull(customToken);
            assertFalse(customToken.isEmpty());
            System.out.println("Successfully created a custom signed token.");
        } catch (Exception e) {
            System.err.println("Token signing test failed: " + e.getMessage());
            throw e;
        }
    }
}

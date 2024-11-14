package com.c1se22.publiclaundsmartsystem.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Configuration
public class FirebaseConfig {
    @Bean
    FirebaseApp firebaseApp() throws IOException {
        FileInputStream serviceAccount;
        try {
            serviceAccount = new FileInputStream("config/firebase_key.json");
        } catch (FileNotFoundException e) {
            serviceAccount = new FileInputStream("src/main/resources/config/firebase_key.json");
        }
        
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://laundrysystem-d73d7-default-rtdb.asia-southeast1.firebasedatabase.app")
                .build();
        return FirebaseApp.initializeApp(options);
    }
    @Bean
    FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) throws IOException {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
    @Bean
    public FirebaseDatabase firebaseDatabase(FirebaseApp firebaseApp) throws IOException {
        return FirebaseDatabase.getInstance(firebaseApp);
    }
}

package edu.wgu.d387_sample_code.rest;

import edu.wgu.d387_sample_code.model.LocalizedWelcomeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@CrossOrigin
@RestController
@RequestMapping("/welcome")
public class WelcomeController {

    private final ExecutorService executor = Executors.newFixedThreadPool(2);
    private final Properties enProperties = new Properties();
    private final Properties frProperties = new Properties();

    private final LocalizedWelcomeMessage localizedWelcomeMessage = new LocalizedWelcomeMessage();

    public LocalizedWelcomeMessage loadWelcomeMessages() {

        executor.execute(() -> {
            try {
                InputStream enStream = new ClassPathResource("i18n/welcome_en_US.properties").getInputStream();
                enProperties.load(enStream);
                localizedWelcomeMessage.setEnglishMessage(enProperties.getProperty("welcome"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.execute(() -> {
            try {
                InputStream frStream = new ClassPathResource("i18n/welcome_fr_CA.properties").getInputStream();
                frProperties.load(frStream);
                localizedWelcomeMessage.setFrenchMessage(frProperties.getProperty("welcome"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return localizedWelcomeMessage;
    }

    @GetMapping("/en")
    public ResponseEntity<String> getEnglishMessage() {
        String englishMessage = loadWelcomeMessages().getEnglishMessage();
        return new ResponseEntity<>(englishMessage, HttpStatus.OK);
    }

    @GetMapping("/fr")
    public ResponseEntity<String> getFrenchMessage() {
        String frenchMessage = loadWelcomeMessages().getFrenchMessage();
        return new ResponseEntity<>(frenchMessage, HttpStatus.OK);
    }
}



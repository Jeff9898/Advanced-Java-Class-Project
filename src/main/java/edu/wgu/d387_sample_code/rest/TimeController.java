package edu.wgu.d387_sample_code.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/time")
public class TimeController {

@GetMapping("/convert")
public ResponseEntity<String> getConvertedTimes() {

    LocalDateTime currentTime = LocalDateTime.now();

    // Define time zones
    ZoneId etZone = ZoneId.of("America/New_York");
    ZoneId mtZone = ZoneId.of("America/Denver");
    ZoneId utcZone = ZoneId.of("UTC");

    // Convert local time to respective time zones
    ZonedDateTime zonedDateTimeEastern = currentTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(etZone);
    ZonedDateTime zonedDateTimeMountain = currentTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(mtZone);
    ZonedDateTime zonedDateTimeUTC = currentTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(utcZone);

    // Change time format to AM/PM
    String etTime = zonedDateTimeEastern.format(DateTimeFormatter.ofPattern("hh:mm a")) + " Eastern Time (ET)";
    String mtTime = zonedDateTimeMountain.format(DateTimeFormatter.ofPattern("hh:mm a")) + " Mountain Time (MT)";
    String utcTime = zonedDateTimeUTC.format(DateTimeFormatter.ofPattern("hh:mm a")) + " Coordinated Universal Time (UTC)";


    String result = etTime + "<br>" + mtTime + "<br>" + utcTime;

    return new ResponseEntity<>(result, HttpStatus.OK);
}


}

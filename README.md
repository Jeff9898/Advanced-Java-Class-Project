**Advanced Java - Jeff Starr**

# **Multithreaded Spring Application with Cloud Deployment**

# Landon Hotel Scheduling Application

This project demonstrates the development and enhancement of the Landon Hotel Scheduling application. It includes multithreading, internationalization/localization, currency display, and containerization using Docker. Below are the steps taken to complete the project.

---

## 1. **Multithreaded Welcome Messages**

### Implementation:
The application displays welcome messages in English and French using multithreading. This improves performance by loading resource bundles for English (`welcome_en_US.properties`) and French (`welcome_fr_CA.properties`) concurrently.

### Key Code Snippet (Backend - WelcomeController):
```java
// Creating threads to load the English and French resource bundles concurrently
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
```
This code defines two threads to simultaneously load the English and French messages into a shared LocalizedWelcomeMessage object. Using threads ensures both files load quickly without blocking each other.

**Front-End Integration:**
Angular fetches the welcome messages from the /welcome/en and /welcome/fr endpoints.

```angular (typescript file)
// Fetching English and French messages from the back end
  getWelcomeMessages(): void {
    // Fetch English welcome message
    this.httpClient.get(`${this.baseURL}/welcome/en`, { responseType: 'text' }).subscribe({
      next: (data: string) => {
        this.englishWelcomeMessage = data;
      },
      error: (error) => {
        console.error('Error fetching English welcome message:', error);
      },
      complete: () => {
        console.log('English welcome message fetch completed');
      }
    });
```
Explanation: These HTTP requests retrieve the English and French welcome messages from the REST API and bind them to variables (welcomeMessageEn and welcomeMessageFr) to be displayed in the UI.


# **2. Currency Display**
Implementation:
The application displays room prices in three currencies: USD, CAD, and Euros. Although the currency values are hardcoded, this meets internationalization requirements for displaying multiple formats.


Explanation: For each room, the code calculates equivalent prices in CAD and Euros based on fixed conversion rates and displays them alongside the USD price.

HTML Display:
```
<strong>Room #: {{ room.roomNumber }}</strong><br/>
<strong>Price: ${{ room.price }}</strong><br>
<p>Price (USD): {{ room.price | currency:'USD' }}</p>
<p>Price (CAD): {{ room.price | currency:'CAD' }}</p>
<p>Price (EUR): {{ room.price | currency:'EUR' }}</p>

```
Explanation: The HTML table dynamically shows room prices in all three currencies. The data is fetched and bound using Angular’s two-way data binding.


# **3. Time Zone Conversion**
Implementation:
The application converts a specific presentation time into three time zones: Eastern Time (ET), Mountain Time (MT), and Coordinated Universal Time (UTC).

Key Code Snippet (Backend - TimeController):
```
// Method to convert time zones
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
```
Explanation:
This method takes an input time and converts it to three different time zones using Java’s ZoneId and LocalTime classes.

HTML Display:
```
<div>
  <h3>Live Presentation Time</h3>
  <p>Eastern Time: {{ timeData.ET }}</p>
  <p>Mountain Time: {{ timeData.MT }}</p>
  <p>Coordinated Universal Time: {{ timeData.UTC }}</p>
</div>
```
Explanation:
Angular binds the time zone conversion data to display the presentation times in ET, MT, and UTC.

# **4. Docker Containerization**
Implementation Steps:
Dockerfile:

```
FROM openjdk:17-jdk-slim
COPY target/D387_sample_code-0.0.2-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
EXPOSE 8080
```
Explanation:

Specifies the base image (openjdk:17-jdk-slim).
Copies the application JAR file into the container.
Sets the entry point to run the application.
Exposes port 8080 for the application.

After running the container, the application is accessible via http://localhost:8080.



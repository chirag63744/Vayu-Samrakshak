#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>

#define FIREBASE_HOST "https://vayusamrakshak-default-rtdb.firebaseio.com" // Your Firebase Project URL goes here without "http:" , "\" and "/"
#define FIREBASE_AUTH "tOE7jJ8OknBIHRNnyu9kCCbQQt0os8zNDBOd12Uh"             // Your Firebase Database Secret goes here

#define WIFI_SSID "Shree ram"            // WiFi SSID to which you want NodeMCU to connect
#define WIFI_PASSWORD "itsme63744"       // Password of your wifi network
#define BUZZER_PIN D2                    // Define buzzer pin
#define SMOKE_SENSOR_PIN A0              // Define smoke sensor pin
#define THRESHOLD_VALUE 600              // Define threshold value for triggering the buzzer

// Declare the Firebase Data object in the global scope
FirebaseData firebaseData;

void setup() {
  Serial.begin(115200);                       // Select the same baud rate if you want to see the datas on Serial Monitor
  pinMode(SMOKE_SENSOR_PIN, INPUT);
  pinMode(BUZZER_PIN, OUTPUT);
  Serial.println("Serial communication started\n\n");

  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);        // Try to connect with WiFi
  Serial.print("Connecting to ");
  Serial.print(WIFI_SSID);

  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }

  Serial.println();
  Serial.print("Connected to ");
  Serial.println(WIFI_SSID);
  Serial.print("IP Address is : ");
  Serial.println(WiFi.localIP());              // Print local IP address
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH); // Connect to Firebase

  Firebase.reconnectWiFi(true);
  delay(1000);
}

void loop() {
  int analogSensor = analogRead(SMOKE_SENSOR_PIN);

  // Firebase Error Handling And Writing Data At Specified Path
  if (Firebase.setInt(firebaseData, "/carbonEmission", analogSensor)) { // On successful Write operation, function returns 1
    Serial.println("Value Uploaded Successfully to smart Contract");
    Serial.print("Carbon Emission = ");
    Serial.println(analogSensor);
    Serial.println("\n");

    // Check if carbon emission exceeds threshold value
    if (analogSensor > THRESHOLD_VALUE) {
      triggerBuzzer();
    }
  } else {
    Serial.println(firebaseData.errorReason());
  }
}

// Function to trigger the buzzer
void triggerBuzzer() {
  digitalWrite(BUZZER_PIN, HIGH); // Turn on the buzzer
  delay(1000);                     // Wait for 1 second
  digitalWrite(BUZZER_PIN, LOW);  // Turn off the buzzer
}

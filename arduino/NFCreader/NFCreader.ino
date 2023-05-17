
#include <SPI.h>
#include <MFRC522.h>
#define SS_PIN 5
#define RST_PIN 14
#include <WiFi.h>
#include <HTTPClient.h>

MFRC522 rfid(SS_PIN, RST_PIN); // Instance of the class
 
MFRC522::MIFARE_Key key; 


const char* ssid = "TP-Link_A0D0";
const char* password = "18306736";

const char* loggingUrl = "http://192.168.2.186:8888/api/v1/attendance/log-student";
const char* addCardUrl = "http://192.168.2.186:8888/api/v1/card/create-card";

const String masterCardHex = " b1 ae 43 1d";
String mode = "log";

void initWifiConncection(){
  WiFi.mode(WIFI_AP_STA);
  WiFi.begin(ssid, password);
  Serial.println("Connecting");
    while(WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.print(".");
  }
  Serial.println("");
  Serial.print("Connected to WiFi network with IP Address: ");
  Serial.println(WiFi.localIP());

  WiFi.setAutoReconnect(true);
  WiFi.persistent(true);

}

void setup() {
  Serial.begin(115200);

  pinMode(LED_BUILTIN, OUTPUT); // Sets the builtin led to output mode

  initWifiConncection(); //Init wifi connection

  SPI.begin(); // Init SPI bus

  rfid.PCD_Init(); // Init RC522 
}

void loop() {
  String cardHex = "";
  String newCardHex = "";

  // Reset the loop if no new card present on the sensor/reader. This saves the entire process when idle.
  if ( ! rfid.PICC_IsNewCardPresent())
    return;
 
  // Verify if the NUID has been readed
  if ( ! rfid.PICC_ReadCardSerial())
    return;
  
  MFRC522::PICC_Type piccType = rfid.PICC_GetType(rfid.uid.sak);

  Serial.print(F("RFID Tag UID:"));
  cardHex = hexToStr(rfid.uid.uidByte, rfid.uid.size);
  Serial.println(cardHex);

  if(cardHex == masterCardHex){

  }

  rfid.PICC_HaltA(); 

  if(cardHex == masterCardHex){
    Serial.println("Mode changed from: " + mode);
    toggleMode();
    Serial.println("To: " + mode);
    Serial.println("");
    return;
  }



  if(WiFi.status()== WL_CONNECTED){
    WiFiClient client;
    HTTPClient http;
    int httpResponseCode = 0;
    if(mode == "log"){
      http.begin(client, loggingUrl);
      http.addHeader("Content-Type", "text/plain");
      String httpRequestData = cardHex;           
      httpResponseCode = http.POST(httpRequestData);
      if(httpResponseCode == 200){
        digitalWrite(LED_BUILTIN, HIGH);
        delay(500);                      
        digitalWrite(LED_BUILTIN, LOW);
      }  

      Serial.print("HTTP Response code: ");
      Serial.println(httpResponseCode);
        
      http.end();

    }else if(mode == "add"){
      http.begin(client, addCardUrl);
      http.addHeader("Content-Type", "text/plain");
      String httpRequestData = cardHex;           
      httpResponseCode = http.POST(httpRequestData);
      if(httpResponseCode == 200){
        digitalWrite(LED_BUILTIN, HIGH);
        delay(500);
        digitalWrite(LED_BUILTIN, LOW);
        delay(500);
        digitalWrite(LED_BUILTIN, HIGH);
        delay(500);
        digitalWrite(LED_BUILTIN, LOW);
      }  

      Serial.print("HTTP Response code: ");
      Serial.println(httpResponseCode);
        
      http.end();
    }
  }
  else {
    Serial.println("WiFi Disconnected");
  }

  
}

void toggleMode(){
  if(mode == "log"){
    mode = "add";
  }
  else if(mode == "add"){
    mode = "log";
  }
}



String hexToStr(byte *buffer, byte bufferSize) {
  String str1 = "";
  String str2 = "";
  String str3 = "";
  for (byte i = 0; i < bufferSize; i++) {
    str1 = buffer[i] < 0x10 ? " 0" : " ";
    str2 = String(buffer[i], HEX);
    str3 += str1 + str2;
  }
  return str3;
}
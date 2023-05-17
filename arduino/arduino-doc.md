1. The code includes the necessary libraries for the MFRC522 RFID module, WiFi connectivity, and HTTP client functionality.

2. The `MFRC522` instance is created with the appropriate SS (Slave Select) and RST (Reset) pins.

3. The `MFRC522::MIFARE_Key` object `key` is defined. This key is used for authenticating MIFARE cards.

4. The code sets up the WiFi credentials (`ssid` and `password`) for the network it should connect to.

5. The URLs for logging attendance (`loggingUrl`) and adding a new card (`addCardUrl`) are specified.

6. The `masterCardHex` variable stores the hexadecimal representation of the master card that triggers a mode change.

7. The `mode` variable is initialized as "log". It determines whether the code is in "log" mode or "add" mode.

8. The `initWifiConncection()` function is responsible for initiating the WiFi connection using the provided credentials.

9. In the `setup()` function:
   - Serial communication is started.
   - The built-in LED pin is configured as an output.
   - The WiFi connection is established.
   - The SPI bus is initialized.
   - The RC522 module is initialized.

10. In the `loop()` function:
    - The `cardHex` variable is used to store the UID of the detected RFID card as a hexadecimal string.
    - If no new card is present, the loop is reset.
    - The UID of the card is read and stored in `cardHex`.
    - If the master card is detected, it triggers a mode change, toggling between "log" and "add".
    - The card detection is halted.
    - If the WiFi is connected:
      - A `WiFiClient` and an `HTTPClient` object are created.
      - Depending on the mode ("log" or "add"), the appropriate HTTP request is made to the corresponding URL.
      - The HTTP response code is checked, and the built-in LED is toggled accordingly.
      - The HTTP connection is closed.
    - If the WiFi is disconnected, a message is printed.

11. The `toggleMode()` function switches the `mode` variable between "log" and "add".

12. The `hexToStr()` function converts a byte array to a hexadecimal string representation.

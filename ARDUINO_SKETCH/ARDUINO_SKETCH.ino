#include <Wire.h>
#include <Adafruit_NeoPixel.h>
#include <SPI.h>
#include <WiFiNINA.h>
#include "arduino_secrets.h"

#define OUTPUT_TEXTS true

#define PIN         3
#define PIN_7       5
#define PIN8        4
#define PIN9        6
#define PIN10       7

#define TCAADDR 0x70
uint8_t /*const int*/ MPU_addr = 0x68; // I2C address of the MPU-6050

#define __BAUD__ 57600
#define NUMPIXELS   75
#define GYRO_RANGE  12000
#define CHANGE_MILLIS_TIMER 1500
Adafruit_NeoPixel pixels = Adafruit_NeoPixel(NUMPIXELS, PIN, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel pixels_7 = Adafruit_NeoPixel(3, PIN_7, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel pixels_8 = Adafruit_NeoPixel(3, PIN8, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel pixels_9 = Adafruit_NeoPixel(3, PIN9, NEO_GRB + NEO_KHZ800);
Adafruit_NeoPixel pixels_10 = Adafruit_NeoPixel(3, PIN10, NEO_GRB + NEO_KHZ800);

char ssid[] = SECRET_SSID;        // your network SSID (name)
char pass[] = SECRET_PASS;    // your network password (use for WPA, or use as key for WEP)
char server[] = SECRET_SERVER;

int port = SECRET_PORT;
int status = WL_IDLE_STATUS;     // the Wifi radio's status
WiFiClient client;


long change_millis[4];
boolean change = true;
byte byteArray[25];
byte legArray[4];
int previous[] = {0, 0, 0, 0, 0};
int AcX[5], AcY[5], AcZ[5];
boolean tca[5];

char startcond = 64;
char endcond = 35;

int numGyroConst;
int trueFalse[5];

int seven, eight, nine, ten;

int receivedArr[25];
bool startDataTransmit = false;

void start_pixels() {
  pixels.begin();
  pixels.show();
  pixels.setBrightness(255);

  pixels_7.begin();
  pixels_7.show();
  pixels_7.setBrightness(255);

  pixels_8.begin();
  pixels_8.show();
  pixels_8.setBrightness(255);

  pixels_9.begin();
  pixels_9.show();
  pixels_9.setBrightness(255);

  pixels_10.begin();
  pixels_10.show();
  pixels_10.setBrightness(255);
}

void tcaselect (uint8_t i) {
  if (i > 7) return;
  Wire.beginTransmission(TCAADDR);
  Wire.write(1 << i);
  Wire.endTransmission();
}

size_t helper_14 = 14;

void dataGet() {
  for (int numberGyro = 0; numberGyro < 4; numberGyro++) {
    if (tca[numberGyro] == true) {
      tcaselect(numberGyro);
      Wire.beginTransmission(MPU_addr);
      Wire.write(0x3B);  // starting with register 0x3B (ACCEL_XOUT_H)
      Wire.endTransmission(false);
      Wire.requestFrom(MPU_addr, /*14*/helper_14, true); // request a total of 14 registers
      AcX[numberGyro] = Wire.read() << 8 | Wire.read(); // 0x3B (ACCEL_XOUT_H) & 0x3C (ACCEL_XOUT_L)
      AcY[numberGyro] = Wire.read() << 8 | Wire.read(); // 0x3D (ACCEL_YOUT_H) & 0x3E (ACCEL_YOUT_L)
      AcZ[numberGyro] = Wire.read() << 8 | Wire.read(); // 0x3F (ACCEL_ZOUT_H) & 0x40 (ACCEL_ZOUT_L)
      if (AcY[numberGyro] > GYRO_RANGE || AcY[numberGyro] < -GYRO_RANGE) {
        trueFalse[numberGyro] = 1;
      } else {
        trueFalse[numberGyro] = 0;
      }
    } else {
      trueFalse[numberGyro] = 0;
    }
  }
}

void numberGyro() {
  for (int numberGyro = 0; numberGyro < 4; numberGyro ++) {
    tcaselect(numberGyro);
    Wire.beginTransmission(MPU_addr);
    Wire.write(0x3B);  // starting with register 0x3B (ACCEL_XOUT_H)
    Wire.endTransmission(false);
    Wire.requestFrom(MPU_addr, helper_14/*14*/, true); // request a total of 14 registers
    AcX[numberGyro] = Wire.read() << 8 | Wire.read(); // 0x3B (ACCEL_XOUT_H) & 0x3C (ACCEL_XOUT_L)
    AcY[numberGyro] = Wire.read() << 8 | Wire.read(); // 0x3D (ACCEL_YOUT_H) & 0x3E (ACCEL_YOUT_L)
    AcZ[numberGyro] = Wire.read() << 8 | Wire.read(); // 0x3F (ACCEL_ZOUT_H) & 0x40 (ACCEL_ZOUT_L)
  }

  for (int numberGyro = 0; numberGyro < 4; numberGyro++) {
    if (AcX[numberGyro] == -1) {
      tca[numberGyro] = false;
    }
    else {
      tca[numberGyro] = true;
    }
  }
}

void distDataShow() {
  if(distDataGet()) {
    for (int b = 0, chip = 0; chip < 75; chip += 3, b++) {
      pixels.setPixelColor(chip, pixels.Color(receivedArr[b], receivedArr[b], receivedArr[b]));
      pixels.setPixelColor(chip + 1, pixels.Color(receivedArr[b], receivedArr[b], receivedArr[b]));
      pixels.setPixelColor(chip + 2, pixels.Color(receivedArr[b], receivedArr[b], receivedArr[b]));
      //Serial.println("### " + String(receivedArr[b]));
    }
    pixels.show();
  }
}

String receivedByte;
char recChar;
  
bool distDataGet() {
  receivedByte = "";
  if (client.available()) {
    recChar = char(client.read());
    if (recChar == startcond) {
      receivedByte = recChar;
      //Serial.println("Begin");
      do {
        recChar = char(client.read());
        //delay(3);
        if(byte(recChar) == 255) {
          continue;
        }
        //Serial.print(recChar);
        receivedByte += String(recChar);
      } while (recChar != endcond);
      //Serial.println("!!! " + receivedByte);
    }
  } else {
    return false;
  }

  if (receivedByte.indexOf(startcond) != -1 && receivedByte.indexOf(byte('V')) != -1) {
    //Serial.println("Voila " + receivedByte + "\t" + String(receivedByte.substring(receivedByte.indexOf("V") + 1, receivedByte.indexOf(":")).toInt()));
    //@V10:255#
    switch (receivedByte.substring(receivedByte.indexOf("V") + 1, receivedByte.indexOf(":")).toInt()) {
      case 7:
        seven = receivedByte.substring(receivedByte.indexOf(":") + 1, receivedByte.indexOf("#")).toInt();
        pixels_7.setPixelColor(0, pixels.Color(eight, eight, eight));
        pixels_7.setPixelColor(1, pixels.Color(eight, eight, eight));
        pixels_7.setPixelColor(2, pixels.Color(eight, eight, eight));
        pixels_7.show();
        break;
      case 8:
        eight = receivedByte.substring(receivedByte.indexOf(":") + 1, receivedByte.indexOf("#")).toInt();
        pixels_8.setPixelColor(0, pixels.Color(seven, seven, seven));
        pixels_8.setPixelColor(1, pixels.Color(seven, seven, seven));
        pixels_8.setPixelColor(2, pixels.Color(seven, seven, seven));
        pixels_8.show();
        break;
      case 9:
        nine = receivedByte.substring(receivedByte.indexOf(":") + 1, receivedByte.indexOf("#")).toInt();
        pixels_9.setPixelColor(0, pixels.Color(nine, nine, nine));
        pixels_9.setPixelColor(1, pixels.Color(nine, nine, nine));
        pixels_9.setPixelColor(2, pixels.Color(nine, nine, nine));
        pixels_9.show();
        break;
      case 10:
        ten = receivedByte.substring(receivedByte.indexOf(":") + 1, receivedByte.indexOf("#")).toInt();
        pixels_10.setPixelColor(0, pixels.Color(ten, ten, ten));
        pixels_10.setPixelColor(1, pixels.Color(ten, ten, ten));
        pixels_10.setPixelColor(2, pixels.Color(ten, ten, ten));
        pixels_10.show();
        break;
      default:
        if(OUTPUT_TEXTS) {
        Serial.println("Error: Couldn't assign gyroscopes with received string");
        Serial.println("---> " + String(receivedByte.substring(receivedByte.indexOf("V") + 1, receivedByte.indexOf(":")).toInt()));
        }
        break;
    }
    if(OUTPUT_TEXTS) {
    Serial.println("Legs: " + String(seven) + " " + String(eight) + " " + String(nine) + " " + String(ten));
    }
    //Upper body must not be set up again, because only lower-body data got received
    return false;
  } else if (receivedByte.indexOf(startcond) != -1) {
    stringToIntArr(receivedByte, receivedArr);
    
    //Received data from raspi. Initialisation of the raspi is over and legs can be 
    //controlled nowon by the arduino. W/o this imp., the raspi-initialisation would fail.
    if(!startDataTransmit) {
      startDataTransmit = true;
    }
  }
  
  return true;
}

int nextInt = 0;

void stringToIntArr(String str, int *arr) {
//  Serial.println("!!! " + str);
  for (int i = 0, j = 0; i < 25 && j < 1000; j++) {
    nextInt = str.toInt();
    if (nextInt == 0) {
      str = str.substring(1, str.length());
      continue;
    }
    arr[i] = nextInt;
    i++;
    str = str.substring(str.indexOf(","), str.length());
  }
  if(OUTPUT_TEXTS) {
    Serial.println("###################################");
    for (int i = 24; i >= 20; i--) {
      Serial.println(String(arr[i]) + "\t" + String(arr[i - 5]) + "\t" + String(arr[i - 10]) + "\t" + String(arr[i - 15]) + "\t" + String(arr[i - 20]));
    }
    Serial.println("###################################");
  }
}

void dataTransmit() {
  dataGet();
  for (int i = 0; i < 4; i++) {
    //Change sending state due to position change
    if (trueFalse[i] != previous[i]) {
      change = true;
      //Stop sending due to millis change to reset the sensor-filtering
    } else if (millis() - change_millis[i] >= CHANGE_MILLIS_TIMER) {
      change_millis[i] = millis();
      //Only change sending-state from yes to no; no to yes is not wanted
      if (trueFalse[i] == 1) {
        change = true;
        trueFalse[i] = 0;
      }

      //Did not change
    } else {
      change = false;
    }

    if (change == true) {
      //Example-String to send: "@S-8:f#" -> Sensor 8 should transmit waves; f = fire, p = peace
      client.println(String(startcond) + "S-" + String(i + 7) + ":" + (trueFalse[i] == 1 ? "f" : "p") + String(endcond));
      //Serial.println("### " + String(startcond) + "S-" + String(i + 7) + ":" + (trueFalse[i] == 1 ? "f" : "p") + String(endcond));
      /*
      if(trueFalse[i] == 0) {
        if(i == 0) {
          pixels_8.setPixelColor(0, pixels.Color(0, 0, 0));
          pixels_8.setPixelColor(1, pixels.Color(0, 0, 0));
          pixels_8.setPixelColor(2, pixels.Color(0, 0, 0));
          pixels_8.show();
        } else if(i == 1) {
          pixels_7.setPixelColor(0, pixels.Color(0, 0, 0));
          pixels_7.setPixelColor(1, pixels.Color(0, 0, 0));
          pixels_7.setPixelColor(2, pixels.Color(0, 0, 0));
          pixels_7.show();
        } else if(i == 2) {
          pixels_9.setPixelColor(0, pixels.Color(0, 0, 0));
          pixels_9.setPixelColor(1, pixels.Color(0, 0, 0));
          pixels_9.setPixelColor(2, pixels.Color(0, 0, 0));
          pixels_9.show();
        } else if(i == 3) {
          pixels_10.setPixelColor(0, pixels.Color(0, 0, 0));
          pixels_10.setPixelColor(1, pixels.Color(0, 0, 0));
          pixels_10.setPixelColor(2, pixels.Color(0, 0, 0));
          pixels_10.show();
        }
      }
      */
      change = false;
    }

    //Update sending state for sensor
    previous[i] = trueFalse[i];
  }
}

void wifiConnect() {
  Serial.println("Attempting to connect to WPA network...");
  // check for the WiFi module:
  if (WiFi.status() == WL_NO_MODULE) {
    Serial.println("Communication with WiFi module failed!");
    // don't continue
    while (true);
  }

  // attempt to connect to Wifi network:
  while (status != WL_CONNECTED) {
    Serial.print("Attempting to connect to WPA SSID: ");
    Serial.println(ssid);
    // Connect to WPA/WPA2 network:
    status = WiFi.begin(ssid, pass);
  }

  // you're connected now, so print out the data:
  Serial.print("You're connected to the network");
  printCurrentNet();
  printWifiData();
}

//Methods to print out information about the connected Network
void printWifiData() {
  // print your board's IP address:
  IPAddress ip = WiFi.localIP();
  Serial.print("IP Address: ");
  Serial.println(ip);
  Serial.println(ip);

  // print your MAC address:
  byte mac[6];
  WiFi.macAddress(mac);
  Serial.print("MAC address: ");
  printMacAddress(mac);
}

void printCurrentNet() {
  // print the SSID of the network you're attached to:
  Serial.print("SSID: ");
  Serial.println(WiFi.SSID());

  // print the MAC address of the router you're attached to:
  byte bssid[6];
  WiFi.BSSID(bssid);
  Serial.print("BSSID: ");
  printMacAddress(bssid);

  // print the received signal strength:
  long rssi = WiFi.RSSI();
  Serial.print("signal strength (RSSI):");
  Serial.println(rssi);

  // print the encryption type:
  byte encryption = WiFi.encryptionType();
  Serial.print("Encryption Type:");
  Serial.println(encryption, HEX);
  Serial.println();
}

void printMacAddress(byte mac[]) {
  for (int i = 5; i >= 0; i--) {
    if (mac[i] < 16) {
      Serial.print("0");
    }
    Serial.print(mac[i], HEX);
    if (i > 0) {
      Serial.print(":");
    }
  }
  Serial.println();
}

void connectToServer() {
  Serial.println("\nStarting connection to server...");
  // if you get a connection, report back via serial:
  for(int i = 0; i < 10; i++) {
    if (client.connect(server, port)) {
      Serial.println("connected to server");
      client.println("a");
      break;
    }
  }
}

void setup() {
  Wire.begin();
  Serial.begin(__BAUD__);
//  Serial.println("Wait for 25 seconds");
//  delay(25000);
  wifiConnect();
  connectToServer();

  for (int numberGyro = 0; numberGyro < 4; numberGyro++) {
    Serial.println("5");
    tcaselect(numberGyro);
    Wire.begin();
    Wire.beginTransmission(MPU_addr);
    Serial.println("4");
    Wire.write(0x6B);  // PWR_MGMT_1 register
    Serial.println("Hallo1");
    Wire.write(0);     // set to zero (wakes up the MPU-6050)
    Serial.println("Hallo2");
    
    Wire.endTransmission(true);
    Serial.println("Hallo3");
    
    Serial.println("3");
  }

  Serial.println("1");
  start_pixels();
  numberGyro();
  Serial.println("2");
  client.println(startcond);
  client.flush();
  //Initialize a timer which stores the millis to reset each sensor at a specific time
  for (int i = 0; i < 4; i++) {
    change_millis[i] = millis();
  }
  Serial.println("3");
}

byte counter_runs = 0;
void loop() {
  distDataShow();
  if(startDataTransmit && counter_runs >= 3) {
    dataTransmit();
  } else {
    counter_runs++;
  }
}

# Java
Some Java scripts what might come in handy

### GenerateGPSCoordinates.java
This file contains a method that generates new GPS coordinates based on an initial Position, the distance between the old and new position and the bearing (heading) in degrees. 

If this method is used for other calculations than for satellites, the `heightSatellite` variable will be set to 0, and the `distanceOrbit` variable describes the real distance on the ground. Otherwise the `distanceOrbit` describes the distance the satellite makes between each method call and the `heightSatellite` variable contains the satellite's height in meters in the sky **measured from the earth surface**.

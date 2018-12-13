# Java
Some Java scripts what might come in handy

### GenerateGPSCoordinates.java
This file contains a method that generates new GPS coordinates based on an initial Position, the distance between the old and new position and the bearing (heading) in degrees. 

If this method is used for other calculations than for satellites, the `heightSatellite` variable will be set to 0, and the `distanceOrbit` variable describes the real distance on the ground. Otherwise the `distanceOrbit` describes the distance the satellite makes between each method call and the `heightSatellite` variable contains the satellite's height in meters in the sky **measured from the earth surface**.

### KerasImageClassifierInJava.java
This file is an example of how to read in a keras model in Java using <a href="https://deeplearning4j.org/" target="blanc_">deeplearning4j</a>. To change the shape of the numpy arrays use the <a href="https://deeplearning4j.org/docs/latest/nd4j-overview" target="blanc_">Nd4j package</a> like reshaping etc.

A model gets read in including weights (see the link of deeplearning4j for other possibilities), then the image will be read and transformed to fit the input layer of the model. Then the classifier returns the result.

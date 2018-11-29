/**
 * simulate a moving satellite by generating every clock tick a new gps position based on 
 * the old position (current) and the distance in the orbit as well as the bearing.
 * The generator method does NOT generate a new height.
 *
 * @param lat_1                 latitude of the satellite
 * @param lon_1                 longitude of the satellite
 * @param heightSatellite       height of the satellite in the sky in meters
 * @param distanceOrbit         distance per tick of the satellite in orbit in meters
 * @param theta_0               bearing of the orbit
 *                              0 means the satellite goes north, 
 *                              90 means the satellite goes along/parallel to the equator direction east,
 *                              180 means the satellite goes south,
 *                              270 means the satellite goes along/parallel to the equator direction west,
 *                              everything in between is of course also possible
 * @param updateSpeed           how often does the gps get updated, 2x in a second = 0.5, 1x in a second = 1, 
 *                              1x in 2 seconds = 2, etc...
 * @param includeEarthRotation  boolean flag to add earths rotation to the calculation
 *
 * @return an array with new gps location [0][1] and bearing [2]
 */
private double[] generateNextGPSLocation(double lat_1, double lon_1, double heightSatellite, double distanceOrbit,
      double theta_0, double updateSpeed, boolean includeEarthRotation) {
    final double radiusEarth = 6371010; // Earth Radius in meters

    theta_0 = Math.toRadians(theta_0);

    double phi_0 = Math.toRadians(lat_1); // translate lat to radians
    double lambda_0 = Math.toRadians(lon_1); // translate lon to radians

    double phi_1;
    double lambda_1;
    double theta_1;
    if (includeEarthRotation == true) {
        // include the earth movement and add it to the initial position; since the earth is
        // moving at 460 meters per second and the axis has an angle of 23.5 degrees
        // the total change is c = 460 / cos (23.5) = 501.60290786666682224128644758636 per second
        double delta_0 = ((updateSpeed * 460) / Math.cos(Math.toRadians(23.5))) / radiusEarth;
        /*
        * phi_1 = asin( sin phi_0 * cos delta_0 + cos phi_0 * sin delta_0 * cos theta_0 )
        * lambda_1 = lambda_0 + atan2( sin theta_0 * sin delta_0 * cos phi_0, cos delta_0 - sin phi_0 * sin phi_1 )
         */
        // calculate new lat and lon
        phi_1 = Math.asin(Math.sin(phi_0) * Math.cos(delta_0) + Math.cos(phi_0) * Math.sin(delta_0) * Math.cos(theta_0));
        lambda_1 = lambda_0 + Math.atan2(Math.sin(theta_0) * Math.sin(delta_0) * Math.cos(phi_1), 
                    Math.cos(delta_0) - Math.sin(phi_1) * Math.sin(phi_1));

        /*
        * theta_1 = atan2( sin delta_lambda_0 * cos phi_2 , cos phi_1 * sin phi_2 - sin phi_1 * cos phi_2 * cos delta_lambda_0 )
         */
        double delta_lambda_0 = lambda_0 - lambda_1;
        theta_1 = Math.atan2(Math.sin(delta_lambda_0) * Math.cos(phi_0), Math.cos(phi_1) * Math.sin(phi_0) 
                  - Math.sin(phi_1) * Math.cos(phi_0) * Math.cos(delta_lambda_0));
        theta_1 += Math.PI; // add PI to the result to look in the opposite direction
    } else {
        phi_1 = phi_0;
        lambda_1 = lambda_0;
        theta_1 = theta_0;
    }

    // calculate the distance on the earth's surface
    double circumfence_earth = 2 * radiusEarth * Math.PI;
    double circumfence_orbit = 2 * (radiusEarth + heightSatellite) * Math.PI;

    double distanceEarth = distanceOrbit / circumfence_orbit * circumfence_earth;

    /*
    * phi_2 = asin( sin phi_1 * cos delta_1 + cos phi_1 * sin delta_1 * cos theta_1 )
    * lambda_2 = lambda_1 + atan2( sin theta_1 * sin delta_1 * cos phi_1, cos delta_1 - sin * phi_1 * sin phi_2 )
     */
    double delta_1 = distanceEarth / radiusEarth;

    double phi_2 = Math.asin(Math.sin(phi_1) * Math.cos(delta_1) + Math.cos(phi_1) * Math.sin(delta_1) * Math.cos(theta_1));
    double lambda_2 = lambda_1 + Math.atan2(Math.sin(theta_1) * Math.sin(delta_1) * Math.cos(phi_1), 
                      Math.cos(delta_1) - Math.sin(phi_1) * Math.sin(phi_2));

    /*
    * theta_2 = atan2( sin delta_lambda_1 * cos phi_2 , cos phi_1 * sin phi_2 - sin phi_1 * cos phi_2 * cos delta_lambda_1 )
     */
    double delta_lambda_1 = lambda_1 - lambda_2;
    double theta_2 = Math.atan2(Math.sin(delta_lambda_1) * Math.cos(phi_1), 
                    Math.cos(phi_2) * Math.sin(phi_1) - Math.sin(phi_2) * Math.cos(phi_1) * Math.cos(delta_lambda_1));
    theta_2 = Math.toDegrees(theta_2);
    theta_2 = (theta_2 + 180) % 360;

    // translate everything back to degrees
    phi_2 = Math.toDegrees(phi_2);
    phi_2 = (phi_2 + 270) % 180 - 90; //normalise
    lambda_2 = Math.toDegrees(lambda_2);
    lambda_2 = (lambda_2 + 540) % 360 - 180; //normalise

    return new double[]{phi_2, lambda_2, theta_2};
}

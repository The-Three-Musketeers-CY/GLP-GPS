package main;

import config.GPSConfig;
import gui.MainGUI;

/**
 * This class is the main & test class of this program
 */
public class MainGPS {

    public static void main(String[] args) {
        new MainGUI(GPSConfig.GPS_FRAME_NAME, GPSConfig.MAP_PATH);
    }

}

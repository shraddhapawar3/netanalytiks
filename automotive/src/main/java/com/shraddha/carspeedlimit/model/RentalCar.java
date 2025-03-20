package com.shraddha.carspeedlimit.model;

import android.car.Car;
import android.content.Context;

/**
 * This class represents a rental car.
 */
public class RentalCar {

    private static Car rentalCar;

    /**
     * Private constructor to allow only single instance of this class.
     */
    private RentalCar() {

    }

    /**
     * This method is used to create and return a single instance of this class.
     * @param context - Context value to create a Car instance.
     * @return - Returns an instance of this class.
     */
    public static synchronized Car getInstance(Context context) {
        if (rentalCar == null) {
            rentalCar = Car.createCar(context);
        }
        return rentalCar;
    }

    /**
     * This method is used to disconnect car instance.
     */
    public static void disconnect() {
        if (rentalCar != null) {
            rentalCar.disconnect();
        }
    }
}

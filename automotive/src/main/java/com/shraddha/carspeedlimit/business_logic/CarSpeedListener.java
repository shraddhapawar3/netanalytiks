package com.shraddha.carspeedlimit.business_logic;

import android.car.Car;
import android.car.VehiclePropertyIds;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.property.CarPropertyManager;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;


/**
 * This class is used to register a callback for car speed related property.
 */
public class CarSpeedListener {

    private static final String TAG = CarSpeedListener.class.toString();

    private CarPropertyManager carPropertyManager;

    private MutableLiveData<Float> carSpeed = new MutableLiveData<>();

    /**
     * CarSpeedListener constructor to initialize carPropertyManager and register for car speed
     * property.
     * @param rentalCar - A rental car instance of which speed needs to be observed.
     */
    public CarSpeedListener(Car rentalCar) {
        carPropertyManager = (CarPropertyManager) rentalCar.getCarManager(
                Car.PROPERTY_SERVICE);
        boolean isCarSpeedListenerRegistered = carPropertyManager.subscribePropertyEvents(
                VehiclePropertyIds.PERF_VEHICLE_SPEED, CarPropertyManager.SENSOR_RATE_NORMAL,
                carPropertyEventCallback);
        Log.d(TAG, "Is car speed listener registered: " + isCarSpeedListenerRegistered);
    }

    CarPropertyManager.CarPropertyEventCallback carPropertyEventCallback = new
            CarPropertyManager.CarPropertyEventCallback() {
            @Override
            public void onChangeEvent(CarPropertyValue carPropertyValue) {
                if (carPropertyValue != null) {
                    Log.d(TAG, "CarPropertyValue: " + carPropertyValue.toString());
                    if (carPropertyValue.getPropertyId() == VehiclePropertyIds.PERF_VEHICLE_SPEED &&
                    carPropertyValue.getPropertyStatus() == CarPropertyValue.STATUS_AVAILABLE) {
                        float carSpeedValue = (Float) carPropertyValue.getValue();
                        float absCarSpeed = Math.abs(carSpeedValue);
                        carSpeed.postValue(absCarSpeed);
                    }
                }
            }

            @Override
            public void onErrorEvent(int propertyId, int areaId) {
                Log.d(TAG, "Error occurred for propertyId:" + propertyId + " with areaId: " +
                        areaId);
            }
    };

    /**
     * Returns car speed.
     * @return MutableLiveData<Float> - Returns car speed.
     */
    public MutableLiveData<Float> getCarSpeed() {
        return carSpeed;
    }
}

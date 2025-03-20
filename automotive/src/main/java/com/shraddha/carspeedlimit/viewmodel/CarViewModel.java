package com.shraddha.carspeedlimit.viewmodel;

import android.car.Car;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.shraddha.carspeedlimit.business_logic.CarSpeedListener;
import com.shraddha.carspeedlimit.database.FirebaseDB;
import com.shraddha.carspeedlimit.model.RentalCar;

/**
 * A viewmodel class to communicate with view and model.
 */
public class CarViewModel extends ViewModel {

    private static final String TAG = CarViewModel.class.toString();

    private Context context = null;
    private Car rentalCar = null;
    private float carSpeed;
    private float carSpeedLimit;

    public CarViewModel() {

    }
    /**
     * This method is used to initialize an instance of this class and perform some prerequisites
     * like initializing FirebaseDB and observing a car speed limit value changes in Firebase DB.
     * @param context - Context parameter to be utilized by certain APIs.
     */
    public void setup(Context context) {
        this.context = context;
        initializeFirebaseDB();
        FirebaseDB.getSpeedLimit().observe((LifecycleOwner) context, carSpeedLimitObserver);
    }

    /**
     * This method is used to create an instance of rental car.
     */
    public void createCar() {
        rentalCar = RentalCar.getInstance(context);
    }

    /**
     * This method is used to disconnect a rental car.
     */
    public void disconnectCar() {
        RentalCar.disconnect();
    }

    /**
     * This method is used to observe car speed.
     */
    public void observeCarSpeed() {
        CarSpeedListener carSpeedListener = new CarSpeedListener(rentalCar);
        carSpeedListener.getCarSpeed().observe((LifecycleOwner) context, carSpeedObserver);
    }

    private Observer<Float> carSpeedObserver = new Observer<Float>() {
        @Override
        public void onChanged(Float newCarSpeed) {
            carSpeed = newCarSpeed;
            if (!isSpeedWithinLimit()) {
                notifySpeedLimitWarning();
            }
        }
    };

    /**
     * This method is used to create Firebase DB instance and perform some prerequisites.
     */
    public void initializeFirebaseDB() {
        FirebaseDB.getInstance(context).initialize();
    }

    private Observer<Float> carSpeedLimitObserver = new Observer<Float>() {
        @Override
        public void onChanged(Float newCarSpeedLimit) {
            carSpeedLimit = newCarSpeedLimit;
            if (!isSpeedWithinLimit()) {
                notifySpeedLimitWarning();
            }
        }
    };

    /**
     * This method is used to check whether car speed is within specified speed limit or not.
     * @return Boolean - return true is car speed is within the specified speed limit otherwise
     * returns false.
     */
    private boolean isSpeedWithinLimit() {
        return carSpeed <= carSpeedLimit;
    }

    /**
     * This method is used to notify user with speed limit crossed warning and inform a car rental
     * company regarding the same.
     */
    private void notifySpeedLimitWarning() {
        //Update UI with warning by creating a dialog or a Toast.
        Log.w(TAG, "Speed limit crossed. Allowed speed limit is " + carSpeedLimit);
        FirebaseDB.notifySpeedLimitCrossed();
    }
}

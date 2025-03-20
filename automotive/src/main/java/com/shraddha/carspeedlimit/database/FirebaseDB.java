package com.shraddha.carspeedlimit.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * This class is used to communicate with Firebase DB.
 */
public class FirebaseDB implements Database, ValueEventListener {

    private static final String TAG = FirebaseDB.class.toString();

    private Context context = null;

    private static FirebaseDB firebaseDB = null;

    private FirebaseDatabase database = null;
    private DatabaseReference databaseReference = null;
    private static MutableLiveData<Float> speedLimit = new MutableLiveData<>();

    /**
     * Private constructor to allow only single instance of this class.
     */
    private FirebaseDB(Context context) {
        this.context = context;
    }

    /**
     * This method is used to create and return a single instance of this class.
     * @return FirebaseDB - Returns an instance of this class.
     */
    public static synchronized FirebaseDB getInstance(Context context) {
        if (firebaseDB == null) {
            firebaseDB = new FirebaseDB(context);
        }
        return firebaseDB;
    }

    @Override
    public void initialize() {
        FirebaseApp.initializeApp(context);
        database = FirebaseDatabase.getInstance();
        //Assuming this rental car is Car1. In actual application, driver's authentication will
        // happen and the speed limit value of the car assigned to that driver will be fetched from
        // the backend.
        databaseReference = database.getReference("Car1");
        databaseReference.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        float speedLimitValue = dataSnapshot.getValue(Float.class);
        speedLimit.postValue(speedLimitValue);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {
        Log.e(TAG, "Inside onCancelled: " + error.toException());
    }

    /**
     * This method is used to return the speed limit set by the rental company for this car.
     * @return MutableLiveData<Float> - Return speed limit for this car.
     */
    public static MutableLiveData<Float> getSpeedLimit() {
        return speedLimit;
    }

    /**
     * This method is used to notify rental company that driver of this car has crossed the
     * specified speed limit.
     */
    public static void notifySpeedLimitCrossed() {
        //Call Firebase API to send a warning message to server to notify a rental company
    }
}

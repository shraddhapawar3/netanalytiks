package com.shraddha.carspeedlimit.ui;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.shraddha.carspeedlimit.R;
import com.shraddha.carspeedlimit.business_logic.CarSpeedListener;
import com.shraddha.carspeedlimit.model.RentalCar;
import com.shraddha.carspeedlimit.viewmodel.CarViewModel;

/**
 * MainActivity to authenticate rental car user.
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.toString();

    private String[] permissions = {"android.car.permission.CAR_SPEED"};
    private static final int PERMISSION_REQUEST_CODE = 200;

    private CarViewModel carViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        carViewModel.setup(this.getApplicationContext());
        carViewModel.createCar();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                Log.d(TAG, "In onRequestPermissionsResult with code:" + PERMISSION_REQUEST_CODE);
                if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    carViewModel.observeCarSpeed();
                } else {
                    requestPermissions();
                }
            }
            break;
            default: {
                Log.d(TAG, "In onRequestPermissionsResult default case with requestCode:" +
                        requestCode);
            }
        }
    }

    /**
     * This method is used to request permission for reading car speed data.
     */
    private void requestPermissions() {
        requestPermissions(permissions, PERMISSION_REQUEST_CODE);
    }

    @Override
    protected void onStop() {
        carViewModel.disconnectCar();
        super.onStop();
    }
}
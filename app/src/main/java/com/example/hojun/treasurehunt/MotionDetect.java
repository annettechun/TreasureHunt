package com.example.hojun.treasurehunt;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

import retrofit.client.Response;

import android.database.sqlite.SQLiteDatabase;

public class MotionDetect extends AppCompatActivity {
    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;

    private View motionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        motionView = findViewById(R.id.motionScroll);
        // ShakeDetector initialization
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
				/*
				 * The following method, "handleShakeEvent(count):" is a stub //
				 * method you would use to setup whatever you want done once the
				 * device has been shook.
				 */
                String username = getIntent().getStringExtra("username");
                FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getApplicationContext());
                SQLiteDatabase db = mDbHelper.getReadableDatabase();

                // Define a projection that specifies which columns from the database
                // you will actually use after this query.

                String[] projection = {
                        FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE
                };

                // Filter results WHERE "title" = 'My Title'
                String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " = ?";
                String[] selectionArgs = {username};

                // How you want the results sorted in the resulting Cursor
                String sortOrder =
                        FeedReaderContract.FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";

                Cursor c = db.query(
                        FeedReaderContract.FeedEntry.TABLE_NAME,                     // The table to query
                        projection,                               // The columns to return
                        selection,                                // The columns for the WHERE clause
                        selectionArgs,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        sortOrder                                 // The sort order
                );

                c.moveToFirst();
                int itemId = c.getInt(c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE));

                //Update the score by adding 1 point to it
                int newScore = itemId + 1;

                ContentValues values = new ContentValues();
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE, newScore);

                String selection2 = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " LIKE ?";
                String[] selection2Args = {username};

                int count2 = db.update(
                        FeedReaderContract.FeedEntry.TABLE_NAME,
                        values,
                        selection2,
                        selection2Args);

                Cursor c2 = db.query(
                        FeedReaderContract.FeedEntry.TABLE_NAME,                     // The table to query
                        projection,                               // The columns to return
                        selection,                                // The columns for the WHERE clause
                        selectionArgs,                            // The values for the WHERE clause
                        null,                                     // don't group the rows
                        null,                                     // don't filter by row groups
                        sortOrder                                 // The sort order
                );

                c2.moveToFirst();
                int itemId2 = c2.getInt(c2.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE));

                Log.d("print", Integer.toString(itemId2));

                updateUser(username, itemId2);

                Toast toast = Toast.makeText(getApplicationContext(), "You have earned 1 point! Your current score is " + itemId2, Toast.LENGTH_SHORT);
                toast.show();

                finish();
            }
        });

        setContentView(R.layout.activity_motion_detect);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            motionView.setVisibility(show ? View.GONE : View.VISIBLE);
            motionView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    motionView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            motionView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer, SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    public void updateUser(String username, int score_value) {
        String name = username;
        int score = score_value;
        String url = "http://plato.cs.virginia.edu/~amc4sq/";
        RestAdapter adapter = new RestAdapter.Builder().setEndpoint(url).build();
        Interface api = adapter.create(Interface.class);
        api.insertUser(name, score, new Callback<Response>() {
            @Override
            public void success(Response result, Response response) {
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(result.getBody().in()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(MotionDetect.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
package com.example.saksham.chaljaa;

import android.Manifest;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {

    int REQUEST_PERMISSIONS;
    Button btnn;
    EditText et1;
    Button settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnn=(Button)findViewById(R.id.btn);
        et1=(EditText)findViewById(R.id.et);
        settings=(Button)findViewById(R.id.set);


        //final BroadCastReceiver cst=new BroadCastReceiver(MainActivity.this);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) + ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CONTACTS) + ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_CALL_LOG)+ ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CALL_LOG) || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_CONTACTS) || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_CALL_LOG)|| ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Please Grant Permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission
                                                .READ_CALL_LOG, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CALL_LOG,Manifest.permission.READ_PHONE_STATE},
                                        REQUEST_PERMISSIONS);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission
                                .READ_CALL_LOG, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CALL_LOG,Manifest.permission.READ_PHONE_STATE},
                        REQUEST_PERMISSIONS);
            }

        }
        else
        {
            clear();
            btnn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String i=et1.getText().toString();
                    if(!i.isEmpty())
                    {
                        int val=Integer.parseInt(i);
                        //CustomPhoneStateListener.alpha=val;
                        //BroadCastReceiver.beta=val;
                        setvalues(val);
                        Toast.makeText(getApplicationContext(), "this is initially "+val, Toast.LENGTH_SHORT).show();

                        //Toast.makeText(getApplicationContext(),,Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Empty input",Toast.LENGTH_SHORT).show();
                    }


                }
            });
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();

                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);

                }
            });

        }

    }
    public void setvalues(int value)
    {
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(MainActivity.this);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, value);

        long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);

    }
    public void clear()
    {
        ContentValues values = new ContentValues();
        //values.put(CallLog.Calls.NEW, 0);
        values.put(CallLog.Calls.IS_READ, 1);
        StringBuilder where = new StringBuilder();

        where.append(CallLog.Calls.TYPE);
        where.append(" = ?");
        getContentResolver().update(CallLog.Calls.CONTENT_URI, values, where.toString(),
                new String[]{ Integer.toString(CallLog.Calls.MISSED_TYPE) });
    }
}


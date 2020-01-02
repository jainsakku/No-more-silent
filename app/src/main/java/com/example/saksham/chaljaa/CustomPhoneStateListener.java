package com.example.saksham.chaljaa;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.os.PowerManager;
import android.provider.BaseColumns;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.ContentValues.TAG;
public class CustomPhoneStateListener extends PhoneStateListener {
   static int alpha;

    AudioManager aManager;
    Context context;
    //int alpha;
    public CustomPhoneStateListener(Context context) {
        super();
        this.context = context;


    }

    public int getvalue()
    {
        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,

        };
        String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE + " > "+0;
        String[] selectionArgs = { };
        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null              // The sort order
        );
        if(cursor.getCount()>0)
        {
            cursor.moveToLast();
            int ab=cursor.getInt(cursor.getColumnIndex(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE));

            //Toast.makeText(getApplicationContext(),"Found "+ab,Toast.LENGTH_SHORT).show();
            return ab;
        }
        else
        {
            return 100;
        }




    }



    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "call_lock");

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                //when Idle i.e no call
                Toast.makeText(context, "Phone state Idle", Toast.LENGTH_LONG).show();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //when Off hook i.e in call
                //Make intent and start your service here
                Toast.makeText(context, "Phone state Off hook", Toast.LENGTH_LONG).show();
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                //when Ringing
                //String abc = String.valueOf(alpha);
                alpha=getvalue();
                //Toast.makeText(context, "this is "+alpha, Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "Phone state Ringing", Toast.LENGTH_LONG).show();
                final ContentResolver resolver = context.getContentResolver();
                final String[] selection = {CallLog.Calls.CACHED_NAME, CallLog.Calls.CACHED_NUMBER_LABEL, CallLog.Calls.TYPE, CallLog.Calls.IS_READ};
                String where = CallLog.Calls.TYPE + "=" + CallLog.Calls.MISSED_TYPE + " AND " + CallLog.Calls.IS_READ + "!=" + 1;
                Cursor c = resolver.query(CallLog.Calls.CONTENT_URI, selection, where, null, null);
                c.moveToFirst();
                Log.d("CALL", "" + c.getCount());
               // String abc = String.valueOf(c.getCount());


                if (c.getCount() > alpha) {
                    // Toast.makeText(context.getApplicationContext(), "missed call", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Missed Call");


                    aManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                    int currentMode = aManager.getRingerMode();
                    if (currentMode == aManager.RINGER_MODE_VIBRATE) {
                        aManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        //Toast.makeText(context.getApplicationContext(), "Ringing Mode Activated", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Ring Mode");

                    }
                    if(currentMode==aManager.RINGER_MODE_SILENT)
                    {
                        aManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                        //Toast.makeText(context.getApplicationContext(), "Ringing Mode Activated", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Ring Mode");

                    }



                }
                wakeLock.release();
                break;
            default:
                break;
        }
    }

}

package com.example.saksham.chaljaa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class BroadCastReceiver extends BroadcastReceiver {
   //public static int beta;

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context,"this is now "+beta,Toast.LENGTH_SHORT).show();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new CustomPhoneStateListener(context), PhoneStateListener.LISTEN_CALL_STATE);
    }
}


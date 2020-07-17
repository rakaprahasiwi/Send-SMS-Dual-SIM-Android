package id.prahasiwi.sms;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    private Button btn_lewatApp, btn_redirect;
    private Dialog dialogSendRedirect;
    private ImageView iv_close;
    private EditText edt_phone, edt_message;
    private CardView cv_input, cv_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_lewatApp = findViewById(R.id.btn_lewatApp);
        btn_redirect = findViewById(R.id.btn_redirect);
        dialogSendRedirect = new Dialog(MainActivity.this);

        btn_lewatApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sendSMS("098877", "tessss");
                addFragmentOnTop(LewatAplikasiFragment.newInstance("FIRST", "LEWAT APPS"));
            }
        });

        btn_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogSendRedirect.setContentView(R.layout.pop_up_send_redirect);
                iv_close = dialogSendRedirect.findViewById(R.id.iv_close);
                edt_phone = dialogSendRedirect.findViewById(R.id.edt_phone);
                edt_message = dialogSendRedirect.findViewById(R.id.edt_message);
                cv_input = dialogSendRedirect.findViewById(R.id.cv_input);
                cv_cancel = dialogSendRedirect.findViewById(R.id.cv_cancel);

                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogSendRedirect.dismiss();
                    }
                });

                cv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogSendRedirect.dismiss();
                    }
                });

                cv_input.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (edt_phone.getText().toString().isEmpty()) {
                            Toast.makeText(MainActivity.this, "Phone number masih kosong", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i("Send SMS", "");
                            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                            smsIntent.setData(Uri.parse("smsto:"));
                            smsIntent.setType("vnd.android-dir/mms-sms");
                            smsIntent.putExtra("address", new String(edt_phone.getText().toString()));
                            if (edt_message.getText().toString().isEmpty()) {
                                smsIntent.putExtra("sms_body", "");
                            } else {
                                smsIntent.putExtra("sms_body", edt_message.getText().toString());
                            }

                            try {
                                startActivity(smsIntent);
                                finish();
                                Log.i("Finished sending SMS...", "");
                            } catch (ActivityNotFoundException ex) {
                                Toast.makeText(MainActivity.this, "SMS failed, please try again later", Toast.LENGTH_SHORT).show();
                            }
                            dialogSendRedirect.dismiss();
                        }
                    }
                });

                dialogSendRedirect.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogSendRedirect.show();
            }
        });
    }

    public void addFragmentOnTop(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();
    }

//    private void sendSMS(String phoneNumber, String message)
//    {
//        String SENT = "SMS_SENT";
//        String DELIVERED = "SMS_DELIVERED";
//
//        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
//                new Intent(SENT), 0);
//
//        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
//                new Intent(DELIVERED), 0);
//
//        //---when the SMS has been sent---
//        registerReceiver(new BroadcastReceiver(){
//            @Override
//            public void onReceive(Context arg0, Intent arg1) {
//                switch (getResultCode())
//                {
//                    case Activity.RESULT_OK:
//                        Toast.makeText(getBaseContext(), "SMS sent",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        Toast.makeText(getBaseContext(), "Generic failure",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_NO_SERVICE:
//                        Toast.makeText(getBaseContext(), "No service",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_NULL_PDU:
//                        Toast.makeText(getBaseContext(), "Null PDU",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case SmsManager.RESULT_ERROR_RADIO_OFF:
//                        Toast.makeText(getBaseContext(), "Radio off",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        }, new IntentFilter(SENT));
//
//        //---when the SMS has been delivered---
//        registerReceiver(new BroadcastReceiver(){
//            @Override
//            public void onReceive(Context arg0, Intent arg1) {
//                switch (getResultCode())
//                {
//                    case Activity.RESULT_OK:
//                        Toast.makeText(getBaseContext(), "SMS delivered",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        Toast.makeText(getBaseContext(), "SMS not delivered",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//        }, new IntentFilter(DELIVERED));
//
//        SmsManager sms = SmsManager.getDefault();
//        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
//    }

}
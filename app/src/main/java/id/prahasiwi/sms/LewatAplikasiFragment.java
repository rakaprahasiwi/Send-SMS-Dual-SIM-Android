package id.prahasiwi.sms;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LewatAplikasiFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LewatAplikasiFragment extends Fragment {

    private RelativeLayout root;
    private CircleImageView iv_backButton;
    private EditText edt_phone, edt_message;
    private Button btn_send;
    private Context context;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LewatAplikasiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LewatAplikasiFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LewatAplikasiFragment newInstance(String param1, String param2) {
        LewatAplikasiFragment fragment = new LewatAplikasiFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_lewat_aplikasi, container, false);
        context = getContext();
        initComponent(v);
        return v;
    }

    private void initComponent(View v) {
        root = v.findViewById(R.id.root);
        iv_backButton = v.findViewById(R.id.iv_backButton);
        edt_phone = v.findViewById(R.id.edt_phone);
        edt_message = v.findViewById(R.id.edt_message);
        btn_send = v.findViewById(R.id.btn_send);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSMS();
            }
        });

        iv_backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void sendSMS() {
        if (edt_phone.getText().toString().isEmpty() || edt_message.getText().toString().isEmpty()) {
            Toast.makeText(context, "Data masih kosong", Toast.LENGTH_SHORT).show();
        } else {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.SEND_SMS)) {

                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
                }
            } else {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
                        != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_PHONE_STATE)) {

                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                        SubscriptionManager localSubscriptionManager = SubscriptionManager.from(context);
                        if (localSubscriptionManager.getActiveSubscriptionInfoCount() > 1) {
                            List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
                            SubscriptionInfo simInfo1 = (SubscriptionInfo) localList.get(0);
                            SubscriptionInfo simInfo2 = (SubscriptionInfo) localList.get(1);
                            selectOption(simInfo1, simInfo2);
                        } else {
                            SmsManager.getDefault().sendTextMessage(edt_phone.getText().toString(), null, edt_message.getText().toString(), null, null);
                        }
                    } else {
                        SmsManager.getDefault().sendTextMessage(edt_phone.getText().toString(), null, edt_message.getText().toString(), null, null);
                    }
                }
            }
        }
    }

    private void selectOption(final SubscriptionInfo simInfo1, final SubscriptionInfo simInfo2) {
        final CharSequence[] items = {"SIM 1", "SIM 2"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Pilih SIM");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("SIM 1")) {
                    //SendSMS From SIM One
                    SmsManager.getSmsManagerForSubscriptionId(simInfo1.getSubscriptionId()).sendTextMessage(edt_phone.getText().toString(), null, edt_message.getText().toString(), null, null);
                } else {
                    //SendSMS From SIM Two
                    SmsManager.getSmsManagerForSubscriptionId(simInfo2.getSubscriptionId()).sendTextMessage(edt_phone.getText().toString(), null, edt_message.getText().toString(), null, null);
                }
            }
        });
        builder.show();
    }
}
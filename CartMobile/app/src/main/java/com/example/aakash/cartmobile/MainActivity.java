/**
 *   CartMobile application - An application that handles the cart system for shopping
 *
 *   Consists of Authentication through OTP, followed by the shopping process -
 *   1. Scan and add/remove the items
 *   2. Checkout
 *  3. Update the Balance
 *
 **/


package com.example.aakash.cartmobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends Activity {

    private Button mButton;

    public static Float Amount_wallet,Amount_Ref;
    public static int item_count;
    static EditText otp;
    static String otpstr,name,phone;

    TextView t;
    DatabaseReference sec;
    static int age;
    Float amt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        otp = findViewById(R.id.OTP);
        t = findViewById(R.id.test);

        Amount_wallet = Float.valueOf((0));

        Firebase.setAndroidContext(this);

        mButton = findViewById(R.id.cont);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otpstr = otp.getText().toString();
                Log.e("otpcheck", otpstr);
                mButton.setClickable(false);
                switchActivity(v);

            }
        });
    }

    public void switchActivity(View v) {

        if(otpstr.isEmpty())
        {
            Toast.makeText(this,"Please Enter OTP",Toast.LENGTH_LONG).show();
            mButton.setClickable(true);
        }
        else {


                sec = FirebaseDatabase.getInstance("https://test-kit-1-otpgengkit.firebaseio.com/").getReference(otpstr);

                sec.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {

                        try {

                            amt = dataSnapshot.child("Wallet:").getValue(Float.class);
                            name = dataSnapshot.child("Name:").getValue(String.class);
                            age = dataSnapshot.child("Age:").getValue(Integer.class);
                            phone = dataSnapshot.child("Phone:").getValue(String.class);

                            Amount_wallet += amt;

                            Amount_Ref = Amount_wallet;

                            finish();

                            Intent intent = new Intent(MainActivity.this,Confirm.class);
                            Bundle b = new Bundle();
                            b.putString("Name",name);
                            b.putString("OTP",otpstr);
                            intent.putExtras(b);
                            mButton.setClickable(false);
                            startActivity(intent);

                        } catch (Exception e) {

                            Toast.makeText(MainActivity.this,"Wrong OTP",Toast.LENGTH_LONG).show();
                            mButton.setClickable(true);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        }
    }

    @Override
    public void onBackPressed() {

        // Simply Do noting!
    }
}
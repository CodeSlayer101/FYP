/**
 *
 *  Checkout page where total bill amount is shown along with the remaining balance
 *
 */
package com.example.aakash.cartmobile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class Checkout extends AppCompatActivity {

    int purchase;
    TextView bill,wallet,name,thank;
    Button submit;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        name = findViewById(R.id.name);
        bill = findViewById(R.id.bill);
        wallet = findViewById(R.id.wallet);
        submit = findViewById(R.id.submit);
        thank = findViewById(R.id.Thank);
        name.setText(MainActivity.name);
        bill.setText(Float.toString(MainActivity.Amount_Ref - MainActivity.Amount_wallet));
        wallet.setText(Float.toString((MainActivity.Amount_wallet)));


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end(v);
            }
        });
    }
    public void end(View v)
    {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
        String formattedDate = df.format(c);

        String urlhist = "https://test-kit-1-history.firebaseio.com/";
        mRef = FirebaseDatabase.getInstance(urlhist).getReference();

        mRef.child(MainActivity.phone).child(formattedDate).setValue(Float.valueOf(MainActivity.Amount_Ref - MainActivity.Amount_wallet));

        String urluser = "https://test-kit-1-users.firebaseio.com/";
        mRef = FirebaseDatabase.getInstance(urluser).getReference();
        mRef.child(MainActivity.phone).child("Wallet Balance").setValue(MainActivity.Amount_wallet);

        String urlotp = "https://test-kit-1-otpgengkit.firebaseio.com/";
        mRef = FirebaseDatabase.getInstance(urlotp).getReference();
        mRef.child(MainActivity.otpstr).removeValue();

        String urllist = "https://test-kit-1-shoplist.firebaseio.com/";
        mRef = FirebaseDatabase.getInstance(urllist).getReference();
        mRef.child(MainActivity.otpstr).removeValue();

        thank.setText("Thank You for Shopping with us\nCome back soon\nClosing in 5 seconds\nYou can leave the cart at the counter");

        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    public void onBackPressed() {

        // Simply Do noting!
    }
}
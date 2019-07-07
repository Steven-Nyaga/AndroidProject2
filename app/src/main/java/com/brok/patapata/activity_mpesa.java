package com.brok.patapata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidstudy.daraja.Daraja;
import com.androidstudy.daraja.DarajaListener;
import com.androidstudy.daraja.model.AccessToken;
import com.androidstudy.daraja.model.LNMExpress;
import com.androidstudy.daraja.model.LNMResult;
import com.androidstudy.daraja.util.TransactionType;

public class activity_mpesa extends AppCompatActivity {
    EditText editTextPhone;
    Button btnLipa;

    //declare daraja as a global variable
    Daraja daraja;
    String phonenumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa);

        editTextPhone =findViewById(R.id.mtext);
        btnLipa =findViewById(R.id.mpay);
        btnLipa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get phone number
                phonenumber = editTextPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phonenumber)) {
                    Toast.makeText(activity_mpesa.this, "please insert phone number", Toast.LENGTH_LONG).show();
                    return;
                }
                LipaNaMpesa(phonenumber);
            }
        });
        //Init daraja
        daraja = Daraja.with("cSuIFLeHAnsOFTAVVlQ3Spd5n6J3sEGr", "Q5XTbAsBLAv29SQv", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i(activity_mpesa.this.getClass().getSimpleName(),accessToken.getAccess_token());
                //  Toast.makeText(Payment.this,"TOKEN :"+ accessToken.getAccess_token(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Log.e(activity_mpesa.this.getClass().getSimpleName(),error);

            }
        });
    }

    private void LipaNaMpesa(String phonenumber) {
//party A is the number sending the money.It has to be a valid safaricom phone number
// phonenumber is the mobile number to receive the stk pin prompt.The number can be the sam as partyA.
//BusinessShort code = PartyB
        final LNMExpress lnmExpress = new LNMExpress(
                "174379",
                "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",
                TransactionType.CustomerBuyGoodsOnline,
                "50",
                "254712828157",
                "174379",
                phonenumber,
                "http://mycallbackurl.com/checkout.php",
                "001ABC",
                "Goods Paymnet"
        );
        //actual method
        daraja.requestMPESAExpress(lnmExpress,
                new DarajaListener<LNMResult>() {
                    @Override
                    public void onResult(@NonNull LNMResult lnmResult) {
                        Log.i(activity_mpesa.this.getClass().getSimpleName(), lnmResult.ResponseDescription);
                    }

                    @Override
                    public void onError(String error) {
                        Log.i(activity_mpesa.this.getClass().getSimpleName(), error);
                    }
                });

    }
}

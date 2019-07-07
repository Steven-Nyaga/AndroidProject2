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
    private EditText meditText;
    private Button mbutton;
    Daraja daraja;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa);

        meditText = (EditText) findViewById(R.id.mtext);
        mbutton = (Button) findViewById(R.id.mpay);

        daraja = Daraja.with("Uku3wUhDw9z0Otdk2hUAbGZck8ZGILyh", "JDjpQBm5HpYwk38b", new DarajaListener<AccessToken>() {
            @Override
            public void onResult(@NonNull AccessToken accessToken) {
                Log.i(activity_mpesa.this.getClass().getSimpleName(), accessToken.getAccess_token());
                Toast.makeText(activity_mpesa.this, "TOKEN : " + accessToken.getAccess_token(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String error) {
                Log.e(activity_mpesa.this.getClass().getSimpleName(), error);
            }
        });

        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = meditText.getText().toString().trim();
                if (TextUtils.isEmpty(phoneNumber)) {
                    meditText.setError("Please Provide a Phone Number");
                    return;
                }
                LNMExpress lnmExpress = new LNMExpress(
                        "174379",
                        "bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919",  //https://developer.safaricom.co.ke/test_credentials
                        TransactionType.CustomerPayBillOnline,
                        "100",
                        "254712828157",
                        "174379",
                        phoneNumber,
                        "http://mycallbackurl.com/checkout.php",
                        "001ABC",
                        "Goods Payment"
                );
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
                        }
                );
            }
        });
    }
}

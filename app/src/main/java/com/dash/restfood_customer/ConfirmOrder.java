package com.dash.restfood_customer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dash.restfood_customer.Config.Config;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.Objects;

public class ConfirmOrder extends BaseActivity implements View.OnClickListener {

    private static final String TAG ="ConfirmOrder" ;
    public static final int PAYPAL_REQUEST_CODE =7171 ;

    private static PayPalConfiguration config=new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);

    TextView tv_price;
    Button btn_checkout;
    EditText et_notes;
    RadioButton rb_selected;
    RadioGroup radioPayment;

    int total;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_confirm_order, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end

        //start paypal service
        Intent intent=new Intent(this,PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        startService(intent);

        tv_price=findViewById(R.id.tv_price);
        btn_checkout=findViewById(R.id.btn_checkout);
        et_notes=findViewById(R.id.et_notes);
        radioPayment=findViewById(R.id.radioPayment);

        total=5;
        tv_price.setText(String.valueOf(total));
        btn_checkout.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v==btn_checkout){
            int selectedId = radioPayment.getCheckedRadioButtonId();

            // find the radiobutton by returned id
            rb_selected = findViewById(selectedId);


            Log.d(TAG,"selected option is"+rb_selected.getText());
            if(Objects.equals(rb_selected.getText(),"Pay By Card")){
                Log.d(TAG,"By card");
                processPayment();
            }
        }
    }

    private void processPayment() {
        PayPalPayment payPalPayment=new PayPalPayment(new BigDecimal(String.valueOf(total)),"USD","Pay to restaurant"
                ,PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent=new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION,config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT,payPalPayment);
        startActivityForResult(intent,PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    try {
                        String paymentDetails = confirmation.toJSONObject().toString(4);
                        Log.d(TAG,"Payment success"+paymentDetails);
                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", total));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show();
        }

    }
}

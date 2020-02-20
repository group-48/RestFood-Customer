package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.dash.restfood_customer.InternetConfig.InternetConfig;
import com.dash.restfood_customer.models.CartItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ConfirmOrder extends BaseActivity implements View.OnClickListener {

    private static final String TAG ="ConfirmOrder" ;
    public static final int PAYPAL_REQUEST_CODE =7171 ;

    private static PayPalConfiguration config=new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(Config.PAYPAL_CLIENT_ID);


    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference ref=db.collection("users").document(user.getUid()).collection("cart");

    TextView tv_price;
    Button btn_checkout;
    EditText et_notes;
    RadioButton rb_selected;
    RadioGroup radioPayment;
    private ProgressDialog progressDialog;

    int[] total = new int[1];
    List<String> food_list = new ArrayList<String>();
    List<String> qty=new ArrayList<String>();
    List<String> food_name = new ArrayList<String>();
    public String[] shopId = new String[1];
    int c=0;
    CartItem[] cartItem=new CartItem[10];
    String notes,paymentId;
    int amount;

    public int flag=0;



    @Override
    protected void onDestroy() {
        stopService(new Intent(this,PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Placing order");

        Log.d(TAG,"Total is "+getIntent().getStringExtra("Total"));
        amount=Integer.valueOf(getIntent().getStringExtra("Total"));
        //amount=5;
        tv_price.setText(String.valueOf(amount));
        btn_checkout.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        //check if internet connection exists
        if(InternetConfig.isConnectedToInternet(getBaseContext())){
            if(v==btn_checkout){
                notes=et_notes.getText().toString();
                int selectedId = radioPayment.getCheckedRadioButtonId();
                Log.d(TAG,"Selected Id"+selectedId);

                if(selectedId==-1){
                    Toast.makeText(this, "Please Select a payment option", Toast.LENGTH_SHORT).show();
                }else{
                    // find the radiobutton by returned id
                    rb_selected = findViewById(selectedId);


                    Log.d(TAG,"selected option is"+rb_selected.getText());
                    if(Objects.equals(rb_selected.getText(),"Pay By Card")){
                        Log.d(TAG,"By card");
                        processPayment();


                    }
                    else{
                        progressDialog.show();
                        placeOrderCash();
                    }
                }

            }
        }
        else{
            Toast.makeText(this,"Please Check your Internet Connection",Toast.LENGTH_SHORT).show();
        }

    }

    private void placeOrderCash() {
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref",0);
        SharedPreferences.Editor editor = sharedPref.edit();


        db.collection("users").document(user.getUid()).collection("cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("CartActvity", document.getId() + " => " + document.getData());
                                Log.d("CartActvity", document.getId() + " => " + document.get("foodId"));
                                food_list.add(document.get("foodId").toString());
                                qty.add(document.get("qty").toString());
                                total[0] =total[0]+(Integer.parseInt(document.get("price").toString())*Integer.parseInt(document.get("qty").toString()));
                                shopId[0] =document.get("shopId").toString();
                                food_name.add(document.get("name").toString());
                                Log.d("CartActvity", "doc reference" + document.getDocumentReference(document.getId()));
                                cartItem[c]=document.toObject(CartItem.class);
                                c++;
                                Log.d("CartActvity", "c is"+c);
                                db.collection("users").document(user.getUid()).collection("cart").document(document.getId()).delete();
                            }
                            String[] foods = new String[ food_list.size() ];
                            food_list.toArray( foods);

                            String[] quantity = new String[ qty.size() ];
                            qty.toArray( quantity);

                            String[] fName = new String[ food_name.size() ];
                            food_name.toArray(fName);

                            Log.d("CartActvity", shopId[0]);
                            final Map<String,Object> order=new HashMap<>();
                            order.put("Total",total[0]);
                            order.put("Food_List", Arrays.asList(foods));
                            order.put("Qty_List", Arrays.asList(quantity));
                            order.put("Food_Names", Arrays.asList(fName));
                            order.put("Timestamp", FieldValue.serverTimestamp());
                            order.put("User",user.getUid());
                            order.put("Shop",shopId[0]);
                            order.put("Status","Pending");
                            order.put("PaymentMode","Cash");
                            order.put("PaymentStatus","Pending");
                            order.put("Notes",notes);
                            order.put("Done",false);
                            db.collection("orders").add(order)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            final String docId=documentReference.getId();
                                            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref",0);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.remove("OrderId");
                                            editor.commit();
                                            editor.putString("OrderId", docId);
                                            editor.commit();
                                            Log.d("Track", "Error getting documents: ");

                                            for(int i=0;i<c;i++){
                                                Log.d("CartActvity", "food id is "+cartItem[i].getFoodId());
                                                db.collection("orders").document(docId).collection("foods").document(cartItem[i].getFoodId()).set(cartItem[i]);
                                                Map<String, Object> order = new HashMap<>();
                                                order.put("orderId",docId);
                                                db.collection("orders").document(docId).collection("foods").document(cartItem[i].getFoodId()).set(order, SetOptions.merge());
                                            }
                                            db.collection("orders").document(docId).update("OrderId",docId);


                                            progressDialog.dismiss();
                                            Intent intent=new Intent(ConfirmOrder.this,TrackOrder.class);
                                            intent.putExtra("OrderId",docId);
                                            startActivity(intent);
                                            finish();

                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Track", "Error getting documents: ", e);
                                }
                            });
                            //db.collection("users").document(user.getUid()).collection("Orders").document(docId).set(order);

                        } else {
                            Log.d("Track", "Error getting documents: ", task.getException());
                        }
                    }
                });


        String orderId=sharedPref.getString("OrderId",null);
        Log.w("Track","Order id is"+orderId);

    }



    private void placeOrderPayPal() {
        progressDialog.show();
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref",0);
        SharedPreferences.Editor editor = sharedPref.edit();


        db.collection("users").document(user.getUid()).collection("cart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("CartActvity", document.getId() + " => " + document.getData());
                                Log.d("CartActvity", document.getId() + " => " + document.get("foodId"));
                                food_list.add(document.get("foodId").toString());
                                qty.add(document.get("qty").toString());
                                total[0] =total[0]+(Integer.parseInt(document.get("price").toString())*Integer.parseInt(document.get("qty").toString()));
                                shopId[0] =document.get("shopId").toString();
                                food_name.add(document.get("name").toString());
                                Log.d("CartActvity", "doc reference" + document.getDocumentReference(document.getId()));
                                cartItem[c]=document.toObject(CartItem.class);
                                c++;
                                Log.d("CartActvity", "c is"+c);
                                db.collection("users").document(user.getUid()).collection("cart").document(document.getId()).delete();

                            }
                            String[] foods = new String[ food_list.size() ];
                            food_list.toArray( foods);

                            String[] quantity = new String[ qty.size() ];
                            qty.toArray( quantity);

                            String[] fName = new String[ food_name.size() ];
                            food_name.toArray(fName);

                            Log.d("CartActvity", shopId[0]);
                            final Map<String,Object> order=new HashMap<>();
                            order.put("Total",total[0]);
                            order.put("Food_List", Arrays.asList(foods));
                            order.put("Qty_List", Arrays.asList(quantity));
                            order.put("Food_Names", Arrays.asList(fName));
                            order.put("Timestamp", FieldValue.serverTimestamp());
                            order.put("User",user.getUid());
                            order.put("Shop",shopId[0]);
                            order.put("Status","Pending");
                            order.put("PaymentMode","PayPal");
                            order.put("PaymentStatus","Done");
                            order.put("PaymentId",paymentId);
                            order.put("Notes",notes);
                            order.put("Done",false);
                            db.collection("orders").add(order)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {
                                            final String docId=documentReference.getId();
                                            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref",0);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.remove("OrderId");
                                            editor.commit();
                                            editor.putString("OrderId", docId);
                                            editor.commit();
                                            Log.d("Track", "Error getting documents: ");

                                            for(int i=0;i<c;i++){
                                                Log.d("CartActvity", "food id is "+cartItem[i].getFoodId());

                                                db.collection("orders").document(docId).collection("foods").document(cartItem[i].getFoodId()).set(cartItem[i]);
                                                Map<String, Object> order = new HashMap<>();
                                                order.put("orderId",docId);
                                                db.collection("orders").document(docId).collection("foods").document(cartItem[i].getFoodId()).set(order, SetOptions.merge());
                                            }

                                            db.collection("orders").document(docId).update("OrderId",docId);
                                            progressDialog.dismiss();
                                            Intent intent=new Intent(ConfirmOrder.this,TrackOrder.class);
                                            intent.putExtra("OrderId",docId);
                                            startActivity(intent);
                                            finish();
                                        }

                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Track", "Error getting documents: ", e);
                                }
                            });
                            //db.collection("users").document(user.getUid()).collection("Orders").document(docId).set(order);

                        } else {
                            Log.d("Track", "Error getting documents: ", task.getException());
                        }
                    }
                });


        String orderId=sharedPref.getString("OrderId",null);
        Log.w("Track","Order id is"+orderId);

    }




    private void processPayment() {
        PayPalPayment payPalPayment=new PayPalPayment(new BigDecimal(String.valueOf(amount)),"USD","Pay to restaurant"
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
                        JSONObject jsonObject=new JSONObject(paymentDetails);
                        JSONObject response=jsonObject.getJSONObject("response");

                        Log.d(TAG,"Payment success"+paymentDetails);
                        Log.d(TAG,"Payment ID"+response.getString("id"));
                        paymentId=response.getString("id");
                        placeOrderPayPal();

                        /*startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", total));*/
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

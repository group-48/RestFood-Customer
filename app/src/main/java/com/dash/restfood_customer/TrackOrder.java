package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baoyachi.stepview.VerticalStepView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TrackOrder extends BaseActivity {

    private static final String TAG = "TrackOrder";
    private static final String CHANNEL_ID ="Track" ;
    FirebaseFirestore db= FirebaseFirestore.getInstance();
    FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();

    private TextView tv_status;
    private TextView tv_order;
    private TextView tv_total;
    private EditText et_food;
    private VerticalStepView stepView;
    private CardView cv1,cv2,cv3;

    public String orderId;

    public int x=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_track_order, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end

        tv_status=(TextView)findViewById(R.id.tv_status);
        tv_order=(TextView)findViewById(R.id.tv_order);
        tv_total=(TextView)findViewById(R.id.tv_total);
        et_food=(EditText) findViewById(R.id.et_food);
        stepView=(VerticalStepView) findViewById(R.id.step_view);
        cv1=findViewById(R.id.cardView1);
        cv2=findViewById(R.id.cardView2);
        cv3=findViewById(R.id.cardView3);



        List<String> statuses=new ArrayList<>();
        statuses.add("Pending");
        statuses.add("Accepted");
        statuses.add("Ready");
        statuses.add("Complete");

        stepView.setStepsViewIndicatorComplectingPosition(statuses.size()-2)
                .reverseDraw(false)
                .setStepViewTexts(statuses)
                .setLinePaddingProportion(0.85f)
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#FFFF00"))
                .setStepViewComplectedTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark))
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(this,R.color.colorPrimary))
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#FFFFFF"))
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this,R.drawable.done))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this,R.drawable.done))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this,R.drawable.done));

        stepView.setStepsViewIndicatorComplectingPosition(0);



        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref",0);
        SharedPreferences.Editor editor = sharedPref.edit();
        orderId=sharedPref.getString("OrderId",null);
        editor.remove("Done");
        editor.commit();
        Log.w(TAG,"Order id is"+orderId);
        if(orderId==null || getIntent().getStringExtra("OrderId").isEmpty()){
            tv_order.setText("No pending Orders");
            tv_status.setText("");
            tv_total.setText("");
            et_food.setText("");
            cv2.setVisibility(View.GONE);
            cv3.setVisibility(View.GONE);

        }
        else{
            if(!getIntent().getStringExtra("OrderId").isEmpty()){
                orderId=getIntent().getStringExtra("OrderId");
            }
            db.collection("orders").document(orderId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        if (snapshot.exists()) {
                            if(Objects.equals("Done",snapshot.getString("Status")) || Objects.equals("true",snapshot.get("Done").toString())){
                                tv_order.setText("No pending Orders");
                                tv_status.setText("");
                                tv_total.setText("");
                                et_food.setText("");
                                cv2.setVisibility(View.GONE);
                                cv3.setVisibility(View.GONE);


                            }
                            else{
                                cv2.setVisibility(View.VISIBLE);
                                cv3.setVisibility(View.VISIBLE);

                                Log.d(TAG, "DocumentSnapshot data: " + snapshot.getData());

                                tv_order.setText("Order id: "+orderId);
                                //tv_status.setText("Order Status: "+snapshot.get("Status"));
                                tv_total.setText("Order Total: "+snapshot.get("Total"));

                                List<String> foods = (List<String>) snapshot.get("Food_Names");
                                final List<String> qty = (List<String>) snapshot.get("Qty_List");

                                et_food.setText("Foods:\n");


                                for (int i=0;i<foods.size();i++) {
                                    et_food.setText("\t"+et_food.getText().toString()+"\n"+foods.get(i)+": "+qty.get(i));

                                }
                            }

                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

            createNotificationChannel();

            final DocumentReference docRef = db.collection("orders").document(orderId)  ;
            final ListenerRegistration registration=docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable final DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, "Current data: " + snapshot.getData());
                        Log.d(TAG, "Current cites in CA: " + snapshot.get("Status"));
                        tv_status.setText("Order Status: "+snapshot.get("Status"));

                        if(Objects.equals("Done",snapshot.getString("Status")) && Objects.equals("false",snapshot.get("Done").toString())){
                            tv_order.setText("No pending Orders");
                            tv_status.setText("");
                            tv_total.setText("");
                            et_food.setText("");
                            cv2.setVisibility(View.GONE);
                            cv3.setVisibility(View.GONE);

                            SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("MyPref",0);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("Done", "1");
                            editor.commit();
                            stepView.setStepsViewIndicatorComplectingPosition(3);

                            db.collection("orders").document(orderId).update("Done",true);

                        }
                        else if(Objects.equals("Ready",snapshot.getString("Status"))){
                            stepView.setStepsViewIndicatorComplectingPosition(2);
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(TrackOrder.this, CHANNEL_ID)
                                    .setSmallIcon(R.drawable.food_icon)
                                    .setContentTitle("Order status")
                                    .setContentText("Your order is ready, Please pick it up")
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(TrackOrder.this);
                            notificationManager.notify(1, builder.build());
                        }
                        else if(Objects.equals("Preparing",snapshot.getString("Status"))){
                            stepView.setStepsViewIndicatorComplectingPosition(1);
                        }


                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });

            if(Objects.equals("1",sharedPref.getString("Done",null))){

                Log.d(TAG, "reg removed");
                registration.remove();
                tv_order.setText("No pending Orders");
                tv_status.setText("");
                tv_total.setText("");
                cv2.setVisibility(View.GONE);
                cv3.setVisibility(View.GONE);

            }







        /*db.collection("orders")
                .whereEqualTo("Done",false).whereEqualTo("User",user.getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        List<String> cities = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value) {
                            /*if (doc.get("name") != null) {
                                cities.add(doc.getString("Status"));
                            }
                            Log.d(TAG, "Current cites in CA: " + doc.get("Status"));
                            if(Objects.equals("Done",doc.getString("Status"))){
                                Log.d(TAG, "ska" );
                                NotificationCompat.Builder builder = new NotificationCompat.Builder(TrackOrder.this, CHANNEL_ID)
                                        .setSmallIcon(R.drawable.food_icon)
                                        .setContentTitle("Order status")
                                        .setContentText("Your order is complete")
                                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(TrackOrder.this);
                                notificationManager.notify(1, builder.build());
                                db.collection("orders").document(doc.getId()).update("Done",true);
                            }
                        }

                    }
                });*/



        }


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            String description = getString(R.string.app_name);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

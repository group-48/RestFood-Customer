package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dash.restfood_customer.models.Reserve;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.core.Tag;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

import static com.dash.restfood_customer.InternetConfig.InternetConfig.user;

public class ReserveTable extends BaseActivity implements TimePickerDialog.OnTimeSetListener,View.OnClickListener {

    Button selectDate,selectTime,btn_done;
    String ShopName,bdate,btime,ReserveId;
    int guestno;
    TextView date;
    TextView time;
    TextView status;
    EditText rval,tableval,guestval;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;
    String shopId;

    FirebaseFirestore db= FirebaseFirestore.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_reserve_table, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end

        selectDate=findViewById(R.id.btn_date);
        date=findViewById(R.id.txt_date);
        selectTime=findViewById(R.id.btn_time);
        btn_done=findViewById(R.id.btn_done);
        time=findViewById(R.id.txt_time);
        guestval=findViewById(R.id.guestval);
        status=findViewById(R.id.booking_status);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Placing reservation");

        if (getIntent()!=null){

            ShopName=getIntent().getStringExtra("sName");
            shopId=getIntent().getStringExtra("shop");

        }

        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar=Calendar.getInstance();
                year=calendar.get(Calendar.YEAR);
                month=calendar.get(Calendar.MONTH);
                dayOfMonth=calendar.get(Calendar.DAY_OF_MONTH);


                datePickerDialog=new DatePickerDialog(ReserveTable.this,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        date.setText(day +"/" + (month+1) + "/" + year);
                    }
                },year,month,dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);//disable the past dates
                datePickerDialog.show();
            }
        });

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timepicker=new TimePickerFragment();
                timepicker.show(getSupportFragmentManager(),"time picker");
            }
        });
        btn_done.setOnClickListener(this);
    }
    private void reserve(){

        if(!validate()){
            progressDialog.dismiss();
            return;
        }
        guestno=Integer.parseInt(guestval.getText().toString());
        bdate=date.getText().toString();
        btime=time.getText().toString();

        final Reserve reserve=new Reserve(user.getUid(),ShopName,guestno,bdate,btime,ReserveId);

        reserve.setDate(bdate);
        reserve.setGuestno(guestno);
        reserve.setTime(btime);
        reserve.setUserId(user.getUid());
        reserve.setShopName(ShopName);
        reserve.setStatus("Requested");
        reserve.setShopId(shopId);

        db.collection("reserve").add(reserve).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                db.collection("reserve").document(documentReference.getId()).update("bookingId",documentReference.getId());
                //Toast.makeText(ReserveTable.this, "Booking successful", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                finish();
                Intent intent=new Intent(ReserveTable.this,Viewbooking.class);
                startActivity(intent);
                Log.d("TAG","Done");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ReserveTable.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });



    }

    private boolean validate() {

        String guestno=guestval.getText().toString();
        String bdate=date.getText().toString();
        String btime=time.getText().toString();

        if(TextUtils.isEmpty((guestno))){
            Toast.makeText((this), "Please enter the number of guests", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty((bdate)) && (TextUtils.isEmpty((btime)))){
            Toast.makeText((this), "Please enter the date and time", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty((bdate))){
            Toast.makeText((this), "Please enter the date", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.isEmpty((btime))){
            Toast.makeText((this), "Please enter the time", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        time=findViewById(R.id.txt_time);
        time.setText(hour+":"+ minute );
    }

    @Override
    public void onClick(View v) {
        if(v==btn_done){
            progressDialog.show();
            reserve();
            /*Intent intent=new Intent(ReserveTable.this,MainActivity.class);
            startActivity(intent);*/
        }
    }



}

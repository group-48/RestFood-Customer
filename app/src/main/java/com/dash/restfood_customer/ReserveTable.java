package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.dash.restfood_customer.models.Reserve;
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
    String ShopId,bdate,btime,ReserveId;
    int guestno;
    TextView date;
    TextView time;
    EditText rval,tableval,guestval;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    FirebaseFirestore db= FirebaseFirestore.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

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
        /*rval=findViewById(R.id.rval);
        tableval=findViewById(R.id.tableval);*/
        guestval=findViewById(R.id.guestval);

        if (getIntent()!=null){

            ShopId=getIntent().getStringExtra("shop");

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
        /*custname=rval.getText().toString();
        tableno=Integer.parseInt(tableval.getText().toString());*/
        guestno=Integer.parseInt(guestval.getText().toString());
        bdate=date.getText().toString();
        btime=time.getText().toString();

        final Reserve reserve=new Reserve(user.getUid(),ShopId,guestno,bdate,btime,ReserveId);

        reserve.setDate(bdate);
        reserve.setGuestno(guestno);
        reserve.setTime(btime);
        reserve.setUserId(user.getUid());
        reserve.setShopId(ShopId);

        db.collection("reserve").add(reserve).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                db.collection("reserve").document(documentReference.getId()).update("bookingId",documentReference.getId());
                Log.d("TAG","Done");
            }
        });



    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        time=findViewById(R.id.txt_time);
        time.setText(hour+":"+ minute );
    }

    @Override
    public void onClick(View v) {
        if(v==btn_done){
            reserve();
            Intent intent=new Intent(ReserveTable.this,MainActivity.class);
            startActivity(intent);
        }
    }



}

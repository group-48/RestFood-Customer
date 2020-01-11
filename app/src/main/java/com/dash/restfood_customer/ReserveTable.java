package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class ReserveTable extends BaseActivity implements TimePickerDialog.OnTimeSetListener {

    Button selectDate;
    Button selectTime;
    TextView date;
    TextView time;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;


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
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        time=findViewById(R.id.txt_time);
        time.setText(hour+":"+ minute );
    }
}

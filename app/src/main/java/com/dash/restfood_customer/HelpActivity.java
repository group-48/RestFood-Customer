package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.sliderviewlibrary.SliderView;

import java.util.ArrayList;
import java.util.Objects;

public class HelpActivity extends BaseActivity {

    SliderView sliderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_help, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end

        sliderView=findViewById(R.id.sliderView);

        ArrayList<Integer> Images = new ArrayList<>();
        String action=getIntent().getStringExtra("Help");
        if(Objects.equals("reserve",action)){
            Images.add(R.drawable.res1);
            Images.add(R.drawable.res2);
            Images.add(R.drawable.res3);
            Images.add(R.drawable.res4);
        }
        else if(Objects.equals("checkout",action)){
            Images.add(R.drawable.co1);
            Images.add(R.drawable.c02);
            Images.add(R.drawable.co3);
            Images.add(R.drawable.co4);
            Images.add(R.drawable.co5);
            Images.add(R.drawable.co6);
        }
        else if(Objects.equals("order",action)){
            Images.add(R.drawable.place1);
            Images.add(R.drawable.place2);
            Images.add(R.drawable.place3);
            Images.add(R.drawable.place4);
            Images.add(R.drawable.place5);
        }








        sliderView.setImages(Images);

    }
}

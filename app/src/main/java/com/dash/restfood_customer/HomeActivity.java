  package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

  public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_menu;
    ImageView bgapp,cloverimg;
    LinearLayout splashText,homeText;
    Animation fromBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //to extend the base activity comment out the next line and add the code below
        //setContentView(R.layout.activity_home);

        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_home, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end


        fromBottom= AnimationUtils.loadAnimation(this,R.anim.frombottom);

        bgapp=findViewById(R.id.bgapp);
        cloverimg=findViewById(R.id.clover);
        splashText=findViewById(R.id.splashText);
        homeText=findViewById(R.id.homeText);

        bgapp.animate().translationY(-1300).setDuration(800).setStartDelay(300);
        cloverimg.animate().alpha(0).setDuration(800).setStartDelay(600);

        splashText.animate().translationY(140).alpha(0).setDuration(800).setStartDelay(300);
        homeText.startAnimation(fromBottom);


    }

      @Override
      public void onClick(View view) {

      }
  }

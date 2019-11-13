  package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

  public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_menu;

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


        btn_menu=(Button)findViewById(R.id.btn_menu);

        btn_menu.setOnClickListener(this);
    }

      @Override
      public void onClick(View view) {
          if(view==btn_menu){

          }
      }
  }

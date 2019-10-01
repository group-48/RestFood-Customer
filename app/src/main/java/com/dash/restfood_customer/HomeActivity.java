  package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

  public class HomeActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btn_menu=(Button)findViewById(R.id.btn_menu);

        btn_menu.setOnClickListener(this);
    }

      @Override
      public void onClick(View view) {
          if(view==btn_menu){
              Intent intent=new Intent(this,FoodCategory.class);
              startActivity(intent);
          }
      }
  }

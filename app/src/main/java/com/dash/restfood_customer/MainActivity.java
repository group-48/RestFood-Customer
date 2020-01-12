package com.dash.restfood_customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.dash.restfood_customer.models.Category;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.cardview.widget.CardView;

public class MainActivity extends BaseActivity implements View.OnClickListener {


   public MenuAdapter adapter;
   CardView cv_browse,cv_test;

   Button btn_test,btn_select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_main, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end

        cv_browse=findViewById(R.id.cv_browse);
        cv_test=findViewById(R.id.cv_test);

        cv_test.setOnClickListener(this);
        cv_browse.setOnClickListener(this);

        btn_select=findViewById(R.id.btn_select);

        btn_select.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        if(v==btn_select){
            Intent intent=new Intent(this, CategoryList.class);
            intent.putExtra("shop", "uilyCqrK3iU0U895PIiHZwyouZH3");
            intent.putExtra("id","uilyCqrK3iU0U895PIiHZwyouZH3");
            intent.putExtra("Browse","False");
            startActivity(intent);
        }
        else if(v==cv_browse){
            Intent inta=new Intent(MainActivity.this, ShopList.class);
            startActivity(inta);

        }
        else if(v==cv_test){
            Intent intent=new Intent(this, ScanShop.class);
            startActivity(intent);
        }

    }
}

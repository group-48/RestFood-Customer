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
   CardView cv_browse,cv_menu,cv_cart,cv_profile;


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
        cv_menu=findViewById(R.id.cv_menu);
        cv_cart=findViewById(R.id.cv_cart);
        cv_profile=findViewById(R.id.cv_profile);

        cv_menu.setOnClickListener(this);
        cv_profile.setOnClickListener(this);
        cv_browse.setOnClickListener(this);
        cv_cart.setOnClickListener(this);
    }




    @Override
    public void onClick(View v) {
        if(v==cv_cart){
            Intent intent=new Intent(this, CartActivity.class);
            startActivity(intent);
        }
        else if(v==cv_browse){
            Intent intent=new Intent(MainActivity.this, ShopList.class);
            startActivity(intent);

        }
        else if(v==cv_menu){
            Intent intent=new Intent(this, ScanShop.class);
            startActivity(intent);
        }
        else if(v==cv_profile){
            Intent intent=new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }
    }
}

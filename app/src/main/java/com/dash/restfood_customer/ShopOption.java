package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class ShopOption extends BaseActivity implements View.OnClickListener {

    private CardView cv_reserve,cv_menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_shop_option, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end

        cv_reserve=findViewById(R.id.cv_reserve);
        cv_menu=findViewById(R.id.cv_menu);

        cv_menu.setOnClickListener(this);
        cv_reserve.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String shopId=getIntent().getStringExtra("shop");
        String shopName=getIntent().getStringExtra("sName");
        String id=getIntent().getStringExtra("id");

        Intent intent=new Intent(ShopOption.this,CategoryList.class);
        if(v==cv_reserve){
            intent=new Intent(ShopOption.this,ReserveTable.class);
        }
        else if(v==cv_menu){

            intent=new Intent(ShopOption.this,CategoryList.class);


        }
        intent.putExtra("shop",shopId);
        intent.putExtra("sName",shopName);
        intent.putExtra("id",id);
        intent.putExtra("Browse","True");
        startActivity(intent);
    }
}

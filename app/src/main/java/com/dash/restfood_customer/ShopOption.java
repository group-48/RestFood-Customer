package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

public class ShopOption extends BaseActivity implements View.OnClickListener {

    private Button btn_reserve,btn_menu;
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

        btn_menu=findViewById(R.id.btn_menu);
        btn_reserve=findViewById(R.id.btn_reserve);

        btn_reserve.setOnClickListener(this);
        btn_menu.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        String shopId=getIntent().getStringExtra("shop");
        String id=getIntent().getStringExtra("id");

        Intent intent=new Intent(ShopOption.this,CategoryList.class);
        if(v==btn_reserve){
            intent=new Intent(ShopOption.this,ReserveTable.class);
        }
        else if(v==btn_menu){

            intent=new Intent(ShopOption.this,CategoryList.class);


        }
        intent.putExtra("shop",shopId);
        intent.putExtra("id",id);
        intent.putExtra("Browse","True");
        startActivity(intent);
    }
}

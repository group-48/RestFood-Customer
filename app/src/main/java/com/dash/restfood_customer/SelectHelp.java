package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public class SelectHelp extends BaseActivity implements View.OnClickListener {

    CardView cv_reserve,cv_order,cv_checkout;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_select_help, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end

        intent=new Intent(this,HelpActivity.class);

        cv_reserve=findViewById(R.id.cv_reserve);
        cv_order=findViewById(R.id.cv_order);
        cv_checkout=findViewById(R.id.cv_checkout);

        cv_reserve.setOnClickListener(this);
        cv_order.setOnClickListener(this);
        cv_checkout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==cv_checkout){
           intent.putExtra("Help","checkout");
           startActivity(intent);
        }
        if(v==cv_reserve){
            intent.putExtra("Help","reserve");
            startActivity(intent);
        }
        if(v==cv_order){
            intent.putExtra("Help","order");
            startActivity(intent);
        }
    }
}

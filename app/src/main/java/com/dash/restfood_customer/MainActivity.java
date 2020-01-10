package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends BaseActivity implements View.OnClickListener {

   private FirebaseFirestore db=FirebaseFirestore.getInstance();
   private CollectionReference ref=db.collection("shop").document("dILfWEqZh7fN5LBtiWMFMoeCShe2").collection("Category");
   public MenuAdapter adapter;

   Button btn_test;

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



        btn_test=findViewById(R.id.btn_test);
        btn_test.setOnClickListener(this);

    }

    public void sublit(View v)
    {
        Intent inta=new Intent(MainActivity.this, shoplist.class);
        startActivity(inta);
    }


    @Override
    public void onClick(View v) {
        if(v==btn_test){


        }
    }
}

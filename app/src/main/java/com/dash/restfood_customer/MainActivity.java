package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity  {

   private FirebaseFirestore db=FirebaseFirestore.getInstance();
   private CollectionReference ref=db.collection("shop").document("dILfWEqZh7fN5LBtiWMFMoeCShe2").collection("Category");
   public MenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






    }

    public void sublit(View v)
    {
        Intent inta=new Intent(MainActivity.this, shoplist.class);
        startActivity(inta);
    }



}

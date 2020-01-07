package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        NavigationView navigationView=(NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        checkAuth();
        progressDialog=new ProgressDialog(this);
    }

    private void checkAuth() {
        firebaseAuth= FirebaseAuth.getInstance();
        FirebaseUser user=firebaseAuth.getCurrentUser();
        if(user==null){
            Toast.makeText(this,"no user",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,user.getUid(),Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id=menuItem.getItemId();

        if(id==R.id.home){
            Intent intent=new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        else if(id==R.id.logOut){
            progressDialog.setMessage("Logging out");
            progressDialog.show();
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(this,LoginActivity.class);
            progressDialog.hide();
            startActivity(intent);
            this.finish();
        }
        else if(id==R.id.editProfile){
            Intent intent=new Intent(this,EditProfile.class);
            startActivity(intent);

        }
        else if(id==R.id.cart){
            Intent intent=new Intent(this,CartActivity.class);
            startActivity(intent);

        }
        else if(id==R.id.trackOrders){
            Intent intent=new Intent(this,TrackOrder.class);
            startActivity(intent);
        }
        else if(id==R.id.viewOrders){
            Intent intent=new Intent(this,DisplayOrders.class);
            startActivity(intent);
        }
        return false;
    }
}

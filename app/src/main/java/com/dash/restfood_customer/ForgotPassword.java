package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dash.restfood_customer.InternetConfig.InternetConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail;
    private Button btnReset;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail=(EditText)findViewById(R.id.etEmail);
        btnReset=(Button)findViewById(R.id.btnReset);

        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Sending password reset email");

        btnReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==btnReset){
            progressDialog.show();

            String email=etEmail.getText().toString().trim();
            String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
            if(email.isEmpty() || !email.matches(emailPattern)){
                etEmail.setError("Please enter a valid email");
                etEmail.requestFocus();
                progressDialog.dismiss();
                return;
            }


            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                //Toast.makeText(ForgotPassword.this,"Email sent",Toast.LENGTH_SHORT).show();
                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(ForgotPassword.this);
                                builder.setMessage("Goto your email to reset the password")
                                        .setCancelable(false)
                                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                                Intent intent=new Intent(ForgotPassword.this,LoginActivity.class);
                                                startActivity(intent);
                                            }
                                        });
                                //Creating dialog box
                                AlertDialog alert = builder.create();
                                //Setting the title manually
                                alert.setTitle("Password Reset Email sent");
                                alert.show();
                            }
                            else{
                                FirebaseException e=(FirebaseException)task.getException();
                                Toast.makeText(ForgotPassword.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}

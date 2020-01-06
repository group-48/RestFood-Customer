package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail=(EditText)findViewById(R.id.etEmail);
        btnReset=(Button)findViewById(R.id.btnReset);

        btnReset.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==btnReset){
            String email= etEmail.getText().toString().trim();
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPassword.this,"Email sent",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                FirebaseException e=(FirebaseException)task.getException();
                                Toast.makeText(ForgotPassword.this,"failed"+e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}

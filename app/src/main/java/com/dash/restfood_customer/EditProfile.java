package com.dash.restfood_customer;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dash.restfood_customer.models.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfile extends BaseActivity implements View.OnClickListener {
    private EditText et_fname;
    private EditText et_lname;
    private EditText et_DOB;
    private EditText et_phone;
    private Button btn_edit;

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_edit_profile, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end

        et_fname=(EditText)findViewById(R.id.et_fname);
        et_lname=(EditText)findViewById(R.id.et_lname);
        et_DOB=(EditText)findViewById(R.id.et_DOB);
        et_phone=(EditText)findViewById(R.id.et_phone);
        btn_edit=(Button)findViewById(R.id.btn_edit);

        btn_edit.setOnClickListener(this);

        getUserData();
    }

    private void getUserData() {
        String uid=user.getUid();
        DocumentReference documentReference=db.collection("users").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document=task.getResult();
                if (document.exists()) {
                    Log.d("Document", "DocumentSnapshot data: " + document.getData());
                    //customer=new Customer(document.get("email").toString(),document.get("fName").toString(),document.get("lName").toString(),(Integer) (document.get("phone")),document.get("DOB").toString());
                    et_DOB.setText(document.get("DOB").toString());
                    et_lname.setText(document.get("lName").toString());
                    et_fname.setText(document.get("fName").toString());
                    et_phone.setText(document.get("phone").toString());
                } else {
                    Log.d("Document", "No such document");
                }
            }

        });
    }

    @Override
    public void onClick(View v) {
        if(v==btn_edit){
            updateUser();
        }
    }

    private void updateUser() {
        String fname=et_fname.getText().toString().trim();
        String lname=et_lname.getText().toString().trim();
        String dob=et_DOB.getText().toString().trim();
        int phone=Integer.parseInt(et_phone.getText().toString().trim());

        Customer customer=new Customer(user.getEmail(),fname,lname,phone,dob);

        DocumentReference documentReference=db.collection("users").document(user.getUid());
        documentReference.set(customer).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(EditProfile.this,"User updated Successfully",Toast.LENGTH_LONG).show();
            }

        });
    }
}

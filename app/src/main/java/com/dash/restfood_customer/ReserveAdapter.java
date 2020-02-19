package com.dash.restfood_customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dash.restfood_customer.models.Reserve;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ReserveAdapter extends FirestoreRecyclerAdapter<Reserve,ReserveAdapter.ReserveHolder>{

    private OnItemClickListener listener;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();

    public ReserveAdapter(@NonNull FirestoreRecyclerOptions<Reserve> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReserveHolder reserveHolder, int i, @NonNull Reserve reserve) {

        reserveHolder.booking_id.setText(reserve.getBookingId());
        reserveHolder.shop_name.setText(reserve.getShopName());
        reserveHolder.booking_date.setText(reserve.getDate());
        reserveHolder.booking_time.setText(reserve.getTime());
        reserveHolder.status.setText(reserve.getStatus());

        if((Objects.equals("Cancelled",reserve.getStatus())) || (Objects.equals("Completed",reserve.getStatus()))){
            reserveHolder.cancel.setVisibility(View.GONE);
        }

    }

    @NonNull
    @Override
    public ReserveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlebooking,parent,false);
        return new ReserveHolder(v) ;
    }

    public void deleteItem(int position){

        //getSnapshots().getSnapshot(position).getReference().delete();

        db.collection("reserve")
                .document(getSnapshots().getSnapshot(position).getReference().getId())
                .update("status","Cancelled")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // Log.w(TAG, "Error updating document", e);

                    }
                });


    }

    /*public void  onsetItem(int position){
        getSnapshots().getSnapshot(position).getReference().set(10);
    }*/


    class ReserveHolder extends RecyclerView.ViewHolder{

        TextView booking_id,shop_name,booking_date,booking_time,status;
        Button cancel;

        public ReserveHolder(final View itemView) {
            super(itemView);
            booking_id=itemView.findViewById(R.id.tv_booking_id);
            shop_name=itemView.findViewById(R.id.booking_shop_id);
            booking_date=itemView.findViewById(R.id.booking_date);
            booking_time=itemView.findViewById(R.id.booking_time);
            cancel=itemView.findViewById(R.id.btn_cancel);
            status=itemView.findViewById(R.id.booking_status);




            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(itemView.getContext());
                    builder
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    int position=getAdapterPosition();
                                    if(position!=RecyclerView.NO_POSITION && listener!=null){
                                        listener.onDeleteClick(position);
                                    }
                                    cancel.setEnabled(false);
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert = builder.create();

                    alert.setTitle("Are you sure you want to cancel the booking");
                    alert.show();


                }
            });

        }
    }

    public  interface OnItemClickListener{
        void  onItemClick(DocumentSnapshot documentSnapshot, int position);
        void onDeleteClick(int position);
    }

    public  void setOnItemClickListener(ReserveAdapter.OnItemClickListener listener){
        this.listener=listener;
    }



}

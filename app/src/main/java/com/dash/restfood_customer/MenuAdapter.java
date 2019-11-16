package com.dash.restfood_customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dash.restfood_customer.Interface.ItemClickListener;
import com.dash.restfood_customer.models.Category;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class MenuAdapter extends FirestoreRecyclerAdapter<Category, MenuAdapter.MenuHolder> {
    private OnItemClickListener listener;

    public ItemClickListener clickListener;

    public MenuAdapter(@NonNull FirestoreRecyclerOptions<Category> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MenuHolder menuHolder, int i, @NonNull Category category) {
        menuHolder.food_name.setText(category.getName());
        Picasso.get().load(category.getImage()).into(menuHolder.food_image);


    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_item,parent,false);
        return new MenuHolder(v);
    }




    class MenuHolder extends RecyclerView.ViewHolder {

        public TextView food_name;
        public ImageView food_image;


        public MenuHolder( View itemView) {
            super(itemView);

            food_name=itemView.findViewById(R.id.tvTitle);
            food_image=itemView.findViewById(R.id.ivImage);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }


    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}

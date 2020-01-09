package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

import com.dash.restfood_customer.models.OrderFood;
import com.dash.restfood_customer.models.Review;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewReviews extends BaseActivity {

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference ref;
    String queryId;
    public ReviewAdapter adapter;
    Float rating=0.0f;
    RatingBar rb_reviews;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_view_reviews, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end
        rb_reviews=findViewById(R.id.rb_reviews);
        queryId=getIntent().getStringExtra("queryId");
        rb_reviews.setRating(5);
        loadReviews();
        setRating();
    }

    private void setRating() {

        db.collection("reviews").whereEqualTo("foodName",queryId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Review review=document.toObject(Review.class);
                    Log.d("ViewReviews",document.get("rating").toString());
                    rating=rating+review.getRating();
                    count++;
                }
                rb_reviews.setRating(rating/count);
            }
        });
    }

    private void loadReviews() {

        Log.d("ViewReviews",queryId);
        ref=db.collection("reviews");
        Query query=ref.whereEqualTo("foodName",queryId).orderBy("orderId",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Review> options=new FirestoreRecyclerOptions.Builder<Review>().setQuery(query,Review.class).build();
        Log.d("Working","fine");
        adapter=new ReviewAdapter(options);

        RecyclerView recyclerView=findViewById(R.id.review_list );
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(ViewReviews.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

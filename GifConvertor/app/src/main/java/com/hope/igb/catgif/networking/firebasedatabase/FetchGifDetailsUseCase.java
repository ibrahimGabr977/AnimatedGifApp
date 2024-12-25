package com.hope.igb.catgif.networking.firebasedatabase;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hope.igb.catgif.comman.BaseObservable;
import com.hope.igb.catgif.networking.models.OnlineGif;

public class FetchGifDetailsUseCase extends BaseObservable<FetchGifDetailsUseCase.FetchGifDetailsListener> {


    public interface FetchGifDetailsListener {
        void onGifDetailsFetched(OnlineGif onlineGif);
        void onFetchedCanceled(String message);
    }


    private final DatabaseReference reference;


    public FetchGifDetailsUseCase(String gif_id) {
        //the targeted gif reference
        this.reference = FirebaseDatabase.getInstance().getReference().child("CatGIFs").child(gif_id);
    }



    public void fetchGifDetails(){
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        for (FetchGifDetailsListener listener : getListeners())
                            listener.onGifDetailsFetched(snapshot.getValue(OnlineGif.class));

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        for (FetchGifDetailsListener listener : getListeners())
                            listener.onFetchedCanceled(error.getMessage());
                    }
                });

    }



    public void editLikes(int new_value){
        reference.child("likes_count")
                .setValue(new_value)
                .addOnCompleteListener(task -> {

                });
    }


}

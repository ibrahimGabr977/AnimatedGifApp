package com.hope.igb.catgif.networking.firebasedatabase;


import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hope.igb.catgif.comman.BaseObservable;
import com.hope.igb.catgif.networking.models.OnlineGif;

import java.util.ArrayList;

public class FetchGIFsUseCase extends BaseObservable<FetchGIFsUseCase.FetchGIFsListener> {



    //listen for fetching data
    public interface FetchGIFsListener {
        void onGIFsFetched(ArrayList<OnlineGif> onlineGIFs);
        void onFetchedCanceled(String error_message);
    }



    private final DatabaseReference reference;
    private final ArrayList<OnlineGif> onlineGIFs;


    public FetchGIFsUseCase() {
        //the gif reference
        this.reference = FirebaseDatabase.getInstance().getReference().child("CatGIFs");
        onlineGIFs = new ArrayList<>();
    }


    //fetch only children that has a certain category
    public void fetchGIFsByCategory(String category){

        reference.orderByChild("category")
                .equalTo(category)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        onlineGIFs.clear();

                        OnlineGif onlineGif;

                        for (DataSnapshot dss: snapshot.getChildren()) {
                            onlineGif = dss.getValue(OnlineGif.class);
                            onlineGIFs.add(onlineGif);
                        }

                        for (FetchGIFsListener listener : getListeners())
                            listener.onGIFsFetched(onlineGIFs);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        for (FetchGIFsListener listener : getListeners())
                            listener.onFetchedCanceled(error.getMessage());
                    }
                });


    }


    //fetch all children
    public void fetchAllGIFs(){

        reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        onlineGIFs.clear();

                        OnlineGif onlineGif;

                        for (DataSnapshot dss: snapshot.getChildren()) {
                            onlineGif = dss.getValue(OnlineGif.class);
                            onlineGIFs.add(onlineGif);
                        }

                        for (FetchGIFsListener listener : getListeners())
                            listener.onGIFsFetched(onlineGIFs);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        for (FetchGIFsListener listener : getListeners())
                            listener.onFetchedCanceled(error.getMessage());
                    }
                });


    }



    //fetch only top 20 gif ordered by likes
    public void fetchTopGIFs(){

        reference.child("likes_count")
                .orderByValue()
                .limitToFirst(20)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        onlineGIFs.clear();

                        OnlineGif onlineGif;

                        for (DataSnapshot dss: snapshot.getChildren()) {
                            onlineGif = dss.getValue(OnlineGif.class);
                            onlineGIFs.add(onlineGif);
                        }

                        for (FetchGIFsListener listener : getListeners())
                            listener.onGIFsFetched(onlineGIFs);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        for (FetchGIFsListener listener : getListeners())
                            listener.onFetchedCanceled(error.getMessage());
                    }
                });

    }


}

package com.hope.igb.catgif.screens.giflibrary;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.hope.igb.catgif.gifcontroller.GifPlayer;
import java.util.ArrayList;

public class LibraryRecyclerAdapter extends RecyclerView.Adapter<LibraryRecyclerAdapter.LibraryViewHolder>
        implements LibraryItemViewMvc.Listener{

    private final ArrayList<Uri> gif_files;
    private final Context context;
    private final int screen_width;
    private final GifPlayer gifPlayer;
    private final Listener listener;



    interface Listener {
        void onAddGifClickListener();
        void onGifClickedListener(int selected, int last_selected);

    }




    public LibraryRecyclerAdapter(Context context, ArrayList<Uri> gif_files, int screen_width, GifPlayer gifPlayer, Listener listener) {
        this.context = context;
        this.gif_files = gif_files;
        this.screen_width = screen_width;
        this.gifPlayer = gifPlayer;
        this.listener = listener;
    }




    @NonNull
    @Override
    public LibraryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LibraryItemViewMvc viewMvc = new LibraryItemViewMvc(LayoutInflater.from(context), parent, gifPlayer, screen_width);
        viewMvc.registerListener(this);
        return new LibraryViewHolder(viewMvc);
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull LibraryViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.viewMvc.unregisterListener(this);
    }



    @Override
    public void onViewAttachedToWindow(@NonNull LibraryViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.viewMvc.registerListener(this);
    }

    @Override
    public void onBindViewHolder(@NonNull LibraryViewHolder holder, int position) {

        //pass null in case of position 0 because it's not gif but (add_gif button)
        // the length of list equal the list length + 1 so it need to back to normal
        holder.viewMvc.bindGIFs(position == 0 ? null : gif_files.get(position - 1), position);


    }


    @Override
    public int getItemCount() {
        return gif_files.size() + 1;
    }

    @Override
    public void onAddGifClickListener() {
        listener.onAddGifClickListener();

    }

    @Override
    public void onGifClicked(int selected, int last_selected) {
        listener.onGifClickedListener(selected, last_selected);
    }


    public static class LibraryViewHolder extends RecyclerView.ViewHolder {

        private final LibraryItemViewMvc viewMvc;

        public LibraryViewHolder(LibraryItemViewMvc viewMvc) {
            super(viewMvc.getRootView());
            this.viewMvc = viewMvc;
        }

        public LibraryItemViewMvc getViewMvc() {
            return viewMvc;
        }
    }
}

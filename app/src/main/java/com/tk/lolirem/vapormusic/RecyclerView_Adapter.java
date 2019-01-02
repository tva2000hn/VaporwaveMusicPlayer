package com.tk.lolirem.vapormusic;

import android.content.Context;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.FastScroller;
import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;
import com.tk.lolirem.vapormusic.R;

import java.util.Collections;
import java.util.List;


public class RecyclerView_Adapter extends RecyclerView.Adapter<ViewHolder> implements SectionTitleProvider {

    private MainActivity main;
    private String haha;
    List<Audio> list = Collections.emptyList();
    Context context;

    public RecyclerView_Adapter(List<Audio> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;

    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.title.setText(list.get(position).getTitle());
        holder.artist.setText(list.get(position).getArtist());
        String abcd =String.format("%03d", position +1 );
        String abc = String.valueOf(position +1);

        try{
        holder.number.setText(abcd);} catch ( Exception hihi){}

        try{
            if (list.get(position).getMode() == 0 ||
                    list.get(position).getMode() == 3    ){
                haha = list.get(position).getTitle().substring(0,1);
                holder.title.setTypeface(null, Typeface.BOLD);
                holder.artist.setTypeface(null, Typeface.NORMAL);
            }
            if (list.get(position).getMode() == 1){
                haha = list.get(position).getArtist().substring(0,1);

                holder.artist.setText(list.get(position).getTitle());
                holder.title.setText(list.get(position).getArtist());


            }
            if (list.get(position).getMode() == 2){
                haha = list.get(position).getAlbum().substring(0,1);
                holder.title.setText(list.get(position).getAlbum());
                holder.artist.setText(list.get(position).getTitle());

            }}catch (Exception hh){
            Log.d("ohno","damn it");
            haha = list.get(position).getTitle().substring(0,1);
        }

        holder.st.setText(haha);
    }


    @Override
    public String getSectionTitle(int position) {
        //this String will be shown in a bubble for specified position
        return this.list.get(position).getTitle().toUpperCase().substring(0,2);
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }


}

class ViewHolder extends RecyclerView.ViewHolder {
public int haha1;
    TextView title;
    TextView artist;
    TextView st;
    TextView number;

    ViewHolder(View itemView) {

        super(itemView);
        title = (TextView) itemView.findViewById(R.id.title);
        st = (TextView) itemView.findViewById(R.id.oneletter);
        artist = (TextView) itemView.findViewById(R.id.artist);
        number = (TextView) itemView.findViewById(R.id.numberlist);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                haha1 =1;

            }
        });
    }
}
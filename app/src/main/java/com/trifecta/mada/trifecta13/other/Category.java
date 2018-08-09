package com.trifecta.mada.trifecta13.other;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.activity.SearchResults;
import java.util.ArrayList;


/**
 * Created by Mada on 3/1/2017.
 */

public class Category extends RecyclerView.Adapter<Category.PlanetViewHolder> {

    ArrayList<String> planetList;
    Context context;

    public Category(ArrayList<String> planetList, Context context) {
        this.planetList = planetList;
        this.context = context;
    }

    @Override
    public Category.PlanetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_row,parent,false);
        PlanetViewHolder viewHolder=new PlanetViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Category.PlanetViewHolder holder, final int position) {
        holder.text.setText(planetList.get(position).toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context,
//                        "You have clicked " + planetList.get(position).toString(),
//                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(), SearchResults.class);
                intent.putExtra("category",planetList.get(position).toString());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return planetList.size();
    }

    public static class PlanetViewHolder extends RecyclerView.ViewHolder{

        protected TextView text;

        public PlanetViewHolder(View itemView) {
            super(itemView);
            text= (TextView) itemView.findViewById(R.id.search_row);
        }
    }
}
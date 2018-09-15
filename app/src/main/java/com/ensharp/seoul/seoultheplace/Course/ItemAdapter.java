package com.ensharp.seoul.seoultheplace.Course;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.R;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    CourseModify aniyaActivity;
    Context context;
    List<Item> items;
    int item_layout;
    RecyclerView recyclerView;

    public ItemAdapter(CourseModify aniyaActivity, List<Item> items, int item_layout, RecyclerView recyclerView) {
        this.aniyaActivity = aniyaActivity;
        this.items = items;
        this.item_layout = item_layout;
        this.recyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Item item = items.get(position);

        Drawable drawable = ContextCompat.getDrawable(aniyaActivity, item.getImage());
        holder.image.setBackground(drawable);
        holder.title.setText(item.getaddress());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aniyaActivity.ChangeData(item);
                Toast.makeText(aniyaActivity, item.getaddress(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        CardView cardview;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.AddCardImage);
            title = (TextView) itemView.findViewById(R.id.AddCardName);
            cardview = (CardView) itemView.findViewById(R.id.AddCardView);
        }
    }
}
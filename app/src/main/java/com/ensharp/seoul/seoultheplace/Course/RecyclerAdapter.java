package com.ensharp.seoul.seoultheplace.Course;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ensharp.seoul.seoultheplace.PicassoImage;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    List<PlaceVO> datas;
    int item_layout;
    CourseModifyFragment fragment;
    public int choosedMember = 0;


    public RecyclerAdapter(Context context, List<PlaceVO> datas, int item_layout, CourseModifyFragment fragment) {
        this.context = context;
        this.datas = datas;
        this.item_layout = item_layout;
        this.fragment = fragment;
    }

    public int GetChoosedMemeber(){
        return choosedMember;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final PlaceVO data = datas.get(position);
        SetImageBox(holder,position);
        PicassoImage.DownLoadImage(data.getImageURL(),holder.image);
        holder.title.setText(data.getName());
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosedMember = position;
                NotifyDataSetChanged(position);
                if(position>0&&!datas.get(position-1).getName().equals("+")) //앞에 선택한게 +가 아니고  0보다 빠르면
                    fragment.SetTouchName(datas.get(position-1).getName() + "에서");
                else{
                    fragment.SetTouchName("");
                }
                if(choosedMember>0){ //맨앞이 아니면
                    if(!datas.get(position-1).getName().equals("+")) { //선택된거 앞에가 +가 아니면
                        fragment.setItemData(datas.get(position - 1)); //앞에것과의 거리를 넣는다.
                        return ;
                    }
                    else //선택된거 앞이 + 면
                        fragment.setItemData(null); //그냥 데이터를 넣는다.
                }
                else{ //맨앞인경우
                    fragment.setItemData(null);
                }
                fragment.ChangeItemData();
            }
        });
    }

    public void NotifyDataSetChanged(int position){
        choosedMember = position;
        notifyDataSetChanged();
    }
    public void SetImageBox(ViewHolder holder,final int position){
        if(choosedMember == position){
            holder.cardview.setLayoutParams(holder.params1);
        }
        else{
            holder.cardview.setLayoutParams(holder.params2);
        }
    }

    @Override
    public int getItemCount() {
        return this.datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        CardView cardview;
        RelativeLayout.LayoutParams params1;
        RelativeLayout.LayoutParams params2;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.OriginCardImage);
            title = (TextView) itemView.findViewById(R.id.OriginCardName);
            cardview = (CardView) itemView.findViewById(R.id.OriginCardView);
            params1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ChangeDp(200));
            params1.setMargins(ChangeDp(10),ChangeDp(10),ChangeDp(10),ChangeDp(10));
            params2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ChangeDp(100));
            params2.setMargins(ChangeDp(10),ChangeDp(10),ChangeDp(10),ChangeDp(10));
        }
    }
    public int ChangeDp(int data){
        int dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, data, context.getResources().getDisplayMetrics());
        return dp;
    }
}
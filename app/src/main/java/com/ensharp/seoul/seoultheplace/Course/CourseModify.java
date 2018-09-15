package com.ensharp.seoul.seoultheplace.Course;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourseModify extends AppCompatActivity {

    public int ITEM_SIZE = 3;
    List<Item> datas;
    List<Item> items;
    private RecyclerAdapter adapter;
    private ItemAdapter iadapter;
    RecyclerView recyclerView;
    RecyclerView itemview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_course);
        setData();
        setItemData();
        initView();

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int swipeFlags = 0;
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN ;
                if(ITEM_SIZE > 0 ) { //+++밖에 안남았을때
                   swipeFlags = ItemTouchHelper.LEFT;
                }
                Log.d("Move Ready: ","getMovementFlags" + dragFlags);
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    Log.d("Move : ", "onMove");
                    Collections.swap(datas, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                    Log.d("Move : ", String.valueOf(viewHolder.getAdapterPosition()) + "   " + String.valueOf(target.getAdapterPosition()));
                    adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                    adapter.choosedMember = target.getAdapterPosition();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if(viewHolder.getAdapterPosition()!=adapter.getItemCount()) { //itemCount 는 +++까지 합친 갯수. ITEM_SIZE는 +++뺀 갯수
                    Log.d("Move : ", "onSwiped, ITEM_SIZE : " + adapter.getItemCount() +" getAdapterPotion : "+viewHolder.getAdapterPosition());
                    datas.remove(viewHolder.getAdapterPosition());
                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    Log.d("Move : ", "onSwiped, ITEM_SIZE : " + ITEM_SIZE);
                    if (ITEM_SIZE == 4) { //+++가 2개가 생김 아놔 ㅡㅡ;;
                        datas.add(new Item(R.drawable.kakao_default_profile_image, "+++"));
                    }
                    //}
                    if (adapter.choosedMember == viewHolder.getAdapterPosition()) {
                        adapter.choosedMember = viewHolder.getAdapterPosition() - 1;
                    }
                    ITEM_SIZE -= 1; //1개 이상 남아있어서 하나를 삭제했기에 하나 지움.
                    adapter.choosedMember -= 1;
                    adapter.NotifyDataSetChanged(adapter.choosedMember);
                    if(ITEM_SIZE == 0){ //다 지우고 +++만 남았을때에
                        adapter.choosedMember = 0;
                        adapter.NotifyDataSetChanged(adapter.choosedMember);
                    }

                }
            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        });
        helper.attachToRecyclerView(recyclerView);

        ItemTouchHelper helpers = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = 0;
                int swipeFlags = 0;
                Toast.makeText(CourseModify.this,"여기다가 넣으면 플레이스정보가 뾰오옹~", Toast.LENGTH_LONG).show();
                Log.d("Move Ready: ","getMovementFlags" + dragFlags);
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }

            @Override
            public boolean isLongPressDragEnabled() {
                return true;
            }
        });
        helpers.attachToRecyclerView(itemview);
    }


    public void ChangeData(Item item){
        if(adapter.choosedMember == datas.size()-1&&datas.size()<5){ //5개가 아직 아닐경우 위치에다가 추가만함.
            Log.d("ChangeData : ","5개 adapter : "+adapter.choosedMember+"  datas Size : "+datas.size());
            adapter.choosedMember+=1; //새로추가하면서 +++로 가게하기 위해
            addData(item);
        }
        else { //5개일경우 자리만 바꿈
            if(datas.size()<adapter.choosedMember){ //하다보니 계산이 안맞아서 추가.
                adapter.choosedMember-=1;
            }
            Log.d("ChangeData : ","5개 아님 adapter : "+adapter.choosedMember+"   datas Size"+datas.size());
            datas.remove(adapter.choosedMember);
            adapter.notifyItemRemoved(adapter.choosedMember);
            datas.add(adapter.GetChoosedMemeber(), item);
            adapter.notifyItemInserted(adapter.choosedMember);
        }
    }

    public void addData(Item item){
        datas.add(ITEM_SIZE,item);
        adapter.notifyItemInserted(ITEM_SIZE);
        ITEM_SIZE += 1;
        if(datas.size() == 6){
            datas.remove(5);
            adapter.notifyItemRemoved(6);
        }
    }

    private void setData() {
         datas = new ArrayList<>();
        Item[] item = new Item[ITEM_SIZE];
        for(int i = 0; i<ITEM_SIZE;i++){
            item[i] = new Item(R.drawable.kakao_default_profile_image,"#"+i);
            datas.add(item[i]);
        }
        Log.d("data : ", String.valueOf(datas.size()));
        if(datas.size() < 5){
            datas.add(new Item(R.drawable.kakao_default_profile_image,"+++"));
        }
    }

    private void setItemData(){
        items = new ArrayList<>();
        Item[] item = new Item[10];
        for(int i = 0; i<10;i++){
            item[i] = new Item(R.drawable.kakao_default_profile_image,"#"+i);
            items.add(item[i]);
        }
    }

    private void initView(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        adapter = new RecyclerAdapter(getApplicationContext(), datas,R.layout.modify_course);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        itemview = (RecyclerView)findViewById(R.id.Itemview);
        iadapter = new ItemAdapter(this,items,R.layout.modify_course,recyclerView);
        LinearLayoutManager layoutManagers = new LinearLayoutManager(getApplicationContext());
        itemview.setLayoutManager(layoutManagers);
        itemview.setAdapter(iadapter);
    }
}

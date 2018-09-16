package com.ensharp.seoul.seoultheplace.Course;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressLint("ValidFragment")
public class CourseModifyFragment extends Fragment {

    View view;
    public int ITEM_SIZE = 0;
    List<PlaceVO> datas;
    List<PlaceVO> items;
    private RecyclerAdapter adapter;
    private ItemAdapter iadapter;
    RecyclerView recyclerView;
    RecyclerView itemview;

    @SuppressLint("ValidFragment")
    public CourseModifyFragment(List<PlaceVO> list){
        this.datas = list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
        setItemData();
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.modify_course, container, false);

        initView();
        CheckData("CreateView");
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                PlaceVO item = items.get(viewHolder.getAdapterPosition());
                int swipeFlags = 0;
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN ;
                if(ITEM_SIZE > 0 &&!item.getName().equals("+")) { //+++밖에 안남았을때를 뺀다.
                    swipeFlags = ItemTouchHelper.LEFT;
                }
                CheckData("TouchDataName : "+item.getName());
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                Collections.swap(datas, viewHolder.getAdapterPosition(), target.getAdapterPosition());
                adapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                adapter.choosedMember = target.getAdapterPosition();
                CheckData("Move");
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if(viewHolder.getAdapterPosition()!=adapter.getItemCount()) { //itemCount 는 +++까지 합친 갯수. ITEM_SIZE는 +++뺀 갯수
                    datas.remove(viewHolder.getAdapterPosition());
                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    ITEM_SIZE -= 1; //1개 이상 남아있어서 하나를 삭제했기에 하나 지움.
                    adapter.choosedMember -= 1;
                    Log.d("Move : ", "onSwiped, ITEM_SIZE : " + ITEM_SIZE);
                    if (ITEM_SIZE == 4) { //+++가 2개가 생김 아놔 ㅡㅡ;;
                        AddPlusBox();
                    }
                    //}
                    if (adapter.choosedMember == viewHolder.getAdapterPosition()) {
                        adapter.choosedMember = viewHolder.getAdapterPosition() - 1;
                    }
                    adapter.NotifyDataSetChanged(adapter.choosedMember);
                    if(adapter.choosedMember < 0) { //음수로 가는걸 막기위해
                        adapter.choosedMember = 0;
                    }
                    if(ITEM_SIZE < 0){ //음수로 가는걸 막기위해
                        ITEM_SIZE = 0;
                    }
                    if(ITEM_SIZE == 0){ //다 지우고 +++만 남았을때에
                        adapter.choosedMember = 0;
                        adapter.NotifyDataSetChanged(adapter.choosedMember);
                    }

                }
                CheckData("SwipeDATA");
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
                MainActivity mActivity = (MainActivity)getActivity();
                mActivity.changeFragment("j111",1);
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
        return  view;
    }


        public void ChangeData(PlaceVO item){
        if(adapter.choosedMember == datas.size()-1&&datas.size()<5){ //5개가 아직 아닐경우 위치에다가 추가만함.
            adapter.choosedMember+=1; //새로추가하면서 +++로 가게하기 위해
            addData(item);
        }
        else { //5개일경우 자리만 바꿈
            if(datas.size()<adapter.choosedMember){ //하다보니 계산이 안맞아서 추가.
                adapter.choosedMember-=1;
            }
            datas.remove(adapter.choosedMember);
            adapter.notifyItemRemoved(adapter.choosedMember);
            ITEM_SIZE +=1;
            datas.add(adapter.GetChoosedMemeber(), item);
            adapter.notifyItemInserted(adapter.choosedMember);
        }
        CheckData("ChangeData");
    }

    public void addData(PlaceVO item){
        datas.add(ITEM_SIZE,item);
        adapter.notifyItemInserted(ITEM_SIZE);
        ITEM_SIZE += 1;
        if(datas.size() == 6){
            datas.remove(5);
            adapter.notifyItemRemoved(5);
        }
    }

    public void CheckData(String TAG){
        Toast.makeText(getActivity(),TAG + "   adapter : "+adapter.choosedMember+"   datas Size : "+datas.size() + "   ITEM_SIZE : " + ITEM_SIZE,Toast.LENGTH_LONG).show();
    }

    private void setData() {
        ITEM_SIZE = datas.size();
        if(ITEM_SIZE < 5) {
            AddPlusBox();
        }
    }
    private void AddPlusBox(){
        String[] imageURL= {"data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAA1BMVEX///+nxBvIAAAASElEQVR4nO3BgQAAAADDoPlTX+AIVQEAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAADwDcaiAAFXD1ujAAAAAElFTkSuQmCC",null,null};
        datas.add(new PlaceVO(null,"+",null,imageURL,null,null,null,null,0,null,null,null));
    }

    private void setItemData(){
        items = datas;
    }

    private void initView(){
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        adapter = new RecyclerAdapter(getActivity(), datas,R.layout.modify_course);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        itemview = (RecyclerView)view.findViewById(R.id.Itemview);
        iadapter = new ItemAdapter(this,items,R.layout.modify_course,recyclerView);
        LinearLayoutManager layoutManagers = new LinearLayoutManager(getActivity());
        itemview.setLayoutManager(layoutManagers);
        itemview.setAdapter(iadapter);
    }
}

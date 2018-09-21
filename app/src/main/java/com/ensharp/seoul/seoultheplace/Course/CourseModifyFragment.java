package com.ensharp.seoul.seoultheplace.Course;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;
import com.ensharp.seoul.seoultheplace.UIElement.CustomAnimationDialog;

import org.json.JSONArray;
import org.json.JSONException;

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
    Button saveBtn;
    Button searchBtn;
    MainActivity mActivity;
    EditText searchData;

    @SuppressLint("ValidFragment")
    public CourseModifyFragment(List<PlaceVO> list) {
        this.datas = list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
        setItemData(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.modify_course, container, false);
        initView();

        CheckData("CreateView");
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                CheckData("TouchDataName : " + viewHolder.getAdapterPosition());
                PlaceVO item = datas.get(viewHolder.getAdapterPosition());
                int swipeFlags = 0;
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                if (ITEM_SIZE > 0 && !item.getName().equals("+")) { //+++밖에 안남았을때를 뺀다.
                    swipeFlags = ItemTouchHelper.LEFT;
                }
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
                if (viewHolder.getAdapterPosition() != adapter.getItemCount()) { //itemCount 는 +++까지 합친 갯수. ITEM_SIZE는 +++뺀 갯수
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
                    if (adapter.choosedMember < 0) { //음수로 가는걸 막기위해
                        adapter.choosedMember = 0;
                    }
                    if (ITEM_SIZE < 0) { //음수로 가는걸 막기위해
                        ITEM_SIZE = 0;
                    }
                    if (ITEM_SIZE == 0) { //다 지우고 +++만 남았을때에
                        adapter.choosedMember = 0;
                        adapter.NotifyDataSetChanged(adapter.choosedMember);
                    }
                    setItemData(null);
                    ChangeItemData();
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
                PlaceVO item = items.get(viewHolder.getAdapterPosition());
                int dragFlags = 0;
                int swipeFlags = 0;
                mActivity.changeToPlaceFragment(item.getCode());
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
        return view;
    }

    public void ChangeData(PlaceVO item) {
        if(datas.get(adapter.choosedMember).getName().equals("+")){
            addData(item,adapter.choosedMember); //+ 있는 자리에 추가하기위해
        } else { //5개일경우 자리만 바꿈
            if (datas.size() < adapter.choosedMember) { //하다보니 계산이 안맞아서 추가.
                adapter.choosedMember -= 1;
            }
            datas.remove(adapter.choosedMember);
            adapter.notifyItemRemoved(adapter.choosedMember);
            datas.add(adapter.GetChoosedMemeber(), item);
            adapter.notifyItemInserted(adapter.choosedMember);
        }
        CheckData("ChangeData");
    }

    public void addData(PlaceVO item,int position) {
        datas.add(position,item);
        adapter.notifyItemInserted(ITEM_SIZE);
        ITEM_SIZE += 1;
        if (datas.size() == 6) { //5개가 넘어서 +를 삭제
            datas.remove(5);
            adapter.notifyItemRemoved(5);
        }
        else{
            adapter.choosedMember +=1; //focus를 +로 넘기기 위해
        }
    }

    public void CheckData(String TAG){
        Log.e("TEST_TEST_ : ",TAG + "   adapter : "+adapter.choosedMember+"   datas Size : "+datas.size() + "   ITEM_SIZE : " + ITEM_SIZE);
    }

    private void setData() {
        ITEM_SIZE = datas.size();
        if(ITEM_SIZE < 5) {
            AddPlusBox();
        }
    }
    private void AddPlusBox(){
        datas.add(new PlaceVO(null,"+",null,null,null));
    }

    public void setItemData(PlaceVO item){
        items = new ArrayList<PlaceVO>();
        DAO dao = new DAO();
        JSONArray jsonArray = dao.AllPlaceDownload();
        if(item==null) { //그냥 생성할때
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    PlaceVO mplace = dao.getPlaceData(jsonArray.getJSONObject(i).getString("Code"));
                    if(CheckInData(mplace))
                        continue;
                    items.add(mplace);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            for(int i = 0; i<jsonArray.length(); i++){
                boolean setData = false;
                try {
                    PlaceVO instantVO = dao.getPlaceData(jsonArray.getJSONObject(i).getString("Code"));
                    if(CheckInData(instantVO)) //이미 있는곳은 안만듬
                        continue;
                    instantVO.setDistance(LocationDistance.distance(instantVO.getCoordinate_x()
                            ,instantVO.getCoordinate_y()
                            ,item.getCoordinate_x()
                            ,item.getCoordinate_y()
                            ,"meter"));
                    if(items.size() ==0) { //0이면 일단 넣어준다.
                        items.add(instantVO);
                    }
                    else {
                        for(int j = 0; j<items.size();j++){ //거리순으로 배열 생성
                            if(items.get(j).getDistance() > instantVO.getDistance()){
                                items.add(j,instantVO);
                                setData = true;
                                break;
                            }
                        }
                        if(!setData && items.size()<=10){ //제일 사이즈가 큼 10개 이하일때만
                            items.add(instantVO);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ChangeItemData();
        }
    }

    //이미 데이터에 넣어놓은 것들은 뜨지않게 하기 위해.
    public boolean CheckInData(PlaceVO insertItemVO){
        for(int i = 0; i<datas.size();i++){
            if(datas.get(i).getName().equals(insertItemVO.getName()))
                return true;
        }
        return false;
    }

    private void initView(){
        mActivity = (MainActivity)getActivity();
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerview);
        adapter = new RecyclerAdapter(getActivity(), datas,R.layout.modify_course,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        itemview = (RecyclerView)view.findViewById(R.id.Itemview);
        ChangeItemData();

        saveBtn = (Button)view.findViewById(R.id.modify_save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ITEM_SIZE > 0&&ITEM_SIZE<=5){ //아이템이 있을경우.
                    mActivity.SetSaveData(GetPlaceCode(datas));
                }
                else{ //아무것도 없음.
                    mActivity.changeCourseViewFragment(datas);
                }
            }
        });
        searchData = (EditText)view.findViewById(R.id.search_Place);
        searchBtn = (Button)view.findViewById(R.id.saveSearch);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchData.getText().length()<2){
                    Toast.makeText(getContext(),"검색어가 너무 짧습니다.",Toast.LENGTH_LONG).show();
                    return;
                }
                else if(searchData.getText().length()>20){
                    Toast.makeText(getContext(),"검색어가 너무 깁니다.",Toast.LENGTH_LONG).show();
                }
                else{
                    DAO dao = new DAO();
                    items = dao.searchPlace(String.valueOf(searchData.getText()));
                    if(items == null){
                        Toast.makeText(getContext(),"검색된 플레이스가 없습니다.",Toast.LENGTH_LONG).show();
                        setItemData(null);
                    }
                    else{
                        ChangeItemData();
                    }
                }
            }
        });
    }

    public void ChangeItemData(){
        iadapter = new ItemAdapter(this,items,R.layout.modify_course,recyclerView);
        LinearLayoutManager layoutManagers = new LinearLayoutManager(getActivity());
        itemview.setLayoutManager(layoutManagers);
        itemview.setAdapter(iadapter);
    }

    public String[] GetPlaceCode(List<PlaceVO> datas){
        String[] codes = new String[]{null,null,null,null,null};
        for(int i=0;i<datas.size();i++){
            if(!datas.get(i).getName().equals("+"))
                codes[i]=datas.get(i).getCode();
        }
        return codes;
    }


}

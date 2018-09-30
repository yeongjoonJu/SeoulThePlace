package com.ensharp.seoul.seoultheplace.Course;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.Fragments.PlaceFragment;
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
    List<PlaceVO> originDatas = null;
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
    String touchName = "";

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
                mActivity.changeToPlaceFragment(item.getCode(), PlaceFragment.DURING_EDITTING_COURSE);
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
        } else { //플레이스 일경우 자리만 바꿈
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
        setItemData(item);
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
        CustomAnimationDialog ca = new CustomAnimationDialog(getActivity());
        ca.show();
        items = new ArrayList<PlaceVO>();
        if(originDatas == null) {
            DAO dao = new DAO();
            JSONArray jsonArray = dao.AllPlaceDownload();
            originDatas = new ArrayList<PlaceVO>();
            for(int i = 0 ; i < jsonArray.length();i++){
                try {
                    originDatas.add(dao.getPlaceData(jsonArray.getJSONObject(i).getString("Code")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if(item==null) { //그냥 생성할때
            for (int i = 0; i < originDatas.size(); i++) {
                if (CheckInData(originDatas.get(i)))
                    continue;
                originDatas.get(i).setDistance(0);
                if (items.size() < 10)
                    items.add(originDatas.get(i));
            }
        }
        else{
            SetTouchName(item.getName());
            for(int i = 0; i<originDatas.size(); i++) {
                boolean setData = false;
                if (CheckInData(originDatas.get(i))) //이미 있는곳은 안만듬
                    continue;
                originDatas.get(i).setDistance(LocationDistance.distance(originDatas.get(i).getCoordinate_x()
                        , originDatas.get(i).getCoordinate_y()
                        , item.getCoordinate_x()
                        , item.getCoordinate_y()
                        , "meter"));
                if (items.size() == 0) { //0이면 일단 넣어준다.
                    items.add(originDatas.get(i));
                } else {
                    for (int j = 0; j < items.size(); j++) { //거리순으로 배열 생성
                        if (items.get(j).getDistance() > originDatas.get(i).getDistance()) {
                            items.add(j, originDatas.get(i));
                            setData = true;
                            break;
                        }
                    }
                    if (!setData && items.size() <= 10) { //제일 사이즈가 큼 10개 이하일때만
                        items.add(originDatas.get(i));
                    }
                    if (items.size() > 10) {
                        items.remove(10);
                    }
                }
            }
            ChangeItemData();
        }
        ca.dismiss();
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
                    mActivity.getSupportFragmentManager().popBackStackImmediate();
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
                        ChangeItemData();
                    }
                    else{
                        ChangeItemData();
                    }
                }

                // 키보드 숨김
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(searchData.getWindowToken(), 0);
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

    public void SetTouchName(String name){
        this.touchName = name;
    }
    public String getTouchName(){
        return touchName;
    }


}

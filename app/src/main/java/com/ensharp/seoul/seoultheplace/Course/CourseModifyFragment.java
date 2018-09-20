package com.ensharp.seoul.seoultheplace.Course;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

import com.ensharp.seoul.seoultheplace.CourseVO;
import com.ensharp.seoul.seoultheplace.DAO;
import com.ensharp.seoul.seoultheplace.MainActivity;
import com.ensharp.seoul.seoultheplace.PlaceVO;
import com.ensharp.seoul.seoultheplace.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    Button saveBtn ;
    MainActivity mActivity;
    EditText searchData;

    @SuppressLint("ValidFragment")
    public CourseModifyFragment(List<PlaceVO> list){
        this.datas = list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
        setItemData(null);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.modify_course, container, false);
        initView();

        CheckData("CreateView");
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                CheckData("TouchDataName : "+viewHolder.getAdapterPosition());
                PlaceVO item = datas.get(viewHolder.getAdapterPosition());
                int swipeFlags = 0;
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN ;
                if(ITEM_SIZE > 0 &&!item.getName().equals("+")) { //+++밖에 안남았을때를 뺀다.
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
                if(viewHolder.getAdapterPosition()!=adapter.getItemCount()) { //itemCount 는 +++까지 합친 갯수. ITEM_SIZE는 +++뺀 갯수
                    datas.remove(viewHolder.getAdapterPosition());
                    adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    ITEM_SIZE -= 1; //1개 이상 남아있어서 하나를 삭제했기에 하나 지움.
                    adapter.choosedMember -= 1;
                    Log.d("Move : ", "onSwiped, ITEM_SIZE : " + ITEM_SIZE);
                    if (ITEM_SIZE < 4) { //+++가 2개가 생김 아놔 ㅡㅡ;;
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
                PlaceVO item = items.get(viewHolder.getAdapterPosition());
                int dragFlags = 0;
                int swipeFlags = 0;
                mActivity.changeFragment(item.getCode());
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
            ITEM_SIZE +=1;
            addData(item);
        }
        else { //5개일경우 자리만 바꿈
            if(datas.size()<adapter.choosedMember){ //하다보니 계산이 안맞아서 추가.
                adapter.choosedMember-=1;
            }
            datas.remove(adapter.choosedMember);
            adapter.notifyItemRemoved(adapter.choosedMember);
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
        if(item==null) {
            try {
                for (int i = 0; i < jsonArray.length(); i++) {
                    PlaceVO mplace = dao.getPlaceData(jsonArray.getJSONObject(i).getString("Code"));
                    items.add(mplace);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            for(int i = 0; i<jsonArray.length(); i++){
                JSONObject jsonObject;
                boolean setData = false;
                try {
/*                  jsonObject = jsonArray.getJSONObject(i);  //dao 에서 이미지 url을 하나만 줘서 무의함..
                    String coordinate_x = jsonObject.getString("Coordinate_X");
                    String coordinate_y = jsonObject.getString("Coordinate_Y");
                    String name = jsonObject.getString("Name");
                    String code = jsonObject.getString("Code");
                    String url = jsonObject.getString("Image1");
                    PlaceVO instantVO = new PlaceVO(code,name,url,coordinate_x,coordinate_y);*/
                    PlaceVO instantVO = dao.getPlaceData(jsonArray.getJSONObject(i).getString("Code"));
                    if(item.getName().equals(instantVO.getName())) //이미 있는곳은 안만듬
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
                if(ITEM_SIZE > 0&&ITEM_SIZE<5){
                    for(int i = 0 ; i <= datas.size();i++){
                        PlaceVO item = datas.get(i);
                        if(item.getName().equals("+")){
                            datas.remove(i);
                        }
                    }
                    mActivity.changeCourseViewFragment(datas);
                    Toast.makeText(mActivity,"이거누르면 저장",Toast.LENGTH_LONG).show();
                }
                else if(ITEM_SIZE==5){
                    Toast.makeText(mActivity,"이거누르면 저장",Toast.LENGTH_LONG).show();
                }
            }
        });
        searchData = (EditText)view.findViewById(R.id.search_Place);
        searchData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //여기다가 넣으면 됨.
            }
        });
    }

    public void ChangeItemData(){
        iadapter = new ItemAdapter(this,items,R.layout.modify_course,recyclerView);
        LinearLayoutManager layoutManagers = new LinearLayoutManager(getActivity());
        itemview.setLayoutManager(layoutManagers);
        itemview.setAdapter(iadapter);
    }
}
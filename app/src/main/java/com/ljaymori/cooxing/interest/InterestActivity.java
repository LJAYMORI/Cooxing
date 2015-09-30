package com.ljaymori.cooxing.interest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.result.IngredientResult;
import com.ljaymori.cooxing.auth.result.UserResult;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.common.MyData;
import com.ljaymori.cooxing.common.manager.NetworkManager;
import com.ljaymori.cooxing.common.vo.IngredientVO;
import com.ljaymori.cooxing.main.MainActivity;
import com.ljaymori.cooxing.tutorial.TutorialActivity;

import java.util.ArrayList;

public class InterestActivity extends ActionBarActivity implements View.OnClickListener {

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_HEADER = 1;

    private RecyclerView recyclerView;
    private GridLayoutManager mGridManager;
    private InterestAdapter mAdapter;

    private TextView tvNotice;
    private TextView tvComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        tvNotice = (TextView) findViewById(R.id.text_notice_interest);
        tvComplete = (TextView) findViewById(R.id.text_complete_interest);
        tvComplete.setOnClickListener(this);

        mGridManager = new GridLayoutManager(this, 3);
        mGridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                if (position == 0 || position == 16) {
                    return 3;
                } else {
                    return 1;
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_interest);
        recyclerView.setLayoutManager(mGridManager);

        mAdapter = new InterestAdapter(this);
        initData();

        recyclerView.setAdapter(mAdapter);

    }

    private void initData() {

        NetworkManager.getInstance().getIngredients(this, new NetworkManager.OnResultListener<IngredientResult>() {
            @Override
            public void onSuccess(IngredientResult data) {
                if (data.result.equals(CooxingConstant.SUCCESS)) {

                    ArrayList<IngredientVO> datas = data.datas;

                    ArrayList<InterestItemData> list = new ArrayList<InterestItemData>();

                    InterestItemData basicHeader = new InterestItemData();
                    basicHeader.setType(TYPE_HEADER);
                    basicHeader.setName(getString(R.string.interest_basic_ingredient));
                    list.add(0, basicHeader);

                    for (IngredientVO vo : datas) {
                        InterestItemData itemData = new InterestItemData();
                        itemData.set_id(vo.get_id());
                        itemData.setImage(vo.getImage());

                        itemData.setType(TYPE_ITEM);

                        list.add(itemData);
                    }


                    InterestItemData popularHeader = new InterestItemData();
                    popularHeader.setType(TYPE_HEADER);
                    popularHeader.setName(getResources().getString(R.string.interest_popular_ingredient));
                    list.add(16, popularHeader);


                    InterestItemData footer = new InterestItemData();
                    footer.setType(TYPE_HEADER);
                    footer.setName("");
                    list.add(footer);

                    mAdapter.initItems(list);


                } else if (data.result.equals(CooxingConstant.FAIL)) {
                    Toast.makeText(InterestActivity.this, data.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(InterestActivity.this, code + "", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.text_complete_interest) {
            SparseBooleanArray array = mAdapter.getBooleanArray();
            int size = array.size();
            if (size < 5) {
                Toast.makeText(InterestActivity.this, getString(R.string.interest_message_minimum_five), Toast.LENGTH_SHORT).show();
                return;
            }

            ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < size; i++) {
//                Log.i("boolArray position - ", array.keyAt(i) + "");
                list.add(mAdapter.getItems().get(array.keyAt(i)).get_id());
            }

            NetworkManager.getInstance().selectIngredients(this, list, new NetworkManager.OnResultListener<UserResult>() {
                @Override
                public void onSuccess(UserResult data) {
                    if(data.result.equals(CooxingConstant.SUCCESS)) {
                        MyData.getInstance().setMyData(data.data);

                        startActivities(new Intent[]{
                                new Intent(InterestActivity.this, MainActivity.class),
                                new Intent(InterestActivity.this, TutorialActivity.class)
                        });
                        finish();

                    } else if(data.result.equals(CooxingConstant.FAIL)) {
                        Toast.makeText(InterestActivity.this, data.message, Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFail(int code) {
                    Toast.makeText(InterestActivity.this, code + "", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}

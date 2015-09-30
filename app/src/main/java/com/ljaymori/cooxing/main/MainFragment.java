package com.ljaymori.cooxing.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.result.RecipeListResult;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.common.manager.NetworkManager;
import com.ljaymori.cooxing.common.vo.RecipeVO;
import com.ljaymori.cooxing.write.WriteActivity;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;

public class MainFragment extends Fragment implements View.OnClickListener {

    public static final float MAX_SCROLL_Y = 500;

    private RecyclerView recyclerView;
    private MainAdapter mAdapter;

    private View viewWrite;
    private float writeY = 0;

    private PullToRefreshView refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        viewWrite = view.findViewById(R.id.view_write_recipe_main);
        viewWrite.setOnClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_main);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recView, int dx, int dy) {
                super.onScrolled(recView, dx, dy);

                float y = writeY + (dy / 2);
                if (y > MAX_SCROLL_Y) {
                    writeY = MAX_SCROLL_Y;
                } else if (y < 0) {
                    writeY = 0;
                } else {
                    writeY = y;
                }

                viewWrite.setTranslationY(writeY);
            }
        });

        mAdapter = new MainAdapter(getActivity());

        recyclerView.setAdapter(mAdapter);

        refreshLayout = (PullToRefreshView) view.findViewById(R.id.refreshLayout_main);
        refreshLayout.setRefreshStyle(PullToRefreshView.STYLE_COOXING_2);
        refreshLayout.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        refreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });

//        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshLayout_main);
//        refreshLayout.setColorSchemeResources(R.color.cooxing_main_color);
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        initData();
//                        refreshLayout.setRefreshing(false);
//                    }
//                }, 1000);
//            }
//        });


        initData();

        return view;
    }

    public void initData() {
//        final LoadingDialogFragment dialog = new LoadingDialogFragment();
//        dialog.setCancelable(false);
//        dialog.show(getChildFragmentManager(), LoadingDialogFragment.DIALOG_LOADING);

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                NetworkManager.getInstance().recipeList(getActivity(), new NetworkManager.OnResultListener<RecipeListResult>() {
                    @Override
                    public void onSuccess(RecipeListResult data) {
                        if (data.result.equals(CooxingConstant.SUCCESS)) {
                            mAdapter.addAll(data.datas);

                        } else if (data.result.equals(CooxingConstant.FAIL)) {
                            Toast.makeText(getActivity(), data.message, Toast.LENGTH_SHORT).show();
                        }
//                        dialog.dismiss();
                    }

                    @Override
                    public void onFail(int code) {
                        Toast.makeText(getActivity(), code + "", Toast.LENGTH_SHORT).show();
//                        dialog.dismiss();
                    }
                });
            }
        });

    }

    public void addNewRecipe() {

    }

    public void addOldRecipe() {

    }


    private ArrayList<StepItemData> initStep() {
        ArrayList<StepItemData> list = new ArrayList<StepItemData>();
        for (int i = 0; i < 10; i++) {
            StepItemData sd = new StepItemData();
            sd.setPicturePath(R.drawable.ic_launcher + "");
            sd.setPosition(i);

            list.add(sd);
        }

        return list;
    }

    public MainAdapter getMainAdapter() {
        return mAdapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_write_recipe_main: {

                startActivity(new Intent(getActivity(), WriteActivity.class));
                break;
            }
        }
    }

    public void updateRecipe(RecipeVO recipeVO) {
        ArrayList<RecipeVO> list = mAdapter.getItems();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            RecipeVO vo = list.get(i);
            if (vo.get_id().equals(recipeVO.get_id())) {
                mAdapter.update(i, recipeVO);
                break;
            }
        }
    }

}

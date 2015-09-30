package com.ljaymori.cooxing.like;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.result.RecipeListResult;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.common.dialog.LoadingDialogFragment;
import com.ljaymori.cooxing.common.manager.NetworkManager;
import com.ljaymori.cooxing.common.utils.FontUtils;
import com.ljaymori.cooxing.common.vo.RecipeVO;

import java.util.ArrayList;

public class LikeRecipeFragment extends Fragment {

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private LikeRecipeAdapter mAdapter;

    private TextView tvNoRecipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_like_receipe, container, false);

        tvNoRecipe = (TextView) v.findViewById(R.id.text_no_recipe_like);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_like);

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        mAdapter = new LikeRecipeAdapter(getActivity());
        recyclerView.setAdapter(mAdapter);

        setFonts();
        initData();

        return v;
    }

    private void setFonts() {
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvNoRecipe);
    }

    private void initData() {
        final LoadingDialogFragment dialog = new LoadingDialogFragment();
        dialog.setCancelable(false);
        dialog.show(getChildFragmentManager(), LoadingDialogFragment.DIALOG_LOADING);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NetworkManager.getInstance().cookList(getActivity(), new NetworkManager.OnResultListener<RecipeListResult>() {
                    @Override
                    public void onSuccess(RecipeListResult data) {
                        if (data.result.equals(CooxingConstant.SUCCESS)) {
                            ArrayList<LikeRecipeData> list = new ArrayList<LikeRecipeData>();
                            for (RecipeVO vo : data.datas) {
                                LikeRecipeData ld = new LikeRecipeData();
                                ld.setRecipeID(vo.get_id());
                                ld.setRecipeName(vo.getTitle());
                                ld.setImageURL(vo.getImages().get(vo.getImages().size() - 1));

                                list.add(0, ld);
                            }

                            mAdapter.addAll(list);
                            if (mAdapter.getItemCount() > 0) {
                                if (tvNoRecipe.getVisibility() == View.VISIBLE) {
                                    tvNoRecipe.setVisibility(View.GONE);
                                }

                            } else {
                                if (tvNoRecipe.getVisibility() == View.GONE) {
                                    tvNoRecipe.setVisibility(View.VISIBLE);
                                }
                            }

                        } else if (data.result.equals(CooxingConstant.FAIL)) {
                            Toast.makeText(getActivity(), data.message, Toast.LENGTH_SHORT).show();
                        }

                        dialog.dismiss();
                    }

                    @Override
                    public void onFail(int code) {
                        Toast.makeText(getActivity(), code + "", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        }, 500);
    }
}

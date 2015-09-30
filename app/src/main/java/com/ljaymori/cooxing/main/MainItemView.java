package com.ljaymori.cooxing.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.auth.NetworkConstant;
import com.ljaymori.cooxing.auth.result.RecipeResult;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.common.MyData;
import com.ljaymori.cooxing.common.manager.NetworkManager;
import com.ljaymori.cooxing.common.utils.FontUtils;
import com.ljaymori.cooxing.common.vo.RecipeVO;
import com.ljaymori.cooxing.detail.DetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainItemView extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

    private RecipeVO mainItemData;

    private ImageView ivMain;
    private TextView tvRecipeName;
    private TextView tvLike;

    private View viewUser;
    private ImageView ivUser;
    private TextView tvNickname;

    private View viewLikeCount;
    private TextView tvLikeCount;

    private View viewReplyCount;
    private TextView tvReplyCount;


    private SwipeLayout swipeLayout;
    private RecyclerView recyclerView;
    private StepAdapter stepAdapter;
    private Context mContext;

    private View viewStrap;
    private TextView tvTopStrap;
    private TextView tvBottomStrap;

    private TextView tvShadow;

    private float strapWidth = 0.0f;
    private float strapItemHeight = 0.0f;

    public MainItemView(View itemView) {
        super(itemView);

        swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipeLayout_main_item);
        swipeLayout.setOnClickListener(this);

        ivMain = (ImageView) swipeLayout.findViewById(R.id.image_main_item);
        ivMain.setOnLongClickListener(this);
        ivMain.setOnClickListener(this);

        tvRecipeName = (TextView) itemView.findViewById(R.id.text_recipe_name_main_item);

        tvLike = (TextView) itemView.findViewById(R.id.text_like_main_item);
        tvLike.setOnClickListener(this);

        viewUser = itemView.findViewById(R.id.view_user_main_item);
        viewUser.setOnClickListener(this);

        ivUser = (ImageView) itemView.findViewById(R.id.image_user_picture_main_item);
        tvNickname = (TextView) itemView.findViewById(R.id.text_nickname_main_item);

        viewLikeCount = itemView.findViewById(R.id.view_like_count_main_item);
        tvLikeCount = (TextView) itemView.findViewById(R.id.text_like_count_main_item);

        viewReplyCount = itemView.findViewById(R.id.view_reply_count_main_item);
        tvReplyCount = (TextView) itemView.findViewById(R.id.text_reply_count_main_item);

        recyclerView = (RecyclerView) swipeLayout.findViewById(R.id.recyclerView_main_item);

        viewStrap = itemView.findViewById(R.id.view_strap_main_item);
        tvTopStrap = (TextView) itemView.findViewById(R.id.text_top_strap_main_item);
        tvBottomStrap = (TextView) itemView.findViewById(R.id.text_bottom_strap_main_item);

        tvShadow = (TextView) itemView.findViewById(R.id.text_shadow_main_item);
        tvShadow.setAlpha(0.0f);

        setFonts();
    }
    private void setFonts() {
        FontUtils.setTextViewFonts(FontUtils.TYPE_BARUN_BOLD, tvRecipeName, tvNickname);
        FontUtils.setTextViewFonts(FontUtils.TYPE_ROBOTO_NOMAL, tvLikeCount, tvReplyCount);
    }

    private boolean isFirst = true;
    public void setMainItemView(RecipeVO vo) {
        mainItemData = vo;

        Picasso.with(mContext)
                .load(NetworkConstant.HTTP_URL + vo.getImages().get(vo.getImages().size() - 1))
                .centerCrop()
                .placeholder(R.drawable.image_placeholder)
                .fit()
                .into(ivMain);


        tvRecipeName.setText(vo.getTitle());
        drawLikeButton(vo.get_id());

        Picasso.with(mContext)
                .load(NetworkConstant.HTTP_URL + vo.getUser().getProfileImage())
                .placeholder(R.drawable.image_placeholder)
                .fit()
                .into(ivUser);
//        ImageLoader.getInstance().displayImage(NetworkConstant.HTTP_URL + vo.getUser().getProfileImage(), ivUser);

        tvNickname.setText(vo.getUser().getNickname());

        tvLikeCount.setText(vo.getBookmarkers().size() + "");
        tvReplyCount.setText(vo.getComments().size() + "");


        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);

        stepAdapter = new StepAdapter(mContext);

        recyclerView.setAdapter(stepAdapter);

        stepAdapter.addAll(initSteps(vo.getImages(), vo.getSteps()));

        swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Right, swipeLayout.findViewById(R.id.view_bottom_wrapper_main_item));
        swipeLayout.addRevealListener(R.id.view_bottom_wrapper_main_item, new SwipeLayout.OnRevealListener() {
            @Override
            public void onReveal(View view, SwipeLayout.DragEdge dragEdge, float fraction, int distance) {

                viewStrap.setTranslationX(strapWidth * fraction);

                tvTopStrap.setRotation(-45 * fraction);
                tvBottomStrap.setRotation(-135 * fraction);

                tvTopStrap.setTranslationY(strapItemHeight * fraction);
                tvBottomStrap.setTranslationY(-strapItemHeight * fraction);

                tvShadow.setAlpha(fraction);
            }
        });

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

//        if (isFirst) {
//            isFirst = false;
//            recyclerView.scrollToPosition(stepAdapter.getItemCount() - 1);
//            swipeLayout.open();
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    recyclerView.smoothScrollToPosition(0);
//                    swipeLayout.open();
//                }
//            }, 100);
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    recyclerView.smoothScrollToPosition(stepAdapter.getItemCount() - 1);
//                    swipeLayout.open();
//                }
//            }, 200);
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    recyclerView.smoothScrollToPosition(0);
//                    swipeLayout.close();
//                }
//            }, 300);
//
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    viewProgress.setVisibility(View.GONE);
//                }
//            }, 500);
//
//        } else {
//            swipeLayout.close();
//        }
        swipeLayout.close();
    }

    private ArrayList<StepItemData> initSteps(ArrayList<String> images, ArrayList<String> steps) {
        ArrayList<StepItemData> list = new ArrayList<StepItemData>();

        int size = images.size();
        for (int i = 0; i < size; i++) {
            StepItemData sd = new StepItemData();
            sd.setPosition(i);
            sd.setPicturePath(images.get(i));
            sd.setDesc(steps.get(i));

            list.add(sd);
        }

        return list;
    }

    public void drawLikeButton(String recipe_id) {
        if(MyData.getInstance().getBookmarks().contains(recipe_id)) {
            tvLike.setBackgroundResource(R.drawable.btn_like);
        } else {
            tvLike.setBackgroundResource(R.drawable.btn_unlike);
        }
    }

    public void setContext(Context context) {
        mContext = context;

        strapWidth = context.getResources().getDimension(R.dimen.main_item_strap_right_margin);
        strapItemHeight = context.getResources().getDimension(R.dimen.main_item_strap_item_top_bottom_margin);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_main_item: {
                redirectDetailActivity();
                break;
            }
            case R.id.view_user_main_item: {
                ((MainActivity) mContext).onClickUser(mainItemData.getUser().get_id());
                break;
            }
            case R.id.text_like_main_item: {
                if (MyData.getInstance().getBookmarks().contains(mainItemData.get_id())) {
                    unlike();
                } else {
                    like();
                }
                break;
            }
        }

    }

    private void like() {
        NetworkManager.getInstance().likeRecipe(mContext, mainItemData.get_id(), new NetworkManager.OnResultListener<RecipeResult>() {
            @Override
            public void onSuccess(RecipeResult data) {
                if (data.result.equals(CooxingConstant.SUCCESS)) {
                    MyData.getInstance().getBookmarks().add(data.data.get_id());
                    if (changeListener != null) {
                        changeListener.onMainItemChanged(data.data);
                    }

                } else if (data.result.equals(CooxingConstant.FAIL)) {
                    Toast.makeText(mContext, data.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(mContext, code + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void unlike() {
        NetworkManager.getInstance().unlikeRecipe(mContext, mainItemData.get_id(), new NetworkManager.OnResultListener<RecipeResult>() {
            @Override
            public void onSuccess(RecipeResult data) {
                if (data.result.equals(CooxingConstant.SUCCESS)) {
                    MyData.getInstance().getBookmarks().remove(data.data.get_id());
                    if (changeListener != null) {
                        changeListener.onMainItemChanged(data.data);
                    }

                } else if (data.result.equals(CooxingConstant.FAIL)) {
                    Toast.makeText(mContext, data.message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFail(int code) {
                Toast.makeText(mContext, code + "", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();
        if (id == R.id.image_main_item) {
            final String[] items = new String[]{
                    mContext.getResources().getString(R.string.main_recipe_hide_dialog_item),
                    mContext.getResources().getString(R.string.main_recipe_share_dialog_item)
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(mContext, items[which], Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }
            });

            builder.create().show();
            return true;
        }
        return false;
    }


    private void redirectDetailActivity() {
        Intent intent = new Intent(mContext, DetailActivity.class);
        intent.putExtra(DetailActivity.RECIPE_ID, mainItemData.get_id());
        ((MainActivity) mContext).startActivityForResult(intent, DetailActivity.DETAIL_REQUEST_CODE);
    }


    public interface OnMainItemChangeListener {
        void onMainItemChanged(RecipeVO vo);
    }
    private OnMainItemChangeListener changeListener;
    public void setOnMainItemChangeListener(OnMainItemChangeListener listener) {
        changeListener = listener;
    }

}

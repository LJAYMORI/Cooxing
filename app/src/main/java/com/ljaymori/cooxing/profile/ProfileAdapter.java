package com.ljaymori.cooxing.profile;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljaymori.cooxing.R;

import java.util.ArrayList;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileParentView> {

    private ArrayList<ProfileData> items = new ArrayList<ProfileData>();
    private Context mContext;
    private String user_id;

    public ProfileAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addAll(ArrayList<ProfileData> list) {
        items.addAll(list);
        notifyDataSetChanged();
    }

    public void initProfileAndTab(ArrayList<ProfileData> list) {
        items.clear();
        addAll(list);
    }

    public void addRecipes(ArrayList<ProfileData> list, String message) {
        ProfileData profile = items.remove(0);

        ProfileData tab = items.remove(0);
        tab.setMessage(message);
        tab.setFocusedTabPosition(((ProfileActivity) mContext).getCurrentTabPosition());

        items.clear();

        items.add(profile);
        items.add(tab);

        items.addAll(list);

        notifyDataSetChanged();
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public ProfileParentView onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ProfileActivity.TYPE_RECIPE) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_profile_recipe, parent, false);

            return new ProfileRecipeView(v);

        } else if (viewType == ProfileActivity.TYPE_USER) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_profile_user_info, parent, false);

            return new ProfileUserView(v);

        } else if (viewType == ProfileActivity.TYPE_TAB) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.item_tab_profile, parent, false);

            return new ProfileTabView(v, user_id);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(ProfileParentView holder, int position) {
        int type = getItemViewType(position);
        if (type == ProfileActivity.TYPE_RECIPE) {
            ((ProfileRecipeView) holder).setRecipeView(items.get(position), mContext);
            ((ProfileRecipeView) holder).setOnClickRecipeViewListener(new ProfileRecipeView.OnClickRecipeViewListener() {
                @Override
                public void onRecipeClick(String id) {
                    ((ProfileActivity) mContext).onClickRecipe(id);
                }
            });

        } else if (type == ProfileActivity.TYPE_USER) {
            ((ProfileUserView) holder).setUserView(items.get(position), mContext);
            ((ProfileUserView) holder).setOnProfileChangeClickListener(new ProfileUserView.OnProfileChangeClickListener() {
                @Override
                public void onProfileChangeClick() {
                    ((ProfileActivity) mContext).onClickProfileChange();
                }
            });

        } else if (type == ProfileActivity.TYPE_TAB) {
            ((ProfileTabView) holder).setProfileTabView(items.get(position), mContext);
            ((ProfileTabView) holder).setOnTabItemClickListener(new ProfileTabView.OnTabItemClickListener() {
                @Override
                public void onClickWroteItem() {
                    ((ProfileActivity) mContext).onClickWroteRecipes();
                }

                @Override
                public void onClickInterestItem() {
                    ((ProfileActivity) mContext).onClickInterestRecipes();
                }

                @Override
                public void onClickOpenItem() {
                    ((ProfileActivity) mContext).onClickOpenedRecipes();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

}

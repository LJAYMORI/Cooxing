package com.ljaymori.cooxing.notify;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljaymori.cooxing.R;

import java.util.ArrayList;

public class NotifyFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotifyAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notify, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_notify);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new NotifyAdapter(getActivity());
        mAdapter.addAll(initData());

        recyclerView.setAdapter(mAdapter);

        return v;
    }

    private ArrayList<NotifyItemData> initData() {
        ArrayList<NotifyItemData> list = new ArrayList<NotifyItemData>();

        for(int i=0; i<10; i++) {
            NotifyItemData nd = new NotifyItemData();
            nd.setImageURL(R.drawable.ic_launcher + "");
            nd.setType(i);
            nd.setUserNickname("nickname - " + i);

            list.add(nd);
        }

        return list;
    }
}

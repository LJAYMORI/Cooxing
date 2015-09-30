package com.ljaymori.cooxing.write.descriptsion;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ljaymori.cooxing.R;
import com.ljaymori.cooxing.common.CooxingConstant;
import com.ljaymori.cooxing.write.DescAddDialog;
import com.ljaymori.cooxing.write.WriteActivity;
import com.ljaymori.cooxing.write.WriteParentFragment;
import com.ljaymori.cooxing.write.album.WriteAlbumFolderActivity;
import com.ljaymori.cooxing.write.camera.WriteCameraActivity;
import com.ljaymori.cooxing.write.tag.TagItemData;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class WriteDescFragment extends WriteParentFragment {

    public static final int REQUEST_CODE = 100;
    public static final String FILE_PATHS = "paths";
    public static final String ITEM_COUNT = "item_count";


    private ArrayList<TagItemData> tagListInDescription = new ArrayList<TagItemData>();

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private DescItemAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_write_description, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_write_page_description);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new DescItemAdapter(getActivity());
        mAdapter.initItems(initData());
        mAdapter.setOnDescAdapterEventListener(new DescItemAdapter.OnDescAdapterEventListener() {
            @Override
            public void onRemoveAllEvent() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                builder.setTitle(getString(R.string.write_description_dialog_title))
                        .setMessage(getString(R.string.write_description_dialog_message))
                        .setCancelable(true)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addFiles(new ArrayList<String>(), new ArrayList<String>());
                            }
                        })
                        .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.create().show();

            }

            @Override
            public void onEditOrderEvent() {
                //TODO
                Toast.makeText(getActivity(), "editorder", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAddItemEvent() {
                DescAddDialog dialog = new DescAddDialog();
                dialog.setOnDescAddDalogEventListener(new DescAddDialog.OnDescAddDalogEventListener() {
                    @Override
                    public void onAlbumClick() {
                        Intent intent = new Intent(getActivity(), WriteAlbumFolderActivity.class);
                        intent.putExtra(ITEM_COUNT, mAdapter.getItemCount() - 2);
                        getActivity().startActivityForResult(intent, REQUEST_CODE);
                    }

                    @Override
                    public void onCameraClick() {
                        Intent intent = new Intent(getActivity(), WriteCameraActivity.class);
                        intent.putExtra(ITEM_COUNT, mAdapter.getItemCount() - 2);
                        getActivity().startActivityForResult(intent, REQUEST_CODE);
                    }
                });
                dialog.show(getFragmentManager(), "add");
            }
        });

        recyclerView.setAdapter(mAdapter);

        addFiles(((WriteActivity) getActivity()).getImageList(), ((WriteActivity) getActivity()).getSteps());

        return v;
    }

    public void checkTags() {
        int size = mAdapter.getItemCount();
        String str = "";
        for (int i = 1; i < size - 1; i++) {
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder instanceof DescItemView) {
                String desc = ((DescItemView) viewHolder).getDescription();
                mAdapter.updateDesc(i, desc);
            }

            String token = mAdapter.getItems().get(i).getDescription();
            if (token.length() > 0) {
                ((WriteActivity) getActivity()).getSteps().set(i - 1, token);
                str += " " + token;
            }
        }

        ArrayList<TagItemData> tags = new ArrayList<TagItemData>();

        StringTokenizer tokenizer = new StringTokenizer(str);
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (Pattern.matches(CooxingConstant.HASHTAG_PATTERN, token.trim())) {
                TagItemData td = new TagItemData();
                td.setName(token);

                tags.add(td);
            }
        }
        initTagList(tags);

        ((WriteActivity) getActivity()).checkTag();
    }

    private void initTagList(ArrayList<TagItemData> list) {
        ((WriteActivity) getActivity()).getDescFragment().getTagListInDescription().clear();
        ((WriteActivity) getActivity()).getDescFragment().getTagListInDescription().addAll(list);
    }

    private ArrayList<DescItemData> initData() {
        ArrayList<DescItemData> list = new ArrayList<DescItemData>();

        DescItemData tabData = new DescItemData();
        tabData.setType(DescItemAdapter.TYPE_TAB);
        list.add(tabData);

        DescItemData addData = new DescItemData();
        addData.setType(DescItemAdapter.TYPE_ADD);
        list.add(addData);

        return list;
    }

    public void addTag(String tag, int id, int position) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        ((DescItemView) viewHolder).addTag(id, tag);
    }

    public void addSharp(int id, int position) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        ((DescItemView) viewHolder).addSharp(id);
    }

    public ArrayList<TagItemData> getTagListInDescription() {
        return tagListInDescription;
    }

    public void addFiles(ArrayList<String> pathList, ArrayList<String> descList) {
        ArrayList<DescItemData> list = new ArrayList<DescItemData>();

        int size = pathList.size();

        for (int i = 0; i < size; i++) {
            String path = pathList.get(i);
            String desc = descList.get(i);

            DescItemData dd = new DescItemData();
            dd.setType(DescItemAdapter.TYPE_ITEM);
            dd.setFilePath(path);
            dd.setDescription(desc);
            list.add(dd);
        }

        mAdapter.clear();
        mAdapter.addAll(list);

        checkRemoveAllAndShuffle(size + mAdapter.getItemCount() - 2);
    }

    private void checkRemoveAllAndShuffle(int size) {
        mAdapter.setTabActivate(size);
    }
}

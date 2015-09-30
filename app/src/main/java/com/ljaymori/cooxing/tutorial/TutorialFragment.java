package com.ljaymori.cooxing.tutorial;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ljaymori.cooxing.R;

public class TutorialFragment extends Fragment {

    private boolean isLast;

    private int position;

    private ImageView ivBackground;
    private int backgroundResource;

    private ImageView ivSymbol;
    private int symbolResource;

    private TextView tvTitle;
    private String title;

    private TextView tvDesc;
    private String desc;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        isLast = arguments.getBoolean(TutorialActivity.KEY_IS_LAST_PAGE, false);
        position = arguments.getInt(TutorialActivity.KEY_POSITION);
        backgroundResource = arguments.getInt(TutorialActivity.KEY_BACKGROUND_RESOURCE);
        symbolResource = arguments.getInt(TutorialActivity.KEY_SYMBOL_RESOURCE);
        title = arguments.getString(TutorialActivity.KEY_TITLE);
        desc = arguments.getString(TutorialActivity.KEY_DESCRIPTION);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial, container, false);

        ivBackground = (ImageView) view.findViewById(R.id.image_background_tutorial);
        ivSymbol = (ImageView) view.findViewById(R.id.image_symbol_tutorial);
        tvTitle = (TextView) view.findViewById(R.id.text_title_tutorial);
        tvDesc = (TextView) view.findViewById(R.id.text_desc_tutorial);

        ivBackground.setImageResource(backgroundResource);
        ivSymbol.setImageResource(symbolResource);
        tvTitle.setText(title);
        tvDesc.setText(desc);

        if(isLast) {
            TextView tvStart = (TextView) view.findViewById(R.id.text_start_app_tutorial);
            tvStart.setVisibility(View.VISIBLE);
            tvStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }

        return view;
    }
}

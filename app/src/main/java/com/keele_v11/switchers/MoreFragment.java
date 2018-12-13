package com.keele_v11.switchers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.keele_v11.R;

public class MoreFragment extends Fragment{

    private com.suke.widget.SwitchButton switchButtonNews;
    private com.suke.widget.SwitchButton switchButtonEvents;
    private com.suke.widget.SwitchButton switchButtonWU;
    private com.suke.widget.SwitchButton switchButtonLNF;

    private RelativeLayout itemSuggest;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_more,container,false);

        switchButtonNews = (com.suke.widget.SwitchButton) v.findViewById(R.id.switchNews);
        switchButtonEvents = (com.suke.widget.SwitchButton) v.findViewById(R.id.switchEvents);
        switchButtonWU = (com.suke.widget.SwitchButton) v.findViewById(R.id.switchWU);
        switchButtonLNF = (com.suke.widget.SwitchButton) v.findViewById(R.id.switchLNF);

        itemSuggest = (RelativeLayout) v.findViewById(R.id.itemSuggestA);

        switchButtonNews.setChecked(true);
        switchButtonEvents.setChecked(true);
        switchButtonWU.setChecked(true);
        switchButtonLNF.setChecked(true);

        /*itemSuggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(getActivity())
                        .title("Post Suggestion")
                        .content("Write suggestion")
                        .inputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE)
                        .input("Write suggestion...", "tjs", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {

                            }
                        }).show();
            }
        });*/
        return v;
    }
}


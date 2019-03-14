package com.prismk.japaneseelearn.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.prismk.japaneseelearn.R;
import com.prismk.japaneseelearn.widgets.Title;

/**
 * 首页Fragment
 */
public class ClassesFragment extends BaseFragment {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_classes;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        initTitle();
        View mClassesFragment = View.inflate(getActivity(), getLayoutId(), null);
        return mRootView;
    }

    private void initTitle() {
        Title title = mRootView.findViewById(R.id.title);
        title.setTitleNameStr("课程");
        title.setTheme(Title.TitleTheme.THEME_LIGHT);
        title.setShowDivider(true);
        /*Title.ButtonInfo buttonInfoRight1 = new Title.ButtonInfo(true, Title.BUTTON_RIGHT1);
        buttonInfoRight1.iconRes = R.drawable.selector_btn_addcontact;
        Title.ButtonInfo bottonRight2 = new Title.ButtonInfo(true, Title
                .BUTTON_RIGHT2, R.mipmap.ic_search_btn, null);
        title.setOnTitleButtonClickListener(new Title.OnTitleButtonClickListener() {
            @Override
            public void onClick(int id, Title.ButtonViewHolder viewHolder) {
                switch (id) {
                    case Title.BUTTON_RIGHT1:
                        SearchRemoteContactActivity.open(getActivity(), SearchRemoteContactActivity.REQUEST_CODE_FROM_CONTACT);
                        break;
                    case Title.BUTTON_RIGHT2:
                        SearchLocalActivity.open(getActivity());
                        break;
                }
            }
        });
        title.setButtonInfo(bottonRight2);
        title.setButtonInfo(buttonInfoRight1);*/
    }

    @Override
    protected void initStatusBar() {
        super.initStatusBar();
        setStatusBarView();
    }

}
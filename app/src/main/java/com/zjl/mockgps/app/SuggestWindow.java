package com.zjl.mockgps.app;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.*;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;

/**
 * Created by C0dEr on 15/8/18.
 */
public class SuggestWindow extends PopupWindow {
    private View suggestPopWindow;

    private ExpandableListView mExpandableListView;

    public SuggestWindow(Activity mContext,BaseExpandableListAdapter adapter) {
        super(mContext);

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        suggestPopWindow = inflater.inflate(R.layout.fragment_suggest_pop, null);
        mExpandableListView = (ExpandableListView) suggestPopWindow.findViewById(R.id.suggest);
        mExpandableListView.setAdapter(adapter);
        this.setContentView(suggestPopWindow);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popWindowAnimation);
        this.setInputMethodMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        suggestPopWindow.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                int height = suggestPopWindow.findViewById(R.id.popLayout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}

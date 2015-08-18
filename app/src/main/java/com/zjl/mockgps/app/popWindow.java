package com.zjl.mockgps.app;

import android.app.Activity;
import android.content.Context;
import android.view.*;
import android.widget.Button;
import android.widget.PopupWindow;


public class popWindow extends PopupWindow {

    private View popupWindow;

    private Button cancel;
    private Button makeStart;
    private Button makeEnd;

    public popWindow(Activity context, View.OnClickListener itemListener) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popupWindow = inflater.inflate(R.layout.fragment_option_pop, null);
        cancel = (Button) popupWindow.findViewById(R.id.cancel);
        makeStart = (Button) popupWindow.findViewById(R.id.makeStart);
        makeEnd = (Button) popupWindow.findViewById(R.id.makeEnd);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        makeStart.setOnClickListener(itemListener);
        makeEnd.setOnClickListener(itemListener);

        this.setContentView(popupWindow);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popWindowAnimation);
        this.setInputMethodMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        popupWindow.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                int height = popupWindow.findViewById(R.id.popLayout).getTop();
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

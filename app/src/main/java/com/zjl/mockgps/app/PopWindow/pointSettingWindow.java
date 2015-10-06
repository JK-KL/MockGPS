package com.zjl.mockgps.app.PopWindow;

import android.app.Activity;
import android.content.Context;
import android.util.TypedValue;
import android.view.*;
import android.widget.Button;
import android.widget.PopupWindow;
import com.zjl.mockgps.app.R;


public class pointSettingWindow extends PopupWindow {

    private View popupWindow;

    private Button cancel;
    private Button makeStart;
    private Button makeEnd;

    public pointSettingWindow(Activity context, View.OnClickListener itemListener) {
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
        this.setWidth((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP
                , 300
                , context.getResources().getDisplayMetrics()));
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.popWindowAnimation);
        this.setInputMethodMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        popupWindow.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                int height = popupWindow.findViewById(R.id.suggest).getTop();
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

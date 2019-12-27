package com.zk.timerpicker.util.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;

public class uiUtil {
    public static void reverseChageButtonArrs(Button btn0, Button btn1) {
        btn0.setEnabled(false);
        btn0.setTypeface(null, Typeface.BOLD);
        btn1.setTypeface(null, Typeface.NORMAL);
        btn1.setEnabled(true);
    }



}

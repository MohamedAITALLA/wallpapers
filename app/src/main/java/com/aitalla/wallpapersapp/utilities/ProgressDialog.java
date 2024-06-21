package com.aitalla.wallpapersapp.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;

import com.aitalla.wallpapersapp.R;

public class ProgressDialog {
    private static Dialog alertDiaLog;

    public static void showProgressDialog(Activity activity) {
        try {
            if (alertDiaLog != null && alertDiaLog.isShowing()) return;
            alertDiaLog = new Dialog(activity);
            alertDiaLog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            alertDiaLog.setCancelable(false);
            alertDiaLog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDiaLog.setContentView(R.layout.dialog_progress);
            alertDiaLog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideProgressDialog() {
        try {
            if (alertDiaLog != null && alertDiaLog.isShowing()) {
                alertDiaLog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

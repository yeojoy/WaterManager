package me.yeojoy.watermanager.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by yeojoy on 15. 6. 19..
 */
public class CommonUtils {

    public static void hideKeyboard(Context context, EditText et) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        //txtName is a reference of an EditText Field
        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
    }

    public static void showKeyboard(Context context, EditText et) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
        imm.showSoftInputFromInputMethod(et.getWindowToken(), InputMethodManager.SHOW_IMPLICIT);
    }
}

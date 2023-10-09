package com.gestione.distributori.utilities;

import android.app.Activity;

import androidx.appcompat.widget.Toolbar;

public class UI {
    private UI() {}

    public static void InitToolbars(Activity activity, int toolBar) {
        Toolbar toolbar = (Toolbar) activity.findViewById(toolBar);

        toolbar.setOnMenuItemClickListener((Toolbar.OnMenuItemClickListener) activity);
    }
}

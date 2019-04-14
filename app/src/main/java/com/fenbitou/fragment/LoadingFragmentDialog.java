package com.fenbitou.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.fenbitou.wantongzaixian.R;


public class LoadingFragmentDialog extends DialogFragment {

    String msg = "请稍后";
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater layoutInflater=getActivity().getLayoutInflater();

        View view=layoutInflater.inflate(R.layout.loading_dialog,null);
        TextView title = (TextView) view.findViewById(R.id.loading_msg);
        title.setText(msg);
        Dialog dialog=new Dialog(getActivity(),R.style.dialog);
        dialog.setContentView(view);
        return dialog;
    }


    @Override
    public void show(FragmentManager manager, String tag) {
        if (tag != null) {
            msg = tag;
        }
        if (!this.isAdded()){
            try {

        super.show(manager, tag);
            }catch (Exception e){}
        }
    }
    @Override
    public void dismiss() {
        try {
            super.dismiss();
        } catch (Exception e) {
        }
    }
}

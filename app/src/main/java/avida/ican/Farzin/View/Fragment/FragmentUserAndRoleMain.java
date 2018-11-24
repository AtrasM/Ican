package avida.ican.Farzin.View.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import avida.ican.Farzin.View.Adapter.AdapterUserAndRoleMain;
import avida.ican.Farzin.View.Dialog.DialogUserAndRole;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.LinearLayoutManagerWithSmoothScroller;
import avida.ican.R;
import butterknife.BindView;

public class FragmentUserAndRoleMain extends BaseFragment {

    @BindView(R.id.edt_search)
    EditText edtSearch;

    @BindView(R.id.rcv_main)
    RecyclerView rcvMain;

    private Activity context;
    private static AdapterUserAndRoleMain adapterUserAndRoleMain;
    private static DialogUserAndRole dialogUserAndRole;

    public static String Tag = "FragmentUserAndRoleMain";

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_user_and_role_main;
    }

    public FragmentUserAndRoleMain newInstance(Activity context, AdapterUserAndRoleMain adapterUserAndRoleMain, DialogUserAndRole dialogUserAndRole) {
        // FragmentUserAndRoleMain fragment = new FragmentUserAndRoleMain();
        this.context = context;
        this.dialogUserAndRole = dialogUserAndRole;
        this.adapterUserAndRoleMain = adapterUserAndRoleMain;
        return this;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManagerWithSmoothScroller linearLayoutManager = new LinearLayoutManagerWithSmoothScroller(context);
        rcvMain.setLayoutManager(linearLayoutManager);
        rcvMain.setAdapter(this.adapterUserAndRoleMain);

        edtSearch.setFilters(new InputFilter[]{new CustomFunction().ignoreFirstWhiteSpace()});
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = charSequence.toString().trim();
                if (s.length() > 2) {
                    dialogUserAndRole.performSearch(s, false);
                } else if (s.length() == 2) {
                    dialogUserAndRole.performSearch(null, true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

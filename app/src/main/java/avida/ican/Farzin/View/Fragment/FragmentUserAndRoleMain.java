package avida.ican.Farzin.View.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.View.Adapter.AdapterUserAndRoleMain;
import avida.ican.Farzin.View.Dialog.DialogUserAndRole;
import avida.ican.Farzin.View.Interface.ListenerUserAndRollSearch;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.LinearLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.R;
import butterknife.BindView;

import static avida.ican.Ican.BaseActivity.closeKeyboard;
import static avida.ican.Ican.BaseActivity.openKeyboard;

public class FragmentUserAndRoleMain extends BaseFragment {

    @BindView(R.id.edt_search)
    EditText edtSearch;
    @BindView(R.id.img_delete_search)
    ImageView imgDeleteSearch;
    @BindView(R.id.img_search)
    ImageView imgSearch;

    @BindView(R.id.rcv_main)
    RecyclerView rcvMain;

    @BindView(R.id.ln_loading)
    LinearLayout lnSearchLoading;

    private Activity context;
    private static AdapterUserAndRoleMain adapterUserAndRoleMain;
    private static DialogUserAndRole dialogUserAndRole;

    public static String Tag = "FragmentUserAndRoleMain";
    private String query = "";

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
        rcvMain.setOnClickListener(view1 -> closeKeyboard());
        imgDeleteSearch.setVisibility(View.GONE);
        edtSearch.setFilters(new InputFilter[]{CustomFunction.ignoreFirstWhiteSpace()});

        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!query.equals(edtSearch.getText().toString().trim())) {
                    query = edtSearch.getText().toString().trim();
                    search(query);
                }

                return true;
            }
            return false;
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (s.toString().trim().length() == 2) {
                    query = "";
                    search(query);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        imgSearch.setOnClickListener(view12 -> {
            if (!query.equals(edtSearch.getText().toString().trim())) {
                query = edtSearch.getText().toString().trim();
                search(query);
            }
        });
        imgDeleteSearch.setOnClickListener(view12 -> {
                query = "";
                edtSearch.setText(query);
                search(query);
        });
    }

    private void search(String query) {
        boolean reset = false;

        if (query.length() > 2) {
            initSearchViewVisibility(true);
            reset = false;
            imgDeleteSearch.setVisibility(View.VISIBLE);
        } else if (query.length() <= 2) {
            query = null;
            reset = true;
            imgDeleteSearch.setVisibility(View.GONE);
        }
        dialogUserAndRole.performSearch(query, reset, new ListenerUserAndRollSearch() {
            @Override
            public void onSuccess(List<StructureUserAndRoleDB> structureUserAndRolesSearch) {
                initSearchViewVisibility(false);

            }

            @Override
            public void onFailed() {
                initSearchViewVisibility(false);
            }

            @Override
            public void onCancel() {
                initSearchViewVisibility(false);
            }
        });
    }

    private void initSearchViewVisibility(boolean visible) {
        App.getHandlerMainThread().post(() -> {
            if (visible) {
                lnSearchLoading.setVisibility(View.VISIBLE);
                edtSearch.setEnabled(false);
            } else {
                lnSearchLoading.setVisibility(View.GONE);
                edtSearch.setEnabled(true);
                /*edtSearch.setFocusable(true);
                openKeyboard();*/
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

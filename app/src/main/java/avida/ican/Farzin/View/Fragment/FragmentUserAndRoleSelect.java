package avida.ican.Farzin.View.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import avida.ican.Farzin.View.Adapter.AdapterUserAndRoleSelected;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.View.Custom.LinearLayoutManagerWithSmoothScroller;
import avida.ican.R;
import butterknife.BindView;

import static avida.ican.Ican.BaseActivity.closeKeyboard;

public class FragmentUserAndRoleSelect extends BaseFragment {

    @BindView(R.id.rcv_selected)
    RecyclerView rcvSelected;
    @BindView(R.id.ln_user_and_role_select)
    LinearLayout lnUserAndRoleSelect;
    private Activity context;
    private static AdapterUserAndRoleSelected adapterUserAndRoleSelected;
    public final String Tag = "FragmentUserAndRoleSelect";


    public FragmentUserAndRoleSelect newInstance(Activity context, AdapterUserAndRoleSelected adapterUserAndRoleSelected) {
       // FragmentUserAndRoleSelect fragment = new FragmentUserAndRoleSelect();
        this.context = context;
        this.adapterUserAndRoleSelected = adapterUserAndRoleSelected;
        return this;
    }
    @Override
    public int getLayoutResId() {
        return R.layout.fragment_user_and_role_selected;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayoutManagerWithSmoothScroller linearLayoutManager = new LinearLayoutManagerWithSmoothScroller(context);
        rcvSelected.setLayoutManager(linearLayoutManager);
        rcvSelected.setAdapter(this.adapterUserAndRoleSelected);

        rcvSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
            }
        });
        lnUserAndRoleSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboard();
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

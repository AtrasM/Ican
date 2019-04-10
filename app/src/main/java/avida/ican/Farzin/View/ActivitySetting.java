package avida.ican.Farzin.View;

import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.R;
import butterknife.BindView;

import android.os.Bundle;
import android.widget.LinearLayout;

public class ActivitySetting extends BaseToolbarActivity {
    @BindView(R.id.ln_document_setting)
    LinearLayout lnDocumentSetting;
    @BindView(R.id.ln_message_setting)
    LinearLayout lnMessageSetting;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        lnDocumentSetting.setOnClickListener(view -> {
            goToActivity(ActivitySetting.class);
        });
    }


}

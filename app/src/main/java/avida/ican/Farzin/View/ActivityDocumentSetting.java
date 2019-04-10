package avida.ican.Farzin.View;

import androidx.appcompat.app.AppCompatActivity;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.R;

import android.os.Bundle;

public class ActivityDocumentSetting extends BaseToolbarActivity {


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_document_setting;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {

    }
}

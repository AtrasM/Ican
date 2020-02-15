package avida.ican.Ican.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.cardview.widget.CardView;

import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.View.Enum.CurentProject;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.R;
import butterknife.BindView;

public class ActivityMain extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.cv_farzin)
    CardView cvFarzin;
    @BindView(R.id.pdf_viewer)
    PDFView pdfViewer;
    @BindView(R.id.cv_audio_recorder)
    CardView cvAudioRecorder;
    @BindView(R.id.txt_pdf_page_number)
    TextView txt_pdf_page_number;
    @BindView(R.id.cv_file_picker)
    CardView cvFilePicker;
    @BindView(R.id.cv_media_picker)
    CardView cvMediaPicker;
    @BindView(R.id.cv_user_and_role)
    CardView cvUserAndRole;
    @BindView(R.id.sp_size)
    Spinner spSize;

    private Context context;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        cvFarzin.setOnClickListener(this);
        App.setCurentProject(CurentProject.Ican);
        cvUserAndRole.setOnClickListener(this);

    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cv_farzin: {
                new FarzinCartableQuery().setErrorToDocumentOperatorQueue(1, "");
                break;
            }
        }
    }


}

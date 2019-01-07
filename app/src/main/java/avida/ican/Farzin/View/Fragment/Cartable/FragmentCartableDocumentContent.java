package avida.ican.Farzin.View.Fragment.Cartable;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentContentDB;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentContentPresenter;
import avida.ican.Farzin.View.Interface.Cartable.ListenerCartableDocumentContent;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.R;
import butterknife.BindView;

public class FragmentCartableDocumentContent extends BaseFragment {


    @BindView(R.id.pdf_viewer)
    PDFView pdfViewer;
    @BindView(R.id.txt_no_data)
    TextView txtNoData;
    @BindView(R.id.ln_loading)
    LinearLayout lnLoading;
    @BindView(R.id.txt_pdf_page_number)
    TextView txtPdfPageNumber;
    private Activity context;
    private int Etc;
    private int Ec;
    private CartableDocumentContentPresenter cartableDocumentContentPresenter;
    public static String Tag = "FragmentCartableDocumentContent";
    private byte[] FileAsBytes = null;

    public FragmentCartableDocumentContent newInstance(Activity context, int Etc, int Ec) {
        this.context = context;
        this.Etc = Etc;
        this.Ec = Ec;
        return this;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_cartable_document_content;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtNoData.setText(Resorse.getString(R.string.error_cartable_document_content));
        initPresenter();
    }


    private void initPresenter() {
        cartableDocumentContentPresenter = new CartableDocumentContentPresenter(Etc, Ec, new ListenerCartableDocumentContent() {

            @Override
            public void newData(final String filePath) {
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lnLoading.setVisibility(View.GONE);
                        initPdfViewer(filePath);
                    }
                });
            }

            @Override
            public void noData() {
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lnLoading.setVisibility(View.GONE);
                        if (FileAsBytes == null) {
                            txtNoData.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });
        initData();

    }


    private void reGetData() {
        StructureCartableDocumentContentDB cartableDocumentContentDB = cartableDocumentContentPresenter.GetFromLocal();
        if (cartableDocumentContentDB.getETC() > 0) {
            initPdfViewer(cartableDocumentContentDB.getFile_path());
        } else {
            if (App.networkStatus == NetworkStatus.Connected) {
                lnLoading.setVisibility(View.VISIBLE);
                cartableDocumentContentPresenter.GetFromServer();
            } else {
                lnLoading.setVisibility(View.GONE);
                txtNoData.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initData() {
        StructureCartableDocumentContentDB cartableDocumentContentDB = cartableDocumentContentPresenter.GetFromLocal();
        if (cartableDocumentContentDB.getETC() > 0) {
            initPdfViewer(cartableDocumentContentDB.getFile_path());
        } else {
            if (App.networkStatus == NetworkStatus.Connected) {
                lnLoading.setVisibility(View.VISIBLE);
                cartableDocumentContentPresenter.GetFromServer();
            } else {
                lnLoading.setVisibility(View.GONE);
                txtNoData.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initPdfViewer(String filePath) {
        if (filePath != null && !filePath.isEmpty()) {
           /* String fileAsBase64 = new CustomFunction().getFileFromStorageAsByte(filePath);
            byte[] FileAsbytes = new Base64EncodeDecodeFile().DecodeBase64ToByte(fileAsBase64);*/
            byte[] FileAsbytes = new CustomFunction().getFileFromStorageAsByte(filePath);
            if (FileAsbytes != null && FileAsbytes.length > 0) {
                pdfViewer.fromBytes(FileAsbytes)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .spacing(6)
                        .onPageScroll(new OnPageScrollListener() {
                            @Override
                            public void onPageScrolled(int page, float positionOffset) {

                            }
                        })
                        .onPageChange(new OnPageChangeListener() {
                            @Override
                            public void onPageChanged(int page, int pageCount) {
                                String desc = Resorse.getString(R.string.page) + " " + (page + 1) + " " + Resorse.getString(R.string.of) + " " + pageCount;
                                txtPdfPageNumber.setText(desc);
                            }
                        })
                        .load();
            } else {
                txtNoData.setVisibility(View.VISIBLE);
            }

        } else {
            txtNoData.setVisibility(View.VISIBLE);
        }

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

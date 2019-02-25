package avida.ican.Farzin.View.Fragment.Cartable;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;

import java.io.IOException;

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
    /*    @BindView(R.id.srl_refresh)
        SwipeRefreshLayout srlRefresh;*/
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
      /*  srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reGetData();
            }
        });*/
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
                        //srlRefresh.setRefreshing(false);
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
                        // srlRefresh.setRefreshing(false);
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
        txtNoData.setVisibility(View.GONE);
        StructureCartableDocumentContentDB cartableDocumentContentDB = cartableDocumentContentPresenter.GetFromLocal();
        if (cartableDocumentContentDB.getETC() > 0 && !cartableDocumentContentDB.getFile_path().isEmpty()) {
            initPdfViewer(cartableDocumentContentDB.getFile_path());
        } else {
            if (App.networkStatus == NetworkStatus.Connected) {
                cartableDocumentContentPresenter.GetFromServer();
            } else {
                lnLoading.setVisibility(View.GONE);
                txtNoData.setVisibility(View.VISIBLE);
                //srlRefresh.setRefreshing(false);
            }
        }
    }

    private void initData() {
        StructureCartableDocumentContentDB cartableDocumentContentDB = cartableDocumentContentPresenter.GetFromLocal();
        if (cartableDocumentContentDB.getETC() > 0 && !cartableDocumentContentDB.getFile_path().isEmpty()) {
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
        //srlRefresh.setRefreshing(false);
        if (filePath != null && !filePath.isEmpty()) {
            byte[] fileAsbytes = new CustomFunction().getFileFromStorageAsByte(filePath);
            Log.i("PdfViewer", "initPdfViewer fileAsbytes.length = " + fileAsbytes.length);
            if (fileAsbytes != null && fileAsbytes.length > 0) {
                pdfViewer.fromBytes(fileAsbytes)
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

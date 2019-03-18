package avida.ican.Farzin.View.Fragment.Cartable;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.ln_document_content_number)
    LinearLayout lnDocumentContentNumber;
    @BindView(R.id.sp_document_content_number)
    Spinner spDocumentContentNumber;
    @BindView(R.id.img_refresh)
    ImageView imgRefresh;
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
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reGetData();
            }
        });
        initPresenter();
    }


    private void initPresenter() {
        cartableDocumentContentPresenter = new CartableDocumentContentPresenter(Etc, Ec, new ListenerCartableDocumentContent() {


            @Override
            public void newData(List<StructureCartableDocumentContentDB> structureCartableDocumentContentDBS) {
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initSpiner(structureCartableDocumentContentDBS);

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
                            imgRefresh.setVisibility(View.VISIBLE);
                        }
                    }
                });

            }
        });
        initData();

    }

    private void initSpiner(List<StructureCartableDocumentContentDB> cartableDocumentContentsDB) {
        lnLoading.setVisibility(View.VISIBLE);
        if (cartableDocumentContentsDB == null || cartableDocumentContentsDB.size() <= 0) {
            txtNoData.setVisibility(View.VISIBLE);
            imgRefresh.setVisibility(View.VISIBLE);
        } else {
            imgRefresh.setVisibility(View.GONE);
            txtNoData.setVisibility(View.GONE);
            ArrayList<String> item = new ArrayList<>();
            for (int i = 1; i <= cartableDocumentContentsDB.size(); i++) {
                item.add("شماره " + i);
            }
            initPdfViewer(cartableDocumentContentsDB.get(0).getFile_path());
            if (item.size() <= 1) {
                lnDocumentContentNumber.setVisibility(View.GONE);
            } else {
                lnDocumentContentNumber.setVisibility(View.VISIBLE);
            }
            ArrayAdapter<String> adapterSpinner = new CustomFunction(App.CurentActivity).getSpinnerAdapter(item);
            spDocumentContentNumber.setAdapter(adapterSpinner);
            spDocumentContentNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    initPdfViewer(cartableDocumentContentsDB.get(i).getFile_path());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }


    }


    private void initData() {
        imgRefresh.setVisibility(View.GONE);
        List<StructureCartableDocumentContentDB> cartableDocumentContentsDB = cartableDocumentContentPresenter.GetFromLocal();
        if (cartableDocumentContentsDB == null || cartableDocumentContentsDB.size() <= 0) {
            checkData(cartableDocumentContentsDB);
        } else {
            initSpiner(cartableDocumentContentsDB);
        }

    }

    public void reGetData() {
        imgRefresh.setVisibility(View.GONE);
        List<StructureCartableDocumentContentDB> cartableDocumentContentsDB = cartableDocumentContentPresenter.GetFromLocal();
        if (cartableDocumentContentsDB == null || cartableDocumentContentsDB.size() <= 0) {
            checkData(cartableDocumentContentsDB);
        } else {
            int counter = cartableDocumentContentsDB.size();
            for (StructureCartableDocumentContentDB cartableDocumentContentDB : cartableDocumentContentsDB) {
                if (cartableDocumentContentDB.getFile_path() == null || cartableDocumentContentDB.getFile_path().isEmpty()) {
                    counter--;
                }
            }
            if (counter <= 0) {
                checkData(cartableDocumentContentsDB);
            } else {
                initSpiner(cartableDocumentContentsDB);
            }

        }


    }

    private void checkData(List<StructureCartableDocumentContentDB> cartableDocumentContentsDB) {
        if (App.networkStatus == NetworkStatus.Connected) {
            lnLoading.setVisibility(View.VISIBLE);
            txtNoData.setVisibility(View.GONE);
            cartableDocumentContentPresenter.GetFromServer();
        } else {
            initSpiner(cartableDocumentContentsDB);
        }
    }


    private void initPdfViewer(String filePath) {
        lnLoading.setVisibility(View.GONE);
        txtNoData.setVisibility(View.GONE);

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
                        }).onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        txtNoData.setVisibility(View.VISIBLE);
                        imgRefresh.setVisibility(View.VISIBLE);
                    }
                })
                        .load();
            } else {
                txtNoData.setVisibility(View.VISIBLE);
                imgRefresh.setVisibility(View.VISIBLE);
                //reGetData();
            }

        } else {
            txtNoData.setVisibility(View.VISIBLE);
            imgRefresh.setVisibility(View.VISIBLE);

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

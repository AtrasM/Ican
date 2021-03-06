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

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import avida.ican.Farzin.Model.Enum.DocumentContentFileTypeEnum;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentContentDB;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentContentPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.View.Interface.Cartable.ListenerCartableDocumentContent;
import avida.ican.Farzin.View.Interface.ListenerFile;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Adapter.AdapterAttach;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Interface.ListenerAdapterAttach;
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
    @BindView(R.id.rcv_attach)
    RecyclerView rcvAttach;
    /*    @BindView(R.id.srl_refresh)
        SwipeRefreshLayout srlRefresh;*/
    private Activity context;
    private int Etc;
    private int Ec;
    private CartableDocumentContentPresenter cartableDocumentContentPresenter;
    public final String Tag = "FragmentCartableDocumentContent";
    private byte[] FileAsBytes = null;
    private ListenerFile listenerFile;
    private AdapterAttach adapterAttach;
    private boolean isImportEntityNumber = false;

    public FragmentCartableDocumentContent newInstance(Activity context, int Etc, int Ec, boolean isImportEntityNumber, ListenerFile listenerFile) {
        this.context = context;
        this.Etc = Etc;
        this.Ec = Ec;
        this.isImportEntityNumber = isImportEntityNumber;
        this.listenerFile = listenerFile;
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

        imgRefresh.setOnClickListener(view1 -> reGetData());
        initPresenter();
    }


    private void initPresenter() {
        cartableDocumentContentPresenter = new CartableDocumentContentPresenter(Etc, Ec, new ListenerCartableDocumentContent() {
            @Override
            public void newData(List<StructureCartableDocumentContentDB> structureCartableDocumentContentDBS) {
                App.CurentActivity.runOnUiThread(() -> initSpiner(structureCartableDocumentContentDBS));
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
        if (cartableDocumentContentsDB == null || cartableDocumentContentsDB.size() <= 0) {
            lnLoading.setVisibility(View.GONE);
            txtNoData.setVisibility(View.VISIBLE);
            imgRefresh.setVisibility(View.VISIBLE);
        } else {
            imgRefresh.setVisibility(View.GONE);
            txtNoData.setVisibility(View.GONE);
            ArrayList<String> item = new ArrayList<>();
            for (int i = 1; i <= cartableDocumentContentsDB.size(); i++) {
                item.add(" شماره " + i);
            }
            if (item.size() <= 1) {
                lnDocumentContentNumber.setVisibility(View.GONE);
            } else {
                lnDocumentContentNumber.setVisibility(View.VISIBLE);
            }
            initFile(cartableDocumentContentsDB.get(0).getId(), cartableDocumentContentsDB.get(0).getFile_path(), cartableDocumentContentsDB.get(0).getFile_extension(), item.get(0));

            ArrayAdapter<String> adapterSpinner = new CustomFunction(App.CurentActivity).getSpinnerAdapter(item);
            spDocumentContentNumber.setAdapter(adapterSpinner);
            spDocumentContentNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    initFile(cartableDocumentContentsDB.get(i).getId(), cartableDocumentContentsDB.get(i).getFile_path(), cartableDocumentContentsDB.get(i).getFile_extension(), item.get(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }


    }


    private void initData() {
        imgRefresh.setVisibility(View.GONE);
        List<StructureCartableDocumentContentDB> cartableDocumentContentsDB;
        if (isImportEntityNumber) {
            cartableDocumentContentsDB = cartableDocumentContentPresenter.GetFromLocal(DocumentContentFileTypeEnum.IndicatorScanedFile);
        } else {
            cartableDocumentContentsDB = cartableDocumentContentPresenter.GetFromLocal(DocumentContentFileTypeEnum.CONTENT);
        }
        if (cartableDocumentContentsDB == null || cartableDocumentContentsDB.size() <= 0) {
            checkData(cartableDocumentContentsDB);
        } else {
            initSpiner(cartableDocumentContentsDB);
        }
    }

    public void reGetData() {
        App.getHandlerMainThread().post(() -> {
            List<StructureCartableDocumentContentDB> cartableDocumentContentsDB;
            if (isImportEntityNumber) {
                cartableDocumentContentsDB = cartableDocumentContentPresenter.GetFromLocal(DocumentContentFileTypeEnum.IndicatorScanedFile);
            } else {
                cartableDocumentContentsDB = cartableDocumentContentPresenter.GetFromLocal(DocumentContentFileTypeEnum.CONTENT);
            }
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
        });

    }

    private void checkData(List<StructureCartableDocumentContentDB> cartableDocumentContentsDB) {
        if (isImportEntityNumber) {
            if (App.networkStatus == NetworkStatus.Connected) {
                lnLoading.setVisibility(View.VISIBLE);
                txtNoData.setVisibility(View.GONE);
                imgRefresh.setVisibility(View.GONE);
            } else {
                lnLoading.setVisibility(View.GONE);
                txtNoData.setVisibility(View.VISIBLE);
                imgRefresh.setVisibility(View.VISIBLE);
            }
        } else {
            if (App.networkStatus == NetworkStatus.Connected) {
                lnLoading.setVisibility(View.VISIBLE);
                txtNoData.setVisibility(View.GONE);
                imgRefresh.setVisibility(View.GONE);
                cartableDocumentContentPresenter.GetFromServer();
            } else {
                initSpiner(cartableDocumentContentsDB);
            }
        }

    }


    private void initPdfViewer(int id, String filePath, String fileExtension) {
        lnLoading.setVisibility(View.GONE);
        txtNoData.setVisibility(View.GONE);
        imgRefresh.setVisibility(View.GONE);
        if (filePath != null && !filePath.isEmpty()) {
            byte[] fileAsbytes = new CustomFunction().getFileFromStorageAsByte(filePath);
            Log.i("PdfViewer", "initPdfViewer fileAsbytes.length = " + fileAsbytes.length);
            if (fileAsbytes != null && fileAsbytes.length > 0) {

                pdfViewer.fromBytes(fileAsbytes)
                        .enableSwipe(true)
                        .swipeHorizontal(false)
                        .enableDoubletap(true)
                        .spacing(6)
                        .onPageScroll((page, positionOffset) -> {
                        })
                        .onPageChange((page, pageCount) -> {
                            String desc = Resorse.getString(R.string.page) + " " + (page + 1) + " " + Resorse.getString(R.string.of) + " " + pageCount;
                            txtPdfPageNumber.setText(desc);
                        }).onError(t -> {
                    t.printStackTrace();
                    txtNoData.setVisibility(View.VISIBLE);
                    imgRefresh.setVisibility(View.VISIBLE);
                    deletContentFromLocal(id);
                }).load();
            } else {
                deletContentFromLocal(id);
                txtNoData.setVisibility(View.VISIBLE);
                imgRefresh.setVisibility(View.VISIBLE);
                //reGetData();
            }
        } else {
            deletContentFromLocal(id);
            txtNoData.setVisibility(View.VISIBLE);
            imgRefresh.setVisibility(View.VISIBLE);
        }

    }

    private void initFile(int id, String filePath, String fileExtension, String fileName) {
        lnLoading.setVisibility(View.GONE);
        txtNoData.setVisibility(View.GONE);
        imgRefresh.setVisibility(View.GONE);
        if (filePath != null && !filePath.isEmpty()) {
            if (fileExtension.contains(".pdf")) {
                ArrayList<StructureAttach> structureAttaches = new ArrayList<>();
                initAdapter(structureAttaches);
                rcvAttach.setVisibility(View.GONE);
                pdfViewer.setVisibility(View.VISIBLE);
                initPdfViewer(id, filePath, fileExtension);

            } else {
                rcvAttach.setVisibility(View.VISIBLE);
                pdfViewer.setVisibility(View.GONE);
                StructureAttach structureAttach = new StructureAttach(filePath, fileName, fileExtension);
                ArrayList<StructureAttach> structureAttaches = new ArrayList<>();
                structureAttaches.add(structureAttach);
                initAdapter(structureAttaches);
            }
        } else {
            deletContentFromLocal(id);
            txtNoData.setVisibility(View.VISIBLE);
            imgRefresh.setVisibility(View.VISIBLE);
            //reGetData();
        }

    }

    private void initAdapter(ArrayList<StructureAttach> structureAttaches) {
        GridLayoutManagerWithSmoothScroller linearLayoutManagerWithSmoothScroller = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        rcvAttach.setLayoutManager(linearLayoutManagerWithSmoothScroller);
        adapterAttach = new AdapterAttach(App.CurentActivity, structureAttaches, false, new ListenerAdapterAttach() {
            @Override
            public void onOpenFile(StructureAttach structureAttach) {
                listenerFile.onOpenFile(structureAttach);
            }

            @Override
            public void onDeletFile(StructureAttach structureAttach) {
                structureAttaches.remove(structureAttach);
            }
        });
        rcvAttach.setAdapter(adapterAttach);
    }

    private boolean deletContentFromLocal(int id) {
        try {
            return new FarzinCartableQuery().deletCartableDocumentContent(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
    private boolean deletContentFromLocal(int ETC, int EC) {
        try {
            return new FarzinCartableQuery().deletCartableDocumentContent(ETC,EC);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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

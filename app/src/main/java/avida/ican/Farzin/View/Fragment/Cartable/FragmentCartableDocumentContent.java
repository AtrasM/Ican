package avida.ican.Farzin.View.Fragment.Cartable;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;

import org.apache.commons.codec.Charsets;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentContentDB;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureHameshDB;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentContentPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinHameshListPresenter;
import avida.ican.Farzin.View.Adapter.AdapterHamesh;
import avida.ican.Farzin.View.Interface.Cartable.ListenerAdapterHameshList;
import avida.ican.Farzin.View.Interface.Cartable.ListenerCartableDocumentContent;
import avida.ican.Farzin.View.Interface.Cartable.ListenerHamesh;
import avida.ican.Farzin.View.Interface.ListenerFile;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Custom.Base64EncodeDecodeFile;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
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
        initPresenter();
    }


    private void initPresenter() {
        cartableDocumentContentPresenter = new CartableDocumentContentPresenter(Etc, Ec, new ListenerCartableDocumentContent() {

            @Override
            public void newData(final String FileBinary) {
                App.CurentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lnLoading.setVisibility(View.GONE);
                        initPdfViewer(FileBinary);
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
            initPdfViewer(cartableDocumentContentDB.getFile_binary());
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
            initPdfViewer(cartableDocumentContentDB.getFile_binary());
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

    private void initPdfViewer(String strFile) {
      /*  byte[] FileAsbytes = new Base64EncodeDecodeFile().DecodeBase64ToByte(strFile);
        pdfViewer.fromBytes(FileAsbytes);*/
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

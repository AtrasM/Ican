package avida.ican.Farzin.Presenter.Cartable;

import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.DocumentContentFileTypeEnum;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentContentListener;
import avida.ican.Farzin.Model.Interface.Cartable.CartableDocumentContentQuerySaveListener;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentContentBND;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureCartableDocumentContentDB;
import avida.ican.Farzin.View.Interface.Cartable.ListenerCartableDocumentContent;
import avida.ican.Ican.App;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;

/**
 * Created by AtrasVida on 2018-12-05 at 10:28 AM
 */

public class CartableDocumentContentPresenter {
    private final long DELAY = TimeValue.SecondsInMilli() * 15;
    private final ListenerCartableDocumentContent listenerCartableDocumentContent;
    private final int Etc;
    private final int Ec;
    private CartableDocumentContentListener cartableDocumentContentListener;
    private GetCartableDocumentContentFromServerPresenter getCartableDocumentContentFromServerPresenter;
    private FarzinCartableQuery farzinCartableQuery;
    private static int counterFailed = 0;
    private int MaxTry = 2;

    public CartableDocumentContentPresenter(int Etc, int Ec, ListenerCartableDocumentContent listenerCartableDocumentContent) {
        this.Etc = Etc;
        this.Ec = Ec;
        this.listenerCartableDocumentContent = listenerCartableDocumentContent;
        initCartableDocumentContenthListListener();
    }

    private void initCartableDocumentContenthListListener() {
        getCartableDocumentContentFromServerPresenter = new GetCartableDocumentContentFromServerPresenter();
        farzinCartableQuery = new FarzinCartableQuery();
        cartableDocumentContentListener = new CartableDocumentContentListener() {


            @Override
            public void onSuccess(StringBuilder FileBinaryAsStringBuilder) {
                SaveData(FileBinaryAsStringBuilder);
            }

            @Override
            public void onFailed(String message) {
                listenerCartableDocumentContent.noData();
            }

            @Override
            public void onCancel() {
                listenerCartableDocumentContent.noData();
            }
        };
    }

    public void GetFromServer() {
        getCartableDocumentContentFromServerPresenter.GetContent(Etc, Ec, cartableDocumentContentListener);
    }

    public List<StructureCartableDocumentContentDB> GetFromLocal(DocumentContentFileTypeEnum fileTypeEnum) {
        List<StructureCartableDocumentContentDB> cartableDocumentContentDB = new ArrayList<>();
        if (farzinCartableQuery.IsDocumentContentExist(Etc, Ec,fileTypeEnum)) {
            cartableDocumentContentDB = farzinCartableQuery.getDocumentContent(Etc, Ec,fileTypeEnum);
        }

        return cartableDocumentContentDB;
    }


    private void SaveData(final StringBuilder fileBinaryAsStringBuilder) {

        StructureCartableDocumentContentBND cartableDocumentContentBND = new StructureCartableDocumentContentBND(fileBinaryAsStringBuilder,".pdf", DocumentContentFileTypeEnum.CONTENT, Etc, Ec);
        farzinCartableQuery.saveCartableDocumentContent(cartableDocumentContentBND, new CartableDocumentContentQuerySaveListener() {


            @Override
            public void onSuccess(List<StructureCartableDocumentContentDB> structureCartableDocumentContentsDB) {
                listenerCartableDocumentContent.newData(structureCartableDocumentContentsDB);

            }

            @Override
            public void onExisting() {
                listenerCartableDocumentContent.noData();
            }

            @Override
            public void onFailed(String message) {
                listenerCartableDocumentContent.noData();

            }

            @Override
            public void onCancel() {
                listenerCartableDocumentContent.noData();
            }
        });

    }


    private void reGetData() {
        counterFailed++;
        if (counterFailed > MaxTry) {
            listenerCartableDocumentContent.noData();
        } else {
            App.getHandlerMainThread().postDelayed(new Runnable() {
                @Override
                public void run() {
                    GetFromServer();
                }
            }, DELAY);
        }
    }


}

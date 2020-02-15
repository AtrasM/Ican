package avida.ican.Farzin.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.byox.drawview.views.DrawView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import avida.ican.Farzin.Model.Enum.DependencyTypeEnum;
import avida.ican.Farzin.Model.Enum.DocumentOperatoresTypeEnum;
import avida.ican.Farzin.Model.Interface.Cartable.CreateDocumentListener;
import avida.ican.Farzin.Model.Interface.Cartable.DocumentAttachFileListener;
import avida.ican.Farzin.Model.Interface.Queue.DocumentOperatorQueuesListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.Queue.StructureCreateDocumentQueueBND;
import avida.ican.Farzin.Model.Structure.Bundle.Queue.StructureDocumentAttachFileBND;
import avida.ican.Farzin.Model.Structure.Bundle.Queue.StructureDocumentOperatorsQueueBND;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Request.DocumentOpratores.StructureAppendREQ;
import avida.ican.Farzin.Model.Structure.Request.StructurePersonREQ;
import avida.ican.Farzin.Model.Structure.Response.Cartable.StructureImportDocIndicatorRES;
import avida.ican.Farzin.Presenter.Cartable.CartableDocumentAppendPresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.FarzinMetaDataQuery;
import avida.ican.Farzin.Presenter.Queue.FarzinCreateDocumentPresenter;
import avida.ican.Farzin.Presenter.Queue.FarzinDocumentAttachFilePresenter;
import avida.ican.Farzin.View.Dialog.DialogUserAndRole;
import avida.ican.Farzin.View.Enum.UserAndRoleEnum;
import avida.ican.Farzin.View.Interface.ListenerUserAndRoll;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.Presenter.DrawingViewPresenter;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Enum.SimpleDateFormatEnum;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Dialog.Loading;
import avida.ican.Ican.View.Enum.GravityEnum;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;

public class ActivityCreateDocument extends BaseToolbarActivity {
    @BindView(R.id.draw_view)
    DrawView mDrawView;
    @BindView(R.id.ln_trash)
    LinearLayout lnTrash;
    @BindView(R.id.ln_eraser)
    LinearLayout lnEraser;
    @BindView(R.id.ln_redo)
    LinearLayout lnRedo;
    @BindView(R.id.ln_undo)
    LinearLayout lnUndo;
    @BindView(R.id.ln_pen)
    LinearLayout lnPen;
    @BindView(R.id.ln_color_blue)
    LinearLayout lnColorBlue;
    @BindView(R.id.ln_color_pink)
    LinearLayout lnColorPink;
    @BindView(R.id.edt_subject)
    EditText edtSubject;
    @BindString(R.string.title_creat_document)
    String Title;

    private DrawingViewPresenter drawingViewPresenter;
    private byte[] documentAsByte = null;
    private DialogUserAndRole dialogUserAndRole;
    private List<StructureUserAndRoleDB> userAndRolesMain = new ArrayList<>();
    private long FailedDelay = TimeValue.SecondsInMilli() * 3;
    private StructureAttach structureAttach;
    private FarzinPrefrences farzinPrefrences;
    private Loading loading;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_create_document;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initTollBar(Title);
        farzinPrefrences = getFarzinPrefrences();
        loading = new Loading(App.CurentActivity).Creat();
        initView();
    }

    private void initView() {
        initDrawingPresenter();
        drawingViewPresenter.ChangeModeToDraw();
        drawingViewPresenter.ChangeDrawingColor(R.color.color_blue_draw_pen);
        drawingViewPresenter.setToolSelected(GravityEnum.Center, lnPen);
        drawingViewPresenter.setColorSelected(GravityEnum.Right, lnColorBlue);

        lnUndo.setOnClickListener(view -> drawingViewPresenter.Undo());
        lnRedo.setOnClickListener(view -> drawingViewPresenter.Redo());
        lnTrash.setOnClickListener(view -> drawingViewPresenter.clearAll());

        lnColorPink.setOnClickListener(view -> {
            drawingViewPresenter.setColorSelected(GravityEnum.Center, lnColorPink);
            drawingViewPresenter.setToolSelected(GravityEnum.Center, lnPen);
            drawingViewPresenter.ChangeDrawingColor(R.color.color_pink_draw_pen);
            drawingViewPresenter.ChangeModeToDraw();
        });

        lnColorBlue.setOnClickListener(view -> {
            drawingViewPresenter.setToolSelected(GravityEnum.Center, lnPen);
            drawingViewPresenter.setColorSelected(GravityEnum.Right, lnColorBlue);
            drawingViewPresenter.ChangeDrawingColor(R.color.color_blue_draw_pen);
            drawingViewPresenter.ChangeModeToDraw();
        });

        lnEraser.setOnClickListener(view -> {
            drawingViewPresenter.setToolSelected(GravityEnum.Center, lnEraser);
            drawingViewPresenter.setColorUnSelected();
            drawingViewPresenter.setColorUnSelected();
            drawingViewPresenter.ChangeModeToERASER();
        });

        lnPen.setOnClickListener(view -> {
            drawingViewPresenter.setToolSelected(GravityEnum.Center, lnPen);
            drawingViewPresenter.setColorSelected(GravityEnum.Right, lnColorBlue);
            drawingViewPresenter.ChangeDrawingColor(R.color.color_blue_draw_pen);
            drawingViewPresenter.ChangeModeToDraw();
        });


    }

    private void initDrawingPresenter() {
        drawingViewPresenter = new DrawingViewPresenter(App.CurentActivity, mDrawView);
    }


    private void uploadDocument() {
        if (isValid()) {
            showDialogAppend();
        }
    }

    private boolean isValid() {
        boolean valid = true;
       /* documentAsByte = drawingViewPresenter.getDrawingAsByteArray();
        if (documentAsByte == null) {
            App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.error_no_data_for_upload), SnackBarEnum.SNACKBAR_LONG_TIME);
            valid = false;
        }*/
        return valid;
    }

    private void showDialogAppend() {
        dialogUserAndRole = new DialogUserAndRole(App.CurentActivity, -1, -1).setTitle(Resorse.getString(R.string.title_erja)).init(getSupportFragmentManager(), (List<StructureUserAndRoleDB>) CustomFunction.deepClone(userAndRolesMain), new ArrayList<>(), UserAndRoleEnum.SEND, new ListenerUserAndRoll() {
            @Override
            public void onSuccess(List<StructureUserAndRoleDB> structureUserAndRolesMain, List<StructureUserAndRoleDB> structureUserAndRolesSelect) {
            }

            @Override
            public void onSuccess(StructureAppendREQ structureAppendREQ) {
                creatDocument(structureAppendREQ);
            }

            @Override
            public void onFailed() {
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onCancel(final List<StructureUserAndRoleDB> tmpItemSelect) {
            }


        });

    }

    private void creatDocument(StructureAppendREQ structureAppendREQ) {
        App.getHandlerMainThread().postDelayed(() -> {
            loading.Show();
        }, TimeValue.SecondsInMilli());

        String strCurrentDate = CustomFunction.getCurentLocalDateTimeAsStringFormat();
        Date date = CustomFunction.getCurentDateTimeAsDateFormat(SimpleDateFormatEnum.DateTime_as_iso_8601.getValue());
        int ETC = (int) date.getTime();
        int EC = ETC - 1000;
        String subject = edtSubject.getText().toString();
        byte[] bytes = drawingViewPresenter.getDrawingAsByteArray();
        if (bytes != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(drawingViewPresenter.getFileAsBase64(bytes));
            structureAttach = new StructureAttach(stringBuilder, "CreateDocumentFromApp", ".png");
        }
        StructureCreateDocumentQueueBND createDocumentQueueBND = new StructureCreateDocumentQueueBND(-1,ETC, EC, subject, farzinPrefrences.getUserName(), getSenderFullName(), getRecieverFullName(structureAppendREQ), strCurrentDate);
        sendData(createDocumentQueueBND, structureAppendREQ);
    }
    //_____________________***********START CREAT DOCUMENT***********_______________________

    private void sendData(StructureCreateDocumentQueueBND createDocumentQueueBND, StructureAppendREQ structureAppendREQ) {

        new FarzinCreateDocumentPresenter().sendData(createDocumentQueueBND, new CreateDocumentListener() {

            @Override
            public void onSuccess(StructureImportDocIndicatorRES importDocIndicatorRES) {
                attachFile(importDocIndicatorRES.getETC(), importDocIndicatorRES.getEntityCode(), false, structureAppendREQ);
            }

            @Override
            public void onSuccessAddToQueue() {
                attachFileAddToQueue(createDocumentQueueBND.getETC(), createDocumentQueueBND.getEC(), true, structureAppendREQ);
            }

            @Override
            public void onFailed(String message) {
                creatDocumentAddToQueue(createDocumentQueueBND, structureAppendREQ);
            }

            @Override
            public void onCancel() {
                creatDocumentAddToQueue(createDocumentQueueBND, structureAppendREQ);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void creatDocumentAddToQueue(StructureCreateDocumentQueueBND createDocumentQueueBND, StructureAppendREQ structureAppendREQ) {
        new FarzinCreateDocumentPresenter().addToQueue(createDocumentQueueBND, new CreateDocumentListener() {


            @Override
            public void onSuccess(StructureImportDocIndicatorRES importDocIndicatorRES) {

            }

            @Override
            public void onSuccessAddToQueue() {

                attachFileAddToQueue(createDocumentQueueBND.getETC(), createDocumentQueueBND.getEC(), true, structureAppendREQ);
            }

            @Override
            public void onFailed(String message) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFinish() {

            }
        });

    }


    //_____________________***********END CREAT DOCUMENT***********_______________________


    //_____________________***********START ATTACH***********_______________________

    private void attachFile(int ETC, int EC, boolean isLock, StructureAppendREQ structureAppendREQ) {

        StructureDocumentAttachFileBND structureDocumentAttachFileBND = new StructureDocumentAttachFileBND(ETC, EC, false, structureAttach.getName(), structureAttach.getFileAsStringBuilder(), structureAttach.getFileExtension(), DependencyTypeEnum.Document, "");
        structureDocumentAttachFileBND.setLock(isLock);
        new FarzinDocumentAttachFilePresenter().sendData(structureDocumentAttachFileBND, new DocumentAttachFileListener() {
            @Override
            public void onSuccess() {
                append(ETC, EC, false, structureAppendREQ);
            }

            @Override
            public void onSuccessAddToQueue() {
                attachFileAddToQueue(ETC, EC, isLock, structureAppendREQ);
            }

            @Override
            public void onFailed(String message) {
                attachFileAddToQueue(ETC, EC, isLock, structureAppendREQ);
            }

            @Override
            public void onCancel() {
                attachFileAddToQueue(ETC, EC, isLock, structureAppendREQ);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void attachFileAddToQueue(int ETC, int EC, boolean isLock, StructureAppendREQ structureAppendREQ) {
        StructureDocumentAttachFileBND structureDocumentAttachFileBND = new StructureDocumentAttachFileBND(ETC, EC, false, structureAttach.getName(), structureAttach.getFileAsStringBuilder(), structureAttach.getFileExtension(), DependencyTypeEnum.Document, "");
        structureDocumentAttachFileBND.setLock(isLock);
        new FarzinDocumentAttachFilePresenter().documentAttachFileAddToQueue(structureDocumentAttachFileBND, new DocumentAttachFileListener() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onSuccessAddToQueue() {
                String strDataREQ = new CustomFunction().ConvertObjectToString(structureAppendREQ);
                appendAddToQueue(ETC, EC, true, strDataREQ);
            }

            @Override
            public void onFailed(String message) {
                String strDataREQ = new CustomFunction().ConvertObjectToString(structureAppendREQ);
                appendAddToQueue(ETC, EC, true, strDataREQ);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onFinish() {

            }
        });

    }

    //_____________________***********END ATTACH***********_______________________


    //_____________________***********START APPEND***********_______________________
    private void append(int ETC, int EC, boolean isLock, final StructureAppendREQ structureAppendREQ) {
        String strDataREQ = new CustomFunction().ConvertObjectToString(structureAppendREQ);
        if (App.networkStatus != NetworkStatus.Connected && App.networkStatus != NetworkStatus.Syncing) {
            appendAddToQueue(ETC, EC, isLock, strDataREQ);
        } else {
            new CartableDocumentAppendPresenter().AppendDocument(ETC, EC, structureAppendREQ, new DocumentOperatorQueuesListener() {
                @Override
                public void onSuccess() {
                    onFinish();
                }

                @Override
                public void onFailed(String message) {
                    String strDataREQ = new CustomFunction().ConvertObjectToString(structureAppendREQ);
                    appendAddToQueue(ETC, EC, isLock, strDataREQ);
                }

                @Override
                public void onCancel() {
                    String strDataREQ = new CustomFunction().ConvertObjectToString(structureAppendREQ);
                    appendAddToQueue(ETC, EC, isLock, strDataREQ);
                }

                @Override
                public void onFinish() {
                    App.getHandlerMainThread().post(() -> {
                        loading.Hide();
                        App.ShowMessage().ShowToast(Resorse.getString(R.string.the_command_was_successful), ToastEnum.TOAST_LONG_TIME);
                        Back();
                    });


                }
            });

        }
    }

    private void appendAddToQueue(int ETC, int EC, boolean isLock, String strDataREQ) {

        StructureDocumentOperatorsQueueBND structureDocumentOperatorsQueueBND = new StructureDocumentOperatorsQueueBND(ETC, EC, isLock, DocumentOperatoresTypeEnum.Append, strDataREQ);
        new FarzinCartableQuery().saveDocumentOperatorsQueue(structureDocumentOperatorsQueueBND, new DocumentOperatorQueuesListener() {

            @Override
            public void onSuccess() {
                App.getHandlerMainThread().post(() -> {
                    loading.Hide();
                    App.ShowMessage().ShowToast(Resorse.getString(R.string.the_command_was_placed_in_the_queue), ToastEnum.TOAST_LONG_TIME);
                    Back();
                });
            }

            @Override
            public void onFailed(String message) {
                tryDocumentOperatorAddToQueue(ETC, EC, isLock, strDataREQ);
            }

            @Override
            public void onCancel() {
                tryDocumentOperatorAddToQueue(ETC, EC, isLock, strDataREQ);
            }

            @Override
            public void onFinish() {

            }
        });
    }

    private void tryDocumentOperatorAddToQueue(int ETC, int EC, boolean isLock, String strDataREQ) {
        App.getHandlerMainThread().postDelayed(() -> appendAddToQueue(ETC, EC, isLock, strDataREQ), FailedDelay);
    }

    //_____________________***********END APPEND***********_______________________


    private String getSenderFullName() {
        StructureUserAndRoleDB structureUserAndRoleDB;
        if (farzinPrefrences.getRoleID() > 0) {
            structureUserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(farzinPrefrences.getUserID(), farzinPrefrences.getRoleID());
        } else {
            structureUserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(farzinPrefrences.getUserID());
        }
        String fullName = structureUserAndRoleDB.getFirstName() + " " + structureUserAndRoleDB.getLastName() + " [" + structureUserAndRoleDB.getRoleName() + "]";
        return fullName;
    }

    private String getRecieverFullName(StructureAppendREQ structureAppendREQ) {
        String fullName = "";

        for (int i = 0; i < structureAppendREQ.getStructurePersonsREQ().size(); i++) {
            StructurePersonREQ structurePersonREQ = structureAppendREQ.getStructurePersonsREQ().get(i);
            fullName = fullName + structurePersonREQ.getFullName() + " || ";
        }
        int index = fullName.lastIndexOf(" || ");
        fullName = fullName.substring(0, index);

        return fullName;
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.creat_document_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem actionErja = menu.findItem(R.id.action_erja);
        Drawable drawable = new CustomFunction(App.CurentActivity).ChengeDrawableColor(R.drawable.ic_erja, R.color.color_White);
        actionErja.setIcon(drawable);
        actionErja.setOnMenuItemClickListener(menuItem -> {
            closeKeyboard();
            uploadDocument();

            return false;
        });

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Back();
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Back();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void Back() {
        if (!App.isLoading) {
            Intent returnIntent = new Intent();
            //setResult(settingType, returnIntent);
            Finish(App.CurentActivity);
        }

    }
}

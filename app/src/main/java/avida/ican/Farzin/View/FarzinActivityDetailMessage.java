package avida.ican.Farzin.View;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.util.ArrayList;

import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.StructureDetailMessageBND;
import avida.ican.Farzin.Model.Structure.Bundle.StructureFwdReplyBND;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageFileDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureReceiverDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Presenter.FarzinMetaDataQuery;
import avida.ican.Farzin.Presenter.Message.FarzinGetMessageAttachmentPresenter;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.View.Dialog.DialogShowMore;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.Interface.Message.MessageQueryAttachmentListListener;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.Model.ChangeXml;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Adapter.AdapterAttach;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.Ican.View.Interface.ListenerAdapterAttach;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;

public class FarzinActivityDetailMessage extends BaseToolbarActivity {
    @BindView(R.id.txt_subject)
    TextView txtSubject;
    @BindView(R.id.txt_time)
    TextView txtTime;
    @BindView(R.id.txt_date)
    TextView txtDate;
    @BindView(R.id.txt_role_name)
    TextView txtRoleName;
    @BindView(R.id.txt_name)
    TextView txtName;
    @BindView(R.id.rcv_attach)
    RecyclerView rcvAttach;
    @BindView(R.id.img_delet)
    ImageView imgDelet;
    @BindView(R.id.img_forward)
    ImageView imgForward;
    @BindView(R.id.img_reply)
    ImageView imgReply;
    @BindView(R.id.ln_reply)
    LinearLayout lnReply;
    @BindView(R.id.ex_txt_message)
    ExpandableTextView exTxtMessage;
    @BindView(R.id.ln_main)
    LinearLayout lnMain;
    @BindView(R.id.ln_divider)
    LinearLayout lnDivider;
    @BindView(R.id.ln_main_download)
    LinearLayout lnMainDownload;
    @BindView(R.id.ln_download)
    LinearLayout lnDownload;
    @BindView(R.id.av_loading_download)
    AVLoadingIndicatorView avLoadingDownload;

    @BindString(R.string.TitleDetailMessage)
    String Title;


    public static StructureDetailMessageBND structureDetailMessageBND = new StructureDetailMessageBND();
    private ArrayList<StructureAttach> structureAttaches = new ArrayList<>();
    private AdapterAttach adapterAttach;
    private File file;
    private Bundle bundleObject = new Bundle();
    private String receiveNames = "";
    private FarzinGetMessageAttachmentPresenter farzinGetMessageAttachmentPresenter;
    private String messageContent = "";

    @Override
    protected void onResume() {
        if (file != null) {
            boolean b = file.delete();
            if (b) {
                file = null;
            }

        }
        super.onResume();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.farzin_activity_detail_message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lnMainDownload.setVisibility(View.GONE);
        FarzinMessageQuery farzinMessageQuery = new FarzinMessageQuery();
        farzinGetMessageAttachmentPresenter = new FarzinGetMessageAttachmentPresenter(intPresenterListener());
        farzinMessageQuery.UpdateMessageStatus(structureDetailMessageBND.getId(), Status.READ);
        farzinMessageQuery.updateMessageIsNewStatus(structureDetailMessageBND.getId(), false);

        if (structureDetailMessageBND.getMessageType() == Type.SENDED) {
            lnReply.setVisibility(View.GONE);
        }
        initTollBar(Title);
        txtSubject.setText(structureDetailMessageBND.getSubject());
        if (structureDetailMessageBND.getReceiverRoleName().isEmpty()) {
            txtRoleName.setVisibility(View.GONE);
        } else {
            txtRoleName.setText(structureDetailMessageBND.getReceiverRoleName());
        }

        txtDate.setText(structureDetailMessageBND.getSent_date());
        txtTime.setText(structureDetailMessageBND.getSent_time());
        messageContent = new ChangeXml().saxCharEncoder(structureDetailMessageBND.getContent());
        messageContent = new ChangeXml().charDecoder(messageContent);
        if (messageContent.contains("<div ")) {
            messageContent = messageContent.replaceAll("<div>", "").replaceAll("</div>", "") + "</div>";
        }
        new CustomFunction(this).setHtmlText(exTxtMessage, messageContent);
        CheckAttachment();

        new CustomFunction(this).ChengeDrawableColorAndSetToImageView(imgDelet, R.drawable.ic_trash, R.color.color_Icons);
        imgReply.setOnClickListener(view -> gotoActivityWriteMessage(true));
        imgForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (structureDetailMessageBND.getAttachmentCount() > 0 && !structureDetailMessageBND.isFilesDownloaded()){
                    App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.error_forward_message),SnackBarEnum.SNACKBAR_LONG_TIME);
                }else{
                    gotoActivityWriteMessage(false);
                }

            }
        });

        if (structureDetailMessageBND.getStructureReceiverDBS().size() > 1) {
            txtRoleName.setVisibility(View.GONE);
            StructureUserAndRoleDB structureUserAndRoleDB;
            for (StructureReceiverDB structureReceiverDB : structureDetailMessageBND.getStructureReceiverDBS()) {
                if (structureReceiverDB.getRole_id() <= 0) {
                    structureUserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(structureReceiverDB.getUser_id());
                } else {
                    structureUserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(structureReceiverDB.getUser_id(), structureReceiverDB.getRole_id());
                }
                receiveNames = receiveNames + "" + structureUserAndRoleDB.getFirstName() + " " + structureUserAndRoleDB.getLastName() + " ,";
            }
            receiveNames = receiveNames.substring(0, receiveNames.length() - 1);
        } else {
            receiveNames = structureDetailMessageBND.getReceiverFullName();
        }

        txtName.setText(receiveNames);
        txtName.setOnClickListener(view -> {
            if (structureDetailMessageBND.getStructureReceiverDBS().size() > 1) {
                DialogShowMore dialogShowMore = new DialogShowMore(App.CurentActivity);
                dialogShowMore.setData(Resorse.getString(R.string.receivers), receiveNames);
                dialogShowMore.Creat();
            }
        });

        lnDownload.setOnClickListener(view -> {
            lnDownload.setVisibility(View.GONE);
            avLoadingDownload.setVisibility(View.VISIBLE);
            farzinGetMessageAttachmentPresenter.getData(structureDetailMessageBND.getId(), structureDetailMessageBND.getMain_id());

        });
    }

    private MessageQueryAttachmentListListener intPresenterListener() {

        MessageQueryAttachmentListListener messageQueryAttachmentListListener = new MessageQueryAttachmentListListener() {
            @Override
            public void newData(ArrayList<StructureMessageFileDB> structureMessageFilesDB) {
                runOnUiThread(() -> {
                    initRcvAndAdapterAttachment(structureMessageFilesDB);
                    structureDetailMessageBND.setFilesDownloaded(true);
                    App.fragmentMessageList.UpdateLastMessageShowData();
                });
            }

            @Override
            public void noData() {
                runOnUiThread(() -> {
                    structureDetailMessageBND.setFilesDownloaded(true);
                    lnMainDownload.setVisibility(View.GONE);
                });
            }

            @Override
            public void onFailed(String message) {
                runOnUiThread(() -> {
                    lnMainDownload.setVisibility(View.VISIBLE);
                    lnDownload.setVisibility(View.VISIBLE);
                    avLoadingDownload.setVisibility(View.GONE);
                    App.getHandlerMainThread().postDelayed(() -> {
                        App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.error_get_file_faild), SnackBarEnum.SNACKBAR_LONG_TIME);

                    }, 100);
                });
            }

            @Override
            public void onCancel() {
                runOnUiThread(() -> {
                    lnMainDownload.setVisibility(View.VISIBLE);
                    lnDownload.setVisibility(View.VISIBLE);
                    avLoadingDownload.setVisibility(View.GONE);
                    App.getHandlerMainThread().postDelayed(() -> {
                        App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.error_get_file_faild), SnackBarEnum.SNACKBAR_LONG_TIME);
                    }, 100);
                });
            }
        };
        return messageQueryAttachmentListListener;
    }

    private void gotoActivityWriteMessage(boolean isReply) {
        StructureFwdReplyBND structureFwdReplyBND = new StructureFwdReplyBND(structureDetailMessageBND.getSender_user_id(), structureDetailMessageBND.getSender_role_id(), structureDetailMessageBND.getReceiverFullName(), structureDetailMessageBND.getReceiverRoleName(), structureDetailMessageBND.getSubject(), messageContent, structureAttaches, isReply);
        FarzinActivityWriteMessage.structureFwdReplyBND = structureFwdReplyBND;
        // bundleObject.putSerializable(PutExtraEnum.ISFwdReplyMessage.getValue(), structureFwdReplyBND);
        Intent intent = new Intent(App.CurentActivity, FarzinActivityWriteMessage.class);
        intent.putExtra(PutExtraEnum.ISFwdReplyMessage.getValue(), true);
        goToActivity(intent);
    }

    private void CheckAttachment() {
        if (structureDetailMessageBND.getAttachmentCount() > 0) {
            if (structureDetailMessageBND.isFilesDownloaded()) {
                initRcvAndAdapterAttachment(structureDetailMessageBND.getMessage_files());
            } else {
                lnMainDownload.setVisibility(View.VISIBLE);
            }
        } else {
            lnDivider.setVisibility(View.GONE);
        }

        /* if (structureDetailMessageBND.getMessage_files() == null || structureDetailMessageBND.getMessage_files().size() <= 0) {
            lnDivider.setVisibility(View.GONE);
        } else {
        }*/

    }

    private void initRcvAndAdapterAttachment(ArrayList<StructureMessageFileDB> message_files) {
        lnMainDownload.setVisibility(View.GONE);
        lnDownload.setVisibility(View.VISIBLE);
        avLoadingDownload.setVisibility(View.GONE);
        for (StructureMessageFileDB MessageFileDB : message_files) {
            StructureAttach structureAttach = new StructureAttach(MessageFileDB.getFile_path(), MessageFileDB.getFile_name(), MessageFileDB.getFile_extension());
            structureAttaches.add(structureAttach);
        }
        GridLayoutManagerWithSmoothScroller linearLayoutManagerWithSmoothScroller = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        rcvAttach.setLayoutManager(linearLayoutManagerWithSmoothScroller);
        adapterAttach = new AdapterAttach(App.CurentActivity, structureAttaches, false, new ListenerAdapterAttach() {
            @Override
            public void onOpenFile(StructureAttach structureAttach) {
                file = new CustomFunction(App.CurentActivity).OpenFile(structureAttach);
            }

            @Override
            public void onDeletFile(StructureAttach structureAttach) {
                structureAttaches.remove(structureAttach);
            }
        });
        rcvAttach.setAdapter(adapterAttach);
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

}

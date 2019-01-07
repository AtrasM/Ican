package avida.ican.Farzin.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.io.File;
import java.util.ArrayList;

import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.StructureDetailMessageBND;
import avida.ican.Farzin.Model.Structure.Bundle.StructureFwdReplyBND;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageFileDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureReceiverDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Presenter.FarzinMetaDataQuery;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Adapter.AdapterAttach;
import avida.ican.Ican.View.Custom.Base64EncodeDecodeFile;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
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
    @BindView(R.id.ln_loading)
    LinearLayout lnLoading;

    @BindString(R.string.TitleDetailMessage)
    String Title;


    public static StructureDetailMessageBND structureDetailMessageBND = new StructureDetailMessageBND();
    private ArrayList<StructureAttach> structureAttaches = new ArrayList<>();
    private AdapterAttach adapterAttach;
    private File file;
    private Bundle bundleObject = new Bundle();
    private String receiveNames = "";

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
/*        Bundle bundleObject = getIntent().getExtras();
        structureDetailMessageBND = (StructureDetailMessageBND) bundleObject.getSerializable(PutExtraEnum.BundleMessage.getValue());
       */
        lnMain.setVisibility(View.GONE);
        lnLoading.setVisibility(View.VISIBLE);
        if (structureDetailMessageBND.getMessageType() == Type.SENDED) {
            lnReply.setVisibility(View.GONE);
        }
        initTollBar(Title);
        txtSubject.setText(structureDetailMessageBND.getSubject());
        txtRoleName.setText(structureDetailMessageBND.getReceiverRoleName());
        txtDate.setText(structureDetailMessageBND.getSent_date());
        txtTime.setText(structureDetailMessageBND.getSent_time());
        new CustomFunction(this).setHtmlText(exTxtMessage, structureDetailMessageBND.getContent());
        initRcvAndAdapter();

        new CustomFunction(this).ChengeDrawableColorAndSetToImageView(imgDelet, R.drawable.ic_trash, R.color.color_Icons);
        imgReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoActivityWriteMessage(true);
            }
        });
        imgForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoActivityWriteMessage(false);
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
        lnMain.setVisibility(View.VISIBLE);
        lnLoading.setVisibility(View.GONE);
    }

    private void gotoActivityWriteMessage(boolean isReply) {
        StructureFwdReplyBND structureFwdReplyBND = new StructureFwdReplyBND(structureDetailMessageBND.getSender_user_id(), structureDetailMessageBND.getSender_role_id(), structureDetailMessageBND.getReceiverFullName(), structureDetailMessageBND.getReceiverRoleName(), structureDetailMessageBND.getSubject(), exTxtMessage.getText().toString(), structureAttaches, isReply);
        FarzinActivityWriteMessage.structureFwdReplyBND = structureFwdReplyBND;
        // bundleObject.putSerializable(PutExtraEnum.ISFwdReplyMessage.getValue(), structureFwdReplyBND);
        Intent intent = new Intent(App.CurentActivity, FarzinActivityWriteMessage.class);
        intent.putExtra(PutExtraEnum.ISFwdReplyMessage.getValue(), true);
        goToActivity(intent);
    }

    private void initRcvAndAdapter() {
        for (StructureMessageFileDB MessageFileDB : structureDetailMessageBND.getMessage_files()) {
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
        });
        rcvAttach.setAdapter(adapterAttach);

        // adapterAttach.addAll(structureAttaches);
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

}

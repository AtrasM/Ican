package avida.ican.Farzin.View;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.acra.ACRA;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import avida.ican.Farzin.FarzinBroadcastReceiver;
import avida.ican.Farzin.Model.CustomLogger;
import avida.ican.Farzin.Model.Enum.DataSyncingNameEnum;
import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Interface.ChangeActiveRoleListener;
import avida.ican.Farzin.Model.Interface.MetaDataSyncListener;
import avida.ican.Farzin.Model.Interface.MetaDataParentSyncListener;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.StructureCartableDocumentDetailBND;
import avida.ican.Farzin.Model.Structure.Bundle.StructureDetailMessageBND;
import avida.ican.Farzin.Model.Structure.Database.Cartable.StructureInboxDocumentDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureReceiverDB;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureUserAndRoleDB;
import avida.ican.Farzin.Model.Structure.Database.StructureUserRoleDB;
import avida.ican.Farzin.Model.Structure.Request.StructureUserRoleREQ;
import avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoom.StructureChatRoomModelRES;
import avida.ican.Farzin.Model.Structure.Response.Chat.ChatRoomMessages.StructureChatRoomMessageModelRES;
import avida.ican.Farzin.Presenter.Cartable.ChangeActiveRolePresenter;
import avida.ican.Farzin.Presenter.Cartable.FarzinCartableQuery;
import avida.ican.Farzin.Presenter.FarzinMetaDataQuery;
import avida.ican.Farzin.Presenter.FarzinMetaDataSync;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.Presenter.Queue.FarzinCartableDocumentPublicQueuePresenter;
import avida.ican.Farzin.Presenter.SignalR.SignalRService;
import avida.ican.Farzin.Presenter.SignalR.SignalRSingleton;
import avida.ican.Farzin.View.Dialog.DialogDataSyncing;
import avida.ican.Farzin.View.Dialog.DialogNewData;
import avida.ican.Farzin.View.Dialog.DialogUserRoleList;
import avida.ican.Farzin.View.Enum.CurentProject;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.Fragment.FragmentCartable;
import avida.ican.Farzin.View.Fragment.FragmentHome;
import avida.ican.Farzin.View.Fragment.Message.FragmentMessageList;
import avida.ican.Farzin.View.Interface.ListenerDialogNewData;
import avida.ican.Farzin.View.Interface.ListenerDialogUserRole;
import avida.ican.Farzin.View.Interface.ListenerFilter;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseActivity;
import avida.ican.Ican.BaseNavigationDrawerActivity;
import avida.ican.Ican.View.Custom.CheckNetworkAvailability;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.Ican.View.Interface.ListenerNetwork;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;
import microsoft.aspnet.signalr.client.Action;

import static avida.ican.Farzin.View.Enum.SettingEnum.MANUALLY;
import static avida.ican.Farzin.View.Enum.SettingEnum.SYNC;

public class FarzinActivityMain extends BaseNavigationDrawerActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.bottom_navigation)
    BottomNavigationViewEx bottomNavigation;
    @BindView(R.id.container)
    LinearLayout container;
    @BindView(R.id.ln_loading)
    LinearLayout lnLoading;

    @BindString(R.string.title_home)
    String Title;
    private boolean menuShow = false;
    private FarzinMetaDataSync farzinMetaDataSync;
    private FarzinBroadcastReceiver broadcastReceiver;
    private IntentFilter intentFilter;
    private static final String TAB_CARTABLE = "tab_cartable";
    private static final String TAB_DASHBOARD = "tab_dashboard";
    private static final String TAB_Message = "tab_message_list";
    private String mCurrentTab = "";
    private Fragment CurentFragment = null;
    private FarzinPrefrences farzinPrefrences;
    private MenuItem actionfilter;
    private ListenerFilter listenerFilter;
    private boolean isFilter = false;
    private boolean filtering = false;
    private String curentTab = "";
    private int ACTIVITYSETTING = 200;
    private ChangeActiveRoleListener mChangeActiveRoleListener;
    private FarzinMetaDataQuery farzinMetaDataQuery;
    private String notificationTag = "";
    private SignalRService mService;
    private boolean mBound = false;
    private FirebaseJobDispatcher dispatcher;
    private DialogNewData dialogNewData;
    private Bundle bundleObject = new Bundle();
    private int DOCUMENTDETAILCODE = 001;

    @Override
    protected int getLayoutResource() {
        return R.layout.farzin_activity_main;
    }

    @Override
    protected void onResume() {
        CheckNetWork();
        if (App.fragmentStacks != null && App.fragmentStacks.size() > 0) {
            if (actionfilter != null) {
                if (curentTab == TAB_DASHBOARD) {
                    if (App.needToReGetMessageList || App.needToReGetCartableDocumentList) {
                        FragmentHome fragmentHome = (FragmentHome) App.fragmentStacks.get(TAB_DASHBOARD).lastElement();
                        fragmentHome.reGetDataFromLocal();
                    }

                    try {
                        actionfilter.setVisible(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        if (dialogNewData != null && !dialogNewData.isShowing()) {
            checkNewData();
        }

        super.onResume();
    }

    private void checkNewData() {
        if (notificationTag == null || notificationTag.isEmpty()) {
            long messageCount = new FarzinMessageQuery().getNewMessageCount();
            long cartableDocumentCount = new FarzinCartableQuery().getNewCartableDocumentCount();
            if (messageCount > 0 || cartableDocumentCount > 0) {
                dialogNewData.show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        farzinPrefrences = getFarzinPrefrences();
        String actorID = farzinPrefrences.getActorIDToken();
        String userID = farzinPrefrences.getUserIDToken();
        String cookie = farzinPrefrences.getCookie();
        Log.i("cookie", "Ican cookie= " + cookie);
        //Log.i("GetRoleList", "actorID= " + actorID + " userID= " + userID);
        isFilter = getIntent().getBooleanExtra(PutExtraEnum.IsFilter.getValue(), false);
        notificationTag = getIntent().getStringExtra(PutExtraEnum.Notification.getValue());
        App.needToReGetCartableDocumentList = false;
        App.needToReGetMessageList = false;
        container.setVisibility(View.GONE);
        lnLoading.setVisibility(View.VISIBLE);
        CheckNetWork();
        farzinMetaDataQuery = new FarzinMetaDataQuery(App.CurentActivity);
        initNavigationBar(Title, R.menu.main_drawer);
        App.setCurentProject(CurentProject.Farzin);
        broadcastReceiver = App.initBroadcastReceiver();
        initFragmentStack();
        App.getHandlerMainThread().postDelayed(() -> initFarzinMetaDataSyncClass(), TimeValue.SecondsInMilli());
        initChangeActiveRoleListener();
        dialogNewData = new DialogNewData(this, new ListenerDialogNewData() {
            @Override
            public void showMessageList() {
                selectMessageFragment();
            }

            @Override
            public void showCartableDocumentList() {

            }
        }).init();

       /* App.getHandlerMainThread().postDelayed(new Runnable() {
            @Override
            public void run() {
                SignalRSingleton.getInstance().getChatHubProxy().invoke("SendMessage", "e2fb501f623748daaa7d32c220498e15", "Test3", "randomID1").done(new Action<Void>() {
                    @Override
                    public void run(Void aVoid) throws Exception {
                        aVoid.toString();
                    }
                });


                SignalRSingleton.getInstance().getChatHubProxy().on("receiveMessage", (structureChatRoomMessageModelRES, structureChatRoomModelRES) -> {
                    structureChatRoomMessageModelRES.getChatRoomID();
                    CustomLogger.setLog("chat receiveMessage on getMessageContent= " + structureChatRoomMessageModelRES.getMessageContent());
                }, StructureChatRoomMessageModelRES.class, StructureChatRoomModelRES.class);
            }
        }, TimeValue.SecondsInMilli() * 2);*/


    }

    private void initChangeActiveRoleListener() {
        ArrayList<StructureUserRoleDB> structureUserRoleDBS = farzinMetaDataQuery.getUserRole();
        if (structureUserRoleDBS != null && structureUserRoleDBS.size() > 0) {
            changeVisibilityItem(R.id.nav_change_active_role, true);
        } else {
            changeVisibilityItem(R.id.nav_change_active_role, false);
        }

        mChangeActiveRoleListener = new ChangeActiveRoleListener() {
            @Override
            public void doProcess() {
                String roleIDToken = farzinPrefrences.getRoleIDToken();
                showDialogUserRoles(roleIDToken, farzinPrefrences.getUserIDToken());
            }

            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailed(String error) {

            }
        };
        mainChangeActiveRoleListener = mChangeActiveRoleListener;
    }

    private void initFragmentStack() {
        App.fragmentStacks = new HashMap<>();
        App.fragmentStacks.put(TAB_DASHBOARD, new Stack<>());
        App.fragmentStacks.put(TAB_Message, new Stack<>());
        App.fragmentStacks.put(TAB_CARTABLE, new Stack<>());
        CurentFragment = new FragmentHome();
     /*   Title = "خانه";
        selectedTab(Title, R.menu.main_drawer, TAB_HOME);*/
        initBottomNavigation();
    }

    private void initBottomNavigation() {
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        bottomNavigation.setTextVisibility(false);
        bottomNavigation.setCurrentItem(0);
        bottomNavigation.setCurrentItem(2);
        bottomNavigation.setCurrentItem(1);
        //staticbottomNavigation = bottomNavigation;
    }


    private void initFarzinMetaDataSyncClass() {
        farzinMetaDataSync = new FarzinMetaDataSync().RunONschedule(new MetaDataSyncListener() {
            @Override
            public void onSuccess(DataSyncingNameEnum dataSyncingNameEnum) {
                if (BaseActivity.dialogDataSyncing != null) {
                    BaseActivity.dialogDataSyncing.serviceGetDataFinish(dataSyncingNameEnum);
                }
            }

            @Override
            public void onFailed(DataSyncingNameEnum dataSyncingNameEnum) {

            }

            @Override
            public void onCancel(DataSyncingNameEnum dataSyncingNameEnum) {

            }

            @Override
            public void onFinish() {
                setNavTitle();
                checkAppSynced();
            }
        });

        if (!farzinPrefrences.isMetaDataForFirstTimeSync()) {
            callDialogDataSyncing(DataSyncingNameEnum.getMetaDataCount(), true);
        }

    }

    private void setNavTitle() {
        StructureUserAndRoleDB structureUserAndRoleDB = new StructureUserAndRoleDB();
        if (farzinPrefrences.getRoleID() > 0) {
            structureUserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(farzinPrefrences.getUserID(), farzinPrefrences.getRoleID());
        } else {
            structureUserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(farzinPrefrences.getUserID());
        }
        String name = structureUserAndRoleDB.getFirstName() + " " + structureUserAndRoleDB.getLastName();
        String roleName = "[" + structureUserAndRoleDB.getRoleName() + "]";
        setNavHeadeViewTitle(name, roleName);
    }

    private void checkAppSynced() {
        App.getHandlerMainThread().postDelayed(() -> {
            if (!farzinPrefrences.isDataForFirstTimeSync()) {
                App.canBack = true;
                Intent intent = new Intent(App.CurentActivity, ActivitySetting.class);
                intent.putExtra(PutExtraEnum.SettingType.getValue(), SYNC.getValue());
                goToActivityForResult(intent, ACTIVITYSETTING);
            } else {
                runServices();
                container.setVisibility(View.VISIBLE);
                lnLoading.setVisibility(View.GONE);
            }
        }, 100);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        String Title = "";
        switch (item.getItemId()) {

            case R.id.navigation_home: {
                Title = "خانه";
                selectedTab(Title, R.menu.main_drawer, TAB_DASHBOARD);
                break;
            }
            case R.id.navigation_message: {
                Title = "سیستم پیام";
                selectedTab(Title, R.menu.main_drawer, TAB_Message);

                break;
            }
            case R.id.navigation_cartable: {
                Title = "کارتابل";
                selectedTab(Title, R.menu.main_drawer, TAB_CARTABLE);
                break;
            }

        }
        //StartFragment(selectedFragment, BACK_STACK_ROOT_TAG);
        return true;
    }

    private void selectedTab(String title, int drawer, String tabId) {
        mCurrentTab = tabId;

        if (App.fragmentStacks.get(tabId).size() == 0) {
         /*   if (drawer > 0) {
                changeDrawerMenu(drawer);
            }*/

            /*
             *    First time this tab is selected. So add first fragment of that tab.
             *    Dont need animation, so that argument is false.
             *    We are adding a new fragment which is not present in stack. So add to stack is true.
             */

            switch (tabId) {

                case TAB_DASHBOARD: {
                    pushFragments(tabId, new FragmentHome(), true);
                    break;
                }
                case TAB_CARTABLE: {
                    App.fragmentCartable = new FragmentCartable().newInstance(getSupportFragmentManager());
                    Fragment fragment = App.fragmentCartable;
                    pushFragments(tabId, fragment, true);
                    break;
                }

                case TAB_Message: {
                    App.fragmentMessageList = new FragmentMessageList().newInstance(getSupportFragmentManager());
                    Fragment fragment = App.fragmentMessageList;
                    pushFragments(tabId, fragment, true);
                    break;
                }

            }

        } else {
            /*
             *    We are switching tabs, and target tab is already has atleast one fragment.
             *    No need of animation, no need of stack pushing. Just show the target fragment
             */
            pushFragments(tabId, App.fragmentStacks.get(tabId).lastElement(), false);
        }

        App.getHandlerMainThread().post(new Runnable() {
            @Override
            public void run() {
                setTollbarTitle(title);
            }
        });

    }

    public void pushFragments(String tab, Fragment fragment, boolean shouldAdd) {

        if (shouldAdd) {
            curentTab = tab;
            App.fragmentStacks.get(tab).push(fragment);
            if (tab == TAB_Message) {
                try {
                    actionfilter.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(CurentFragment)
                    .add(R.id.frm_main, fragment)
                    .commitAllowingStateLoss();
        } else {
            if (!tab.equals(curentTab)) {
                curentTab = tab;
                if (tab == TAB_DASHBOARD) {
                    if (dialogNewData != null && !dialogNewData.isShowing()) {
                        checkNewData();
                    }
                    if (App.needToReGetMessageList || App.needToReGetCartableDocumentList) {
                        FragmentHome fragmentHome = (FragmentHome) App.fragmentStacks.get(tab).lastElement();
                        fragmentHome.reGetDataFromLocal();
                        fragment = fragmentHome;
                    }

                    try {
                        actionfilter.setVisible(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (tab == TAB_CARTABLE) {
                    if (App.needToReGetCartableDocumentList) {
                        FragmentCartable fragmentCartable = (FragmentCartable) App.fragmentStacks.get(tab).lastElement();
                        fragmentCartable.reGetDataFromLocal();
                        fragment = fragmentCartable;
                        App.needToReGetCartableDocumentList = false;
                    }
                    try {
                        actionfilter.setVisible(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (tab == TAB_Message) {
                    if (App.needToReGetMessageList) {
                        if (!filtering) {
                            filterReceiveMessage();
                        }
                        App.needToReGetMessageList = false;
                    }

                }
                getSupportFragmentManager()
                        .beginTransaction()
                        .hide(CurentFragment)
                        .show(fragment)
                        .commitAllowingStateLoss();


            }
        }
        CurentFragment = fragment;

    }

    public void popFragments() {
        /*
         *    Select the second last fragment in current tab's stack..
         *    which will be shown after the fragment transaction given below
         */
        Fragment fragment = App.fragmentStacks.get(mCurrentTab).elementAt(App.fragmentStacks.get(mCurrentTab).size() - 2);

        /*pop current fragment from stack.. */
        // fragment=   App.fragmentStacks.get(mCurrentTab).pop();

        /* We have the target fragment in hand.. Just show it.. Show a standard navigation animation*/
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frm_main, fragment)
                .commit();

    }


    public void selectMessageFragment(boolean isFilter) {

        if (bottomNavigation != null) {
            this.isFilter = isFilter;
            bottomNavigation.setCurrentItem(2);
        }
        if (isFilter) {
            filterReceiveMessage();
        }

    }

    public void selectMessageFragment() {
        if (bottomNavigation != null) {
            bottomNavigation.setCurrentItem(2);
            try {
                if (!filtering) {
                    filterReceiveMessage();
                }
            } catch (Exception e) {
                e.printStackTrace();
                ACRA.getErrorReporter().handleSilentException(e);
            }
        }
    }


    public void selectCartableDocumentFragment() {
        if (bottomNavigation != null) {
            bottomNavigation.setCurrentItem(0);
        }
        try {
            actionfilter.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void CheckNetWork() {
        new CheckNetworkAvailability().isServerAvailable(new ListenerNetwork() {
            @Override
            public void onConnected() {
                App.networkStatus = NetworkStatus.Connected;
            }

            @Override
            public void disConnected() {
                App.networkStatus = NetworkStatus.WatingForNetwork;
            }

            @Override
            public void onFailed() {
                App.networkStatus = NetworkStatus.WatingForNetwork;
            }
        });
    }

    public void setFilter(boolean isFilter) {
        this.isFilter = isFilter;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        actionfilter = menu.findItem(R.id.action_filter);

        try {
            actionfilter.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        actionfilter.setOnMenuItemClickListener(menuItem -> {
            if (!filtering) {
                isFilter = !isFilter;
                selectMessageFragment();
            }

            return false;
        });


        checkNotification();

        return super.onPrepareOptionsMenu(menu);
    }

    private void checkNotification() {
        if (notificationTag != null && !notificationTag.isEmpty()) {
            if (notificationTag.equals(PutExtraEnum.MultyCartableDocument.getValue())) {
                long cartableDocumentCount = new FarzinCartableQuery().getNewCartableDocumentCount();
                if (cartableDocumentCount == 1) {
                    StructureInboxDocumentDB structureInboxDocumentDB = new FarzinCartableQuery().getNewCartableDocument();
                    gotoDocumentDetail(structureInboxDocumentDB);
                } else if (cartableDocumentCount > 1) {
                    dialogNewData.show();
                } else {
                    selectCartableDocumentFragment();
                }

            } else if (notificationTag.equals(PutExtraEnum.MultyMessage.getValue())) {
                long messageCount = new FarzinMessageQuery().getNewMessageCount();
                if (messageCount == 1) {
                    StructureMessageDB structureMessageDB = new FarzinMessageQuery().getNewMessage();
                    goToMessageDetail(structureMessageDB);
                } else if (messageCount > 1) {
                    dialogNewData.show();
                } else {
                    selectMessageFragment();
                }
            }
            notificationTag = "";
        }
        notificationTag = "";
    }

    private void gotoDocumentDetail(final StructureInboxDocumentDB item) {
        FarzinCartableQuery farzinCartableQuery = new FarzinCartableQuery();
        App.getHandlerMainThread().post(() -> {
            if (farzinCartableQuery.IsDocumentExist(item.getReceiverCode())) {
                StructureCartableDocumentDetailBND cartableDocumentDetailBND = new StructureCartableDocumentDetailBND(item.getEntityTypeCode(), item.getEntityCode(), item.getSendCode(), item.getReceiverCode(), item.getReceiveDate(), item.getTitle(), item.getSenderName(), item.getSenderRoleName(), item.getEntityNumber(), item.getImportEntityNumber(), item.isbInWorkFlow());
                farzinCartableQuery.updateCartableDocumentIsNewStatus(item.getId(), false);
                bundleObject.putSerializable(PutExtraEnum.BundleCartableDocumentDetail.getValue(), cartableDocumentDetailBND);
                Intent intent = new Intent(App.CurentActivity, FarzinActivityCartableDocumentDetail.class);
                intent.putExtras(bundleObject);
                goToActivityForResult(intent, DOCUMENTDETAILCODE);
                farzinCartableQuery.updateCartableDocumentStatus(item.getId(), Status.READ);
            }
        });
    }

    public void goToMessageDetail(StructureMessageDB item) {


        StructureUserAndRoleDB structureUserAndRoleDB;
        if (item.getSender_role_id() > 0) {
            structureUserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(item.getSender_user_id(), item.getSender_role_id());
        } else {
            structureUserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(item.getSender_user_id());
        }

        String Name = "" + structureUserAndRoleDB.getFirstName() + " " + structureUserAndRoleDB.getLastName();
        String RoleName = "";
        if (item.getSender_role_id() > 0) {
            RoleName = "[ " + structureUserAndRoleDB.getRoleName() + " ]";
        }

        String[] splitDateTime = CustomFunction.MiladyToJalaly(item.getSent_date().toString()).split(" ");
        final String date = splitDateTime[0];
        final String time = splitDateTime[1];
        StructureDetailMessageBND structureDetailMessageBND = new StructureDetailMessageBND(item.getId(), item.getMain_id(), item.getSender_user_id(), item.getSender_role_id(), Name, RoleName, item.getSubject(), item.getContent(), date, time, new ArrayList<>(item.getMessage_files()), item.getAttachmentCount(), item.isFilesDownloaded(), new ArrayList<StructureReceiverDB>(), Type.RECEIVED);

        FarzinMessageQuery farzinMessageQuery = new FarzinMessageQuery();
        FarzinActivityDetailMessage.structureDetailMessageBND = structureDetailMessageBND;
        goToActivity(FarzinActivityDetailMessage.class);

        farzinMessageQuery.UpdateMessageStatus(structureDetailMessageBND.getId(), Status.READ);
        farzinMessageQuery.updateMessageIsNewStatus(structureDetailMessageBND.getId(), false);

    }

    @Override
    public void invalidateOptionsMenu() {
        super.invalidateOptionsMenu();
    }

    private void filterReceiveMessage() {

        filtering = true;

        FragmentMessageList fragmentMessageList = (FragmentMessageList) App.fragmentStacks.get(TAB_Message).lastElement();

        try {
            if (actionfilter != null) {
                actionfilter.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        listenerFilter = mIsFilter -> {
            isFilter = mIsFilter;
            if (actionfilter != null) {
                if (isFilter) {
                    actionfilter.setIcon(Resorse.getDrawable(R.drawable.ic_filter));
                } else {
                    actionfilter.setIcon(Resorse.getDrawable(R.drawable.ic_unfilter));
                }
            }
        };

        if (isFilter) {
            if (actionfilter != null) {
                actionfilter.setIcon(Resorse.getDrawable(R.drawable.ic_filter));
            }
            fragmentMessageList.filterMessage(Status.UnRead, isFilter, listenerFilter);

        } else {
            if (actionfilter != null) {
                actionfilter.setIcon(Resorse.getDrawable(R.drawable.ic_unfilter));
            }
            fragmentMessageList.filterMessage(null, isFilter, listenerFilter);
        }

        App.getHandlerMainThread().postDelayed(() -> filtering = false, 1000);

    }

    private void callDialogDataSyncing(int ServiceCount, boolean isMetaData) {

        runOnUiThread(() -> {

            if (!isMetaData) {
                runBaseService();
            }

            BaseActivity.dialogDataSyncing = new DialogDataSyncing(App.CurentActivity, ServiceCount, isMetaData, new MetaDataParentSyncListener() {
                @Override
                public void onFinish() {
                    runOnUiThread(() -> {
                        if (!isMetaData) {
                            runServices();
                            container.setVisibility(View.VISIBLE);
                            lnLoading.setVisibility(View.GONE);
                            if (bottomNavigation != null) {
                                bottomNavigation.setCurrentItem(0);
                                bottomNavigation.setCurrentItem(1);
                            }
                        }
                    });
                }

                @Override
                public void onFailed() {

                }

                @Override
                public void onCancel() {

                }

            });
            BaseActivity.dialogDataSyncing.Creat();
        });

    }

    private void runBaseService() {
        broadcastReceiver.runBaseService();
    }

    private void stopServices() {
        broadcastReceiver.stopServices();
    }

    private void runServices() {
        String lastDate = farzinPrefrences.getConfirmationListDataSyncDate();
        if (lastDate == null || lastDate.isEmpty()) {
            lastDate = farzinPrefrences.getCartableDocumentDataSyncDate();
            farzinPrefrences.putConfirmationListDataSyncDate(lastDate);
        }
        broadcastReceiver.runServices();
    }


    private void showDialogUserRoles(String curentRoleIDToken, String mainUserCodeToken) {
        lnLoading.setVisibility(View.GONE);
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
            if (BaseActivity.dialog != null) {
                BaseActivity.dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String userName = farzinPrefrences.getUserName();

        DialogUserRoleList dialogUserRoleList = new DialogUserRoleList(App.CurentActivity, farzinMetaDataQuery.getUserRole(), true);

        dialogUserRoleList.setOnListener(new ListenerDialogUserRole() {
            @Override
            public void onSuccess(StructureUserRoleDB userRoleDB) {
                if (!userRoleDB.getRoleIDToken().equals(curentRoleIDToken)) {
                    StructureUserRoleREQ structureUserRoleREQ = new StructureUserRoleREQ(userName, curentRoleIDToken, userRoleDB.getRoleIDToken(), userRoleDB.getRoleID(), userRoleDB.getActorIDToken(), mainUserCodeToken);
                    ChangeActiveRole(structureUserRoleREQ);
                }

            }

            @Override
            public void onCancel() {

            }
        });

        App.getHandlerMainThread().postDelayed(() -> dialogUserRoleList.Show(), 500);
    }


    private void ChangeActiveRole(StructureUserRoleREQ structureUserRoleREQ) {
        if (new FarzinCartableDocumentPublicQueuePresenter().userHasQueue()) {
            App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.invalid_user_has_queue), SnackBarEnum.SNACKBAR_INDEFINITE);
        } else {
            lnLoading.setVisibility(View.VISIBLE);
            App.canBack = false;

            ChangeActiveRolePresenter changeActiveRolePresenter = new ChangeActiveRolePresenter();
            changeActiveRolePresenter.CallRequest(structureUserRoleREQ, new ChangeActiveRoleListener() {
                @Override
                public void doProcess() {
                }

                @Override
                public void onSuccess() {
                    try {
                        App.getFarzinDatabaseHelper().clearDocumentOperatorsQueue();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    farzinPrefrences.putRoleIDToken(structureUserRoleREQ.getNewRoleID());
                    farzinPrefrences.putUserBaseInfo(structureUserRoleREQ.getUserName(), structureUserRoleREQ.getRoleID(), structureUserRoleREQ.getActorIDToken());
                    App.canBack = true;
                    lnLoading.setVisibility(View.GONE);
                    recreate();
                }

                @Override
                public void onFailed(String error) {
                    App.canBack = true;
                    lnLoading.setVisibility(View.GONE);
                }

            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ACTIVITYSETTING) {
            if (resultCode == SYNC.getValue()) {
                App.getHandlerMainThread().postDelayed(() -> {
                    callDialogDataSyncing(DataSyncingNameEnum.getDataSyncingCount(), false);
                }, TimeValue.SecondsInMilli());

            } else if (resultCode == MANUALLY.getValue()) {
                runServices();
            }
        }
    }

    @Override
    protected void onDestroy() {
        try {
            App.activityStacks = null;
            App.getFarzinBroadCastReceiver().restartServices();
            farzinMetaDataSync.onDestory();
        } catch (Exception e) {
            e.printStackTrace();
            ACRA.getErrorReporter().handleSilentException(e);
        }

        super.onDestroy();
    }

/*    @Override
    protected void onStop() {
        // Unbind from the service
        if (signalRPresenter.getBound()) {
            unbindService(signalRPresenter.mConnection);
            signalRPresenter.setBound(false);
        }
        super.onStop();
    }*/

    @Override
    public void onBackPressed() {
        if (App.fragmentStacks.get(mCurrentTab).size() == 1) {
            // We are already showing firstf fragment of current tab, so when back pressed, we will finish this activity..
            if (mCurrentTab == TAB_DASHBOARD) {
                FinishNavigationActivity();
            } else {
                if (bottomNavigation != null) {
                    bottomNavigation.setCurrentItem(1);
                }
            }

            return;
        }

        /* Goto previous fragment in navigation stack of this tab */
        popFragments();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (!getDrawer().isDrawerOpen(GravityCompat.START)) {
                if (App.fragmentStacks.get(mCurrentTab).size() == 1) {
                    // We are already showing first fragment of current tab, so when back pressed, we will finish this activity..
                    if (mCurrentTab == TAB_DASHBOARD) {
                        FinishNavigationActivity();
                    } else {
                        if (bottomNavigation != null) {
                            bottomNavigation.setCurrentItem(1);
                        }
                    }

                }
            } else {
                getDrawer().closeDrawer(GravityCompat.START);
            }

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }


}

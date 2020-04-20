package avida.ican.Farzin.View;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import avida.ican.Farzin.Model.Structure.Bundle.chat.StructureChatHubProxyONBND;
import avida.ican.Farzin.Model.Structure.Database.Chat.RoomMessage.StructureChatRoomMessageDB;
import avida.ican.Farzin.Chat.RoomMessage.FarzinChatRoomMessagePresenter;
import avida.ican.Farzin.View.Enum.Chat.ChatPutExtraEnum;
import avida.ican.Farzin.View.Fragment.Chat.FragmentChatRoomMessageList;
import avida.ican.Farzin.View.Interface.Chat.RoomMessage.ChatRoomMessageDataListener;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseChatToolbarActivity;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.View.Adapter.ViewPagerAdapter;
import avida.ican.Ican.View.Custom.Animator;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;


public class FarzinActivityChatRoomMessage extends BaseChatToolbarActivity {
    @Nullable
    @BindView(R.id.smart_tabLayout)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.mview_pager)
    ViewPager mViewPager;
    @BindView(R.id.ln_loading)
    LinearLayout lnLoading;
    @BindString(R.string.titleChatRoom)
    String Title;

    private FragmentChatRoomMessageList fragmentChatRoomMessageList;
    private FragmentManager mfragmentManager;
    private int privateStart = 0;
    private int GroupStart = 0;
    private int channelStart = 0;
    private boolean windowSizeIsMax = false;
    private Animator animator;
    private FarzinChatRoomMessagePresenter farzinChatRoomMessagePresenter;
    private String chatRoomId;
    private BroadcastReceiver updateUIReciver;
    public static StructureChatHubProxyONBND chatHubProxyONBND = new StructureChatHubProxyONBND();

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.farzin_activity_chat_room;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.canBack = true;
        chatRoomId = getIntent().getStringExtra(ChatPutExtraEnum.RoomMessageIDString.getValue());
        mfragmentManager = getSupportFragmentManager();
        animator = new Animator(App.CurentActivity);
        initTollBar(Title);
        initView();
        initUpdateUiReceiver();
    }


    @SuppressLint("SetTextI18n")
    private void initView() {
        initViewPagerFragment();
    }

    private void initViewPagerFragment() {
        fragmentChatRoomMessageList = new FragmentChatRoomMessageList().newInstance(App.CurentActivity, chatRoomId);
        initTab();
    }

    private void initTab() {
        assert smartTabLayout != null;
        smartTabLayout.setCustomTabView(R.layout.layout_txt_tab, R.id.txt_title_tab);
        ViewPagerAdapter adapter = new ViewPagerAdapter(mfragmentManager);
        adapter.addFrag(fragmentChatRoomMessageList, R.string.titleChatRoomPrivate);
        mViewPager.setAdapter(adapter);
        // mViewPager.setOffscreenPageLimit(3);//where 3 is the amount of pages to keep in memory on either side of the current page.
        smartTabLayout.setViewPager(mViewPager);
        smartTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 1: {
                        break;
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1: {
                        //fragmentCartableHameshList.checkQueue();
                        break;
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        initPresenter();
    }

    private void initPresenter() {
        farzinChatRoomMessagePresenter = new FarzinChatRoomMessagePresenter(new ChatRoomMessageDataListener() {
            @Override
            public void downloadedReplyData(StructureChatRoomMessageDB structureChatRoomMessageDB) {

            }

            @Override
            public void inputMessage(StructureChatRoomMessageDB structureChatRoomMessageDB) {

            }

            @Override
            public void newData(StructureChatRoomMessageDB structureChatRoomMessageDB) {
                App.CurentActivity.runOnUiThread(() -> {
                    lnLoading.setVisibility(View.GONE);
                    reGetDataFromLocal();
                });
            }

            @Override
            public void noData() {
                App.CurentActivity.runOnUiThread(() -> {
                    lnLoading.setVisibility(View.GONE);
                    reGetDataFromLocal();
                });
            }
        });
        checkData();
    }

    private void checkData() {
        if (App.networkStatus == NetworkStatus.Connected) {
           /* if (!farzinChatRoomMessagePresenter.hasChatRoomMessageData(chatRoomId)) {
                lnLoading.setVisibility(View.VISIBLE);
                farzinChatRoomMessagePresenter.getDataFromServer(chatRoomId);
            }  */
            lnLoading.setVisibility(View.VISIBLE);
            farzinChatRoomMessagePresenter.getDataFromServer(chatRoomId);
        }

    }

 /*   private void initDataFromLocal() {
        lnLoading.setVisibility(View.VISIBLE);
        fragmentChatRoomMessageList.initData();

        lnLoading.setVisibility(View.GONE);
    }*/

    private void reGetDataFromLocal() {
        lnLoading.setVisibility(View.VISIBLE);
        fragmentChatRoomMessageList.reGetDataFromLocal();

        lnLoading.setVisibility(View.GONE);
    }

    private void initUpdateUiReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ChatPutExtraEnum.MessageReceiver.getValue());
        updateUIReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //UI update here
                if (intent != null) {
                    if (intent.getAction().equals(ChatPutExtraEnum.MessageReceiver.getValue())) {
                        Bundle bundleObject = getIntent().getExtras();
                        //StructureChatHubProxyONBND chatHubProxyONBND = (StructureChatHubProxyONBND) bundleObject.getSerializable(ChatPutExtraEnum.MessageBND.getValue());
                        switch (chatHubProxyONBND.getMessageActionsEnum()) {
                            case ReceiveMessage: {
                                fragmentChatRoomMessageList.receiveMessageOnline(chatHubProxyONBND);
                                break;
                            }
                            case ShowSendMessage: {
                                fragmentChatRoomMessageList.showSendMessageOnline(chatHubProxyONBND);
                                break;
                            }
                        }
                    }

                }
                chatHubProxyONBND = new StructureChatHubProxyONBND();
            }
        };
        registerReceiver(updateUIReciver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateUIReciver);           //<-- Unregister to avoid memoryleak
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
      /*  if (requestCode == AttachFileCODE && resultCode == RESULT_OK) {
            mViewPager.setCurrentItem(3);
            fragmentZanjireMadrak.reGetData();
        }*/
    }


}

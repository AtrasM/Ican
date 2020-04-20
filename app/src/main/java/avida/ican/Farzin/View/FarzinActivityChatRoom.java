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


import avida.ican.Farzin.Model.Enum.Chat.ChatRoomTypeEnum;
import avida.ican.Farzin.Model.Structure.Database.Chat.Room.StructureChatRoomDB;
import avida.ican.Farzin.Chat.Room.FarzinChatRoomPresenter;
import avida.ican.Farzin.View.Enum.Chat.ChatPutExtraEnum;
import avida.ican.Farzin.View.Fragment.Chat.FragmentChatRoomList;
import avida.ican.Farzin.View.Interface.Chat.Room.ChatRoomDataListener;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseChatToolbarActivity;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.View.Adapter.ViewPagerAdapter;
import avida.ican.Ican.View.Custom.Animator;
import avida.ican.Ican.View.Custom.TimeValue;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;


public class FarzinActivityChatRoom extends BaseChatToolbarActivity {
    @Nullable
    @BindView(R.id.smart_tabLayout)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.mview_pager)
    ViewPager mViewPager;
    @BindView(R.id.ln_loading)
    LinearLayout lnLoading;
    @BindString(R.string.titleChatRoom)
    String Title;

    private FragmentChatRoomList fragmentChatRoomPrivate;
    private FragmentChatRoomList fragmentChatRoomGroup;
    private FragmentChatRoomList fragmentChatRoomChannel;
    private FragmentManager mfragmentManager;
    private int privateStart = 0;
    private int GroupStart = 0;
    private int channelStart = 0;
    private boolean windowSizeIsMax = false;
    private Animator animator;
    private FarzinChatRoomPresenter farzinChatRoomPresenter;
    private ChatRoomTypeEnum tmpChatRoomTypeEnum = ChatRoomTypeEnum.All;
    private BroadcastReceiver updateUIReciver;

    @Override
    protected void onResume() {

        if (App.hasChatRoomChanged != ChatRoomTypeEnum.NoType) {
            try {
                App.CurentActivity.runOnUiThread(() -> reGetDataFromLocal(App.hasChatRoomChanged));
                App.hasChatRoomChanged = ChatRoomTypeEnum.NoType;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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
        fragmentChatRoomPrivate = new FragmentChatRoomList().newInstance(App.CurentActivity, ChatRoomTypeEnum.Private);
        fragmentChatRoomGroup = new FragmentChatRoomList().newInstance(App.CurentActivity, ChatRoomTypeEnum.Room);
        fragmentChatRoomChannel = new FragmentChatRoomList().newInstance(App.CurentActivity, ChatRoomTypeEnum.Channele);
        initTab();
    }

    private void initTab() {
        assert smartTabLayout != null;
        smartTabLayout.setCustomTabView(R.layout.layout_txt_tab, R.id.txt_title_tab);
        ViewPagerAdapter adapter = new ViewPagerAdapter(mfragmentManager);
        adapter.addFrag(fragmentChatRoomPrivate, R.string.titleChatRoomPrivate);
        adapter.addFrag(fragmentChatRoomGroup, R.string.titleChatRoomGroup);
        adapter.addFrag(fragmentChatRoomChannel, R.string.titleChatRoomChannel);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);//where 3 is the amount of pages to keep in memory on either side of the current page.
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
        farzinChatRoomPresenter = new FarzinChatRoomPresenter(new ChatRoomDataListener() {
            @Override
            public void newData(StructureChatRoomDB structureChatRoomDB) {
                App.CurentActivity.runOnUiThread(() -> {
                    lnLoading.setVisibility(View.GONE);
                    reGetDataFromLocal(tmpChatRoomTypeEnum);
                });
            }

            @Override
            public void noData() {
                App.CurentActivity.runOnUiThread(() -> {
                    lnLoading.setVisibility(View.GONE);
                    reGetDataFromLocal(tmpChatRoomTypeEnum);
                });

            }
        });

        checkData(ChatRoomTypeEnum.All);
    }

    private void checkData(ChatRoomTypeEnum chatRoomTypeEnum) {

        tmpChatRoomTypeEnum = chatRoomTypeEnum;
        if (App.networkStatus == NetworkStatus.Connected) {
          /*  if (!farzinChatRoomPresenter.hasChatRoomData(ChatRoomTypeEnum.All)) {
                lnLoading.setVisibility(View.VISIBLE);
                farzinChatRoomPresenter.getDataFromServer();
            }*/
            lnLoading.setVisibility(View.VISIBLE);
            farzinChatRoomPresenter.getDataFromServer();

        }

    }

    private void reGetDataFromLocal(ChatRoomTypeEnum chatRoomTypeEnum) {
        if (chatRoomTypeEnum == ChatRoomTypeEnum.All) {
            fragmentChatRoomPrivate.reGetDataFromLocal();
            fragmentChatRoomGroup.reGetDataFromLocal();
            fragmentChatRoomChannel.reGetDataFromLocal();
        } else if (chatRoomTypeEnum == ChatRoomTypeEnum.Private) {
            fragmentChatRoomPrivate.reGetDataFromLocal();
        } else if (chatRoomTypeEnum == ChatRoomTypeEnum.Room) {
            fragmentChatRoomGroup.reGetDataFromLocal();
        } else if (chatRoomTypeEnum == ChatRoomTypeEnum.Channele) {
            fragmentChatRoomChannel.reGetDataFromLocal();
        }

        lnLoading.setVisibility(View.GONE);
    }

   /* private void initDataFromLocal(ChatRoomTypeEnum chatRoomTypeEnum) {
        lnLoading.setVisibility(View.VISIBLE);
        if (chatRoomTypeEnum == ChatRoomTypeEnum.All) {
            fragmentChatRoomPrivate.initDataFromLocal();
            fragmentChatRoomGroup.initDataFromLocal();
            fragmentChatRoomChannel.initDataFromLocal();
        } else if (chatRoomTypeEnum == ChatRoomTypeEnum.Private) {
            fragmentChatRoomPrivate.initDataFromLocal();
        } else if (chatRoomTypeEnum == ChatRoomTypeEnum.Room) {
            fragmentChatRoomGroup.initDataFromLocal();
        } else if (chatRoomTypeEnum == ChatRoomTypeEnum.Channele) {
            fragmentChatRoomChannel.initDataFromLocal();
        }

        lnLoading.setVisibility(View.GONE);
    }*/

    private void reGetDataFromLocal(int chatRoomTypeEnum) {
        if (chatRoomTypeEnum == ChatRoomTypeEnum.All.getIntValue()) {
            fragmentChatRoomPrivate.reGetDataFromLocal();
            fragmentChatRoomGroup.reGetDataFromLocal();
            fragmentChatRoomChannel.reGetDataFromLocal();
        } else if (chatRoomTypeEnum == ChatRoomTypeEnum.Private.getIntValue()) {
            fragmentChatRoomPrivate.reGetDataFromLocal();
        } else if (chatRoomTypeEnum == ChatRoomTypeEnum.Room.getIntValue()) {
            fragmentChatRoomGroup.reGetDataFromLocal();
        } else if (chatRoomTypeEnum == ChatRoomTypeEnum.Channele.getIntValue()) {
            fragmentChatRoomChannel.reGetDataFromLocal();
        }

        lnLoading.setVisibility(View.GONE);
    }

    private void initUpdateUiReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ChatPutExtraEnum.RoomReceiver.getValue());
        updateUIReciver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //UI update here
                if (intent != null) {
                    if (intent.getAction().equals(ChatPutExtraEnum.RoomReceiver.getValue())) {
                        int chatRoomTypeEnum = intent.getIntExtra(ChatPutExtraEnum.UpdateRoom.getValue(), -1);
                        String connectionState = intent.getStringExtra(ChatPutExtraEnum.ConnectionStatus.getValue());

                        if (chatRoomTypeEnum != ChatRoomTypeEnum.NoType.getIntValue()) {
                            App.getHandlerMainThread().postDelayed(() -> {
                                try {
                                    reGetDataFromLocal(chatRoomTypeEnum);
                                    App.hasChatRoomChanged = ChatRoomTypeEnum.NoType;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }, TimeValue.SecondsInMilli());

                        }
                    }

                }
            }
        };
        registerReceiver(updateUIReciver, filter);
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

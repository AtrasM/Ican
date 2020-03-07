package avida.ican.Farzin.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import avida.ican.Farzin.Model.Enum.Chat.ChatRoomTypeEnum;
import avida.ican.Farzin.Model.Structure.Database.Chat.Room.StructureChatRoomListDB;
import avida.ican.Farzin.Model.Structure.Database.Chat.RoomMessage.StructureChatRoomMessageDB;
import avida.ican.Farzin.Presenter.Chat.Room.FarzinChatRoomPresenter;
import avida.ican.Farzin.Presenter.Chat.RoomMessage.FarzinChatRoomMessagePresenter;
import avida.ican.Farzin.View.Enum.ChatPutExtraEnum;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.Fragment.Chat.FragmentChatRoomList;
import avida.ican.Farzin.View.Fragment.Chat.FragmentChatRoomMessageList;
import avida.ican.Farzin.View.Interface.Chat.Room.ChatRoomDataListener;
import avida.ican.Farzin.View.Interface.Chat.RoomMessage.ChatRoomMessageDataListener;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.View.Adapter.ViewPagerAdapter;
import avida.ican.Ican.View.Custom.Animator;
import avida.ican.Ican.View.Enum.NetworkStatus;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;


public class FarzinActivityChatRoomMessage extends BaseToolbarActivity {
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
        lnLoading.setVisibility(View.VISIBLE);
        initTollBar(Title);
        initView();
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
            public void newData(StructureChatRoomMessageDB structureChatRoomMessageDB) {
                App.CurentActivity.runOnUiThread(() -> {
                    lnLoading.setVisibility(View.GONE);
                    updateData(chatRoomId);
                });
            }

            @Override
            public void noData() {
                App.CurentActivity.runOnUiThread(() -> {
                    lnLoading.setVisibility(View.GONE);
                    updateData(chatRoomId);
                });
            }
        });
        getDataFromServer();
    }

    private void getDataFromServer() {
        lnLoading.setVisibility(View.VISIBLE);
        if (App.networkStatus == NetworkStatus.Connected) {
            farzinChatRoomMessagePresenter.getDataFromServer(chatRoomId);
        } else {
            lnLoading.setVisibility(View.GONE);
            // App.getHandlerMainThread().postDelayed(() -> updateData(chatRoomTypeEnum), TimeValue.SecondsInMilli());
        }

    }

    private void updateData(String chatRoomId) {
        fragmentChatRoomMessageList.reGetData(chatRoomId);

        lnLoading.setVisibility(View.GONE);
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

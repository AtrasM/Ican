package avida.ican.Farzin.View.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.StructureDetailMessageBND;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.View.Adapter.AdapterReceiveMessageList;
import avida.ican.Farzin.View.Adapter.AdapterSentMessageList;
import avida.ican.Farzin.View.FarzinActivityDetailMessage;
import avida.ican.Farzin.View.FarzinActivityWriteMessage;
import avida.ican.Farzin.View.Interface.Message.ListenerAdapterMessageList;
import avida.ican.Farzin.View.Interface.ListenerRcv;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.View.Adapter.ViewPagerAdapter;
import avida.ican.Ican.View.Dialog.Loading;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindView;

import static avida.ican.Ican.BaseActivity.goToActivity;

public class FragmentMessageList extends BaseFragment {
    private List<StructureMessageDB> mstructuresSentMessages = new ArrayList<>();
    private List<StructureMessageDB> mstructuresReceiveMessages = new ArrayList<>();
    private List<StructureMessageDB> mstructuresSearch = new ArrayList<>();
    private AdapterReceiveMessageList adapterReceiveMessageList;
    private AdapterSentMessageList adapterSentMessageList;
    private FragmentSentMessageList fragmentSentMessageList;
    private FragmentReceiveMessageList fragmentReceiveMessageList;
    private static FragmentManager mfragmentManager;


    @Nullable
    @BindView(R.id.smart_tabLayout)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.fab_new_msg)
    FloatingActionButton fabNewMsg;
    public static String Tag = "FragmentMessageList";
    private long ReceiveMessageStart = 0;
    private long SentMessageStart = 0;
    private long Count = 10;
    private int UserId = -1;
    private Bundle bundleObject = new Bundle();
    private Intent intent;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_message_list;
    }

    public FragmentMessageList() {
        // Required empty public constructor
    }


    public FragmentMessageList newInstance(FragmentManager fragmentManager) {
        mfragmentManager = fragmentManager;
        return this;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fabNewMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToActivity(FarzinActivityWriteMessage.class);
            }
        });
        initTab();
    }


    private void initTab() {
        initData();
        assert smartTabLayout != null;
        smartTabLayout.setCustomTabView(R.layout.layout_txt_tab, R.id.txt_title_tab);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(fragmentReceiveMessageList, R.string.receive_message);
        adapter.addFrag(fragmentSentMessageList, R.string.set_message);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1);
        smartTabLayout.setViewPager(viewPager);
    }

    private void initReceiveMessageAdapter(List<StructureMessageDB> structureMessageDBS) {
        mstructuresReceiveMessages = structureMessageDBS;
        adapterReceiveMessageList = null;
        adapterReceiveMessageList = new AdapterReceiveMessageList(structureMessageDBS, new ListenerAdapterMessageList() {
            @Override
            public void onDelet(StructureMessageDB structureMessageDB) {

                final Loading loading = new Loading(App.CurentActivity).Creat();
                loading.Show();
                App.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading.Hide();
                        App.ShowMessage().ShowToast("delet", ToastEnum.TOAST_SHORT_TIME);
                    }
                }, 2000);
            }

            @Override
            public void onItemClick(StructureDetailMessageBND structureDetailMessageBND) {
                goToMessageDetail(structureDetailMessageBND);
            }
        });

    }

    private void initSendMessageAdapter(List<StructureMessageDB> structureMessageDBS) {
        mstructuresSentMessages = structureMessageDBS;
        adapterSentMessageList = null;
        adapterSentMessageList = new AdapterSentMessageList(structureMessageDBS, new ListenerAdapterMessageList() {
            @Override
            public void onDelet(StructureMessageDB structureMessageDB) {
                final Loading loading = new Loading(App.CurentActivity).Creat();
                loading.Show();
                App.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading.Hide();
                        App.ShowMessage().ShowToast("delet", ToastEnum.TOAST_SHORT_TIME);
                    }
                }, 2000);
            }

            @Override
            public void onItemClick(StructureDetailMessageBND structureDetailMessageBND) {
                goToMessageDetail(structureDetailMessageBND);
            }
        });
        if (fragmentReceiveMessageList == null && fragmentSentMessageList == null) {
            initViewPagerFragment();
        }

    }

    private void goToMessageDetail(StructureDetailMessageBND structureDetailMessageBND) {
        FarzinActivityDetailMessage.structureDetailMessageBND=structureDetailMessageBND;
       // bundleObject.putSerializable(PutExtraEnum.BundleMessage.getValue(), structureDetailMessageBND);
        intent = new Intent(App.CurentActivity, FarzinActivityDetailMessage.class);
        //intent.putExtras(bundleObject);
        goToActivity(intent);
    }

    private void initData() {
    /*    StructureUserAndRoleDB structureUserAndRoleDB = new FarzinMetaDataQuery(App.CurentActivity).getUserInfo(getFarzinPrefrences().getUserName());
        int user_id = structureUserAndRoleDB.getUser_ID();
        int role_id = structureUserAndRoleDB.getRole_ID();*/
        UserId = getFarzinPrefrences().getUserID();
        mstructuresReceiveMessages = new FarzinMessageQuery().GetReceiveMessages(UserId, null, ReceiveMessageStart, Count);
        mstructuresSentMessages = new FarzinMessageQuery().GetSendMessages(UserId, null, SentMessageStart, Count);
        ReceiveMessageStart = mstructuresReceiveMessages.size();
        SentMessageStart = mstructuresSentMessages.size();
        new FarzinMessageQuery().UpdateAllNewMessageStatusToUnreadStatus();
        initReceiveMessageAdapter(mstructuresReceiveMessages);
        initSendMessageAdapter(mstructuresSentMessages);
    }


    private void GetAllNewReceiveMessage() {
        List<StructureMessageDB> ReceiveMessages = new ArrayList<>();
        ReceiveMessages = new FarzinMessageQuery().GetReceiveMessages(UserId, Status.IsNew, 0, -1);
        ReceiveMessageStart = ReceiveMessageStart + ReceiveMessages.size();
        mstructuresReceiveMessages.addAll(0, ReceiveMessages);
        adapterReceiveMessageList.updateData(0, ReceiveMessages);
        new FarzinMessageQuery().UpdateAllNewMessageStatusToUnreadStatus();
    }

    public void AddReceiveNewMessage(List<StructureMessageDB> ReceiveMessages) {
        ReceiveMessageStart = ReceiveMessageStart + ReceiveMessages.size();
        mstructuresReceiveMessages.addAll(0, ReceiveMessages);
        adapterReceiveMessageList.updateData(mstructuresReceiveMessages);

    }

    public void AddSendNewMessage(StructureMessageDB SendMessages) {
        SentMessageStart = SentMessageStart + 1;
        mstructuresSentMessages.add(0, SendMessages);

        adapterSentMessageList.updateData(0, SendMessages);

    }

    public void UpdateSendMessageStatus(StructureMessageDB SendMessages) {
        adapterSentMessageList.updateItem(SendMessages);
    }

    private void ReGetReceiveMessage(final SwipeRefreshLayout swipeRefreshLayout) {
        List<StructureMessageDB> ReceiveMessages = new ArrayList<>();
        ReceiveMessageStart = 0;
        ReceiveMessages = new FarzinMessageQuery().GetReceiveMessages(UserId, null, ReceiveMessageStart, Count);
        mstructuresReceiveMessages = new ArrayList<>(ReceiveMessages);
        ReceiveMessageStart = ReceiveMessageStart + ReceiveMessages.size();
        adapterReceiveMessageList.updateData(mstructuresReceiveMessages);

        App.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                fragmentReceiveMessageList.setCanLoading(true);
            }
        }, 1000);
    }

    private void ReGetSendMessage(final SwipeRefreshLayout swipeRefreshLayout) {
        List<StructureMessageDB> SentMessages = new ArrayList<>();
        SentMessageStart = 0;
        SentMessages = new FarzinMessageQuery().GetSendMessages(UserId, null, SentMessageStart, Count);
        SentMessageStart = SentMessageStart + SentMessages.size();
        mstructuresSentMessages = new ArrayList<>(SentMessages);
        adapterSentMessageList.updateData(mstructuresSentMessages);

        App.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                fragmentSentMessageList.setCanLoading(true);
            }
        }, 1000);
    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

    private void initViewPagerFragment() {

        fragmentReceiveMessageList = new FragmentReceiveMessageList().newInstance(App.CurentActivity, adapterReceiveMessageList, new ListenerRcv() {
            @Override
            public void onLoadData() {
                continueGetMessage(Type.RECEIVED);
            }

            @Override
            public void onSwipeRefresh(SwipeRefreshLayout swipeRefreshLayout) {
                ReGetReceiveMessage(swipeRefreshLayout);
            }
        });
        fragmentSentMessageList = new FragmentSentMessageList().newInstance(App.CurentActivity, adapterSentMessageList, new ListenerRcv() {
            @Override
            public void onLoadData() {
                continueGetMessage(Type.SENDED);
            }

            @Override
            public void onSwipeRefresh(SwipeRefreshLayout swipeRefreshLayout) {
                ReGetSendMessage(swipeRefreshLayout);
            }
        });


    }

    private void continueGetMessage(Type type) {
        final Loading loading = new Loading(App.CurentActivity).Creat();
        loading.Show();
        if (type == Type.RECEIVED) {
            List<StructureMessageDB> receiveMessages = new ArrayList<>();
            receiveMessages = new FarzinMessageQuery().GetReceiveMessages(UserId, null, ReceiveMessageStart, Count);
            if (receiveMessages.size() > 0) {
                ReceiveMessageStart = ReceiveMessageStart + receiveMessages.size();
                mstructuresReceiveMessages.addAll(receiveMessages);
                adapterReceiveMessageList.updateData(-1, receiveMessages);
                adapterReceiveMessageList.notifyDataSetChanged();
                fragmentReceiveMessageList.setCanLoading(true);
            } else {
                fragmentReceiveMessageList.setCanLoading(false);
            }
        } else {
            List<StructureMessageDB> sentMessages = new ArrayList<>();
            sentMessages = new FarzinMessageQuery().GetSendMessages(UserId, null, SentMessageStart, Count);
            if (sentMessages.size() > 0) {
                SentMessageStart = SentMessageStart + sentMessages.size();
                mstructuresSentMessages.addAll(sentMessages);
                adapterSentMessageList.updateData(-1, sentMessages);
                adapterSentMessageList.notifyDataSetChanged();
                fragmentSentMessageList.setCanLoading(true);
            } else {
                fragmentSentMessageList.setCanLoading(false);
            }
        }
        App.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.Hide();
            }
        }, 1000);
    }

    private void clearFragment() {
        android.support.v4.app.FragmentTransaction transaction = mfragmentManager.beginTransaction();
        transaction.remove(fragmentReceiveMessageList);
        transaction.remove(fragmentSentMessageList);
        transaction.commitAllowingStateLoss();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onStart() {
        super.onStart();
        /*try {
            mListener = (OnFragmentInteractionListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            GetAllNewReceiveMessage();
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDetach() {
        clearFragment();
        super.onDetach();
    }
/*
    *//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}

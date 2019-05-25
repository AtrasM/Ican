package avida.ican.Farzin.View.Fragment.Message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.View;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Status;
import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Prefrences.FarzinPrefrences;
import avida.ican.Farzin.Model.Structure.Bundle.StructureDetailMessageBND;
import avida.ican.Farzin.Model.Structure.Bundle.StructureLastShowMessageBND;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.Presenter.Message.FarzinMessageQuery;
import avida.ican.Farzin.View.Adapter.AdapterReceiveMessage;
import avida.ican.Farzin.View.Adapter.AdapterSentMessage;
import avida.ican.Farzin.View.FarzinActivityDetailMessage;
import avida.ican.Farzin.View.FarzinActivityWriteMessage;
import avida.ican.Farzin.View.Interface.ListenerFilter;
import avida.ican.Farzin.View.Interface.Message.ListenerAdapterMessageList;
import avida.ican.Farzin.View.Interface.ListenerRcv;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.View.Adapter.ViewPagerAdapter;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Dialog.Loading;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.R;
import butterknife.BindView;

import static avida.ican.Ican.BaseActivity.goToActivity;

public class FragmentMessageList extends BaseFragment {
    private List<StructureMessageDB> mstructuresSentMessages = new ArrayList<>();
    private List<StructureMessageDB> mstructuresReceiveMessages = new ArrayList<>();
    private List<StructureMessageDB> mstructuresSearch = new ArrayList<>();
    private AdapterReceiveMessage adapterReceiveMessage;
    private AdapterSentMessage adapterSentMessage;
    private FragmentSentMessageList fragmentSentMessageList;
    private FragmentReceiveMessageList fragmentReceiveMessageList;
    private static FragmentManager mfragmentManager;
    private static boolean isFilter = false;
    private static ListenerFilter listenerFilter;
    private StructureLastShowMessageBND structureLastShowMessageBND = new StructureLastShowMessageBND();

    public FloatingActionButton getFabNewMsg() {
        return fabNewMsg;
    }

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
        adapterReceiveMessage = new AdapterReceiveMessage(structureMessageDBS, new ListenerAdapterMessageList() {
            @Override
            public void onDelet(StructureMessageDB structureMessageDB) {
                App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.delete_action), SnackBarEnum.SNACKBAR_SHORT_TIME);
            }

            @Override
            public void onItemClick(StructureDetailMessageBND structureDetailMessageBND, int position) {
                goToMessageDetail(structureDetailMessageBND, position, Type.RECEIVED);
            }
        });

    }

    private void initSendMessageAdapter(List<StructureMessageDB> structureMessageDBS) {
        mstructuresSentMessages = structureMessageDBS;
        adapterSentMessage = new AdapterSentMessage(structureMessageDBS, new ListenerAdapterMessageList() {
            @Override
            public void onDelet(StructureMessageDB structureMessageDB) {
                final Loading loading = new Loading(App.CurentActivity).Creat();
                loading.Show();
                loading.Hide();
                App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.delete_action), SnackBarEnum.SNACKBAR_SHORT_TIME);
            }

            @Override
            public void onItemClick(StructureDetailMessageBND structureDetailMessageBND, int position) {
                goToMessageDetail(structureDetailMessageBND, position, Type.SENDED);
            }
        });
        if (fragmentReceiveMessageList == null && fragmentSentMessageList == null) {
            initViewPagerFragment();
        }

    }

    private void goToMessageDetail(StructureDetailMessageBND structureDetailMessageBND, int position, Type type) {
        structureLastShowMessageBND = new StructureLastShowMessageBND(structureDetailMessageBND.getId(), structureDetailMessageBND.getMain_id(), position, type);
        FarzinActivityDetailMessage.structureDetailMessageBND = structureDetailMessageBND;
        // bundleObject.putSerializable(PutExtraEnum.BundleMessage.getValue(), structureDetailMessageBND);
        intent = new Intent(App.CurentActivity, FarzinActivityDetailMessage.class);
        //intent.putExtras(bundleObject);
        goToActivity(intent);

        new FarzinMessageQuery().UpdateMessageStatus(structureDetailMessageBND.getId(), Status.READ);

      /*  if (isFilter) {
            reGetReceiveMessage(Status.UnRead, null);
        } else {
            reGetReceiveMessage(null, null);
        }*/

    }

    private void initData() {
        ReceiveMessageStart = 0;
        SentMessageStart = 0;
        UserId = getFarzinPrefrences().getUserID();
        if (isFilter) {
            mstructuresReceiveMessages = new FarzinMessageQuery().GetReceiveMessages(UserId, Status.UnRead, ReceiveMessageStart, Count);
            mstructuresSentMessages = new FarzinMessageQuery().GetSendMessages(UserId, Status.UnRead, SentMessageStart, Count);
        } else {
            mstructuresReceiveMessages = new FarzinMessageQuery().GetReceiveMessages(UserId, null, ReceiveMessageStart, Count);
            mstructuresSentMessages = new FarzinMessageQuery().GetSendMessages(UserId, null, SentMessageStart, Count);
        }
        ReceiveMessageStart = mstructuresReceiveMessages.size();
        SentMessageStart = mstructuresSentMessages.size();
        new FarzinMessageQuery().UpdateAllNewMessageStatusToUnreadStatus();
        if (adapterReceiveMessage == null) {
            initReceiveMessageAdapter(mstructuresReceiveMessages);
            initSendMessageAdapter(mstructuresSentMessages);
        } else {
            adapterReceiveMessage.updateData(mstructuresReceiveMessages);
            adapterSentMessage.AddNewData(mstructuresSentMessages);
        }
    }


    private void GetAllNewReceiveMessage() {
        List<StructureMessageDB> ReceiveMessages = new ArrayList<>();
        ReceiveMessages = new FarzinMessageQuery().GetReceiveMessages(UserId, Status.IsNew, 0, -1);
        ReceiveMessageStart = ReceiveMessageStart + ReceiveMessages.size();
        mstructuresReceiveMessages.addAll(0, ReceiveMessages);
        adapterReceiveMessage.updateData(0, ReceiveMessages);
        new FarzinMessageQuery().UpdateAllNewMessageStatusToUnreadStatus();
    }

    public void AddReceiveNewMessage(List<StructureMessageDB> ReceiveMessages) {
        ReceiveMessageStart = ReceiveMessageStart + ReceiveMessages.size();
        mstructuresReceiveMessages.addAll(0, ReceiveMessages);
        adapterReceiveMessage.updateData(mstructuresReceiveMessages);

    }

    public void UpdateLastMessageShowData() {
        StructureMessageDB structureMessageDB = new FarzinMessageQuery().GetMessage(structureLastShowMessageBND.getMain_id());
        if (structureMessageDB.getId() > 0) {
            if (structureLastShowMessageBND.getType() == Type.RECEIVED) {
                adapterReceiveMessage.updateData(structureLastShowMessageBND.getPosition(), structureMessageDB);
            } else if (structureLastShowMessageBND.getType() == Type.SENDED) {
                adapterSentMessage.updateData(structureLastShowMessageBND.getPosition(), structureMessageDB);
            }
        }

    }

    public void UpdateMessageData() {
        if (isFilter) {
            reGetSendMessage(Status.UnRead, null);
            reGetReceiveMessage(Status.UnRead, null);
        } else {
            reGetSendMessage(null, null);
            reGetReceiveMessage(null, null);
        }
       /* SentMessageStart = SentMessageStart + 1;
        mstructuresSentMessages.add(0, SendMessages);

        adapterSentMessage.AddNewData(0, SendMessages);*/

    }

    public void UpdateSendMessageStatus(StructureMessageDB SendMessages) {
        adapterSentMessage.AddNewData(0, SendMessages);
    }

    public void reGetReceiveMessage(Status status, final SwipeRefreshLayout swipeRefreshLayout) {
        List<StructureMessageDB> ReceiveMessages = new ArrayList<>();
        ReceiveMessageStart = 0;
        mstructuresReceiveMessages.clear();
        ReceiveMessages = new FarzinMessageQuery().GetReceiveMessages(UserId, status, ReceiveMessageStart, Count);
        mstructuresReceiveMessages = new ArrayList<>(ReceiveMessages);
        ReceiveMessageStart = ReceiveMessages.size();
        adapterReceiveMessage.updateData(mstructuresReceiveMessages);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }

        fragmentReceiveMessageList.setCanLoading(true);
    }


    public void reGetSendMessage(Status status, final SwipeRefreshLayout swipeRefreshLayout) {
        List<StructureMessageDB> SentMessages = new ArrayList<>();
        SentMessageStart = 0;
        mstructuresSentMessages.clear();
        SentMessages = new FarzinMessageQuery().GetSendMessages(UserId, status, SentMessageStart, Count);
        mstructuresSentMessages = new ArrayList<>(SentMessages);
        SentMessageStart = SentMessages.size();
        adapterSentMessage.AddNewData(mstructuresSentMessages);
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }

        fragmentSentMessageList.setCanLoading(true);
    }

    public void filterMessage(Status status, boolean isFilter, ListenerFilter listenerFilter) {
        this.isFilter = isFilter;
        this.listenerFilter = listenerFilter;
        if (adapterReceiveMessage == null) {
            initData();
        } else {
            List<StructureMessageDB> ReceiveMessages;
            List<StructureMessageDB> SendMessages;
            ReceiveMessageStart = 0;
            SentMessageStart = 0;
            ReceiveMessages = new FarzinMessageQuery().GetReceiveMessages(UserId, status, ReceiveMessageStart, Count);
            SendMessages = new FarzinMessageQuery().GetSendMessages(UserId, status, SentMessageStart, Count);
            mstructuresReceiveMessages = new ArrayList<>(ReceiveMessages);
            mstructuresSentMessages = new ArrayList<>(SendMessages);
            ReceiveMessageStart = ReceiveMessages.size();
            SentMessageStart = SendMessages.size();
            adapterReceiveMessage.updateData(mstructuresReceiveMessages);
            adapterSentMessage.AddNewData(mstructuresSentMessages);
            fragmentReceiveMessageList.setCanLoading(true);

        }


    }

    private FarzinPrefrences getFarzinPrefrences() {
        return new FarzinPrefrences().init();
    }

    private void initViewPagerFragment() {

        fragmentReceiveMessageList = new FragmentReceiveMessageList().newInstance(App.CurentActivity, adapterReceiveMessage, new ListenerRcv() {
            @Override
            public void onLoadData() {
                if (isFilter) {
                    continueGetMessage(Status.UnRead, Type.RECEIVED);
                } else {
                    continueGetMessage(null, Type.RECEIVED);
                }

            }

            @Override
            public void onSwipeRefresh(SwipeRefreshLayout swipeRefreshLayout) {
                //isFilter = false;
                if (listenerFilter != null) {
                    listenerFilter.isFilter(isFilter);
                }
                if (isFilter) {
                    reGetReceiveMessage(Status.UnRead, swipeRefreshLayout);
                } else {
                    reGetReceiveMessage(null, swipeRefreshLayout);
                }


            }
        });

        fragmentSentMessageList = new FragmentSentMessageList().newInstance(App.CurentActivity, adapterSentMessage, new ListenerRcv() {
            @Override
            public void onLoadData() {
                if (isFilter) {
                    continueGetMessage(Status.UnRead, Type.SENDED);
                } else {
                    continueGetMessage(null, Type.SENDED);
                }
            }

            @Override
            public void onSwipeRefresh(SwipeRefreshLayout swipeRefreshLayout) {
                //isFilter = false;
                if (listenerFilter != null) {
                    listenerFilter.isFilter(isFilter);
                }
                if (isFilter) {
                    reGetSendMessage(Status.UnRead, swipeRefreshLayout);
                } else {
                    reGetSendMessage(null, swipeRefreshLayout);
                }

            }
        });

    }

    private void continueGetMessage(Status status, Type type) {
        final Loading loading = new Loading(App.CurentActivity).Creat();
        loading.Show();
        if (type == Type.RECEIVED) {
            List<StructureMessageDB> receiveMessages;
            receiveMessages = new FarzinMessageQuery().GetReceiveMessages(UserId, status, ReceiveMessageStart, Count);
            if (receiveMessages.size() > 0) {
                ReceiveMessageStart = ReceiveMessageStart + receiveMessages.size();
                mstructuresReceiveMessages.addAll(receiveMessages);
                adapterReceiveMessage.updateData(-1, receiveMessages);
                adapterReceiveMessage.notifyDataSetChanged();
                fragmentReceiveMessageList.setCanLoading(true);
            } else {
                fragmentReceiveMessageList.setCanLoading(false);
            }
        } else {
            List<StructureMessageDB> sentMessages;
            sentMessages = new FarzinMessageQuery().GetSendMessages(UserId, status, SentMessageStart, Count);
            if (sentMessages.size() > 0) {
                SentMessageStart = SentMessageStart + sentMessages.size();
                mstructuresSentMessages.addAll(sentMessages);
                adapterSentMessage.AddNewData(-1, sentMessages);
                adapterSentMessage.notifyDataSetChanged();
                fragmentSentMessageList.setCanLoading(true);
            } else {
                fragmentSentMessageList.setCanLoading(false);
            }
        }
        loading.Hide();
    }

    private void clearFragment() {
        FragmentTransaction transaction = mfragmentManager.beginTransaction();
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
                    + " mustplement OnFragmentInteractionListener");
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

package avida.ican.Farzin.View.Fragment.Message;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import avida.ican.Farzin.Model.Enum.Type;
import avida.ican.Farzin.Model.Structure.Bundle.StructureDetailMessageBND;
import avida.ican.Farzin.Model.Structure.Database.Message.StructureMessageDB;
import avida.ican.Farzin.View.Adapter.Message.AdapterSentMessage;
import avida.ican.Farzin.View.Interface.ListenerRcv;
import avida.ican.Farzin.View.Interface.Message.ListenerAdapterMessageList;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.View.Custom.Animator;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.R;
import butterknife.BindView;

public class FragmentSentMessageList extends BaseFragment {

/*    @BindView(R.id.edt_search)
    EditText edtSearch;*/

    @BindView(R.id.rcv_main)
    RecyclerView rcvMain;
    @BindView(R.id.img_move_up)
    ImageView imgMoveUp;
    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout srlNewMessage;

    private Activity context;
    private AdapterSentMessage adapterSentMessage;
    private FragmentMessageList fragmentMessageList;
    public final String Tag = "FragmentSentMessageList";
    private ListenerRcv listenerRcv;
    private int[] pastVisiblesItems;
    private int visibleItemCount;
    private int totalItemCount;
    private boolean isshow = false;
    private Animator animator = null;
    private boolean canLoading = true;


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_sent_message_list;
    }

    public FragmentSentMessageList newInstance(Activity context, FragmentMessageList fragmentMessageList, ListenerRcv listenerRcv) {

        this.context = context;
        this.fragmentMessageList = fragmentMessageList;
        initAdapter();
        this.listenerRcv = listenerRcv;
        animator = new Animator(context);
        return this;
    }

    public AdapterSentMessage getAdapter() {
        return adapterSentMessage;
    }

    public void updateAdapterSendMessage(List<StructureMessageDB> mstructuresSentMessages) {
        if (this.adapterSentMessage != null) {
            this.adapterSentMessage.updateData(mstructuresSentMessages);
            adapterSentMessage.notifyDataSetChanged();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRcv();
        imgMoveUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gridLayoutManager.scrollToPositionWithOffset(2, 20);
                rcvMain.smoothScrollToPosition(0);
                imgMoveUp.setVisibility(View.GONE);
            }
        });

        srlNewMessage.setOnRefreshListener(() -> listenerRcv.onSwipeRefresh(srlNewMessage));
   /*     edtSearch.setFilters(new InputFilter[]{new CustomFunction().ignoreFirstWhiteSpace()});
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = charSequence.toString().trim();
                if (s.length() > 2) {
                    dialogUserAndRole.performSearch(s, false);
                } else if (s.length() == 2) {
                    dialogUserAndRole.performSearch(null, true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });*/
    }

    private void initAdapter() {
        adapterSentMessage = new AdapterSentMessage(new ArrayList<>(), new ListenerAdapterMessageList() {
            @Override
            public void onDelet(StructureMessageDB structureMessageDB) {
             /*   final Loading loading = new Loading(App.CurentActivity).Creat();
                loading.Show();
                loading.Hide();
                App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.delete_action), SnackBarEnum.SNACKBAR_SHORT_TIME);
           */
            }

            @Override
            public void onItemClick(StructureDetailMessageBND structureDetailMessageBND, int position) {
                fragmentMessageList.goToMessageDetail(structureDetailMessageBND, position, Type.SENDED);
            }
        });
    }


    private void initRcv() {
        final GridLayoutManagerWithSmoothScroller gridLayoutManager = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setReverseLayout(false);
        rcvMain.setLayoutManager(gridLayoutManager);
        rcvMain.setAdapter(this.adapterSentMessage);


        rcvMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                visibleItemCount = gridLayoutManager.getChildCount();

                // pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();
                pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPositions(null);
                if (dy < 0) {
                    if ((visibleItemCount + pastVisiblesItems[0]) >= 10 && !isshow) {
                        animator.slideInFromDown(imgMoveUp);
                        imgMoveUp.setVisibility(View.VISIBLE);
                        isshow = true;
                    } else if ((visibleItemCount + pastVisiblesItems[0]) < 10 && isshow) {
                        imgMoveUp.setVisibility(View.GONE);
                        isshow = false;
                    }
                }
                if (dy > 0) //check for scroll down
                {
                    if (canLoading) {
                        totalItemCount = gridLayoutManager.getItemCount();
                        if ((visibleItemCount + pastVisiblesItems[0]) >= totalItemCount - 2) {
                            canLoading = false;
                            listenerRcv.onLoadData();
                        }
                    }
                }
            }
        });

    }

    public void setCanLoading(boolean canLoading) {
        this.canLoading = canLoading;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}

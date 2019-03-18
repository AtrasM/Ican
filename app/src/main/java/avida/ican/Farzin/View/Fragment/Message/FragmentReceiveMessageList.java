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
import android.widget.TextView;

import avida.ican.Farzin.View.Adapter.AdapterReceiveMessage;
import avida.ican.Farzin.View.Interface.ListenerRcv;
import avida.ican.Ican.BaseFragment;
import avida.ican.Ican.View.Custom.Animator;
import avida.ican.Ican.View.Custom.GridLayoutManagerWithSmoothScroller;
import avida.ican.R;
import butterknife.BindView;

public class FragmentReceiveMessageList extends BaseFragment {

/*    @BindView(R.id.edt_search)
    EditText edtSearch;*/

    @BindView(R.id.rcv_main)
    RecyclerView rcvMain;
    @BindView(R.id.img_move_up)
    ImageView imgMoveUp;
    @BindView(R.id.txt_no_data)
    TextView txtNoData;
    @BindView(R.id.srl_new_message)
    SwipeRefreshLayout srlNewMessage;

    private Activity context;
    // private static FragmentReceiveMessageList fragment;
    private static AdapterReceiveMessage adapterReceiveMessage;
    public static String Tag = "FragmentReceiveMessageList";
    private ListenerRcv listenerRcv;
    private int[] pastVisiblesItems;
    private int visibleItemCount;
    private int totalItemCount;
    private boolean isshow = false;
    private Animator animator = null;
    private boolean canLoading = true;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_receive_message_list;
    }


    public static AdapterReceiveMessage getAdapterReceiveMessage() {
        return adapterReceiveMessage;
    }

    public static void setAdapterReceiveMessage(AdapterReceiveMessage adapterReceiveMessage) {
        FragmentReceiveMessageList.adapterReceiveMessage = adapterReceiveMessage;
    }


    public FragmentReceiveMessageList newInstance(Activity context, AdapterReceiveMessage adapterReceiveMessage, ListenerRcv listenerRcv) {
        this.context = context;
        this.adapterReceiveMessage = adapterReceiveMessage;
        this.listenerRcv = listenerRcv;
        animator = new Animator(context);
        return this;
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

        srlNewMessage.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listenerRcv.onSwipeRefresh(srlNewMessage);
            }
        });
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

    private void initRcv() {
        final GridLayoutManagerWithSmoothScroller gridLayoutManager = new GridLayoutManagerWithSmoothScroller(1, StaggeredGridLayoutManager.VERTICAL);
        gridLayoutManager.setReverseLayout(false);
        rcvMain.setLayoutManager(gridLayoutManager);
        rcvMain.setAdapter(adapterReceiveMessage);


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

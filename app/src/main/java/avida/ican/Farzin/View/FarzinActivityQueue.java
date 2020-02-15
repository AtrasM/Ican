package avida.ican.Farzin.View;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import avida.ican.Farzin.View.Enum.PutExtraEnum;
import avida.ican.Farzin.View.Enum.QueueEnum;
import avida.ican.Farzin.View.Fragment.Queue.FragmentDocumentAttachFileQueue;
import avida.ican.Farzin.View.Fragment.Queue.FragmentDocumentOperatorsQueue;
import avida.ican.Farzin.View.Fragment.Queue.FragmentImportDocumentQueue;
import avida.ican.Farzin.View.Fragment.Queue.FragmentQueueMessageList;
import avida.ican.Farzin.View.Interface.ListenerFile;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Adapter.ViewPagerAdapter;
import avida.ican.Ican.View.Custom.CustomFunction;
import avida.ican.Ican.View.Custom.Resorse;
import avida.ican.Ican.View.Enum.SnackBarEnum;
import avida.ican.Ican.View.Enum.ToastEnum;
import avida.ican.R;
import butterknife.BindString;
import butterknife.BindView;

public class FarzinActivityQueue extends BaseToolbarActivity {
    @Nullable
    @BindView(R.id.smart_tabLayout)
    SmartTabLayout smartTabLayout;
    @BindView(R.id.mview_pager)
    ViewPager mViewPager;
    @BindView(R.id.ln_loading)
    LinearLayout lnLoading;
    @BindString(R.string.title_queue)
    String Title;

    private File file;
    private FragmentQueueMessageList fragmentQueueMessageList;
    private FragmentDocumentOperatorsQueue fragmentDocumentOperatorsQueue;
    private FragmentDocumentAttachFileQueue fragmentDocumentAttachFileQueue;
    private FragmentImportDocumentQueue fragmentImportDocumentQueue;
    private ListenerFile listenerFile;
    private FragmentManager mfragmentManager;
    private int defSelectTab;

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
        return R.layout.farzin_activity_queue;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mfragmentManager = getSupportFragmentManager();
        initTollBar(Title);
        defSelectTab = getIntent().getIntExtra(PutExtraEnum.QueueType.getValue(), -1);
        App.getFarzinBroadCastReceiver().stopServices();
        App.ShowMessage().ShowSnackBar(Resorse.getString(R.string.activity_queue_stop_service), SnackBarEnum.SNACKBAR_INDEFINITE);
        initView();
    }

    private void initView() {
        initListenerFile();
        initViewPagerFragment();
    }

    private void initListenerFile() {
        listenerFile = structureAttach -> checkFile(structureAttach);
    }


    private void initViewPagerFragment() {

        fragmentQueueMessageList = new FragmentQueueMessageList().newInstance(App.CurentActivity);
        fragmentDocumentOperatorsQueue = new FragmentDocumentOperatorsQueue().newInstance(App.CurentActivity);
        fragmentDocumentAttachFileQueue = new FragmentDocumentAttachFileQueue().newInstance(App.CurentActivity, listenerFile);
        fragmentImportDocumentQueue = new FragmentImportDocumentQueue().newInstance(App.CurentActivity);

        initTab();
    }

    private void checkFile(StructureAttach structureAttach) {
        file = new CustomFunction(App.CurentActivity).OpenFile(structureAttach);
    }

    private void initTab() {
        assert smartTabLayout != null;
        smartTabLayout.setCustomTabView(R.layout.layout_txt_tab, R.id.txt_title_tab);
        ViewPagerAdapter adapter = new ViewPagerAdapter(mfragmentManager);
        adapter.addFrag(fragmentImportDocumentQueue, R.string.title_frm_create_document_queue);
        adapter.addFrag(fragmentDocumentOperatorsQueue, R.string.title_frm_operator_queue);
        adapter.addFrag(fragmentDocumentAttachFileQueue, R.string.title_frm_attach_file_queue);
        adapter.addFrag(fragmentQueueMessageList, R.string.title_frm_queue_message);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);//where 3 is the amount of pages to keep in memory on either side of the current page.
        smartTabLayout.setViewPager(mViewPager);
        if (defSelectTab > 0) {
            if (defSelectTab == QueueEnum.DocumentOperator.getValue()) {
                mViewPager.setCurrentItem(1);
            } else if (defSelectTab == QueueEnum.DocumentAttachFile.getValue()) {
                mViewPager.setCurrentItem(2);
            } else if (defSelectTab == QueueEnum.Message.getValue()) {
                mViewPager.setCurrentItem(3);
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Finish();
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void Finish() {
        App.canBack = true;
        App.isLoading = false;
        App.getFarzinBroadCastReceiver().runServices();
        App.ShowMessage().ShowToast(Resorse.getString(R.string.services_run), ToastEnum.TOAST_LONG_TIME);
        Finish(App.CurentActivity);
    }
}

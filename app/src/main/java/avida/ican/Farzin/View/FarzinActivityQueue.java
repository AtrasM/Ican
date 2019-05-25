package avida.ican.Farzin.View;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.io.File;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import avida.ican.Farzin.View.Fragment.Queue.FragmentQueueMessageList;
import avida.ican.Farzin.View.Fragment.Queue.FragmentQueueOpticalPenList;
import avida.ican.Farzin.View.Interface.ListenerFile;
import avida.ican.Ican.App;
import avida.ican.Ican.BaseToolbarActivity;
import avida.ican.Ican.Model.Structure.StructureAttach;
import avida.ican.Ican.View.Adapter.ViewPagerAdapter;
import avida.ican.Ican.View.Custom.CustomFunction;
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
    private FragmentQueueOpticalPenList fragmentQueueOpticalPenList;
    private ListenerFile listenerFile;
    private FragmentManager mfragmentManager;

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
        fragmentQueueOpticalPenList = new FragmentQueueOpticalPenList().newInstance(App.CurentActivity, listenerFile);

        initTab();
    }

    private void checkFile(StructureAttach structureAttach) {
        file = new CustomFunction(App.CurentActivity).OpenFile(structureAttach);
    }

    private void initTab() {
        assert smartTabLayout != null;
        smartTabLayout.setCustomTabView(R.layout.layout_txt_tab, R.id.txt_title_tab);
        ViewPagerAdapter adapter = new ViewPagerAdapter(mfragmentManager);
        adapter.addFrag(fragmentQueueMessageList, R.string.title_frm_queue_message);
        adapter.addFrag(fragmentQueueOpticalPenList, R.string.title_frm_queue_optical_pen);
 /*       App.fragmentStacks.remove("documentContent");
        Fragment fragment = fragmentCartableDocumentContent;
        App.fragmentStacks.put("documentContent", new Stack<Fragment>());
        App.fragmentStacks.get("documentContent").push(fragment);
        adapter.addFrag(fragmentCartableHameshList, R.string.title_list_hamesh);
        adapter.addFrag(fragmentCartableHistoryList, R.string.title_gardesh_madrak);
        adapter.addFrag(fragmentZanjireMadrak, R.string.title_zanjireh_madrak);*/
        mViewPager.setAdapter(adapter);
        // mViewPager.setOffscreenPageLimit(3);//where 3 is the amount of pages to keep in memory on either side of the current page.
        smartTabLayout.setViewPager(mViewPager);
    }

}

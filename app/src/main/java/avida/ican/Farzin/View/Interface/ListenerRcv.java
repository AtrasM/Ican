package avida.ican.Farzin.View.Interface;

import android.support.v4.widget.SwipeRefreshLayout;

import java.util.List;

import avida.ican.Farzin.Model.Structure.Database.StructureUserAndRoleDB;

/**
 * Created by AtrasVida on 2018-08-20 at 16:45 PM
 */

public interface ListenerRcv {
    void onLoadData();

    void onSwipeRefresh(SwipeRefreshLayout swipeRefreshLayout);
}

package avida.ican.Ican;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by AtrasVida on 2018-04-15 at 12:13 PM
 */

public abstract class BaseFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(getLayoutResId(), container, false);
        ButterKnife.bind(this, view);
        App.bus().register(this);
        return view;
    }
    @Override
    public void onDestroyView() {
        App.bus().unregister(this);
        super.onDestroyView();
        //ButterKnife.unbind(this);
    }


    public abstract int getLayoutResId();

}

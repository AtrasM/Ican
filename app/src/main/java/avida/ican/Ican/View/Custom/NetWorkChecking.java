package avida.ican.Ican.View.Custom;

import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork;

import avida.ican.Ican.App;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by AtrasVida on 2018-04-03 at 4:50 PM
 */

public class NetWorkChecking {

    public NetWorkChecking() {
        ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isConnectedToInternet) {
                        if (isConnectedToInternet) {
                            App.netWorkStatusListener.Connected();
                        } else {
                            App.netWorkStatusListener.WatingForNetwork();
                        }

                    }

                });

    }


}

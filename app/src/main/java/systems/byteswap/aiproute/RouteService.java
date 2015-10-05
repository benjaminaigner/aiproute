package systems.byteswap.aiproute;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;

/**
 * Service, to invoke the Wifi broadcast receiver.
 * This service will be started via the autostart broadcast receiver.
 * It starts only, if the app has an activated autostart feature.
 *
 */
public class RouteService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("AIProute", "Service started");
        //Attach wifi broadcast receiver
        BroadcastReceiver broadcastReceiver = new WifiBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION );
        this.getApplicationContext().registerReceiver(broadcastReceiver, intentFilter);
    }
}

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

 Copyright (C) 2015  Benjamin Aigner

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
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

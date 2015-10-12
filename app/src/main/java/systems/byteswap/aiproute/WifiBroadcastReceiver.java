package systems.byteswap.aiproute;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Broadcast receiver class for Wifi state changes
 * This receiver checks if the Wifi is available.
 *
 * If available, it is checking the preferences for SSID activation or general activation.
 *
 * For SSID activation:
 * All routes for the current SSID are loaded, if marked as active, they will be loaded
 *
 * For general activation:
 * All routes are loaded, if marked as active, they will be loaded

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
public class WifiBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        StorageProvider storage = new StorageProvider(context);
        if (WifiManager.NETWORK_STATE_CHANGED_ACTION .equals(action)) {
            NetworkInfo state = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO );
            Log.d("AIProute", "EXTRA_NETWORK_INFO: " + state.getState());
            //check for available wifi
            if (state.getState().toString().equals("CONNECTED")) {
                Log.d("AIProute", "Wifi broadcast received, connected");
                //load preferences
                boolean ssidactivation = SettingsActivity.isSSIDbasedActivation(context);
                Cursor routes;
                String active;
                long id;

                //check for SSID activation preference
                if(ssidactivation) {
                    //if activated, look for routes with the new SSID and which are active
                    WifiInfo winfo = wifi.getConnectionInfo();
                    String currentSSID = winfo.getSSID().replaceAll("\"", "");
                    routes = storage.fetchRouteForSSID(currentSSID);
                    Log.i("AIProute", "SSID activation, load all routes for: " + currentSSID + ", found: " + routes.getCount());
                } else {
                    //if deactivated, look for all active routes
                    routes = storage.fetchAll();
                    Log.i("AIProute", "normal activation, load all active route, found: " + routes.getCount());
                }

                if(routes.getCount()>0) {
                    //check first route for active setting
                    active = routes.getString(routes.getColumnIndex(StorageProvider.KEY_ACTIVE));
                    if (active.equals("1")) {
                        //and enable it
                        id = routes.getLong(routes.getColumnIndex(StorageProvider.KEY_ROWID));
                        ShellAccess.execSuCommand(storage.getRoute(id).toRouteStringAdd(context));
                    }


                    while (routes.moveToNext()) {
                        //check each route for active setting
                        active = routes.getString(routes.getColumnIndex(StorageProvider.KEY_ACTIVE));
                        if (active.equals("1")) {
                            //and enable it
                            id = routes.getLong(routes.getColumnIndex(StorageProvider.KEY_ROWID));
                            ShellAccess.execSuCommand(storage.getRoute(id).toRouteStringAdd(context));
                        }
                    }
                }
            }
        }
    }
}

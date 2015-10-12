package systems.byteswap.aiproute;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * This broadcast receiver is attached for the boot complete action.
 *
 * If the autostart preference is activated, the RouteService is started.
 * This service registers for the Wifi Broadcast.

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
public class AutostartBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //recheck for action
        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            //check for autostart preference
            if (SettingsActivity.isAutostart(context)) {
                Log.d("AIProute", "Autostart active, service installed");
                Intent serviceIntent = new Intent(context, RouteService.class);
                context.startService(serviceIntent);
            }
        }
    }
}

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
 *
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

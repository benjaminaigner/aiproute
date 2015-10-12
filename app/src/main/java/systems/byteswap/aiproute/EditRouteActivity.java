package systems.byteswap.aiproute;


import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;


/**
 * This activity is used to modify or create a new route setting.
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

public class EditRouteActivity extends AppCompatActivity implements SelectSSIDDialogFragment.NoticeDialogListener{
    public String message = "";
    public long index = -1;

    private StorageProvider storage = MainActivity.store;

    /** SSID array, to provide a list for the DialogFragment */
    private List<String> ssidArray = new ArrayList<>();
    /** list of all SSIDs with boolean value of active status, initial state */
    private List<Boolean> activeArray = new ArrayList<>();
    /** list of all SSIDs with boolean value of active status, working copy for the Fragment */
    private List<Boolean> activeArrayWorking = new ArrayList<>();
    /** MultiChoice Fragment for selecting SSIDs */
    SelectSSIDDialogFragment ssidSelectFragment;

    WifiManager wifi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_edit_route);

        //load passed data
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        switch(message) {
            case "NEW":
                //do nothing, the fields should be filled by the user
                break;
            case "EDIT":
                //load additional data and fill it into the fields
                index = intent.getIntExtra(MainActivity.ROUTE_INDEX, -1);
                EditText name = (EditText)findViewById(R.id.textEditName);
                EditText address = (EditText)findViewById(R.id.textEditAddress);
                EditText netmask = (EditText)findViewById(R.id.textEditNetmask);
                EditText gateway = (EditText)findViewById(R.id.textEditGateway);
                EditText metric = (EditText)findViewById(R.id.textEditMetric);
                CheckBox active = (CheckBox)findViewById(R.id.checkBoxActive);
                CheckBox persistent = (CheckBox)findViewById(R.id.checkBoxPersistent);


                if(index != -1)
                {
                    //load all text fields
                    Route singleRoute = storage.getRoute(index);
                    name.setText(singleRoute.getName());
                    address.setText(singleRoute.getAddress());
                    netmask.setText(singleRoute.getNetmask());
                    gateway.setText(singleRoute.getGateway());

                    //Load all configured SSIDs
                    Cursor ssidcursor = storage.fetchSSID(index);

                    //load first row
                    if(ssidcursor != null && ssidcursor.getCount() > 0) {
                        if(!ssidArray.contains(ssidcursor.getString(ssidcursor.getColumnIndex(StorageProvider.KEY2_SSID)))) {
                            ssidArray.add(ssidcursor.getString(ssidcursor.getColumnIndex(StorageProvider.KEY2_SSID)));
                            activeArray.add(true);
                            activeArrayWorking.add(true);
                        } else {
                            int indexSSID = ssidArray.indexOf(ssidcursor.getString(ssidcursor.getColumnIndex(StorageProvider.KEY2_SSID)));
                            activeArray.set(indexSSID, true);
                            activeArrayWorking.set(indexSSID, true);
                        }
                        while (ssidcursor.moveToNext()) {
                            if (!ssidArray.contains(ssidcursor.getString(ssidcursor.getColumnIndex(StorageProvider.KEY2_SSID)))) {
                                ssidArray.add(ssidcursor.getString(ssidcursor.getColumnIndex(StorageProvider.KEY2_SSID)));
                                activeArray.add(true);
                                activeArrayWorking.add(true);
                            } else {
                                int indexSSID = ssidArray.indexOf(ssidcursor.getString(ssidcursor.getColumnIndex(StorageProvider.KEY2_SSID)));
                                activeArray.set(indexSSID, true);
                                activeArrayWorking.set(indexSSID, true);
                            }
                        }
                    }

                    if(singleRoute.getMetric() == 0) {
                        metric.setText("");
                    } else {
                        metric.setText(String.valueOf(singleRoute.getMetric()));
                    }
                    persistent.setActivated(singleRoute.isPersistent());
                    active.setActivated(singleRoute.isActive());

                }
                break;
            default:
                Toast.makeText(EditRouteActivity.this, R.string.intenterror, Toast.LENGTH_SHORT).show();
                break;
        }

        //load Wifi service
        wifi = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //load all available system SSIDs to drop down menu...
        List<WifiConfiguration> wifiList = wifi.getConfiguredNetworks();



        //Assemble the Wifi list in the Array. First: current active Wifi, all connections and
        //all saved SSIDs
        WifiInfo info = wifi.getConnectionInfo();
        String currentSSID = info.getSSID().replaceAll("\"", "");
        if(!currentSSID.isEmpty()) {
            if(!ssidArray.contains(currentSSID)) {
                ssidArray.add(currentSSID);
                activeArray.add(false);
                activeArrayWorking.add(false);
            }
        }
        if(wifiList.size()>0) {
            for (WifiConfiguration item : wifiList) {
                if (!ssidArray.contains(item.SSID.replaceAll("\"", ""))) {
                    ssidArray.add(item.SSID.replaceAll("\"", ""));
                    activeArray.add(false);
                    activeArrayWorking.add(false);
                }
            }
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectSSID(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        ssidSelectFragment = new SelectSSIDDialogFragment();
        ssidSelectFragment.setSsidList((ArrayList<String>) ssidArray, (ArrayList<Boolean>) activeArray);
        ssidSelectFragment.show(transaction, "selectssid");
    }



    /** Called when the user clicks the "new" button */
    public void saveRoute(View view) {
        EditText name = (EditText)findViewById(R.id.textEditName);
        EditText address = (EditText)findViewById(R.id.textEditAddress);
        EditText netmask = (EditText)findViewById(R.id.textEditNetmask);
        EditText gateway = (EditText)findViewById(R.id.textEditGateway);
        EditText metric = (EditText)findViewById(R.id.textEditMetric);
        CheckBox active = (CheckBox)findViewById(R.id.checkBoxActive);
        CheckBox persistent = (CheckBox)findViewById(R.id.checkBoxPersistent);

        Boolean dataCheck = true;
        String fieldString = "";
        Matcher matcher;

        //check for address
        matcher = Patterns.IP_ADDRESS.matcher(address.getText().toString());
        if (!matcher.matches()) {
            fieldString += R.string.address + ",";
            dataCheck = false;
        }

        //check for netmask
        if(!netmask.getText().toString().isEmpty()) {
            matcher = Patterns.IP_ADDRESS.matcher(netmask.getText().toString());
            if (!matcher.matches()) {
                fieldString += R.string.netmask + ",";
                dataCheck = false;
            }
        }

        //check for gateway
        if(!gateway.getText().toString().isEmpty()) {
            matcher = Patterns.IP_ADDRESS.matcher(gateway.getText().toString());
            if (!matcher.matches()) {
                fieldString += R.string.gateway + ",";
                dataCheck = false;
            }
        }

        //if any check failed, discard data, show toast and return
        if(!dataCheck) {
            Toast.makeText(EditRouteActivity.this, R.string.inputnotvalid + fieldString, Toast.LENGTH_SHORT).show();
            return;
        }

        Route newRoute = new Route();

        newRoute.setName(name.getText().toString());
        newRoute.setAddress(address.getText().toString());
        newRoute.setNetmask(netmask.getText().toString());
        newRoute.setGateway(gateway.getText().toString());

        try {
            if (!metric.getText().toString().isEmpty()) {
                newRoute.setMetric(Integer.valueOf(metric.getText().toString()));
            }
        } catch (Exception e) {
            Toast.makeText(EditRouteActivity.this, R.string.inputmetricnotvalid, Toast.LENGTH_SHORT).show();
            return;
        }
        newRoute.setPersistent(Boolean.parseBoolean(persistent.getText().toString()));
        newRoute.setActive(Boolean.parseBoolean(active.getText().toString()));
        int j;

        switch(message) {
            case "NEW":
                //insert route to DB
                index = storage.insertRoute(newRoute);

                //add all active SSIDs to DB
                j = 0;
                for (Boolean item : activeArray) {
                    if (item) {
                        storage.insertSSID(ssidArray.get(j), index);
                    }
                    j++;
                }

                Toast.makeText(EditRouteActivity.this, R.string.applyNewRoute, Toast.LENGTH_SHORT).show();
                break;
            case "EDIT":
                //unload old route
                ShellAccess.execSuCommand(storage.getRoute(index).toRouteStringDelete(view.getContext()));
                //update to DB
                storage.updateRoute(index,newRoute);

                //delete all SSIDs from this route & add all again
                storage.deleteSSIDforRoute(index);
                j = 0;
                for (Boolean item : activeArray) {
                    if (item) {
                        storage.insertSSID(ssidArray.get(j), index);
                    }
                    j++;
                }

                Toast.makeText(EditRouteActivity.this, R.string.applyChangeRoute, Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(EditRouteActivity.this, R.string.intenterror, Toast.LENGTH_SHORT).show();
                break;
        }

        //apply the new route, if set to active...
        if(newRoute.isActive()) {
            //check, if the settings are SSID based activation or general
            if(SettingsActivity.isSSIDbasedActivation(this.getApplicationContext())) {
                //if SSID based, load active Wifi and check it
                //load Wifi service
                wifi = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if(storage.isSSIDactiveForRoute(index,wifi.getConnectionInfo().getSSID().replaceAll("\"", ""))) {
                    ShellAccess.execSuCommand(newRoute.toRouteStringAdd(this.getApplicationContext()));
                }
            } else {
                //if not Wifi based, activate in general
                ShellAccess.execSuCommand(newRoute.toRouteStringAdd(this.getApplicationContext()));
            }
        }

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button, save working SSID array to main
        int j = 0;
        for (Boolean item : activeArrayWorking) {
            activeArray.set(j, item);
            j++;
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // User touched the dialog's negative button, restore working SSID array
        int j = 0;
        for (Boolean item : activeArray) {
            activeArrayWorking.set(j, item);
            j++;
        }
    }
    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        activeArrayWorking.set(which, isChecked);
    }

}

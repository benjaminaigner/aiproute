package systems.byteswap.aiproute;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Main activity, which is used to show all active/available routes
 * It is also possible to modify a route or change settings

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
//TODO: Bugfix: singleton of the main activity??? Multiple Instances possible...
//TODO: add "about" dialog


//TODO: publish at FDroid


public class MainActivity extends AppCompatActivity {
    /** message to pass on to the edit/new route activity */
    public final static String EXTRA_MESSAGE = "systems.byteswap.aiproute.MESSAGE";
    /** message to pass on to the edit/new route activity */
    public final static String ROUTE_INDEX = "systems.byteswap.aiproute.ROUTE_INDEX";
    /** Adapter for the route list <-> DB via StorageProvider.fetchAll */
    private SimpleCursorAdapter dataAdapter;

    /** Storage provider to access the SQLite DB */
    public static StorageProvider store;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        store = new StorageProvider(this.getBaseContext());

        //attach adapter to list view
        displayListView();
        Cursor values = store.fetchAll();
        dataAdapter.changeCursor(values);
        dataAdapter.setViewBinder(binder);

        //load every already active route (in case of SSID based activation, check wifi state too)
        if(values != null && values.getCount() > 0) {
            //Load Wifi facility
            WifiManager wifi = (WifiManager)this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            //check first row, if route is active
            if(values.getString(values.getColumnIndex(StorageProvider.KEY_ACTIVE)).equals("1")) {
                //load id of route
                long id = values.getLong(values.getColumnIndex(StorageProvider.KEY_ROWID));

                //check, if the settings are SSID based activation or general
                if(SettingsActivity.isSSIDbasedActivation(this.getApplicationContext())) {
                    //check if SSID corresponds to route, if yes activate
                    if(store.isSSIDactiveForRoute(id,wifi.getConnectionInfo().getSSID().replaceAll("\"", ""))) {
                        ShellAccess.execSuCommand(store.getRoute(id).toRouteStringAdd(this.getApplicationContext()));
                    }
                } else {
                    //if not Wifi based, activate in general
                    ShellAccess.execSuCommand(store.getRoute(id).toRouteStringAdd(this.getApplicationContext()));
                }
            }
            while (values.moveToNext()) {
                //load id of route
                long id = values.getLong(values.getColumnIndex(StorageProvider.KEY_ROWID));

                //check, if the settings are SSID based activation or general
                if(SettingsActivity.isSSIDbasedActivation(this.getApplicationContext())) {
                    //check if SSID corresponds to route, if yes activate
                    if(store.isSSIDactiveForRoute(id,wifi.getConnectionInfo().getSSID().replaceAll("\"", ""))) {
                        ShellAccess.execSuCommand(store.getRoute(id).toRouteStringAdd(this.getApplicationContext()));
                    }
                } else {
                    //if not Wifi based, activate in general
                    ShellAccess.execSuCommand(store.getRoute(id).toRouteStringAdd(this.getApplicationContext()));
                }
            }
        }

        //Attach wifi broadcast receiver
        BroadcastReceiver broadcastReceiver = new WifiBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION );
        this.getApplicationContext().registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private SimpleCursorAdapter.ViewBinder binder = new SimpleCursorAdapter.ViewBinder() {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int columnIndex){
            int getIndex = cursor.getColumnIndex(StorageProvider.KEY_ACTIVE);
            String active = cursor.getString(getIndex);
            String empname;
            int viewId = view.getId();
            TextView tv = (TextView) view;
            tv.setTextColor(Color.BLACK);

            switch(viewId)
            {
                case R.id.name:
                    getIndex = cursor.getColumnIndex(StorageProvider.KEY_NAME);
                    empname = cursor.getString(getIndex);
                    tv.setText(empname);
                    if(active.equals("1")) {
                        tv.setTextColor(Color.GREEN);
                    }
                    break;
                case R.id.address:
                    getIndex = cursor.getColumnIndex(StorageProvider.KEY_ADDRESS);
                    empname = cursor.getString(getIndex);
                    tv.setText(empname);
                    if(active.equals("1")) {
                        tv.setTextColor(Color.GREEN);
                    }
                    break;
                case R.id.netmask:
                    getIndex = cursor.getColumnIndex(StorageProvider.KEY_NETMASK);
                    empname = cursor.getString(getIndex);
                    tv.setText(empname);
                    break;
                case R.id.gateway:
                    getIndex = cursor.getColumnIndex(StorageProvider.KEY_GATEWAY);
                    empname = cursor.getString(getIndex);
                    tv.setText(empname);
                    break;
                case R.id.rowid:
                    getIndex = cursor.getColumnIndex(StorageProvider.KEY_ROWID);
                    empname = cursor.getString(getIndex);
                    tv.setText(empname);
                    break;
                case R.id.active:
                    getIndex = cursor.getColumnIndex(StorageProvider.KEY_ACTIVE);
                    empname = cursor.getString(getIndex);
                    tv.setText(empname);
                    break;
            }
            return true;
        }
    };

    // Create a message handling object as an anonymous class.
    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {


            Cursor cursor = (Cursor) parent.getItemAtPosition(position);

            int routeId = cursor.getInt(cursor.getColumnIndex(StorageProvider.KEY_ROWID));

            Route r = store.getRoute(routeId);

            //Load Wifi facility
            WifiManager wifi = (WifiManager)MainActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            if(r.isActive()) {
                ShellAccess.execSuCommand(r.toRouteStringDelete(parent.getContext()));
                r.setActive(false);
                store.updateRoute(r.getId(), r);
                Log.d("AIProute","Route delete, command: " + r.toRouteStringDelete(parent.getContext()));
                Toast.makeText(MainActivity.this, R.string.routeDeactivated, Toast.LENGTH_SHORT).show();
            } else {
                //check, if the settings are SSID based activation or general
                if (SettingsActivity.isSSIDbasedActivation(parent.getContext())) {
                    //check if SSID corresponds to route, if yes activate
                    if (store.isSSIDactiveForRoute(routeId, wifi.getConnectionInfo().getSSID().replaceAll("\"", ""))) {
                        ShellAccess.execSuCommand(r.toRouteStringAdd(parent.getContext()));
                        Log.d("AIProute", "Route activate, command: " + r.toRouteStringAdd(parent.getContext()));
                    }
                } else {
                    //if not Wifi based, activate in general
                    ShellAccess.execSuCommand(r.toRouteStringAdd(parent.getContext()));
                    Log.d("AIProute", "Route activate, command: " + r.toRouteStringAdd(parent.getContext()));
                }

                r.setActive(true);
                store.updateRoute(r.getId(),r);
                Toast.makeText(MainActivity.this, R.string.routeActivated, Toast.LENGTH_SHORT).show();
            }
            Cursor values = store.fetchAll();
            dataAdapter.changeCursor(values);
        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.contextedit:
                Intent intent = new Intent(MainActivity.this, EditRouteActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "EDIT");
                intent.putExtra(ROUTE_INDEX, (int)info.id);
                startActivity(intent);
                break;
            case R.id.contextdelete:
                Route r = store.getRoute((int)info.id);
                if(r.isActive()) {
                    ShellAccess.execSuCommand(r.toRouteStringDelete(this.getApplicationContext()));
                }
                store.deleteRoute((int) info.id);
                Cursor values = store.fetchAll();
                dataAdapter.changeCursor(values);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return true;
    }

    /** Called when the user clicks the "new" button */
    public void addNewRoute(View view) {
        Intent intent = new Intent(this, EditRouteActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "NEW");
        startActivity(intent);
    }

    private void displayListView() {

        Cursor cursor = store.fetchAll();

        // The desired columns to be bound
        String[] columns = new String[] {
                StorageProvider.KEY_NAME,
                StorageProvider.KEY_ADDRESS,
                StorageProvider.KEY_NETMASK,
                StorageProvider.KEY_GATEWAY,
                StorageProvider.KEY_ROWID,
                StorageProvider.KEY_ACTIVE
        };

        // the XML defined views which the data will be bound to
        int[] to = new int[] {
                R.id.name,
                R.id.address,
                R.id.netmask,
                R.id.gateway,
                R.id.rowid,
                R.id.active
        };


        // create the adapter using the cursor pointing to the desired data
        //as well as the layout information
        dataAdapter = new SimpleCursorAdapter(
                this, R.layout.routeitems,
                cursor,
                columns,
                to,
                0);

        ListView listView = (ListView) findViewById(R.id.listViewRoutes);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        //add the listener for short clicks (activate/deactivate route)
        listView.setOnItemClickListener(mMessageClickedHandler);

        //init context menu
        registerForContextMenu(listView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Cursor values = store.fetchAll();
        dataAdapter.changeCursor(values);
    }
}

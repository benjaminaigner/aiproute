package systems.byteswap.aiproute;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /** list of all available routes (only names), connected to listViewRoutes via an ArrayAdapter */
    private ArrayList<String> routeList = new ArrayList<String>();
    /** list of all available routes (full data), connected to listViewRoutes via an ArrayAdapter */
    private ArrayList<String> routeListFull = new ArrayList<String>();
    /** message to pass on to the edit/new route activity */
    public final static String EXTRA_MESSAGE = "systems.byteswap.aiproute.MESSAGE";
    /** array object to pass on to the edit/new route activity */
    public final static String ROUTE_ARRAY = "systems.byteswap.aiproute.ROUTE_ARRAY";
    /** array object to pass on to the edit/new route activity  (FULL data)*/
    public final static String ROUTE_ARRAY_FULL = "systems.byteswap.aiproute.ROUTE_ARRAY_FULL";
    /** array index to pass on to the edit/new route activity */
    public final static String ROUTE_INDEX = "systems.byteswap.aiproute.ROUTE_INDEX";
    /** filename for the persistent route storage */
    public final static String STORAGE_FILENAME = "routestorage";
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get listview
        ListView list = (ListView)findViewById(R.id.listViewRoutes);

        //create a new adapter to an array
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, routeList);

        //set adapter for content
        list.setAdapter(adapter);
        //add the listener for short clicks (activate/deactivate route)
        list.setOnItemClickListener(mMessageClickedHandler);
        //add the listener for long clicks (open context menu for editing/deleting)
        list.setOnItemLongClickListener(mMessageLongClickedHandler);

        Intent intent = getIntent();
        ArrayList<String> routeListIntent = (ArrayList<String>) intent.getSerializableExtra(ROUTE_ARRAY);

        if(routeListIntent == null) {

            //load all available data from storage file
            FileInputStream fis = null;
            try {
                fis = openFileInput(STORAGE_FILENAME);
                InputStreamReader is = new InputStreamReader(fis);
                BufferedReader br = new BufferedReader(is);
                String read = br.readLine();

                while (read != null) {
                    //TODO: hier den string splitten und ins array speichern...
                    read = br.readLine();
                }

            } catch (FileNotFoundException e) {
                Toast.makeText(MainActivity.this, "No saved routes found, creating a new file", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this, "Error in saved routes found, creating a new file", Toast.LENGTH_SHORT).show();
            }
        } else {
            routeList = routeListIntent;
            adapter.clear();
            adapter.addAll(routeListIntent);
            adapter.notifyDataSetChanged();
        }
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

    // Create a message handling object as an anonymous class.
    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            //TODO: activate/deactivate route and show that in list (highlighting via color)
            System.out.println("onItemClick mit position: " + position + " and id: " + id);
        }
    };

    // Create a message handling object as an anonymous class.
    private AdapterView.OnItemLongClickListener mMessageLongClickedHandler = new AdapterView.OnItemLongClickListener() {
        public boolean onItemLongClick(AdapterView parent, View v, int position, long id) {
            //TODO: open context menu for editing/deleting
            String action = "EDIT";
            switch(action ) {
                case "EDIT":
                    Intent intent = new Intent(MainActivity.this, EditRouteActivity.class);
                    intent.putExtra(EXTRA_MESSAGE, "EDIT");
                    intent.putExtra(ROUTE_ARRAY, routeList);
                    intent.putExtra(ROUTE_ARRAY_FULL, routeListFull);
                    intent.putExtra(ROUTE_INDEX, position);
                    startActivity(intent);
                    break;
                case "DELETE":
                    //TODO: unset from array, reorder
                    adapter.clear();
                    adapter.addAll(routeList);
                    adapter.notifyDataSetChanged();
                    break;
            }
            return true;
        }
    };

    /** Called when the user clicks the "new" button */
    public void addNewRoute(View view) {
        Intent intent = new Intent(this, EditRouteActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "NEW");
        intent.putExtra(ROUTE_ARRAY, routeList);
        intent.putExtra(ROUTE_ARRAY_FULL, routeListFull);
        startActivity(intent);
    }

    @Override
    protected void onStop(){
        super.onStop();
        //TODO: resave config file...
    }

}

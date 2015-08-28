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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> routeList = new ArrayList<String>();
    public final static String EXTRA_MESSAGE = "systems.byteswap.aiproute.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get listview
        ListView list = (ListView)findViewById(R.id.listViewRoutes);

        //create a new adapter to an array
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, routeList);

        //set adapter for content
        list.setAdapter(adapter);
        //add the listener for short clicks (activate/deactivate route)
        list.setOnItemClickListener(mMessageClickedHandler);
        //add the listener for long clicks (open context menu for editing/deleting)
        list.setOnItemLongClickListener(mMessageLongClickedHandler);

        //TODO: open app DB, load all available routes and store it to arraylist...

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
            //TODO: activate/deactivate route and show that in list....
            System.out.println("onItemClick mit position: " + position + " and id: " + id);
        }
    };

    // Create a message handling object as an anonymous class.
    private AdapterView.OnItemLongClickListener mMessageLongClickedHandler = new AdapterView.OnItemLongClickListener() {
        public boolean onItemLongClick(AdapterView parent, View v, int position, long id) {
            //TODO: open context menu for editing/deleting
            System.out.println("onItemLongClick mit position: " + position + " and id: " + id);
            return true;
        }
    };

    /** Called when the user clicks the "new" button */
    public void addNewRoute(View view) {
        Intent intent = new Intent(this, EditRouteActivity.class);
        intent.putExtra(EXTRA_MESSAGE, "NEW");
        startActivity(intent);
    }
}

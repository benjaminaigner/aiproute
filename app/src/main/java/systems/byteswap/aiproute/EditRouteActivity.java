package systems.byteswap.aiproute;

import android.app.Fragment;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class EditRouteActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "systems.byteswap.aiproute.MESSAGE";
    /** list of all available routes, connected to listViewRoutes via an ArrayAdapter */
    private ArrayList<String> routeList = new ArrayList<String>();
    /** array object to pass on to the edit/new route activity */
    public final static String ROUTE_ARRAY = "systems.byteswap.aiproute.ROUTE_ARRAY";
    /** array index to pass on to the edit/new route activity */
    public final static String ROUTE_INDEX = "systems.byteswap.aiproute.ROUTE_INDEX";
    public String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_edit_route);

        //load passed data
        message = intent.getStringExtra(EditRouteActivity.EXTRA_MESSAGE);
        routeList = (ArrayList<String>) intent.getSerializableExtra(ROUTE_ARRAY);

        switch(message) {
            case "NEW":
                //do nothing, the fields should be filled by the user
                break;
            case "EDIT":
                //load additional data and fill it into the fields
                //TODO: load from strings and apply to the textfields
                break;
            default:
                Toast.makeText(EditRouteActivity.this, "Error: Intent passing problem...", Toast.LENGTH_SHORT).show();
                break;
        }
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() { }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_edit_route,
                    container, false);
            return rootView;
        }
    }

    /** Called when the user clicks the "new" button */
    public void saveRoute(View view) {

        EditText name = (EditText)findViewById(R.id.textEditName);
        //TODO: perform data checking, show Toast in case of an error
        switch(message) {
            case "NEW":
                //push new data to the end of the array
                routeList.add(name.getText().toString());

                Toast.makeText(EditRouteActivity.this, "New route added", Toast.LENGTH_SHORT).show();
                break;
            case "EDIT":
                //load additional data and fill it into the fields
                //TODO: load from strings and apply to the textfields
                Toast.makeText(EditRouteActivity.this, "Route changed", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(EditRouteActivity.this, "Error: Intent passing problem...", Toast.LENGTH_SHORT).show();
                break;
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ROUTE_ARRAY, routeList);
        startActivity(intent);
        //TODO: apply the new route, if set to active...
        //TODO: save the new input data...
    }

}

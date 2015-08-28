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

public class EditRouteActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "systems.byteswap.aiproute.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_edit_route);

        //TODO: load data, if this is an edit (not a new route)
        String message = intent.getStringExtra(EditRouteActivity.EXTRA_MESSAGE);
        switch(message) {
            case "NEW":
                //do nothing, the fields should be filled by the user
                break;
            case "EDIT":
                //load additional data and fill it into the fields
                //TODO: load from strings and apply to the textfields
                break;
            default:
                //TODO: error....
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
        //TODO: apply the new route, if set to active...
        //TODO: save the new input data...
    }

}

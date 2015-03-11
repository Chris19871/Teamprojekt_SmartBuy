package smartbuy.teamproject.application;


import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity
{

    final Context context = this;
    //private ActionBar smartBuyactionBar;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  smartBuyactionBar = getSupportActionBar();
        //smartBuyactionBar.setDisplayShowTitleEnabled(false);

    }

    public void onClick()
    {

        // custom add_new_einkaufsmodus
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_new_einkaufsmodus);



        // set the custom add_new_einkaufsmodus components - text, image and button
        TextView text = (TextView) dialog.findViewById(R.id.dialogName);
        text.setText("Beispiel!");

        Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom add_new_einkaufsmodus
        dialogButtonOk.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
               // add_new_einkaufsmodus.dismiss();
            }
        });
        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        // if button is clicked, close the custom add_new_einkaufsmodus
        dialogButtonCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public void settingsOpen()
    {
        final Intent settings = new Intent(this,SettingsActivity.class);
        startActivity(settings);

    }
    public void uberOpen()
    {
        final Dialog uberDialog = new Dialog(context);
        uberDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        uberDialog.setContentView(R.layout.ueber_dialog);

        uberDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            settingsOpen();
            return true;
        }
        if (id == R.id.ueber) {
            uberOpen();
            return true;
        }
        if (id == R.id.action_add) {
            onClick();
            return true;
            }

            return super.onOptionsItemSelected(item);
    }

}

package smartbuy.teamproject.application;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class Auswahllisten extends ActionBarActivity {

    final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auswahllisten);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_auswahllisten, menu);
        return true;
    }
    public void settingsOpen()
    {
        final Intent settings = new Intent(this, SettingsActivity.class);
        startActivity(settings);
    }

    public void uberOpen()
    {
        final Dialog uberDialog = new Dialog(context);
        uberDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        uberDialog.setContentView(R.layout.ueber_dialog);

        uberDialog.show();
    }
    public void addAuswahlliste()
    {
        final Dialog auswahllistenDialog = new Dialog(context);
        auswahllistenDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        auswahllistenDialog.setContentView(R.layout.add_auswahllisten_dialog);

        final EditText name = (EditText) auswahllistenDialog.findViewById(R.id.addAuswahllisteTextView);

        Button dialogButtonSave = (Button) auswahllistenDialog.findViewById(R.id.addAuswahllisteSave);
        dialogButtonSave.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (name.getText().toString().equals(""))
                {
                    name.setHintTextColor(Color.parseColor("#FF0000"));
                    name.setHint("Feld muss ausgefüllt werden!");
                } else
                {
                    auswahllistenDialog.dismiss();
                }
            }
        });

        Button dialogButtonCancel = (Button) auswahllistenDialog.findViewById(R.id.addAuswahllisteCancel);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                auswahllistenDialog.dismiss();
            }
        });
        auswahllistenDialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings)
        {
            settingsOpen();
            return true;
        }
        if (id == R.id.ueber)
        {
            uberOpen();
            return true;
        }
        if (id == R.id.action_add_Auswahlliste)
        {
            addAuswahlliste();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }
}

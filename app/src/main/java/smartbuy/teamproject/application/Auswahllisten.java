package smartbuy.teamproject.application;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import purchase.EinkaufsArtikel;
import purchase.VorauswahlListe;


public class Auswahllisten extends ActionBarActivity {

    final Context context = this;
    private ArrayAdapter<VorauswahlListe> newVorauswahllistenListsAdapter;
    private ArrayList<VorauswahlListe> newVorauswahllisten;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auswahllisten);

        newVorauswahllisten = new ArrayList<>();

        registerForContextMenu(findViewById(R.id.auswahllistenlistView));
        ListView listView = (ListView) findViewById(R.id.auswahllistenlistView);
        newVorauswahllistenListsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, newVorauswahllisten);

        listView.setAdapter(newVorauswahllistenListsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                wechsel();
            }
        });
    }
    public void wechsel()
    {
        final Intent einkaufsliste = new Intent(this, EinkaufslisteActivity.class);
        startActivity(einkaufsliste);
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
                    ArrayList<EinkaufsArtikel> newList = new ArrayList<>();
                    VorauswahlListe liste = new VorauswahlListe(name.getText().toString(),newList);
                    newVorauswahllisten.add(liste);

                    newVorauswahllistenListsAdapter.notifyDataSetChanged();
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

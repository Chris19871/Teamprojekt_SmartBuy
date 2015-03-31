package smartbuy.teamproject.application;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

public class VorauswahllistenActivity extends ActionBarActivity {

    private final Context context = this;
    private ArrayAdapter<VorauswahlListe> newVorauswahllistenListsAdapter;
    private ArrayList<VorauswahlListe> newVorauswahllisten;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vorauswahllisten);

        newVorauswahllisten = new ArrayList<>();

        registerForContextMenu(findViewById(R.id.vorauswahllistenlistView));
        ListView listView = (ListView) findViewById(R.id.vorauswahllistenlistView);
        newVorauswahllistenListsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, newVorauswahllisten);

        listView.setAdapter(newVorauswahllistenListsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                change();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vorauswahllisten, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.settings) {
            settingsOpen();
            return true;
        }
        if (id == R.id.about) {
            aboutOpen();
            return true;
        }
        if (id == R.id.action_add_Auswahlliste) {
            addAuswahllisteOpen();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void change() {
        final Intent vorauswahlliste = new Intent(this, VorauswahllistenBearbeitenActivity.class);
        startActivity(vorauswahlliste);
    }

    public void settingsOpen() {
        final Intent settings = new Intent(this, EinstellungenActivity.class);
        startActivity(settings);
    }

    public void aboutOpen() {
        final Dialog uberDialog = new Dialog(context);
        uberDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        uberDialog.setContentView(R.layout.ueber_dialog);

        uberDialog.show();
    }

    public void addAuswahllisteOpen() {
        final Dialog auswahllistenDialog = new Dialog(context);
        auswahllistenDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        auswahllistenDialog.setContentView(R.layout.neue_vorauswahllisten_dialog);

        final EditText name = (EditText) auswahllistenDialog.findViewById(R.id.addVorauswahllisteTextView);

        Button dialogButtonSave = (Button) auswahllistenDialog.findViewById(R.id.addVorauswahllisteSave);
        dialogButtonSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (name.getText().toString().equals("")) {
                    name.setHintTextColor(Color.parseColor("#FF0000"));
                    name.setHint("Feld muss ausgefüllt werden!");
                } else {
                    ArrayList<EinkaufsArtikel> newList = new ArrayList<>();
                    VorauswahlListe liste = new VorauswahlListe(name.getText().toString(), newList);
                    newVorauswahllisten.add(liste);

                    newVorauswahllistenListsAdapter.notifyDataSetChanged();
                    auswahllistenDialog.dismiss();
                }
            }
        });

        Button dialogButtonCancel = (Button) auswahllistenDialog.findViewById(R.id.addVorauswahllisteCancel);
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                auswahllistenDialog.dismiss();
            }
        });
        auswahllistenDialog.show();

    }


}

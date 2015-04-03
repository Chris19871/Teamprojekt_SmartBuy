package smartbuy.teamproject.application;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import database.DbAdapter;
import database.Vorauswahl;
import swipe.SwipeDismissListViewTouchListener;

public class VorauswahllistenActivity extends ActionBarActivity {

    private boolean delet = false;
    private final Context context = this;
    private ArrayAdapter<Vorauswahl> newVorauswahllistenListsAdapter;
    private ArrayList<Vorauswahl> newVorauswahllisten;
    private static String aktVorauswahlListe;
    private DbAdapter dbAdapter;
    private Vorauswahl zuletztGeleoscht;
    private int zuletztGeleoschtPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vorauswahllisten);

        dbAdapter = StartbildschirmActivity.getDbAdapter();
        dbAdapter.openRead();
        newVorauswahllisten = dbAdapter.getAllEntriesVorauswahlListe();
        dbAdapter.close();

        registerForContextMenu(findViewById(R.id.vorauswahllistenlistView));
        ListView listView = (ListView) findViewById(R.id.vorauswahllistenlistView);
        newVorauswahllistenListsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, newVorauswahllisten);

        listView.setAdapter(newVorauswahllistenListsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                aktVorauswahlListe = newVorauswahllisten.get(position).getName();
                change();
            }
        });

    SwipeDismissListViewTouchListener touchListener =
            new SwipeDismissListViewTouchListener(
                    listView,
                    new SwipeDismissListViewTouchListener.DismissCallbacks() {
                        @Override
                        public boolean canDismiss(int position) {
                            return true;
                        }

                        @Override
                        public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                            for (int position : reverseSortedPositions) {
                                zuletztGeleoschtPosition = position;
                                zuletztGeleoscht = newVorauswahllistenListsAdapter.getItem(position);
                                newVorauswahllistenListsAdapter.remove(newVorauswahllistenListsAdapter.getItem(position));

                                newVorauswahllistenListsAdapter.notifyDataSetChanged();
                            }

                            final Dialog loeschen_rueck = new Dialog(context);

                            loeschen_rueck.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    if (delet == false)
                                    {
                                        dbAdapter.openWrite();
                                        dbAdapter.deleteTableVorauswahl(zuletztGeleoscht.getName());
                                        dbAdapter.close();
                                        delet =false;
                                    }
                                }
                            });
                            loeschen_rueck.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            loeschen_rueck.setContentView(R.layout.loeschen_rueck_dialog);
                            loeschen_rueck.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                            loeschen_rueck.getWindow().setGravity(Gravity.BOTTOM);
                            loeschen_rueck.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


                            final Button loeschen_Ruck = (Button) loeschen_rueck.findViewById(R.id.deleteUndoButton);
                            loeschen_Ruck.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    delet = true;
                                    newVorauswahllistenListsAdapter.insert(zuletztGeleoscht, zuletztGeleoschtPosition);
                                    newVorauswahllistenListsAdapter.notifyDataSetChanged();

                                    loeschen_rueck.dismiss();
                                }
                            });

                            loeschen_rueck.show();
                        }
                    });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.vorauswahllistenlistView) {
            getMenuInflater().inflate(R.menu.vorauswahllisten_contextmenu, menu);
        }
        super.onCreateContextMenu(menu, v, menuInfo);

    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {

            case R.id.action_ContextMenu_Change_Name: {
                final Dialog nameAndern = new Dialog(context);
                nameAndern.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nameAndern.setContentView(R.layout.neue_vorauswahllisten_dialog);

                final EditText name = (EditText) nameAndern.findViewById(R.id.addVorauswahllisteTextView);
                name.setText(newVorauswahllistenListsAdapter.getItem(info.position).getName());

                Button dialogButtonSave = (Button) nameAndern.findViewById(R.id.addVorauswahllisteSave);
                dialogButtonSave.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (name.getText().toString().equals("")) {
                            name.setHintTextColor(Color.parseColor("#FF0000"));
                            name.setHint("Feld muss ausgefüllt werden!");
                        } else {
                            dbAdapter.openWrite();
                            dbAdapter.changeListNameVorauswahlliste(newVorauswahllistenListsAdapter.getItem(info.position).getName(),name.getText().toString());
                            dbAdapter.close();


                            newVorauswahllistenListsAdapter.getItem(info.position).setName(name.getText().toString());
                            newVorauswahllistenListsAdapter.notifyDataSetChanged();
                            nameAndern.dismiss();
                        }
                    }
                });

                Button dialogButtonCancel = (Button) nameAndern.findViewById(R.id.addVorauswahllisteCancel);
                dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        nameAndern.dismiss();
                    }
                });
                nameAndern.show();
                break;
            }
            case R.id.action_ContextMenu_delete: {
                zuletztGeleoschtPosition = info.position;
                zuletztGeleoscht = newVorauswahllistenListsAdapter.getItem(info.position);
                newVorauswahllistenListsAdapter.remove(newVorauswahllistenListsAdapter.getItem(info.position));

                newVorauswahllistenListsAdapter.notifyDataSetChanged();

                final Dialog loeschen_rueck = new Dialog(context);

                loeschen_rueck.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (delet == false)
                        {
                            dbAdapter.openWrite();
                            dbAdapter.deleteTableVorauswahl(zuletztGeleoscht.getName());
                            dbAdapter.close();
                            delet =false;
                        }
                    }
                });
                loeschen_rueck.requestWindowFeature(Window.FEATURE_NO_TITLE);
                loeschen_rueck.setContentView(R.layout.loeschen_rueck_dialog);
                loeschen_rueck.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                loeschen_rueck.getWindow().setGravity(Gravity.BOTTOM);
                loeschen_rueck.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                Button loeschen_Ruck = (Button) loeschen_rueck.findViewById(R.id.deleteUndoButton);
                loeschen_Ruck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delet = true;
                        newVorauswahllistenListsAdapter.insert(zuletztGeleoscht, zuletztGeleoschtPosition);
                        newVorauswahllistenListsAdapter.notifyDataSetChanged();

                        loeschen_rueck.dismiss();
                    }
                });

                loeschen_rueck.show();
                break;

            }
        }
        return super.onContextItemSelected(item);
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
                   // ArrayList<EinkaufsArtikel> newList = new ArrayList<>();
                   // VorauswahlListe liste = new VorauswahlListe(name.getText().toString(), newList);
                   // newVorauswahllisten.add(liste);

                    dbAdapter.openWrite();
                    dbAdapter.createEntryVorauswahlliste(name.getText().toString());
                    dbAdapter.addListe(name.getText().toString());
                    dbAdapter.close();


                    newVorauswahllisten.clear();

                    dbAdapter.openRead();
                    newVorauswahllisten.addAll(dbAdapter.getAllEntriesVorauswahlListe());
                    dbAdapter.close();

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
    public static String getAktVorauswahlListe() {
        return aktVorauswahlListe;
    }


}

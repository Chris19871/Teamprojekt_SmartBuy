package smartbuy.teamproject.application;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import database.DbAdapter;
import database.Vorauswahl;
import purchase.EinkaufsArtikel;
import purchase.Einkaufsliste;
import swipe.SwipeDismissListViewTouchListener;

public class StartbildschirmActivity extends ActionBarActivity {
    private final Context context = this;
    private ArrayAdapter<Vorauswahl> vorauswahllistenitemListsAdapter;
    private ArrayAdapter<database.Einkaufsliste> itemListsAdapter;

    private boolean delet = false;
    private ArrayList<database.EinkaufsArtikel> addNewList;
    private ArrayList<database.Einkaufsliste> einkaufsliste;
    private ArrayList<Vorauswahl> vorauswahllisten;
    private EditText listName;
    private static String aktListe;
    private database.Einkaufsliste zuletztGeleoscht;
    private int zuletztGeleoschtPosition;
    private static DbAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startbildschirm);

        dbAdapter = new DbAdapter(this);
        dbAdapter.openWrite();
        if (dbAdapter.getAllEntriesVorauswahlListe().size() == 0)
        {
            dbAdapter.createVorauswahllisten();
        }
        dbAdapter.close();

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/BAUHS93.TTF");
        TextView startText = (TextView) findViewById(R.id.startScreenText);
        startText.setTypeface(font);

        ActionBar smartBuyActionBar = getSupportActionBar();
        smartBuyActionBar.setDisplayShowTitleEnabled(false);

        vorauswahllisten = new ArrayList<>();

        dbAdapter.openRead();
        einkaufsliste = dbAdapter.getAllEntriesEinkaufsliste();
        dbAdapter.close();
        registerForContextMenu(findViewById(R.id.startscreenListView));
        ListView listView = (ListView) findViewById(R.id.startscreenListView);
        itemListsAdapter = new ArrayAdapter<>(this,
                R.layout.listview_design, R.id.listViewDesign, einkaufsliste);
        listView.setAdapter(itemListsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                aktListe = einkaufsliste.get(position).getName();
                wechsel();
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
                                    zuletztGeleoscht = itemListsAdapter.getItem(position);
                                    itemListsAdapter.remove(itemListsAdapter.getItem(position));

                                    itemListsAdapter.notifyDataSetChanged();
                                }

                                final Dialog loeschen_rueck = new Dialog(context);

                                loeschen_rueck.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialog) {
                                        if (delet == false)
                                        {
                                            dbAdapter.openWrite();
                                            dbAdapter.deleteTableEinkaufliste(itemListsAdapter.getItem(zuletztGeleoschtPosition).getName());
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
                                loeschen_Ruck.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        delet = true;
                                        itemListsAdapter.insert(zuletztGeleoscht, zuletztGeleoschtPosition);
                                        itemListsAdapter.notifyDataSetChanged();

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
        // Inflate the menu; this adds vorauswahllistenitemListsAdapter to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_startbildschirm, menu);
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
        if (id == R.id.vorAuswahlliste) {
            vorAuswahllisteOpen();
            return true;
        }
        if (id == R.id.action_add) {
            newEinkaufslisteOpen();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.startscreenListView) {
            getMenuInflater().inflate(R.menu.startbildschirm_contextmenu, menu);
        }
        super.onCreateContextMenu(menu, v, menuInfo);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.action_ContextMenu_Einkaufsmodus: {
                aktListe = itemListsAdapter.getItem(info.position).getName();
                openEinkaufsmodus();
                break;
            }
            case R.id.action_ContextMenu_Change_Name: {
                final Dialog nameAndern = new Dialog(context);
                nameAndern.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nameAndern.setContentView(R.layout.neue_vorauswahllisten_dialog);

                final EditText name = (EditText) nameAndern.findViewById(R.id.addVorauswahllisteTextView);
                name.setText(itemListsAdapter.getItem(info.position).getName());

                Button dialogButtonSave = (Button) nameAndern.findViewById(R.id.addVorauswahllisteSave);
                dialogButtonSave.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (name.getText().toString().equals("")) {
                            name.setHintTextColor(Color.parseColor("#FF0000"));
                            name.setHint("Feld muss ausgefüllt werden!");
                        } else {
                            dbAdapter.openWrite();
                            dbAdapter.changeListName(itemListsAdapter.getItem(info.position).getName(),name.getText().toString());
                            dbAdapter.close();


                            itemListsAdapter.getItem(info.position).setName(name.getText().toString());
                            itemListsAdapter.notifyDataSetChanged();
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
            case R.id.action_ContextMenu_Best_Time: {
                final Dialog bestzeit = new Dialog(context);
                bestzeit.requestWindowFeature(Window.FEATURE_NO_TITLE);
                bestzeit.setContentView(R.layout.bestzeit_dialog);

                final EditText liste = (EditText) bestzeit.findViewById(R.id.bestTimeList);
                final EditText bestzeitText = (EditText) bestzeit.findViewById(R.id.bestTime);
                Button bestzeitButton = (Button) bestzeit.findViewById(R.id.bestTimeButton);

                liste.setText(itemListsAdapter.getItem(info.position).getName());

                bestzeitButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bestzeitText.setText("00:00:00");

                    }
                });

                bestzeit.show();
            }
            break;
            case R.id.action_ContextMenu_delete: {
                zuletztGeleoschtPosition = info.position;
                zuletztGeleoscht = itemListsAdapter.getItem(info.position);
                itemListsAdapter.remove(itemListsAdapter.getItem(info.position));

                itemListsAdapter.notifyDataSetChanged();

                final Dialog loeschen_rueck = new Dialog(context);

                loeschen_rueck.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        dbAdapter.openWrite();
                        dbAdapter.deleteTableEinkaufliste(itemListsAdapter.getItem(zuletztGeleoschtPosition).getName());
                        dbAdapter.close();
                        delet =false;
                    }
                });
                loeschen_rueck.requestWindowFeature(Window.FEATURE_NO_TITLE);
                loeschen_rueck.setContentView(R.layout.loeschen_rueck_dialog);
                loeschen_rueck.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                loeschen_rueck.getWindow().setGravity(Gravity.BOTTOM);
                loeschen_rueck.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                Button loeschen_Ruck = (Button) loeschen_rueck.findViewById(R.id.deleteUndoButton);
                loeschen_Ruck.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delet = true;
                        itemListsAdapter.insert(zuletztGeleoscht, zuletztGeleoschtPosition);
                        itemListsAdapter.notifyDataSetChanged();

                        loeschen_rueck.dismiss();
                    }
                });

                loeschen_rueck.show();
                break;

            }
            case R.id.action_ContextMenu_reset: {
                resetList(info.position);
            }
            break;
        }
        return super.onContextItemSelected(item);
    }

    public void newEinkaufslisteOpen() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.neue_einkaufsliste_dialog);
        final String empty = "";

        listName = (EditText) dialog.findViewById(R.id.dialogName);

        final GridView grid = (GridView) dialog.findViewById(R.id.gridViewCheckbox);

        dbAdapter.openRead();
        vorauswahllisten = dbAdapter.getAllEntriesVorauswahlListe();
        dbAdapter.close();

        vorauswahllistenitemListsAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line, vorauswahllisten);


        // set the neue_einkaufsliste_dialog dialog components - text, spinner, layout and button
        Spinner spinner = (Spinner) dialog.findViewById(R.id.dialogSpinner);
        spinner.setAdapter(vorauswahllistenitemListsAdapter);

        Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                String item = vorauswahllistenitemListsAdapter.getItem(arg2).getName();

                dbAdapter.openRead();
                ArrayList<database.EinkaufsArtikel> pItems = dbAdapter.getAllEntriesArtikel(item);
                dbAdapter.close();


                addNewList = pItems;
                database.EinkaufsArtikel artikel[] = new database.EinkaufsArtikel[pItems.size()];
                for(int i = 0; i < artikel.length; i++)
                {
                    artikel[i] = pItems.get(i);
                }
                NeueEinkaufslisteAdapter adapter = new NeueEinkaufslisteAdapter(context, artikel);
                grid.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        dialogButtonOk.setOnClickListener(new OnClickListener() {
            public void onClick(View v) throws IllegalArgumentException {
                // Prüfung, ob der neuen Liste ein Name gegeben wurde.
                if (listName.getText().toString().equals("")) {
                    listName.setHintTextColor(Color.parseColor("#FF0000"));
                    listName.setHint("Feld muss ausgefüllt werden!");
                } else {
                    NeueEinkaufslisteAdapter adapter = (NeueEinkaufslisteAdapter)grid.getAdapter();
                    ArrayList<database.EinkaufsArtikel> checkArticle = adapter.getCheckedItems();
                    dbAdapter.openWrite();
                    dbAdapter.createEinkaufsliste(listName.getText().toString(), checkArticle);
                    dbAdapter.close();

                    einkaufsliste.clear();
                    dbAdapter.openRead();
                    einkaufsliste.addAll(dbAdapter.getAllEntriesEinkaufsliste());
                    itemListsAdapter.notifyDataSetChanged();

                    dialog.dismiss();
                }
            }
        });

        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        // if button is clicked, close the custom neue_einkaufsliste_dialog
        dialogButtonCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void addList(database.Einkaufsliste list) {
        einkaufsliste.add(list);
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

    public void vorAuswahllisteOpen() {
        final Intent auswahl = new Intent(this, VorauswahllistenActivity.class);
        startActivity(auswahl);
    }

    public void wechsel() {
        final Intent einkaufsliste = new Intent(this, EinkaufslisteActivity.class);
        startActivity(einkaufsliste);
    }

    public void openEinkaufsmodus() {
        final Intent einkaufsmodus = new Intent(this, EinkaufmodusActivity.class);
        startActivity(einkaufsmodus);

    }

    public void resetList(int pos) {
        dbAdapter.openWrite();
        dbAdapter.resetList(itemListsAdapter.getItem(pos).getName());
        dbAdapter.close();
    }


    public static String getAktListe() {
        return aktListe;
    }

    public static DbAdapter getDbAdapter()
    {
        return dbAdapter;
    }
}

package smartbuy.teamproject.application;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.SparseBooleanArray;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

import purchase.EinkaufsArtikel;
import purchase.Einkaufsliste;
import purchase.VorauswahlListe;
import swipe.SwipeDismissListViewTouchListener;

public class MainActivity extends ActionBarActivity
{
    final Context context = this;
    private GridLayout grid;
    private ArrayAdapter<VorauswahlListe> vorauswahllistenitemListsAdapter;
    private ArrayAdapter<Einkaufsliste> itemListsAdapter;
    private CheckBox[] b;
    private int boxCounter = 0;
    private ArrayList<EinkaufsArtikel> addNewList;
    private ArrayList<Einkaufsliste> einkaufsliste;
    private ArrayList<VorauswahlListe> vorauswahllisten;
    private EditText listName;
    private static Einkaufsliste aktListe;
    private Einkaufsliste zuletztGeleoscht;
    private int zuletztGeleoschtPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/BAUHS93.TTF");
        TextView startText = (TextView) findViewById(R.id.startScreenText);
        startText.setTypeface(font);

        ActionBar smartBuyActionBar = getSupportActionBar();
        smartBuyActionBar.setDisplayShowTitleEnabled(false);

        vorauswahllisten = new ArrayList<>();

        einkaufsliste = new ArrayList<>();
        registerForContextMenu(findViewById(R.id.ListView));
        ListView listView = (ListView) findViewById(R.id.ListView);
        itemListsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, einkaufsliste);
        listView.setAdapter(itemListsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                aktListe = einkaufsliste.get(position);
                wechsel();
            }
        });

                SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks()
                        {
                            @Override
                            public boolean canDismiss(int position)
                            {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions)
                            {
                                for (int position : reverseSortedPositions)
                                {
                                    zuletztGeleoschtPosition = position;
                                    zuletztGeleoscht=itemListsAdapter.getItem(position);
                                    itemListsAdapter.remove(itemListsAdapter.getItem(position));
                                }

                                final Dialog loeschen_rueck = new Dialog(context);
                                loeschen_rueck.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                loeschen_rueck.setContentView(R.layout.loeschen_rueck_dialog);
                                loeschen_rueck.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                loeschen_rueck.getWindow().setGravity(Gravity.BOTTOM);
                                loeschen_rueck.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                                Button loeschen_Ruck = (Button) loeschen_rueck.findViewById(R.id.loeschen_RuckButton);
                                loeschen_Ruck.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        itemListsAdapter.insert(zuletztGeleoscht,zuletztGeleoschtPosition);
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        if (v.getId() == R.id.ListView)
        {
            getMenuInflater().inflate(R.menu.mainactivitycontextmenu, menu);
        }
        super.onCreateContextMenu(menu, v, menuInfo);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId())
        {
            case R.id.action_ContextMenu_Einkaufsmodus:
            {
                openEinkaufsmodus();
                break;
            }
            case R.id.action_ContextMenu_Name_ändern:
            {
                final Dialog nameAndern = new Dialog(context);
                nameAndern.requestWindowFeature(Window.FEATURE_NO_TITLE);
                nameAndern.setContentView(R.layout.add_auswahllisten_dialog);

                final EditText name = (EditText) nameAndern.findViewById(R.id.addAuswahllisteTextView);
                name.setText(itemListsAdapter.getItem(info.position).getName());

                Button dialogButtonSave = (Button) nameAndern.findViewById(R.id.addAuswahllisteSave);
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

                            itemListsAdapter.getItem(info.position).setName(name.getText().toString());
                            itemListsAdapter.notifyDataSetChanged();
                            nameAndern.dismiss();
                        }
                    }
                });

                Button dialogButtonCancel = (Button) nameAndern.findViewById(R.id.addAuswahllisteCancel);
                dialogButtonCancel.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        nameAndern.dismiss();
                    }
                });
                nameAndern.show();
                break;
            }
            case R.id.action_ContextMenu_Löschen:
            {
                zuletztGeleoschtPosition = info.position;
                zuletztGeleoscht = itemListsAdapter.getItem(info.position);
                itemListsAdapter.remove(itemListsAdapter.getItem(info.position));

                final Dialog loeschen_rueck = new Dialog(context);
                loeschen_rueck.requestWindowFeature(Window.FEATURE_NO_TITLE);
                loeschen_rueck.setContentView(R.layout.loeschen_rueck_dialog);
                loeschen_rueck.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                loeschen_rueck.getWindow().setGravity(Gravity.BOTTOM);
                loeschen_rueck.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                Button loeschen_Ruck = (Button) loeschen_rueck.findViewById(R.id.loeschen_RuckButton);
                loeschen_Ruck.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        itemListsAdapter.insert(zuletztGeleoscht,zuletztGeleoschtPosition);
                        itemListsAdapter.notifyDataSetChanged();
                        loeschen_rueck.dismiss();
                    }
                });

                loeschen_rueck.show();
                break;

            }
            case R.id.action_ContextMenu_Zurücksetzen:
            {

            }
            break;
        }
        return super.onContextItemSelected(item);
    }

    public void newEinkaufsliste()
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.preselection_dialog);
        final String empty = "";

        listName = (EditText) dialog.findViewById(R.id.dialogName);
        grid = (GridLayout) dialog.findViewById(R.id.gridLayout);

        // SmartBuy Vorauswahllisten erstellen
        String[] geburstagArtikel = {"Partyhüte", "Besteck"};
        ArrayList<EinkaufsArtikel> gebItems = new ArrayList<>();
        EinkaufsArtikel artikel;
        for (int i = 0; i < geburstagArtikel.length; i++)
        {
            artikel = new EinkaufsArtikel(geburstagArtikel[i], "", null);
            gebItems.add(artikel);

        }

        VorauswahlListe geburtstag = new VorauswahlListe("Geburtstag", gebItems);
        vorauswahllisten.add(geburtstag);

        String[] partyArtikel = {"Fleisch", "Bier"};
        ArrayList<EinkaufsArtikel> partyItems = new ArrayList<>();
        for (int i = 0; i < partyArtikel.length; i++)
        {
            artikel = new EinkaufsArtikel(partyArtikel[i], "", null);
            partyItems.add(artikel);

        }
        VorauswahlListe party = new VorauswahlListe("Party", partyItems);
        vorauswahllisten.add(party);


        vorauswahllistenitemListsAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line, vorauswahllisten);


        // set the preselection_dialog dialog components - text, spinner, layout and button
        Spinner spinner = (Spinner) dialog.findViewById(R.id.dialogSpinner);
        spinner.setAdapter(vorauswahllistenitemListsAdapter);

        Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3)
            {
                addBox(arg2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0)
            {
                // TODO Auto-generated method stub
            }
        });

        dialogButtonOk.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v) throws IllegalArgumentException
            {
                // Prüfung, ob der neuen Liste ein Name gegeben wurde.
                if (listName.getText().toString().equals(""))
                {
                    listName.setHintTextColor(Color.parseColor("#FF0000"));
                    listName.setHint("Feld muss ausgefüllt werden!");
                } else
                {
                    generateItemList();
                    itemListsAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });

        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.dialogButtonCancel);
        // if button is clicked, close the custom preselection_dialog
        dialogButtonCancel.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void addBox(int index)
    {
        VorauswahlListe item = vorauswahllistenitemListsAdapter.getItem(index);
        ArrayList<EinkaufsArtikel> pItems = item.getItems();
        addNewList = pItems;
        grid.removeAllViews();
        grid.setColumnCount(2);

        int columnIndex = 0;
        int rowIndex = 0;

        b = new CheckBox[pItems.size()];
        for (int i = 0; i < pItems.size(); i++)
        {
            b[i] = new CheckBox(this);
            b[i].setText(pItems.get(i).getName());
            b[i].setMinimumWidth(255);
        }

        boxCounter = b.length;

        for (int i = 0; i < b.length; i++)
        {
            if (i > 1 && i % 2 == 0)
            {
                rowIndex++;
            }

            GridLayout.Spec row = GridLayout.spec(rowIndex, 1);
            GridLayout.Spec colspan = GridLayout.spec(columnIndex, 1);
            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, colspan);
            grid.addView(b[i], gridLayoutParam);
            columnIndex++;
        }

    }

    public void generateItemList()
    {
        ArrayList<EinkaufsArtikel> newList = new ArrayList<>();
        for (int i = 0; i < boxCounter; i++)
        {
            if (b[i].isChecked())
            {
                newList.add(addNewList.get(i));
            }
        }
        addList(new Einkaufsliste(listName.getText().toString(), newList));
    }

    public void addList(Einkaufsliste list)
    {
        einkaufsliste.add(list);
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

    public void auswahlliste()
    {
        final Intent auswahl = new Intent(this, Auswahllisten.class);
        startActivity(auswahl);
    }

    public void wechsel()
    {
        final Intent einkaufsliste = new Intent(this, EinkaufslisteActivity.class);
        startActivity(einkaufsliste);
    }

    public void openEinkaufsmodus()
    {
        final Intent einkaufsmodus = new Intent(this, EinkaufmodusActivity.class);
        startActivity(einkaufsmodus);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds vorauswahllistenitemListsAdapter to the action bar if it is present.
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
        if (id == R.id.auswahlliste)
        {
            auswahlliste();
            return true;
        }
        if (id == R.id.action_add)
        {
            newEinkaufsliste();
            return true;
        }
        if (id == R.id.action_delete)
        {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Einkaufsliste getAktListe()
    {
        return aktListe;
    }

    public static void setAktListe(Einkaufsliste aktListe)
    {
        MainActivity.aktListe = aktListe;
    }
}

package smartbuy.teamproject.application;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import database.DbAdapter;
import swipe.SwipeDismissListViewTouchListener;


public class EinkaufslisteActivity extends ActionBarActivity
{
    private String aktListeName;
    private String aktListe;
    private DbAdapter dbAdapter;
    private final Context context = this;
    private ArrayAdapter<database.EinkaufsArtikel> itemAdapter;
    private ListView listView;
    private ArrayList<database.EinkaufsArtikel> allItems;
    private ActionBar einkaufslisteActionBar;
    private boolean longClickEnabled;
    private boolean deleteEnable = false;
    private ArrayList<database.EinkaufsArtikel> geloschteArtikel;
    private ArrayList<Integer> geloschteArtikelPositionen;
    private boolean articleDelete = false;
    private boolean isDeleted = false;
    private boolean delete = false;

    /**
     * Create ListView with all products from an "Einkaufsliste".
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.einkaufsliste);
        einkaufslisteActionBar = getSupportActionBar();

        geloschteArtikel = new ArrayList<>();
        geloschteArtikelPositionen = new ArrayList<>();

        dbAdapter = StartbildschirmActivity.getDbAdapter();
        aktListe = StartbildschirmActivity.getAktListe();
        aktListeName = StartbildschirmActivity.getAktListeName();

        einkaufslisteActionBar.setTitle(aktListeName);
        einkaufslisteActionBar.setDisplayShowTitleEnabled(true);

        listView = (ListView) findViewById(R.id.einkaufslisteListView);

        dbAdapter.openRead();
        allItems = dbAdapter.getAllEntriesArtikel(aktListe);
        dbAdapter.close();

        itemAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.listview_design, R.id.listViewDesign, allItems);
        listView.setAdapter(itemAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (!longClickEnabled)
                {
                    changeArticlelDialog(position);
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                longClickEnabled = true;
                deleteMode();
                return true;
            }
        });

        //Swipe to left or right to delete the item
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks()
                        {
                            int pos;

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
                                    pos = position;
                                    geloschteArtikel.clear();
                                    geloschteArtikelPositionen.clear();
                                    geloschteArtikelPositionen.add(position);
                                    geloschteArtikel.add(itemAdapter.getItem(position));
                                    itemAdapter.remove(itemAdapter.getItem(position));
                                    isDeleted = true;
                                }

                                //Show a dialog to undo the last action
                                final Dialog loeschen_rueck = new Dialog(context);
                                loeschen_rueck.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                loeschen_rueck.setContentView(R.layout.loeschen_rueck_dialog);
                                loeschen_rueck.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                loeschen_rueck.getWindow().setGravity(Gravity.BOTTOM);
                                loeschen_rueck.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                                Button loeschen_Ruck = (Button) loeschen_rueck.findViewById(R.id.deleteUndoButton);
                                loeschen_Ruck.setOnClickListener(new OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        isDeleted = false;
                                        itemAdapter.insert(geloschteArtikel.get(0), geloschteArtikelPositionen.get(0));
                                        itemAdapter.notifyDataSetChanged();
                                        loeschen_rueck.dismiss();
                                    }
                                });

                                loeschen_rueck.show();
                                if (isDeleted)
                                {
                                    dbAdapter.openWrite();
                                    dbAdapter.deleteArtikel(aktListe, geloschteArtikel.get(0).getId());
                                    allItems.clear();
                                    allItems.addAll(dbAdapter.getAllEntriesArtikel(aktListe));
                                    dbAdapter.close();
                                    itemAdapter.notifyDataSetChanged();
                                }
                            }

                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_einkaufsliste, menu);

        MenuItem delete = menu.findItem(R.id.action_delete_Einkaufliste);
        MenuItem add = menu.findItem(R.id.action_newProduct);
        MenuItem cart = menu.findItem(R.id.action_Einkaufsmodus);


        if (deleteEnable)
        {
            delete.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            delete.setVisible(true);
            add.setVisible(false);
            cart.setVisible(false);
        }
        else
        {
            delete.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            delete.setVisible(false);
        }
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
        if (id == R.id.about)
        {
            aboutOpen();
            return true;
        }
        if (id == R.id.vorAuswahlliste)
        {
            vorAuswahllisteOpen();
            return true;
        }
        if (id == R.id.action_newProduct)
        {
            newProductOpen();
            return true;

        }
        if (id == R.id.action_Einkaufsmodus)
        {
            einkaufsmodusOpen();
            return true;
        }
        if (id == R.id.action_delete_Einkaufliste)
        {
            deleteSelectedItems();
            return true;
        }
        if (id == android.R.id.home)
        {
            articleDelete = false;
            normalMode();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void settingsOpen()
    {
        final Intent settings = new Intent(this, EinstellungenActivity.class);
        startActivity(settings);
    }

    public void aboutOpen()
    {
        final Dialog uberDialog = new Dialog(context);
        uberDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        uberDialog.setContentView(R.layout.ueber_dialog);

        uberDialog.show();
    }

    public void vorAuswahllisteOpen()
    {
        final Intent auswahl = new Intent(this, VorauswahllistenActivity.class);
        startActivity(auswahl);
    }

    /**
     * Open dialog to create a new product and add it to the "Einkaufsliste"
     */
    public void newProductOpen()
    {
        final Dialog newProducts = new Dialog(context);
        newProducts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        newProducts.setContentView(R.layout.neues_produkt_dialog);

        final EditText name = (EditText) newProducts.findViewById(R.id.productName);
        final TextView desc = (TextView) newProducts.findViewById(R.id.descNewProduct);
        final ImageView image = (ImageView) newProducts.findViewById(R.id.newProductLogo);

        name.setImeOptions(EditorInfo.IME_ACTION_DONE);
        image.setImageResource(R.mipmap.smartbuy_logo);
        image.setTag(R.mipmap.smartbuy_logo);

        //open EinkaufsArtikelImageAdapter to choose an image
        image.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog imageAuswahlDialog = new Dialog(context);
                imageAuswahlDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                imageAuswahlDialog.setContentView(R.layout.einkaufsartikel_image_dialog);

                final GridView gridView = (GridView) imageAuswahlDialog.findViewById(R.id.einkaufartikelImagelView);
                final EinkaufsArtikelImageAdapter Iadapter = new EinkaufsArtikelImageAdapter(context, imageAuswahlDialog);

                gridView.setAdapter(Iadapter);

                imageAuswahlDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
                {
                    @Override
                    public void onDismiss(DialogInterface dialog)
                    {
                        if(Iadapter.getImageName() != 0) {
                            name.setText(Iadapter.getImageName());
                            image.setImageResource(Iadapter.getImage());
                            image.setTag(Iadapter.getImage());
                        }
                    }
                });
                imageAuswahlDialog.show();
            }
        });

        Button dialogButtonSave = (Button) newProducts.findViewById(R.id.newProductSave);
        dialogButtonSave.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                String last = name.getText().toString().substring(name.getText().toString().length()-1);

                if (name.getText().toString().equals(""))
                {
                    name.setHintTextColor(Color.parseColor("#FF0000"));
                    name.setHint("Feld muss ausgefüllt werden!");
                }
                if(last.equals("."))
                {
                    name.getText().clear();
                    name.setHintTextColor(Color.parseColor("#FF0000"));
                    name.setHint("Punkt am Ende ist unzulässig");

                }
                else
                {
                    int resId = (Integer) image.getTag();
                    addArticle(name.getText().toString(), desc.getText().toString(), resId);
                    newProducts.dismiss();
                }
            }
        });

        Button dialogButtonCancel = (Button) newProducts.findViewById(R.id.newProductCancel);
        dialogButtonCancel.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                newProducts.dismiss();
            }
        });

        newProducts.show();
    }

    /**
     * Change to the EinkaufsmodusActivity
     */
    public void einkaufsmodusOpen()
    {
        final Intent einkaufsmodus = new Intent(this, EinkaufmodusActivity.class);
        startActivity(einkaufsmodus);
        finish();
    }

    /**
     * Change the ActionBar-Items and enable multiple choice to delete items
     */
    public void deleteMode()
    {
        itemAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_multiple_choice, allItems);
        listView.setAdapter(itemAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        einkaufslisteActionBar.setDisplayHomeAsUpEnabled(true);
        deleteEnable = true;
        invalidateOptionsMenu();
    }

    /**
     * Change the ActionBar-Items and enable single choice mode for ArrayAdapter
     */
    public void normalMode()
    {
        itemAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.listview_design, R.id.listViewDesign, allItems);
        listView.setAdapter(itemAdapter);

        einkaufslisteActionBar.setDisplayHomeAsUpEnabled(false);
        longClickEnabled = false;

        deleteEnable = false;
        invalidateOptionsMenu();

        if (articleDelete)
        {
            //Show a dialog to undo the last action
            final Dialog loeschen_rueck = new Dialog(context);

            loeschen_rueck.setOnDismissListener(new DialogInterface.OnDismissListener()
            {
                @Override
                public void onDismiss(DialogInterface dialog)
                {
                    if (!delete)
                    {
                        dbAdapter.openWrite();
                        for (int i = geloschteArtikel.size() - 1; i >= 0; i--)
                        {
                            dbAdapter.deleteArtikel(aktListe, geloschteArtikel.get(i).getId());
                        }
                        dbAdapter.close();
                    }
                }
            });

            loeschen_rueck.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loeschen_rueck.setContentView(R.layout.loeschen_rueck_dialog);
            loeschen_rueck.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            loeschen_rueck.getWindow().setGravity(Gravity.BOTTOM);
            loeschen_rueck.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            Button loeschen_Ruck = (Button) loeschen_rueck.findViewById(R.id.deleteUndoButton);
            loeschen_Ruck.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    delete = true;
                    for (int i = (geloschteArtikel.size() - 1); i >= 0; i--)
                    {
                        itemAdapter.insert(geloschteArtikel.get(i), geloschteArtikelPositionen.get(i));
                    }
                    loeschen_rueck.dismiss();
                }
            });
            loeschen_rueck.show();
        }
        articleDelete = false;
        itemAdapter.notifyDataSetChanged();
    }

    /**
     * Open a dialog to change the values of a product
     * @param pos Position of item in List
     */
    public void changeArticlelDialog(final int pos)
    {
        final EditText name;
        final EditText desc;
        final ImageView image;

        dbAdapter.openRead();
        database.EinkaufsArtikel tmpArtikel = dbAdapter.getArtikel(aktListe, allItems.get(pos).getName());
        dbAdapter.close();

        final Dialog newProducts = new Dialog(context);
        newProducts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        newProducts.setContentView(R.layout.neues_produkt_dialog);

        name = (EditText) newProducts.findViewById(R.id.productName);
        desc = (EditText) newProducts.findViewById(R.id.descNewProduct);
        image = (ImageView) newProducts.findViewById(R.id.newProductLogo);

        dbAdapter.openRead();
        final long id = dbAdapter.getArtikel(aktListe, tmpArtikel.getName()).getId();
        dbAdapter.close();

        name.setText(tmpArtikel.getName());
        name.setImeOptions(EditorInfo.IME_ACTION_DONE);
        desc.setText(tmpArtikel.getDesc());
        image.setImageResource(tmpArtikel.getPic());
        image.setTag(tmpArtikel.getPic());

        //open EinkaufsArtikelImageAdapter to choose an image
        image.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final Dialog imageAuswahlDialog = new Dialog(context);
                imageAuswahlDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                imageAuswahlDialog.setContentView(R.layout.einkaufsartikel_image_dialog);

                final GridView gridView = (GridView) imageAuswahlDialog.findViewById(R.id.einkaufartikelImagelView);
                final EinkaufsArtikelImageAdapter Iadapter = new EinkaufsArtikelImageAdapter(context, imageAuswahlDialog);

                gridView.setAdapter(Iadapter);

                imageAuswahlDialog.setOnDismissListener(new DialogInterface.OnDismissListener()
                {
                    @Override
                    public void onDismiss(DialogInterface dialog)
                    {
                        image.setImageResource(Iadapter.getImage());
                        image.setTag(Iadapter.getImage());
                    }
                });
                imageAuswahlDialog.show();
            }
        });

        Button dialogButtonSave = (Button) newProducts.findViewById(R.id.newProductSave);
        dialogButtonSave.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                String last = name.getText().toString().substring(name.getText().toString().length()-1);

                if (name.getText().toString().equals(""))
                {
                    name.setHintTextColor(Color.parseColor("#FF0000"));
                    name.setHint("Feld muss ausgefüllt werden!");
                }
                if(last.equals("."))
                {
                    name.getText().clear();
                    name.setHintTextColor(Color.parseColor("#FF0000"));
                    name.setHint("Punkt am Ende ist unzulässig");

                }
                else
                {
                    int resId = (Integer) image.getTag();
                    changeArticle(name.getText().toString(), desc.getText().toString(), resId, id);

                    allItems.clear();
                    dbAdapter.openWrite();
                    allItems.addAll(dbAdapter.getAllEntriesArtikel(aktListe));
                    dbAdapter.close();
                    itemAdapter.notifyDataSetChanged();
                    newProducts.dismiss();
                }
            }
        });

        Button dialogButtonCancel = (Button) newProducts.findViewById(R.id.newProductCancel);
        dialogButtonCancel.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                newProducts.dismiss();
            }
        });
        newProducts.show();
    }

    public void changeArticle(String name, String desc, int image, long id)
    {
        dbAdapter.openWrite();
        dbAdapter.changeArtikel(aktListe, name, desc, image, id);
        dbAdapter.close();
    }

    public void addArticle(String name, String desc, int image)
    {
        dbAdapter.openWrite();
        dbAdapter.createEntryEinkaufArtikeltoTable(aktListe, name, desc, image);
        allItems.clear();
        allItems.addAll(dbAdapter.getAllEntriesArtikel(aktListe));
        dbAdapter.close();
        itemAdapter.notifyDataSetChanged();
    }

    public void deleteSelectedItems()
    {
        geloschteArtikel.clear();
        geloschteArtikelPositionen.clear();
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
        int itemCount = listView.getCount();
        for (int i = itemCount - 1; i >= 0; i--)
        {
            if (checkedItemPositions.get(i))
            {
                geloschteArtikel.add(allItems.get(i));
                geloschteArtikelPositionen.add(i);
                itemAdapter.remove(allItems.get(i));
                articleDelete = true;
            }
        }
        checkedItemPositions.clear();
        itemAdapter.notifyDataSetChanged();
        normalMode();
    }
}
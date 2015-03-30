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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import purchase.EinkaufsArtikel;
import purchase.Einkaufsliste;
import swipe.SwipeDismissListViewTouchListener;


public class EinkaufslisteActivity extends ActionBarActivity {
    private String listenName;
    private Einkaufsliste aktListe;
    private final Context context = this;
    private ArrayAdapter<EinkaufsArtikel> itemAdapter;
    private EinkaufsArtikel aktArtikel;
    private ListView listView;
    private ArrayList<EinkaufsArtikel> items;
    private ArrayList<EinkaufsArtikel> allItems;
    private ActionBar einkaufslisteActionBar;
    private boolean longClickEnabled;
    private boolean deleteEnable = false;
    private ArrayList<EinkaufsArtikel> geloschteArtikel;
    private ArrayList<Integer> geloschteArtikelPositionen;
    private boolean articleDelete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.einkaufsliste);
        einkaufslisteActionBar = getSupportActionBar();

        geloschteArtikel = new ArrayList<>();
        geloschteArtikelPositionen = new ArrayList<>();

        aktListe = StartbildschirmActivity.getAktListe();
        listenName = aktListe.getName();
        einkaufslisteActionBar.setTitle(listenName);
        einkaufslisteActionBar.setDisplayShowTitleEnabled(true);

        listView = (ListView) findViewById(R.id.einkaufslisteListView);
        items = aktListe.getItems();
        allItems = aktListe.getAllItems();

        itemAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.listview_design, R.id.listViewDesign, allItems);
        listView.setAdapter(itemAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!longClickEnabled) {
                    aktArtikel = items.get(position);
                    changeArticlelDialog(position);
                    allItems = aktListe.getAllItems();
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickEnabled = true;
                aktArtikel = items.get(position);
                deleteMode();
                return true;
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
                                    geloschteArtikel.clear();
                                    geloschteArtikelPositionen.clear();
                                    geloschteArtikelPositionen.add(position);
                                    geloschteArtikel.add(itemAdapter.getItem(position));
                                    itemAdapter.remove(itemAdapter.getItem(position));
                                }
                                final Dialog loeschen_rueck = new Dialog(context);
                                loeschen_rueck.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                loeschen_rueck.setContentView(R.layout.loeschen_rueck_dialog);
                                loeschen_rueck.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                loeschen_rueck.getWindow().setGravity(Gravity.BOTTOM);
                                loeschen_rueck.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                                Button loeschen_Ruck = (Button) loeschen_rueck.findViewById(R.id.deleteUndoButton);
                                loeschen_Ruck.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        itemAdapter.insert(geloschteArtikel.get(0), geloschteArtikelPositionen.get(0));
                                        itemAdapter.notifyDataSetChanged();
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
        getMenuInflater().inflate(R.menu.menu_einkaufsliste, menu);

        MenuItem delete = menu.findItem(R.id.action_delete_Einkaufliste);
        MenuItem add = menu.findItem(R.id.action_newProduct);
        MenuItem cart = menu.findItem(R.id.action_Einkaufsmodus);


        if (deleteEnable) {
            delete.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            delete.setVisible(true);
            add.setVisible(false);
            cart.setVisible(false);


        } else {
            delete.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            delete.setVisible(false);
            //add.setVisible(true);
            // cart.setVisible(true);
        }

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
        if (id == R.id.action_newProduct) {
            newProductOpen();
            return true;

        }
        if (id == R.id.action_Einkaufsmodus) {
            einkaufsmodusOpen();
            return true;
        }
        if (id == R.id.action_delete_Einkaufliste) {
            deleteSelectedItems();
            return true;
        }
        if (id == android.R.id.home) {
            articleDelete = false;
            normalMode();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    public void newProductOpen() {
        final Dialog newProducts = new Dialog(context);
        newProducts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        newProducts.setContentView(R.layout.neues_produkt_dialog);

        final EditText name = (EditText) newProducts.findViewById(R.id.productName);
        final TextView desc = (TextView) newProducts.findViewById(R.id.descNewProduct);
        final ImageView image = (ImageView) newProducts.findViewById(R.id.newProductLogo);
        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog imageAuswahlDialog = new Dialog(context);
                imageAuswahlDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                imageAuswahlDialog.setContentView(R.layout.eiknkaufsartikel_image_dialog);


                final GridView gridView = (GridView) imageAuswahlDialog.findViewById(R.id.einkaufartikelImagelView);
                final EinkaufsArtikelImageAdapter Iadapter = new EinkaufsArtikelImageAdapter(context, imageAuswahlDialog);

                gridView.setAdapter(Iadapter);


                imageAuswahlDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        image.setImageResource(Iadapter.getImage());
                        name.setText(Iadapter.getImageName());
                    }
                });
                imageAuswahlDialog.show();
            }
        });


        Button dialogButtonSave = (Button) newProducts.findViewById(R.id.newProductSave);
        dialogButtonSave.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (name.getText().toString().equals("")) {
                    name.setHintTextColor(Color.parseColor("#FF0000"));
                    name.setHint("Feld muss ausgefüllt werden!");
                } else {
                    addArticle(name.getText().toString(), desc.getText().toString(), image);
                    allItems = aktListe.getAllItems();
                    itemAdapter.notifyDataSetChanged();
                    newProducts.dismiss();
                }
            }
        });

        Button dialogButtonCancel = (Button) newProducts.findViewById(R.id.newProductCancel);
        dialogButtonCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                newProducts.dismiss();
            }
        });


        newProducts.show();
    }

    public void einkaufsmodusOpen() {
        final Intent einkaufsmodus = new Intent(this, EinkaufmodusActivity.class);
        startActivity(einkaufsmodus);
    }


    public void deleteMode() {
        itemAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_multiple_choice, items);
        listView.setAdapter(itemAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        einkaufslisteActionBar.setDisplayHomeAsUpEnabled(true);
        deleteEnable = true;
        invalidateOptionsMenu();
    }

    public void normalMode() {

        itemAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.listview_design, R.id.listViewDesign, items);
        listView.setAdapter(itemAdapter);

        einkaufslisteActionBar.setDisplayHomeAsUpEnabled(false);
        longClickEnabled = false;

        deleteEnable = false;
        invalidateOptionsMenu();

        if (articleDelete == true) {
            final Dialog loeschen_rueck = new Dialog(context);
            loeschen_rueck.requestWindowFeature(Window.FEATURE_NO_TITLE);
            loeschen_rueck.setContentView(R.layout.loeschen_rueck_dialog);
            loeschen_rueck.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            loeschen_rueck.getWindow().setGravity(Gravity.BOTTOM);
            loeschen_rueck.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            Button loeschen_Ruck = (Button) loeschen_rueck.findViewById(R.id.deleteUndoButton);
            loeschen_Ruck.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = (geloschteArtikel.size() - 1); i >= 0; i--) {
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


    public void changeArticlelDialog(final int pos) {
        final EditText name;
        final EditText desc;
        final ImageView image;

        final Dialog newProducts = new Dialog(context);
        newProducts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        newProducts.setContentView(R.layout.neues_produkt_dialog);

        name = (EditText) newProducts.findViewById(R.id.productName);
        desc = (EditText) newProducts.findViewById(R.id.descNewProduct);
        image = (ImageView) newProducts.findViewById(R.id.newProductLogo);

        name.setText(aktArtikel.getName());
        desc.setText(aktArtikel.getDesc());

        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog imageAuswahlDialog = new Dialog(context);
                imageAuswahlDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                imageAuswahlDialog.setContentView(R.layout.eiknkaufsartikel_image_dialog);


                final GridView gridView = (GridView) imageAuswahlDialog.findViewById(R.id.einkaufartikelImagelView);
                final EinkaufsArtikelImageAdapter Iadapter = new EinkaufsArtikelImageAdapter(context, imageAuswahlDialog);

                gridView.setAdapter(Iadapter);

                imageAuswahlDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        image.setImageResource(Iadapter.getImage());
                    }
                });
                imageAuswahlDialog.show();

            }
        });

        Button dialogButtonSave = (Button) newProducts.findViewById(R.id.newProductSave);
        dialogButtonSave.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (name.getText().toString().equals("")) {
                    name.setHintTextColor(Color.parseColor("#FF0000"));
                    name.setHint("Feld muss ausgefüllt werden!");
                } else {
                    changeArticle(name.getText().toString(), desc.getText().toString(), image, pos);
                    itemAdapter.notifyDataSetChanged();
                    newProducts.dismiss();
                }
            }
        });

        Button dialogButtonCancel = (Button) newProducts.findViewById(R.id.newProductCancel);
        dialogButtonCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                newProducts.dismiss();
            }
        });
        newProducts.show();
    }

    public void changeArticle(String name, String desc, ImageView image, int pos) {
        EinkaufsArtikel newArtikel = new EinkaufsArtikel(name, desc, image);
        aktListe.delItem(pos);
        aktListe.addItemPos(newArtikel, pos);
    }

    public void addArticle(String name, String desc, ImageView image) {
        EinkaufsArtikel newArtikel = new EinkaufsArtikel(name, desc, image);
        aktListe.addItem(newArtikel);
        StartbildschirmActivity.setAktListe(aktListe);
    }

    public void deleteSelectedItems() {
        geloschteArtikel.clear();
        geloschteArtikelPositionen.clear();
        SparseBooleanArray checkedItemPositions = listView.getCheckedItemPositions();
        int itemCount = listView.getCount();
        for (int i = itemCount - 1; i >= 0; i--) {
            if (checkedItemPositions.get(i)) {
                geloschteArtikel.add(items.get(i));
                geloschteArtikelPositionen.add(i);
                itemAdapter.remove(items.get(i));
                articleDelete = true;
            }

        }
        checkedItemPositions.clear();
        itemAdapter.notifyDataSetChanged();
        normalMode();
    }

    public String getListenName() {
        return listenName;
    }

    public void setListenName(String listenName) {
        this.listenName = listenName;
    }

    public Einkaufsliste getAktListe() {
        return aktListe;
    }

    public void setAktListe(Einkaufsliste aktListe) {
        this.aktListe = aktListe;
    }
}

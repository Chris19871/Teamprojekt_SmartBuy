package smartbuy.teamproject.application;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import purchase.EinkaufsArtikel;
import purchase.Einkaufsliste;
import swipe.SwipeDismissListViewTouchListener;


public class EinkaufslisteActivity extends ActionBarActivity
{
    private String listenName;
    private Einkaufsliste aktListe;
    final Context context = this;
    private ArrayAdapter<EinkaufsArtikel> itemAdapter;
    private EinkaufsArtikel aktArtikel;
    private float historicX = Float.NaN, historicY = Float.NaN;
    private static final int DELTA = 50;
    private enum Direction {LEFT, RIGHT;}
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.einkaufsliste);
        ActionBar einkaufslisteActionBar = getSupportActionBar();

        aktListe = MainActivity.getAktListe();
        listenName = aktListe.getName();
        einkaufslisteActionBar.setTitle(listenName);

        listView = (ListView) findViewById(R.id.listView);
        final ArrayList<EinkaufsArtikel> items = aktListe.getItems();

        itemAdapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_expandable_list_item_1, items);
        listView.setAdapter(itemAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                aktArtikel = items.get(position);
                changeArtikelDialog(position);
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
                                    itemAdapter.remove(itemAdapter.getItem(position));
                                }
                                itemAdapter.notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());
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

    public void newProduct()
    {

        final Dialog newProducts = new Dialog(context);
        newProducts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        newProducts.setContentView(R.layout.new_product_dialog);

        final EditText name = (EditText) newProducts.findViewById(R.id.productName);
        final TextView desc = (TextView) newProducts.findViewById(R.id.descnewProduct);
        final ImageView image = (ImageView) newProducts.findViewById(R.id.newProductLogo);

        Button dialogButtonSave = (Button) newProducts.findViewById(R.id.newProductSave);
        dialogButtonSave.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                if(name.getText().toString().equals(""))
                {
                    name.setHintTextColor(Color.parseColor("#FF0000"));
                    name.setHint("Feld muss ausgefüllt werden!");
                }
                else
                {
                    addArtikel(name.getText().toString(), desc.getText().toString(), image);
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

    public void openEinkaufsmodus()
    {
        final Intent einkaufsmodus = new Intent(this, EinkaufmodusActivity.class);
        startActivity(einkaufsmodus);
    }

    public void changeArtikelDialog(final int pos)
    {
        final EditText name;
        final EditText desc;
        final ImageView image;

        final Dialog newProducts = new Dialog(context);
        newProducts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        newProducts.setContentView(R.layout.new_product_dialog);

        name = (EditText) newProducts.findViewById(R.id.productName);
        desc = (EditText) newProducts.findViewById(R.id.descnewProduct);
        image = aktArtikel.getImage();

        name.setText(aktArtikel.getName());
        desc.setText(aktArtikel.getDesc());

        Button dialogButtonSave = (Button) newProducts.findViewById(R.id.newProductSave);
        dialogButtonSave.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {
                if(name.getText().toString().equals(""))
                {
                    name.setHintTextColor(Color.parseColor("#FF0000"));
                    name.setHint("Feld muss ausgefüllt werden!");
                }
                else
                {
                    changeArtikel(name.getText().toString(), desc.getText().toString(), image, pos);
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

    public void addArtikel(String name, String desc, ImageView image)
    {
        EinkaufsArtikel newArtikel = new EinkaufsArtikel(name,desc,image);
        aktListe.addItem(newArtikel);
        MainActivity.setAktListe(aktListe);
    }

    public void changeArtikel(String name, String desc, ImageView image, int pos)
    {
        EinkaufsArtikel newArtikel = new EinkaufsArtikel(name,desc,image);
        aktListe.delItem(pos);
        aktListe.addItemPos(newArtikel, pos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_einkaufsliste, menu);
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
        if (id == R.id.action_newProduct)
        {
            newProduct();
            return true;

        }
        if (id == R.id.action_Einkaufsmodus)
        {
            openEinkaufsmodus();
            return true;
        }
        if (id == R.id.action_delete_Einkaufliste)
        {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String getListenName()
    {
        return listenName;
    }

    public void setListenName(String listenName)
    {
        this.listenName = listenName;
    }

    public Einkaufsliste getAktListe()
    {
        return aktListe;
    }

    public void setAktListe(Einkaufsliste aktListe)
    {
        this.aktListe = aktListe;
    }
}

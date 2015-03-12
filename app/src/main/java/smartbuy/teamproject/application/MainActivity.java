package smartbuy.teamproject.application;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import purchase.ItemList;
import purchase.PreselectionItem;
import purchase.PurchaseItems;

public class MainActivity extends ActionBarActivity
{

    final Context context = this;
    private Button button;
    private GridLayout grid;
    private ActionBar smartBuyActionBar;
    private ArrayAdapter<PreselectionItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Typeface font = Typeface.createFromAsset(getAssets(),"fonts/BAUHS93.TTF");
        TextView startText = (TextView) findViewById(R.id.startScreenText);
        startText.setTypeface(font);

        smartBuyActionBar = getSupportActionBar();
        smartBuyActionBar.setDisplayShowTitleEnabled(false);
    }

    public void newEinkaufsliste()
    {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_new_einkaufsmodus);

        grid = (GridLayout) dialog.findViewById(R.id.gridLayout);


        PurchaseItems[] gebItems = {new PurchaseItems("Partyhüte",null,null), new PurchaseItems("Besteck",null,null)};
        PreselectionItem geburtstag = new PreselectionItem("Geburtstag", gebItems);
        PurchaseItems[] partyItems = {new PurchaseItems("Fleisch",null,null), new PurchaseItems("Bier",null,null)};
        PreselectionItem party = new PreselectionItem("Party", partyItems);

        ArrayList<PreselectionItem> list = new ArrayList<>();
        list.add(geburtstag);
        list.add(party);

        items = new ArrayAdapter<PreselectionItem>(getApplicationContext(),
                android.R.layout.simple_dropdown_item_1line, list);


        // custom add_new_einkaufsmodus


        // set the custom add_new_einkaufsmodus components - text, spinner, layout and button
        TextView text = (TextView) dialog.findViewById(R.id.dialogName);
        Spinner spinner = (Spinner) dialog.findViewById(R.id.dialogSpinner);
        spinner.setAdapter(items);



        Button dialogButtonOk = (Button) dialog.findViewById(R.id.dialogButtonOK);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                addBox(arg2);

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

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
    public void addBox(int index)
    {

        PreselectionItem item = items.getItem(index);
        PurchaseItems[] pItems = item.getItems();
        grid.removeAllViews();
        grid.setColumnCount(2);

        int columnIndex=0; //cols index to which i add the button

        int rowIndex=0; //row index to which i add the button

        CheckBox[] b = new CheckBox[pItems.length];
        for(int i = 0; i < pItems.length; i++)
        {

            b[i] = new CheckBox(this);
            b[i].setText(pItems[i].getName());
            b[i].setWidth(250);
        }

        for (int i = 0; i < b.length; i++)
        {

            if (i > 1 && i % 2 == 0)
            {
                rowIndex++;
            }

            GridLayout.Spec row = GridLayout.spec(rowIndex, 1);
            GridLayout.Spec colspan = GridLayout.spec(columnIndex, 1);
            GridLayout.LayoutParams gridLayoutParam = new GridLayout.LayoutParams(row, colspan);
            //grid.add(b[i], i % 2, k);
            grid.addView(b[i], gridLayoutParam);
            columnIndex++;
        }


    }
    public void addList(ItemList list)
    {

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
    public void wechsel()
    {
        final Intent einkaufsliste = new Intent(this,Einkaufsliste.class);
        startActivity(einkaufsliste);
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
                newEinkaufsliste();
                return true;
         }
        if (id == R.id.action_Einkauf) {
            wechsel();
            return true;
        }

            return super.onOptionsItemSelected(item);
    }

}

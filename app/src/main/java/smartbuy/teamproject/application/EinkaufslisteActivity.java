package smartbuy.teamproject.application;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;


public class EinkaufslisteActivity extends ActionBarActivity
{
    private String listenName;
    private EinkaufslisteActivity aktListe;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.einkaufsliste);
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

    public void newProduct()
    {

        final Dialog newProducts = new Dialog(context);
        newProducts.requestWindowFeature(Window.FEATURE_NO_TITLE);
        newProducts.setContentView(R.layout.new_product_dialog);

        Button dialogButtonSave = (Button) newProducts.findViewById(R.id.newProductSave);
        dialogButtonSave.setOnClickListener(new OnClickListener()
        {
            public void onClick(View v)
            {

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

    public void switchHome()
    {
        final Intent home = new Intent(this, MainActivity.class);
        startActivity(home);
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
        if (id == R.id.action_Home)
        {
            switchHome();
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

    public EinkaufslisteActivity getAktListe()
    {
        return aktListe;
    }

    public void setAktListe(EinkaufslisteActivity aktListe)
    {
        this.aktListe = aktListe;
    }
}

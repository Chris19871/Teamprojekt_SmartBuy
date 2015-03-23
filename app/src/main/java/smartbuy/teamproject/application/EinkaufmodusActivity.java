package smartbuy.teamproject.application;



import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import badge.BadgeView;
import purchase.Einkaufsliste;


public class EinkaufmodusActivity extends ActionBarActivity
{

    final Context context = this;
    private FragmentTabHost einkaufmodusTabHost;
    private ActionBar einkaufsmodusActionBar;
    Einkaufsliste liste;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.einkaufmodus);



        einkaufsmodusActionBar = getSupportActionBar();

        einkaufsmodusActionBar.setTitle(MainActivity.getAktListe().getName());
        einkaufsmodusActionBar.setDisplayShowTitleEnabled(true);


        einkaufmodusTabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        einkaufmodusTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        einkaufmodusTabHost.addTab(einkaufmodusTabHost.newTabSpec("regal").setIndicator("", getResources().getDrawable(R.mipmap.ic_launcher_regal_black)), EinkaufmodusFragment.class, null);
        einkaufmodusTabHost.addTab(einkaufmodusTabHost.newTabSpec("einkauf").setIndicator("", getResources().getDrawable(R.mipmap.ic_launcher_shoppingcar_black)), EinkaufswagenFragment.class, null);

        TabWidget tabs = einkaufmodusTabHost.getTabWidget();

        BadgeView badge7 = new BadgeView(this, tabs, 1);
        badge7.setText("4");
        badge7.setBadgePosition(BadgeView.POSITION_CENTER);
        badge7.show();


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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_einkaufmodus, menu);
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
        return super.onOptionsItemSelected(item);
    }
}

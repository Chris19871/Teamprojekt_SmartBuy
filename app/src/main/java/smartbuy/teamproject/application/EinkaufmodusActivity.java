package smartbuy.teamproject.application;



import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
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

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import badge.BadgeView;
import purchase.EinkaufsArtikel;
import purchase.Einkaufsliste;


public class EinkaufmodusActivity extends ActionBarActivity
{

    final Context context = this;
    private FragmentTabHost einkaufmodusTabHost;
    private ActionBar einkaufsmodusActionBar;
    ArrayList<EinkaufsArtikel> liste;

    public static final long SLEEPTIME = 10;
    boolean running;
    Thread refreshThread;
    double time;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.einkaufmodus);

        liste = MainActivity.getAktListe().getItemsBought();

        einkaufsmodusActionBar = getSupportActionBar();

        einkaufsmodusActionBar.setTitle(MainActivity.getAktListe().getName());
        einkaufsmodusActionBar.setDisplayShowTitleEnabled(true);

        einkaufmodusTabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        einkaufmodusTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);

        einkaufmodusTabHost.addTab(einkaufmodusTabHost.newTabSpec("regal").setIndicator("", getResources().getDrawable(R.mipmap.ic_launcher_regal_black)), EinkaufmodusFragment.class, null);
        einkaufmodusTabHost.addTab(einkaufmodusTabHost.newTabSpec("einkauf").setIndicator("", getResources().getDrawable(R.mipmap.ic_launcher_shoppingcar_black)), EinkaufswagenFragment.class, null);

        SharedPreferences einstellungen = PreferenceManager.getDefaultSharedPreferences(context);
        running = einstellungen.getBoolean("example_checkbox",false);
        if (running)
        {
            initThread();
        }







        TabWidget tabs = einkaufmodusTabHost.getTabWidget();

        BadgeView badge7 = new BadgeView(this, tabs, 1);
        String anzahlGekauft = Integer.toString(liste.size());
        badge7.setText(anzahlGekauft);
        badge7.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badge7.toggle();

    }
    public void initThread() {
        TabWidget tabs = einkaufmodusTabHost.getTabWidget();
        final BadgeView badgetime = new BadgeView(this, tabs, 0);
        badgetime.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgetime.toggle();

        refreshThread = new Thread(new Runnable() {
            public void run() {
                while (running) {
                    time = time + 0.01;
                    try {
                        Thread.sleep(SLEEPTIME);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(MainActivity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            badgetime.setText(getString(R.string.time_string, String.format("%.2f", time)));
                        }
                    });

                }
            }
        });
        refreshThread.start();
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

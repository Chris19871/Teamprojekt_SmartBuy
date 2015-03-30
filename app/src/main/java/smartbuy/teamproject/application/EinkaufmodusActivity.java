package smartbuy.teamproject.application;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TabWidget;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import badge.BadgeView;
import purchase.EinkaufsArtikel;



public class EinkaufmodusActivity extends ActionBarActivity
{

    final Context context = this;
    private FragmentTabHost einkaufmodusTabHost;
    private ActionBar einkaufsmodusActionBar;
    ArrayList<EinkaufsArtikel> liste;

    public static final long SLEEPTIME = 1000;
    boolean running;
    Thread refreshThread;
    int time;
    private String sekunden = "";
    private int sekundenZähler = 0;
    private String minuten = "";
    private int minutenZähler = 0;
    private String stunden = "";
    private int stundenZähler = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.einkaufmodus);

        liste = StartbildschirmActivity.getAktListe().getItemsBought();

        einkaufsmodusActionBar = getSupportActionBar();

        einkaufsmodusActionBar.setTitle(StartbildschirmActivity.getAktListe().getName());
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
        final BadgeView badgetime = new BadgeView(this, tabs, 0);
        badgetime.setBadgePosition(BadgeView.POSITION_CENTER);
        badgetime.setBadgeBackgroundColor(Color.DKGRAY);
        badgetime.setTextColor(Color.GRAY);
        badgetime.setText("00:00:00");
        badgetime.toggle();


        BadgeView badge7 = new BadgeView(this, tabs, 1);
        String anzahlGekauft = Integer.toString(liste.size());
        badge7.setText(anzahlGekauft);
        badge7.setBadgePosition(BadgeView.POSITION_CENTER);
        badgetime.setBadgeBackgroundColor(Color.RED);
        badge7.toggle();

    }
    public void initThread() {
        TabWidget tabs = einkaufmodusTabHost.getTabWidget();
        final BadgeView badgetime = new BadgeView(this, tabs, 0);
        badgetime.setBadgePosition(BadgeView.POSITION_CENTER);
        badgetime.setBadgeBackgroundColor(Color.RED);
        badgetime.setText("00:00:00");
        badgetime.toggle();

        refreshThread = new Thread(new Runnable() {
            public void run() {
                while (running) {
                    time++;
                    try {
                        Thread.sleep(SLEEPTIME);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(StartbildschirmActivity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            sekundenZähler++;
                            if(sekundenZähler<=59) {
                                DecimalFormat df = new DecimalFormat("00");
                                sekunden = df.format(sekundenZähler);
                            } else {
                                sekundenZähler = 0;
                                minutenZähler++;
                            }

                            if(minutenZähler<=59) {
                                DecimalFormat df = new DecimalFormat("00");
                                minuten = df.format(minutenZähler);
                            } else {
                                minutenZähler = 0;
                                stundenZähler++;
                            }

                            DecimalFormat df = new DecimalFormat("00");
                            stunden = df.format(stundenZähler);

                            badgetime.setText(stunden + ":" + minuten + ":" + sekunden);
                        }

                    });
                }
            }
        });
        refreshThread.start();
    }

    public void settingsOpen()
    {
        final Intent settings = new Intent(this, EinstellungenActivity.class);
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
        final Intent auswahl = new Intent(this, VorauswahllistenActivity.class);
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

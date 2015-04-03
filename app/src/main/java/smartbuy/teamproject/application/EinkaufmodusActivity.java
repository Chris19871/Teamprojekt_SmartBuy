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
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import badge.BadgeView;
import database.DbAdapter;


public class EinkaufmodusActivity extends ActionBarActivity
{
    private final Context context = this;
    private FragmentTabHost einkaufmodusTabHost;
    private static final long SLEEPTIME = 1000;
    private boolean running;
    static boolean stopWatch = false;
    private String seconds = "";
    private int secondsCount = 0;
    private String minutes = "";
    private int minutesCount = 0;
    private String hours = "";
    private int hoursCount = 0;
    private static BadgeView badgeCount;
    private DbAdapter dbAdapter;
    private String aktListe;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.einkaufmodus);
        dbAdapter = StartbildschirmActivity.getDbAdapter();

        aktListe = StartbildschirmActivity.getAktListe();

        ActionBar einkaufsmodusActionBar;
        einkaufsmodusActionBar = getSupportActionBar();
        einkaufsmodusActionBar.setTitle(aktListe);
        einkaufsmodusActionBar.setDisplayShowTitleEnabled(true);

        einkaufmodusTabHost = (FragmentTabHost) findViewById(R.id.tabHost);
        einkaufmodusTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        einkaufmodusTabHost.addTab(einkaufmodusTabHost.newTabSpec("regal").setIndicator("", getResources().getDrawable(R.mipmap.ic_launcher_regal_black)), EinkaufmodusFragment.class, null);
        einkaufmodusTabHost.addTab(einkaufmodusTabHost.newTabSpec("einkaufwagen").setIndicator("", getResources().getDrawable(R.mipmap.ic_launcher_shoppingcar_black)), EinkaufswagenFragment.class, null);


        SharedPreferences einstellungen = PreferenceManager.getDefaultSharedPreferences(context);
        boolean stopWatchState = einstellungen.getBoolean("example_checkbox", false);

        dbAdapter.openRead();
        if ((stopWatchState) && (dbAdapter.getAllItemsNotBought(aktListe).size() > 0))
        {
            running = true;
            dbAdapter.close();
            initThread();
        }
        TabWidget tabs = einkaufmodusTabHost.getTabWidget();
        final BadgeView badgeTime = new BadgeView(this, tabs, 0);
        badgeTime.setBadgePosition(BadgeView.POSITION_CENTER);
        badgeTime.setBadgeBackgroundColor(Color.DKGRAY);
        badgeTime.setTextColor(Color.GRAY);
        badgeTime.setText("00:00:00");
        badgeTime.toggle();


        badgeCount = new BadgeView(this, tabs, 1);
        dbAdapter.openRead();
        String anzahlGekauft = Integer.toString(dbAdapter.getAllItemsBought(aktListe).size());
        dbAdapter.close();
        badgeCount.setText(anzahlGekauft);
        badgeCount.setBadgePosition(BadgeView.POSITION_CENTER);
        badgeTime.setBadgeBackgroundColor(Color.RED);
        badgeCount.toggle();

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

    public void initThread()
    {
        TabWidget tabs = einkaufmodusTabHost.getTabWidget();
        final BadgeView badgetime = new BadgeView(this, tabs, 0);
        badgetime.setBadgePosition(BadgeView.POSITION_CENTER);
        badgetime.setBadgeBackgroundColor(Color.RED);

        long aktTime = System.currentTimeMillis();
        long startzeit;
        long stopWatchTime;
        DecimalFormat df = new DecimalFormat("00");

        dbAdapter.openRead();
        startzeit = Long.parseLong(dbAdapter.getStartzeit(aktListe));
        dbAdapter.close();
        if (startzeit == 0)
        {
            startzeit = aktTime;
            dbAdapter.openWrite();
            dbAdapter.setStartzeit(aktListe, Long.toString(startzeit));
            dbAdapter.close();

            hoursCount = 0;
            minutesCount = 0;
            secondsCount = 0;
            hours = df.format(hoursCount);
            minutes = df.format(minutesCount);
            seconds = df.format(secondsCount);

            badgetime.setText(hours + ":" + minutes + ":" + seconds);

        } else
        {

            stopWatchTime = aktTime - startzeit;

            int h = (int) (TimeUnit.MILLISECONDS.toHours(stopWatchTime) % 24);
            hoursCount = h;
            int m = (int) (TimeUnit.MILLISECONDS.toMinutes(stopWatchTime) % 60);
            minutesCount = m;
            int s = (int) (TimeUnit.MILLISECONDS.toSeconds(stopWatchTime) % 60);
            secondsCount = s;
            String hms = String.format("%02d:%02d:%02d", h, m, s);


            badgetime.setText(hms);

        }

        badgetime.toggle();

        Thread refreshThread = new Thread(new Runnable()
        {
            public void run()
            {
                while (running)
                {
                    try
                    {
                        Thread.sleep(SLEEPTIME);
                    } catch (InterruptedException ex)
                    {
                        Logger.getLogger(StartbildschirmActivity.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            secondsCount++;
                            if (secondsCount <= 59)
                            {
                                DecimalFormat df = new DecimalFormat("00");
                                seconds = df.format(secondsCount);
                            } else
                            {
                                secondsCount = 0;
                                minutesCount++;
                            }

                            if (minutesCount <= 59)
                            {
                                DecimalFormat df = new DecimalFormat("00");
                                minutes = df.format(minutesCount);
                            } else
                            {
                                minutesCount = 0;
                                hoursCount++;
                            }

                            DecimalFormat df = new DecimalFormat("00");
                            hours = df.format(hoursCount);

                            badgetime.setText(hours + ":" + minutes + ":" + seconds);

                            if (stopWatch)
                            {
                                running = false;
                            }

                        }

                    });
                }
                dbAdapter.openWrite();
                dbAdapter.setBestzeit(aktListe, badgetime.getText().toString());
                dbAdapter.resetStartTime(aktListe);
                dbAdapter.close();
            }
        });
        refreshThread.start();
    }

    public static void increment()
    {
        badgeCount.increment(1);

    }

    public static void decrement()
    {
        badgeCount.decrement(1);

    }

    public static void setStopWatch(boolean stopWatch)
    {
        EinkaufmodusActivity.stopWatch = stopWatch;

    }

}

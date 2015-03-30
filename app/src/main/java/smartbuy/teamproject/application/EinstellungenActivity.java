package smartbuy.teamproject.application;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class EinstellungenActivity extends PreferenceActivity {
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.smartbuy_einstellungen);
    }

}


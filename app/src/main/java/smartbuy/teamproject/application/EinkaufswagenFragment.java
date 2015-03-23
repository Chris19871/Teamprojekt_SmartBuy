package smartbuy.teamproject.application;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import purchase.Einkaufsliste;

public class EinkaufswagenFragment extends Fragment {

    private GridLayout grid;
    private EinkaufslisteActivity einkaufslisteActivity = new EinkaufslisteActivity();
    private Einkaufsliste liste;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_einkaufswagen, container, false);
    }
}

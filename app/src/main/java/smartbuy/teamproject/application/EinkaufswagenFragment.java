package smartbuy.teamproject.application;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ListView;

import java.util.ArrayList;

import purchase.EinkaufsArtikel;
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
        View view = inflater.inflate(R.layout.fragment_einkaufswagen, container, false);

        ListView list = (ListView) view.findViewById(R.id.fragment_einkaufswagen_listView);
        ArrayList<EinkaufsArtikel> itemsBought = StartbildschirmActivity.getAktListe().getItemsBought();

        ArrayAdapter<EinkaufsArtikel> itemAdapter = new ArrayAdapter<>(view.getContext(),
                R.layout.listview_schema,R.id.listViewdesign, itemsBought);
        list.setAdapter(itemAdapter);

        return view;
    }
}

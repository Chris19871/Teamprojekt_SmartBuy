package smartbuy.teamproject.application;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import database.DbAdapter;

public class EinkaufswagenFragment extends Fragment
{

    private DbAdapter dbAdapter = StartbildschirmActivity.getDbAdapter();
    private ArrayList<database.EinkaufsArtikel> itemsBought = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    /**
     * Show bought items in a ListView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_einkaufswagen, container, false);

        ListView list = (ListView) view.findViewById(R.id.einkaufswagenView);

        String aktListe = StartbildschirmActivity.getAktListe();
        dbAdapter.openRead();
        itemsBought.clear();
        itemsBought.addAll(dbAdapter.getAllItemsBought(aktListe));
        dbAdapter.close();

        ArrayAdapter<database.EinkaufsArtikel> itemAdapter = new ArrayAdapter<>(view.getContext(),
                R.layout.listview_design, R.id.listViewDesign, itemsBought);
        list.setAdapter(itemAdapter);

        itemAdapter.notifyDataSetChanged();

        return view;
    }
}
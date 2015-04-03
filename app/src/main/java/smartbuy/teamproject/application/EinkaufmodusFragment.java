package smartbuy.teamproject.application;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class EinkaufmodusFragment extends Fragment
{

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_einkaufmodus, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.einkaufmodusView);
        EinkaufmodusAdapter adapter = new EinkaufmodusAdapter(view.getContext());

        gridView.setAdapter(adapter);
        return view;
    }
}
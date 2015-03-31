package smartbuy.teamproject.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import java.util.ArrayList;
import database.DbAdapter;

public class NeueEinkaufslisteAdapter extends BaseAdapter {

    private Context context;
    private DbAdapter dbAdapter;
    private final ArrayList<database.EinkaufsArtikel> pItems;
    private ArrayList<CheckBox> checkBoxes;

    public NeueEinkaufslisteAdapter(Context context, DbAdapter dbAdapter, String item)
    {
        this.context = context;
        this.dbAdapter = dbAdapter;
        dbAdapter.openRead();
        this.pItems = dbAdapter.getAllEntriesArtikel(item);
        dbAdapter.close();
        checkBoxes = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return pItems.size();
    }

    @Override
    public Object getItem(int position) {
        return pItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public ArrayList<database.EinkaufsArtikel> getCheckedItems()
    {
        ArrayList<database.EinkaufsArtikel> checkItems = new ArrayList<>();
        for (int i = 0; i < pItems.size(); i++)
        {
            if (checkBoxes.get(i).isChecked() == true)
            {
                checkItems.add(pItems.get(i));
            }
        }

        return checkItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;
        if (convertView == null) {
            gridView = new View(context);
            gridView = inflater.inflate( R.layout.checkbox , null);

            CheckBox box = (CheckBox) gridView.findViewById(R.id.newProductcheckBox);
            box.setText(pItems.get(position).getName());
            box.setTextSize(10);
            checkBoxes.add(box);

        } else {

            gridView = (View) convertView;
        }

        return gridView;
    }
}

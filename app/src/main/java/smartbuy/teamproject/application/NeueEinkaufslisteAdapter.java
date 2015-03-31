package smartbuy.teamproject.application;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import java.util.ArrayList;
import database.DbAdapter;

public class NeueEinkaufslisteAdapter extends BaseAdapter {

    private Context context;
    private DbAdapter dbAdapter;
    private ArrayList<database.EinkaufsArtikel> pItems;
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
        for (int i = 0; i < checkBoxes.size(); i++)
        {
            if (checkBoxes.get(i).isChecked() == true)
            {
                checkItems.add(pItems.get(i));
            }
        }

        return checkItems;
    }

    @Override
    public CheckBox getView(int position, View convertView, ViewGroup parent) {
        CheckBox box = new CheckBox(context);
        box.setText(pItems.get(position).getName());
        box.setTextSize(10);
        checkBoxes.add(box);

        return box;
    }
}

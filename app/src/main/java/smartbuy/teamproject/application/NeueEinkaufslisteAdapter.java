package smartbuy.teamproject.application;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;

import database.EinkaufsArtikel;

public class NeueEinkaufslisteAdapter extends BaseAdapter
{

    private Context context;
    private EinkaufsArtikel artikel[];
    private ArrayList<database.EinkaufsArtikel> checkItems;

    public NeueEinkaufslisteAdapter(Context context, EinkaufsArtikel artikel[])
    {
        this.context = context;
        this.artikel = artikel;
        this.checkItems = new ArrayList<>();
    }

    @Override
    public int getCount()
    {
        return artikel.length;
    }

    @Override
    public Object getItem(int position)
    {
        return artikel[position];
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public ArrayList<database.EinkaufsArtikel> getCheckedItems()
    {
        return checkItems;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {

        Holder holder;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
        {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.checkbox, null);

            holder.checkBox = (CheckBox) convertView.findViewById(R.id.newProductcheckBox);

            convertView.setTag(holder);
        } else
        {
            holder = (Holder) convertView.getTag();
        }
        holder.checkBox.setText(artikel[position].getName());
        holder.checkBox.setTextSize(10);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if (isChecked)
                {
                    checkItems.add(artikel[position]);
                } else
                {
                    database.EinkaufsArtikel temArtikel = artikel[position];
                    checkItems.remove(temArtikel);
                }

            }
        });


        return convertView;
    }
}

class Holder
{
    CheckBox checkBox;

}

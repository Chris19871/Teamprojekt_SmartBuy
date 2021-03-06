package smartbuy.teamproject.application;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import database.DbAdapter;

/**
 * Adapter to show ImageView and TextView in the GridLayout from EinkaufsmodusFragment.
 */
public class EinkaufmodusAdapter extends BaseAdapter
{
    private Context mContext;
    private String aktListe;
    private DbAdapter dbAdapter;
    private ArrayList<database.EinkaufsArtikel> listeArtikel;
    private ArrayList<database.EinkaufsArtikel> listeArtikelGekauft;
    private database.EinkaufsArtikel zuletztGekauft;
    private boolean delete = false;

    public EinkaufmodusAdapter(Context c)
    {
        mContext = c;
        aktListe = StartbildschirmActivity.getAktListe();
        dbAdapter = StartbildschirmActivity.getDbAdapter();
        dbAdapter.openRead();
        listeArtikel = dbAdapter.getAllItemsNotBought(aktListe);
        listeArtikelGekauft = dbAdapter.getAllItemsBought(aktListe);
        dbAdapter.close();
    }

    @Override
    public int getCount()
    {
        return listeArtikel.size();
    }

    @Override
    public Object getItem(int position)
    {
        return listeArtikel.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final LinearLayout layout = new LinearLayout(mContext);
        EinkaufmodusActivity.setStopWatch(false);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setMinimumWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setMinimumHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setBackgroundResource(R.drawable.background_border);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        ImageView image = new ImageView(layout.getContext());
        image.setBackgroundColor(Color.parseColor("#FF5CC1DE"));

        image.setImageResource(listeArtikel.get(position).getPic());
        image.setClickable(true);
        image.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        image.setMinimumHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        //On click at the image this item will be bought.
        //The bought item will be moved from the "EinkaufsmodusFragment" to the
        //"EinkaufswagenFragment".
        image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dbAdapter.openWrite();
                dbAdapter.buyArtikel(aktListe, listeArtikel.get(position).getId());
                dbAdapter.close();
                zuletztGekauft = listeArtikel.get(position);

                dbAdapter.openRead();
                listeArtikel.clear();
                listeArtikel.addAll(dbAdapter.getAllItemsNotBought(aktListe));
                listeArtikelGekauft.clear();
                listeArtikelGekauft.addAll(dbAdapter.getAllItemsBought(aktListe));
                dbAdapter.close();

                notifyDataSetChanged();
                EinkaufmodusActivity.increment();

                //Show a dialog to undo the last action
                final Dialog loeschen_rueck = new Dialog(mContext);

                loeschen_rueck.setOnDismissListener(new DialogInterface.OnDismissListener()
                {
                    @Override
                    public void onDismiss(DialogInterface dialog)
                    {
                        if (!delete)
                        {
                            dbAdapter.openRead();
                            if (dbAdapter.getAllItemsNotBought(aktListe).size() == 0)
                            {
                                EinkaufmodusActivity.setStopWatch(true);

                            } else
                            {
                                EinkaufmodusActivity.setStopWatch(false);
                            }
                            dbAdapter.close();
                        }
                        delete = false;
                    }
                });
                loeschen_rueck.requestWindowFeature(Window.FEATURE_NO_TITLE);
                loeschen_rueck.setContentView(R.layout.loeschen_rueck_dialog);
                loeschen_rueck.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                loeschen_rueck.getWindow().setGravity(Gravity.BOTTOM);
                loeschen_rueck.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                Button loeschen_Ruck = (Button) loeschen_rueck.findViewById(R.id.deleteUndoButton);
                loeschen_Ruck.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        delete = true;
                        dbAdapter.openWrite();
                        dbAdapter.undoBuyArtikel(aktListe, zuletztGekauft.getId());
                        listeArtikel.add(position, zuletztGekauft);
                        listeArtikel.clear();
                        listeArtikel.addAll(dbAdapter.getAllItemsNotBought(aktListe));
                        listeArtikelGekauft.clear();
                        listeArtikelGekauft.addAll(dbAdapter.getAllItemsBought(aktListe));
                        dbAdapter.close();

                        notifyDataSetChanged();
                        EinkaufmodusActivity.decrement();
                        loeschen_rueck.dismiss();
                    }
                });
                loeschen_rueck.show();
            }

        });
        layout.addView(image);

        TextView textView = new TextView(layout.getContext());
        textView.setText(listeArtikel.get(position).getName());
        textView.setClickable(true);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        textView.setMinimumWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setMinimumHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //Show a dialog with product information
        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                final EditText name;
                final EditText desc;
                final ImageView image;

                final Dialog products = new Dialog(layout.getContext());
                products.requestWindowFeature(Window.FEATURE_NO_TITLE);
                products.setContentView(R.layout.produkt_dialog);

                name = (EditText) products.findViewById(R.id.productName);
                desc = (EditText) products.findViewById(R.id.descNewProduct);
                image = (ImageView) products.findViewById(R.id.newProductLogo);

                name.setText(listeArtikel.get(position).getName());
                desc.setText(listeArtikel.get(position).getDesc());
                image.setImageResource(listeArtikel.get(position).getPic());

                products.show();
            }
        });
        layout.addView(textView);

        return layout;
    }
}
package smartbuy.teamproject.application;

import android.app.Dialog;
import android.content.Context;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.TextView;

import java.util.ArrayList;

import purchase.EinkaufsArtikel;
import purchase.Einkaufsliste;


public class MyAdapter extends BaseAdapter {
    private Context mContext;

    Einkaufsliste liste = MainActivity.getAktListe();
    private ArrayList<EinkaufsArtikel> listeArtikel;
    private ArrayList<EinkaufsArtikel> listeArtikelGekauft;
    private EinkaufsArtikel zuletztGekauft;

    public MyAdapter(Context c)
    {
        mContext = c;
        liste = MainActivity.getAktListe();
        listeArtikel = liste.getItems();
        listeArtikelGekauft = liste.getItemsBought();
    }

    @Override
    public int getCount() {
        return listeArtikel.size();
    }

    @Override
    public Object getItem(int position) {
        return listeArtikel.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        final LinearLayout layout = new LinearLayout(mContext);
        layout.setOrientation(LinearLayout.VERTICAL);

        layout.setMinimumWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setMinimumHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        layout.setBackgroundResource(R.drawable.background_border);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);




        ImageView image = new ImageView(layout.getContext());
        image.setBackgroundColor(Color.parseColor("#FF5CC1DE"));


        //Standard Bild
        image.setImageResource(R.mipmap.smartbuy_logo);


        image.setClickable(true);

        image.setMinimumWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        image.setMinimumHeight(ViewGroup.LayoutParams.MATCH_PARENT);



        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listeArtikelGekauft.add(listeArtikel.get(position));
                zuletztGekauft = listeArtikel.get(position);
                listeArtikel.remove(position);
                notifyDataSetChanged();
                liste.setItems(listeArtikel);
                liste.setItemsBought(listeArtikelGekauft);
                MainActivity.setAktListe(liste);

                final Dialog loeschen_rueck = new Dialog(mContext);
                loeschen_rueck.requestWindowFeature(Window.FEATURE_NO_TITLE);
                loeschen_rueck.setContentView(R.layout.loeschen_rueck_dialog);
                loeschen_rueck.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                loeschen_rueck.getWindow().setGravity(Gravity.BOTTOM);
                loeschen_rueck.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

                Button loeschen_Ruck = (Button) loeschen_rueck.findViewById(R.id.loeschen_RuckButton);
                loeschen_Ruck.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        listeArtikel.add(position,zuletztGekauft);
                        listeArtikelGekauft.remove(zuletztGekauft);

                        notifyDataSetChanged();

                        liste.setItems(listeArtikel);
                        liste.setItemsBought(listeArtikelGekauft);
                        MainActivity.setAktListe(liste);

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

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText name;
                final EditText desc;
                final ImageView image;

                final Dialog products = new Dialog(layout.getContext());
                products.requestWindowFeature(Window.FEATURE_NO_TITLE);
                products.setContentView(R.layout.product_dialog);

                name = (EditText) products.findViewById(R.id.productName);
                desc = (EditText) products.findViewById(R.id.descnewProduct);
               // image = aktArtikel.getImage();

                name.setText(listeArtikel.get(position).getName());
                desc.setText(listeArtikel.get(position).getDesc());

                products.show();
            }
        });

        layout.addView(textView);

        return layout;
    }


}




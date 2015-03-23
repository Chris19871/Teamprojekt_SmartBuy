package smartbuy.teamproject.application;

import android.app.Dialog;
import android.content.Context;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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

        LinearLayout.LayoutParams lpl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);

        layout.setLayoutParams(lpl);
        layout.setBackgroundResource(R.drawable.background_border);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(0,10,0,0);

        ImageView image = new ImageView(layout.getContext());
        image.setBackgroundColor(Color.parseColor("#FF5CC1DE"));
        image.setImageResource(R.drawable.smartbuy_logo);
        image.setLayoutParams(lp);


        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listeArtikelGekauft.add(listeArtikel.get(position));
                listeArtikel.remove(position);
                notifyDataSetChanged();
            }
        });

        layout.addView(image);

        TextView textView = new TextView(layout.getContext());
        textView.setText(listeArtikel.get(position).getName());
        textView.setClickable(true);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);

        LinearLayout.LayoutParams lptv = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        lptv.setMargins(20,0,20,20);

        textView.setLayoutParams(lptv);

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




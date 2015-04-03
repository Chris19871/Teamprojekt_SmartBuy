package smartbuy.teamproject.application;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

/**
 * Adapter to show all product images in a GridView
 */
public class EinkaufsArtikelImageAdapter extends BaseAdapter
{
    private Context mContext;
    private Dialog dialog;
    private int click = 0;
    private int name;
    private int[] imageId = {
            R.mipmap.image_battery,
            R.mipmap.image_butter,
            R.mipmap.image_fisch,
            R.mipmap.image_fleisch,
            R.mipmap.image_gemuse,
            R.mipmap.image_kase,
            R.mipmap.image_limonade,
            R.mipmap.image_milch,
            R.mipmap.image_obst,
            R.mipmap.image_yogurt

    };
    private int[] imageString = {
            R.string.battery,
            R.string.butter,
            R.string.fish,
            R.string.meat,
            R.string.vegetable,
            R.string.cheese,
            R.string.lemonade,
            R.string.milk,
            R.string.fruit,
            R.string.yogurt
    };

    public EinkaufsArtikelImageAdapter(Context c, Dialog d)
    {
        mContext = c;
        dialog = d;
    }

    @Override
    public int getCount()
    {
        return imageId.length;
    }

    @Override
    public Object getItem(int position)
    {
        return imageId[position];
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public int getImage()
    {
        return imageId[click];
    }

    public int getImageName()
    {
        return name;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent)
    {
        final ImageButton image = new ImageButton(mContext);
        image.setImageResource(imageId[position]);
        image.setClickable(true);
        image.setBackgroundColor(Color.parseColor("#1D5C6A"));

        image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                click = position;
                name = imageString[position];
                dialog.dismiss();
            }
        });
        return image;
    }
}
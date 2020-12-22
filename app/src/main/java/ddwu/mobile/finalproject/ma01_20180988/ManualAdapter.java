package ddwu.mobile.finalproject.ma01_20180988;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.HashMap;

public class ManualAdapter extends PagerAdapter {
    private static final String TAG = "ManualAdapter";

    private ArrayList<Manual> manuals;
    private Context context;
    private NetworkManager networkManager;
    private ImageFileManager imageFileManager;
    private TextView tvDetailManual;
    private ImageView ivDetailManualImg;

    public ManualAdapter(Context context, ArrayList<Manual> manuals) {
        this.manuals = manuals;
        this.context = context;
        imageFileManager = new ImageFileManager(context);
        networkManager = new NetworkManager(context);
    }

    @Override
    public int getCount() {
        return manuals.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = null;
        if (context != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.manual_adapter_view, container, false);
        }

        ivDetailManualImg = v.findViewById(R.id.ivDetailManualImg);
        tvDetailManual = v.findViewById(R.id.tvDetailManual);

        tvDetailManual.setText(manuals.get(position).getContent());
        if (manuals.get(position).getImageLink() != null) {        Log.d(TAG, manuals.get(position).getImageLink());
            ivDetailManualImg.setVisibility(View.VISIBLE);
            ivDetailManualImg.setImageResource(R.drawable.ic_baseline_image_not_supported_24);
            new GetImageAsyncTask().execute(manuals.get(position).getImageLink());
        }

        container.addView(v);
        return v;
    }

    class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        String imageAddress;

        public GetImageAsyncTask() {
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            imageAddress = params[0];
            Bitmap result;
            result = networkManager.downloadImage(imageAddress);
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                ivDetailManualImg.setImageBitmap(bitmap);
            }
        }
    }
}

package ddwu.mobile.finalproject.ma01_20180988;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class ManualAdapter extends RecyclerView.Adapter<ManualAdapter.ViewHolder> {
    private static final String TAG = "ManualAdapter";

    private ArrayList<String> manuals;
    private HashMap<Integer, String> mImageLinks;

    private RecipeNetworkManager networkManager;
    private ImageFileManager imageFileManager;

    public ManualAdapter(Context context, ArrayList<String> manuals, HashMap<Integer, String> mImageLinks) {
        this.manuals = manuals;
        this.mImageLinks = mImageLinks;
        imageFileManager = new ImageFileManager(context);
        networkManager = new RecipeNetworkManager(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manual_adapter_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvDetailManual.setText(manuals.get(position));

        if (mImageLinks.containsKey(position + 1)) {
            Bitmap savedBitmap = imageFileManager.getBitmapFromTemporary(mImageLinks.get(position + 1));

            if (savedBitmap != null) {
                holder.ivDetailManualImg.setImageBitmap(savedBitmap);
            } else {
                holder.ivDetailManualImg.setImageResource(R.mipmap.ic_launcher);
                new GetImageAsyncTask(holder).execute(mImageLinks.get(position + 1));
            }
        }
        else {
            holder.ivDetailManualImg.setImageResource(R.mipmap.ic_launcher);
        }

    }

    @Override
    public int getItemCount() {
        return manuals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivDetailManualImg;
        public TextView tvDetailManual;

        public ViewHolder(View itemView) {
            super(itemView);
            ivDetailManualImg = itemView.findViewById(R.id.ivDetailManualImg);
            tvDetailManual = itemView.findViewById(R.id.tvDetailManual);
        }
    }

    class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        ViewHolder viewHolder;
        String imageAddress;

        public GetImageAsyncTask(ViewHolder holder) {
            viewHolder = holder;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            imageAddress = params[0];
            Bitmap result = null;
            result = networkManager.downloadImage(imageAddress);
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                viewHolder.ivDetailManualImg.setImageBitmap(bitmap);
                imageFileManager.saveBitmapToTemporary(bitmap, imageAddress);
            }
        }
    }
}

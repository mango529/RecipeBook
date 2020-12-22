package ddwu.mobile.finalproject.ma01_20180988;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<Recipe> list;
    private NetworkManager networkManager;

    public RecipeAdapter(Context context, int resource, ArrayList<Recipe> list) {
        this.context = context;
        this.layout = resource;
        this.list = list;
        networkManager = new NetworkManager(context);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Recipe getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getRecipe_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvRcpName = view.findViewById(R.id.tvRcpName);
            viewHolder.ivRcpImg = view.findViewById(R.id.ivRcpImg);
            viewHolder.tvRcpType = view.findViewById(R.id.tvRcpType);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        Recipe dto = list.get(position);

        viewHolder.tvRcpName.setText(dto.getName());
        if (dto.getType() != null) {
            viewHolder.tvRcpType.setText(dto.getType());
            viewHolder.tvRcpType.setVisibility(View.VISIBLE);
        }


        if (dto.getImageLink() == null) {
            return view;
        }

        new GetImageAsyncTask(viewHolder).execute(dto.getImageLink());

        return view;
    }

    public void setList(ArrayList<Recipe> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        public TextView tvRcpName = null;
        public ImageView ivRcpImg = null;
        public TextView tvRcpType = null;
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
                viewHolder.ivRcpImg.setImageBitmap(bitmap);
            }
        }
    }
}

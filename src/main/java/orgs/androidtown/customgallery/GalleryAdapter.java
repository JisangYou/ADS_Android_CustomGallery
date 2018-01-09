package orgs.androidtown.customgallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jisang on 2017-09-26.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.Holder>{
    Context context;
    List<String> data;

    public GalleryAdapter(Context context){
        this.context = context;
    }

    public void setData(List<String> data){
        this.data = data;
        // 데이터가 변경되었다고 알려주어야지만 그린다.
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_grid, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        String path = data.get(position);
        Uri uri = Uri.parse(path);
        holder.setImageUri(uri);
    }

    class Holder extends RecyclerView.ViewHolder{
        private Uri uri;
        private ImageView imageView;
        private TextView textView;
        public Holder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageItem);
            textView = itemView.findViewById(R.id.textDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.putExtra("imagePath", uri.getPath());
                    // 이미지 경로는 Thumbnail 이 아닌 원본 이미지의 경로를 넘겨야 한다
                    Activity activity = (Activity) context;
                    activity.setResult(Activity.RESULT_OK, intent);
                    activity.finish();
                }
            });
        }
        public void setImageUri(Uri uri){
            this.uri = uri;
            imageView.setImageURI(uri);
        }
        public void setDate(String date){
            textView.setText(date);
        }
    }
}

package orgs.androidtown.customgallery;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends BaseActivity {
    private static final int REQ_GALLERY = 999;
    ImageView imageView;
    @Override
    public void init() {
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    public void onGallery(View view){
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivityForResult(intent, REQ_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQ_GALLERY:
                if(resultCode == RESULT_OK) {
                    if(data != null) {
                        String imagePath = data.getStringExtra("imagePath");
                        Uri imageUri = Uri.parse(imagePath);
                        imageView.setImageURI(imageUri);
                    }
                }
                break;
        }
    }
}

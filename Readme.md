# ADS04 Android

## 수업 내용
- ContentResolver를 활용해 CustomGallery를 학습

## Code Review

### MainActivity

```Java
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

```
### GalleryActivity

```Java
public class GalleryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    GalleryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        init();
    }

    private void init(){
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new GalleryAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        // 데이터 불러오기
        List<String> data = load();
        adapter.setData(data);
    }

    // Content Resolver를 통해서 이미지 목록을 가져온다
    private List<String> load(){
        List<String> data = new ArrayList<>();
        ContentResolver resolver = getContentResolver();
        Uri uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI;
        String proj[] = {
                MediaStore.Images.Thumbnails.DATA
        };
        Cursor cursor = resolver.query(uri, proj, null, null, null);
        if(cursor != null){
            while(cursor.moveToNext()){
                int index = cursor.getColumnIndex(proj[0]);
                String path = cursor.getString(index);
                data.add(path);
            }
        }
        return data;
    }
}
```

### GalleryAdapter

```Java
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
```


## 보충설명

### Parsing이란?

- 다른 형식으로 저장된 데이터를 원하는 형식의 데이터로 변환하는 것 
- xml, json : 데이터를 표현하는 문자열
- Json 파싱 : json 형식의 문자열을 객체로 변환하는 것

### ContentProvider, ContentResolver 추가설명
- Content provider는 데이터를 관계형 데이터베이스의 테이블과 비슷한 형태로 추상화하여 외부에 제공

- ContentResolver
```Java
ContentResolver contentResolver = context.getContentResolver();
Cursor cursor = contentResolver.query(
                contentUri, // 데이터 테이블에 접근하기 위한 주소 같은 것이라고 생각하면 됩니다. -> From table
                projection, // 많은 열(속성)들 중에 어떤 열을 출력할 것인가를 나타냅니다. null이면 모든 열을 출력합니다. -> SELECT col, col...
                selection,  // 어떤 개체를 출력할 것인가에 대한 기준을 제공합니다. 엑셀의 필터링 기능과 동일합니다. null이면 모든 개체를 출력합니다.
                            //-> WHERE col =
                selectionArgs, // -> WHERE col =
                sortOrder);    // 어떤 개체를 출력할 것인가에 대한 기준을 제공합니다. 엑셀의 필터링 기능과 동일합니다. null이면 모든 개체를 출력합니다.                   //->WHERE col =
```

- 한꺼번에 많은 이미지를 화면에 표시할 때에는 원본 이미지는 너무 크기 때문에 성능상 적절하지 않기에, MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI 를 사용한다. 

### 출처

- 출처 : http://shygiants.github.io/android/2016/01/13/contentresolver.html

## TODO

- GridRecyclerView 사용해보기
- SQL문이 무엇인지 예습해보기
- Cursor 및 query에 대해 알아보기 

## Retrospect

- 안드로이드 내부적으로 사용할 수 있는 애플리케이션을 다루는 것이기에, 그냥 따라 쳐보지만 말고, 내부적으로 어떻게 구성이 되어 있는지 살펴보기

## Output
- 생략
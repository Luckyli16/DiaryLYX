package com.DiaryLYX.diary;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

import com.DiaryLYX.DB.initialization_dao;
import com.DiaryLYX.R;

public class update extends AppCompatActivity {
    private TextView author;
    private TextView date;
    private EditText title;
    private EditText content;
    private Button back;
    private Button image;
    private com.DiaryLYX.DB.dao dao;
    private Intent intent;
    private ImageView diaryImage;
    private static final int TAKE_PICTURE = 1;  //拍照
    private static final int CHOOSE_PICTURE = 2;  //从相册中选择照片
    private static final int SCALE = 5;//照片缩小比例
    private String imagename2 = null;
    private  File image_file;
    private Uri image_uri;
    private String imagepath;
    private  String imagename="";   //记录图片名
    //设置布局文件和初始化界面元素。
    // 获取从上一个活动传递过来的日记标题、内容、作者和日期，
    // 并将它们显示在对应的 TextView 和 EditText 中。
    //image 按钮的点击事件中，调用 showmenu() 方法显示菜单，用于选择拍照或从相册选择照片。
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diary);
        author=findViewById(R.id.d_author);
        content=findViewById(R.id.d_content);
        date=findViewById(R.id.d_date);
        title=findViewById(R.id.d_title);
        back=findViewById(R.id.back);
        DbUtil();
        intent = getIntent();
        String str_title = intent.getStringExtra("d_title");
        String str_content = intent.getStringExtra("d_content");
        String str_author = intent.getStringExtra("d_author");
        String syr_date = intent.getStringExtra("d_date");
        author.setText(str_author);
        date.setText(syr_date);
        title.setText(str_title);
        content.setText(str_content);

        imagename =intent.getStringExtra("d_image");
        image=findViewById(R.id.image);
        diaryImage=findViewById(R.id.diary_picture);

        if(!imagename.equals("")){
            Bitmap bitmap = BitmapFactory.decodeFile(getFilesDir().getAbsolutePath()+"/"+imagename);
            diaryImage.setImageBitmap(bitmap);
        }

        //更新
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //获取到两个输入框的值
                String title2 = title.getText().toString();
                String context = content.getText().toString();
                if(!title2.equals("")){
                    intent = new Intent();
                    intent.putExtra("title",title2);
                    intent.putExtra("content",context);
                    intent.putExtra("image",imagename);
                    setResult(RESULT_OK,intent);
                    finish();
                } else if(title2.equals("")){
                    finish();
                }
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showmenu(view);
            }
        });
    }





    /**
     * 显示菜单
     * @param view
     * 创建一个 PopupMenu 对象，并为其设置菜单项和点击事件监听器。
     * 当选择拍照菜单项时，调用 startCamera() 方法启动摄像机拍照。
     * 当选择从相册选择照片菜单项时，创建一个 Intent 对象，
     * 通过 ACTION_GET_CONTENT 动作获取图片，并通过 startActivityForResult() 方法启动该 Intent。
     */
    public void showmenu(View view){
        PopupMenu menu = new PopupMenu(this,view);
        menu.getMenuInflater().inflate(R.menu.menu,menu.getMenu());
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            int REQUEST_CODE;

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.take_pic:
                        startCamera();
                        break;

                    case R.id.select_pic:
                        Intent select = new Intent(Intent.ACTION_GET_CONTENT);
                        REQUEST_CODE = CHOOSE_PICTURE;
                        select.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                        startActivityForResult(select, REQUEST_CODE);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        menu.show();
    }

    /**
     * 启动摄像机
     * 创建一个文件用于保存拍摄的照片，并为其生成一个内容 URI。然后，创建一个启动相机的 Intent 对象，
     * 并通过 EXTRA_OUTPUT 额外数据将文件的 URI 添加到 Intent 中，以指定保存照片的位置。
     * 最后，调用 startActivityForResult() 方法启动相机，并传递一个请求码。
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void startCamera() {
        Intent intent = new Intent();
        //指定动作，启动相机
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        //创建文件
        createImageFile();
        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        //获取uri
        image_uri = FileProvider.getUriForFile(this, "com.DiaryLYX.fileProvider", image_file);
        System.out.println("mImageUri"+image_uri);
        //将uri加入到额外数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        //启动相机并要求返回结果
        startActivityForResult(intent, TAKE_PICTURE);
    }

    /**
     * 创建图片文件
     * 用于创建保存拍摄照片的文件，将文件路径保存到 imagepath 变量中。
     */
    private void createImageFile(){
        //设置图片文件名（含后缀），以当前时间的毫秒值为名称
        imagename2 = Calendar.getInstance().getTimeInMillis() + ".jpg";
        //创建图片文件
        image_file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + "Pictures" +"/", imagename2);
        //将图片的绝对路径设置给mImagePath，后面会用到
        imagepath = image_file.getAbsolutePath();
        System.out.println("mImagePath"+imagepath);
        //按设置好的目录层级创建
        image_file.getParentFile().mkdirs();
        //不加这句会报Read-only警告。且无法写入SD
        image_file.setWritable(true);
    }
//处理从相机或相册选择照片后的结果。根据请求码判断是从相机返回还是从相册返回。
// 从相机返回时，首先从文件中解码出原始照片的 Bitmap 对象，
// 然后通过 zoomBitmap() 方法缩小图片，并在界面上显示。
// 接着调用 savePhotoToSDCard() 方法将缩小后的图片保存到本地，并生成一个以当前时间戳命名的文件名。
// 从相册返回时，通过 ContentResolver 获取原始图片的 Bitmap 对象，然后进行缩小和保存操作。
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_PICTURE:
                    //将保存在本地的图片取出并缩小后显示在界面上
                    Bitmap bitmap = BitmapFactory.decodeFile(imagepath);
                    System.out.println("mImagePath"+imagepath);
                    Bitmap newBitmap = take_Image.zoomBitmap(bitmap, bitmap.getWidth() / SCALE, bitmap.getHeight() / SCALE);
                    //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常
                    bitmap.recycle();

                    //将处理过的图片显示在界面上，并保存到本地
                    diaryImage.setImageBitmap(newBitmap);
                    String s=String.valueOf(System.currentTimeMillis());
                    take_Image.savePhotoToSDCard(newBitmap,getFilesDir().getAbsolutePath(), s);
                    imagename=s+".png";
                    break;

                case CHOOSE_PICTURE:
                    ContentResolver resolver = getContentResolver();
                    //照片的原始资源地址
                    Uri originalUri = data.getData();
                    try {
                        //使用ContentProvider通过URI获取原始图片
                        Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                        if (photo != null) {
                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                            Bitmap smallBitmap = take_Image.zoomBitmap(photo, photo.getWidth() / SCALE, photo.getHeight() / SCALE);
                            //释放原始图片占用的内存，防止out of memory异常发生
                            photo.recycle();

                            //将处理过的图片显示在界面上，并保存到本地
                            diaryImage.setImageBitmap(smallBitmap);
                            String s1=String.valueOf(System.currentTimeMillis());
                            take_Image.savePhotoToSDCard(smallBitmap,getFilesDir().getAbsolutePath(), s1);
                            imagename=s1+".png";
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 初始化dao
     */
    public void DbUtil(){
        dao = ((initialization_dao)this.getApplication()).getDao();
    }

}
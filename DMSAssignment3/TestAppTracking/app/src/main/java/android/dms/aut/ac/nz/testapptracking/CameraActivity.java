package android.dms.aut.ac.nz.testapptracking;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends AppCompatActivity {
    /** Called when the activity is first created. */

    Bitmap thumbnail;
    Button btnTakePhoto;
    ImageView imgTakenPhoto, imageView;
    private static final int CAM_REQUREST = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btnTakePhoto = (Button) findViewById(R.id.button1);
        imgTakenPhoto = (ImageView) findViewById(R.id.imageView1);

        imageView = (ImageView) findViewById(R.id.imageView);
        btnTakePhoto.setOnClickListener(new btnTakePhotoClicker());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAM_REQUREST&& resultCode == AppCompatActivity.RESULT_OK) {
            //Check if your application folder exists in the external storage, if not create it:

            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            String partFilename = currentDateFormat();
            storeCameraPhotoInSDCard(bitmap, partFilename);

            // display the image from SD Card to ImageView Control
            String storeFilename = "photo_" + partFilename + ".jpg";
            thumbnail = getImageFileFromSDCard(storeFilename);
            imgTakenPhoto.setImageBitmap(thumbnail);


            imageView.setVisibility(View.VISIBLE);
            imageView.setMaxHeight(bitmap.getHeight());
            imageView.setMaxWidth(bitmap.getWidth());
            imageView.setImageBitmap(bitmap);
            Toast.makeText(this,"SAVE Photo!", Toast.LENGTH_LONG).show();
        } else { // Result was a failure
            Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getImageFileFromSDCard(String filename){
        Bitmap bitmap = null;
        File imageFile = new File(Environment.getExternalStorageDirectory() + filename);
        try {
            FileInputStream fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private String currentDateFormat(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String  currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    private void storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate){
        File outputFile = new File(Environment.getExternalStorageDirectory(), "photo_" + currentDate + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class btnTakePhotoClicker extends Intent implements Button.OnClickListener
    {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAM_REQUREST);
        }
    }

    public void onBackPressed()
    {
        onClick(findViewById(View.generateViewId()));
    }

    public void onClick(View v) {
        finish();
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }
}
package josim74.github.com.cameraprictice;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    String photoPath;
    static int IMAGE_CAPTURE_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);

    }

    public void capture(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            try {
                File phontFile = createImageFile();
                if (phontFile != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(phontFile));
                    startActivityForResult(intent, IMAGE_CAPTURE_REQUEST_CODE);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDire = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, "jpg", storageDire);
        photoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_CAPTURE_REQUEST_CODE) {
            setPic();
        }
    }

    private void setPic() {
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        BitmapFactory.Options options = new BitmapFactory.Options();
        BitmapFactory.decodeFile(photoPath, options);
        int photoW = options.outWidth;
        int photoH = options.outHeight;
        int scaleFactor = Math.min(photoH / targetH, photoW / targetW);
        options.inSampleSize = scaleFactor;
        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
        imageView.setImageBitmap(bitmap);
    }
}

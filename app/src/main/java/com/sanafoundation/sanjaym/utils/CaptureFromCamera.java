package com.sanafoundation.sanjaym.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sanafoundation.sanjaym.R;
import com.sanafoundation.sanjaym.helper.PrefManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CaptureFromCamera extends AppCompatActivity {

    PrefManager pref;

    public static int REQUEST_IMAGE_CAPTURE = 100;
    public static int CROP_PIC = 300;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private Uri fileUri;
    private static File mediaFile;
    private String filePath;

    ImageView imgPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_from_camera);

        captureImage();

        imgPreview = (ImageView) findViewById(R.id.imgPreview);

        pref = new PrefManager(getApplicationContext());
    }

    private void captureImage() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            previewMediaAndUpload();
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {

        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "SanjayMChat");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("FailedCreatingDirectory", "Oops! Failed create " + "SanjayMChat" + " directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        //File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }

    private void previewMediaAndUpload() {

        filePath = fileUri.getPath();

        if (filePath != null) {
            File sourceFile = new File(filePath);

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            imgPreview.setImageBitmap(bitmap);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
            byte[] byteArray = stream.toByteArray();

            ParseFile file = new ParseFile(pref.getUserName() + ".PNG", byteArray);
            file.saveInBackground();

            if (byteArray != null) {
                ParseUser currentUser = ParseUser.getCurrentUser();
                currentUser.put("pic", file);
                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(getApplicationContext(), "Everythings Is Fine", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Nothing Is Fine", Toast.LENGTH_LONG).show();
                            Log.d("Failliure", e.toString());
                        }
                    }
                });
                Toast.makeText(getApplicationContext(), currentUser.saveInBackground().toString(), Toast.LENGTH_LONG).show();
            }
            finish();
        }
    }

    /*private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(fileUri, "image*//*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }*/

}
package com.sanafoundation.sanjaym.utils;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.sanafoundation.sanjaym.helper.PrefManager;

import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Created by Sanjay on 8/14/15.
 */
public class SelectFromGallery extends Activity {

    private final static int RESULT_SELECT_IMAGE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final String TAG = "GalleryUtil";

    String mCurrentPhotoPath;
    File photoFile = null;

    private PrefManager pref;

    private Uri selectedImage;
    private static File mediaFile;
    private String picturePath;
    private String filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = new PrefManager(getApplicationContext());
        try {
            //Pick Image From Gallery
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, RESULT_SELECT_IMAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SELECT_IMAGE:

                if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                    try {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex);
                        cursor.close();

                        //return Image Path to the Main Activity
                        Intent returnFromGalleryIntent = new Intent();
                        returnFromGalleryIntent.putExtra("picturePath", picturePath);
                        setResult(RESULT_OK, returnFromGalleryIntent);
                        previewMediaAndUpload();
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Intent returnFromGalleryIntent = new Intent();
                        setResult(RESULT_CANCELED, returnFromGalleryIntent);
                        finish();
                    }
                } else {
                    Log.i(TAG, "RESULT_CANCELED");
                    Intent returnFromGalleryIntent = new Intent();
                    setResult(RESULT_CANCELED, returnFromGalleryIntent);
                    finish();
                }
                break;
        }
    }

    private void previewMediaAndUpload() {

        filePath = picturePath;

        if (filePath != null) {
            File sourceFile = new File(filePath);

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 4;

            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, stream);
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
                            Toast.makeText(getApplicationContext(), "Pic successfully uploaded", Toast.LENGTH_LONG).show();
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

}

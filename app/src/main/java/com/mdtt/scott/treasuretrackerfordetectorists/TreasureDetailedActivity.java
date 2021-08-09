package com.mdtt.scott.treasuretrackerfordetectorists;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class TreasureDetailedActivity extends AppCompatActivity {

    private BackgroundTask bt;
    private int treasureId;
    private Treasure treasure;
    private LinearLayout mLinearLayoutText, mLinearLayoutImages;
    private final ArrayList<Bitmap> treasurePhotos = new ArrayList<>();
    private final ArrayList<Bitmap> treasurePhotosFull = new ArrayList<>();
    private ProgressBar mProgressBar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        treasureId = Objects.requireNonNull(getIntent().getExtras()).getInt("treasureId");
        String type = getIntent().getExtras().getString("type");
        type = Objects.requireNonNull(type).substring(0,1).toUpperCase()+ type.substring(1).toLowerCase();
        setTitle(type +":");
        setContentView(R.layout.activity_treasure_detailed);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        mProgressBar = findViewById(R.id.activity_treasure_detailed_progressBar);
        mLinearLayoutText = findViewById(R.id.activity_treasure_detailed_LL_text);
        mLinearLayoutImages = findViewById(R.id.activity_treasure_detailed_LL_images);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mLinearLayoutText.getChildCount() > 0)
            mLinearLayoutText.removeAllViews();
        if(mLinearLayoutImages.getChildCount() > 0)
            mLinearLayoutImages.removeAllViews();
        mProgressBar.setVisibility(View.VISIBLE);
        bt = new BackgroundTask();
        bt.execute();
    }


    @Override
    public void onStop() {
        super.onStop();
        bt.cancel(true);
    }

    private class BackgroundTask extends AsyncTask<Void, Void, Integer> {

        protected Integer doInBackground(Void...voids) {
            final MySQliteHelper helper = new MySQliteHelper(getApplicationContext());

            treasurePhotos.clear();
            treasurePhotosFull.clear();
            treasure = helper.getDetailedTreasure(treasureId);
            //path to subDir: /data/user/0/com.mdtt.scott.treasuretrackerfordetectorists/files/imageDir
            File directory = getApplication().getFilesDir();
            File subDir = new File(directory, "imageDir");
            boolean isSubDirCreated = subDir.exists();
            if (!isSubDirCreated)
                isSubDirCreated = subDir.mkdir();
            if(isSubDirCreated)
            {
                final String prefix = treasure.getTreasurePhotoPath();
                Bitmap photo;

                File [] files = subDir.listFiles((directory1, name) -> name.startsWith(prefix));

                //listFiles returns in reverse alphabetical order, so we need to sort to get alphabetical
                // so that photo order remains the same as when added.
                Arrays.sort(files);

                for (File file : files) {
                    String filepath = file.getPath();

                    // First decode with inJustDecodeBounds=true to check dimensions
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(filepath, options);

                    //Log.d("myTag", "outheight: "+options.outHeight);
                    //Log.d("myTag", "outwidth: "+options.outWidth);
                    //Log.d("myTag", "bitmap: "+options.inBitmap);

                    // Calculate inSampleSize
                    options.inSampleSize = calculateInSampleSize(options, 100, 100);

                    // Decode bitmap with inSampleSize set
                    options.inJustDecodeBounds = false;
                    photo = BitmapFactory.decodeFile(filepath, options);
                    treasurePhotos.add(photo);
                    //Log.d("myTag", "Path of photo is: "+filepath);

                    //now do the same to get high-res version for full screen version
                    // First decode with inJustDecodeBounds=true to check dimensions
                    options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(filepath, options);

                    //Log.d("myTag", "outheight: "+options.outHeight);
                    //Log.d("myTag", "outwidth: "+options.outWidth);
                    //Log.d("myTag", "bitmap: "+options.inBitmap);

                    // Calculate inSampleSize
                    DisplayMetrics metrics = getResources().getDisplayMetrics();
                    int screenHeight = (int) (metrics.heightPixels * 0.80);
                    options.inSampleSize = calculateInSampleSize(options, WindowManager.LayoutParams.MATCH_PARENT, screenHeight);

                    // Decode bitmap with inSampleSize set
                    options.inJustDecodeBounds = false;
                    photo = BitmapFactory.decodeFile(filepath, options);
                    treasurePhotosFull.add(photo);
                }

                if(files.length == 0)
                {
                    photo = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.defaultphoto);
                    treasurePhotos.add(photo);
                }
            }

            return 1;
        }

        protected void onPostExecute(Integer result) {

            mProgressBar.setVisibility(View.GONE);
            mLinearLayoutImages.removeAllViews();

            ViewGroup.MarginLayoutParams lparamsImages = new ViewGroup.MarginLayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            int dimensionInDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
            lparamsImages.height = dimensionInDp;
            lparamsImages.width = dimensionInDp;

            //Log.d("myTag", "bitmap count = "+treasurePhotos.size());
            for (final Bitmap photo: treasurePhotos) {
                final ImageView imageView = new ImageView(getApplicationContext());
                imageView.setId(photo.getGenerationId());
                imageView.setPadding(2, 2, 2, 2);
                imageView.setImageBitmap(photo);
                imageView.setLayoutParams(lparamsImages);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shadow_9));
                mLinearLayoutImages.addView(imageView);
                if(!photo.sameAs(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.defaultphoto)))
                {
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            final Dialog nagDialog = new Dialog(TreasureDetailedActivity.this,android.R.style.Theme_Translucent_NoTitleBar)
                            {
                                @Override
                                public boolean onTouchEvent(MotionEvent event) {
                                    // Tap anywhere to close dialog.
                                    this.dismiss();
                                    return true;
                                }
                            };
                            nagDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            nagDialog.setCancelable(true);

                            DisplayMetrics metrics = getResources().getDisplayMetrics();
                            int screenHeight = (int) (metrics.heightPixels * 0.80);


                            //nagDialog.setContentView(imageView);
                            ImageView fullsize = new ImageView(getApplicationContext());
                            fullsize.setImageBitmap(treasurePhotosFull.get(treasurePhotos.indexOf(photo)));
                            fullsize.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.shadow_14));
                            nagDialog.setContentView(fullsize);
                            Objects.requireNonNull(nagDialog.getWindow()).setLayout(WindowManager.LayoutParams.MATCH_PARENT, screenHeight);
                            nagDialog.show();
                        }
                    });
                }
            }


            ViewGroup.MarginLayoutParams lparamsText = new ViewGroup.MarginLayoutParams(
                    ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
            lparamsText.bottomMargin= 10;
            LinkedHashMap<String, String> emptyDetails = new LinkedHashMap<>();

            for (Map.Entry<String, String> entry : treasure.getTreasureDetailed().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if(value != null)
                {
                    //adds empty entries to the bottom of the detailed list
                    if(value.isEmpty())
                    {
                        emptyDetails.put(key,value);
                    }
                    //adds empty entries only if they are jewelry entries without a weight number.
                    else if(key.equals("Weight: ") && !value.matches(".*\\d.*") && !value.contains("null"))
                    {
                       emptyDetails.put(key, " ");
                    }
                    //adds entries with values to the top of the detailed list
                    else
                    {
                        if(key.equals("Date Found: "))
                        {
                            String[] splitDate = value.split("/");
                            value = splitDate[1]+"/"+splitDate[2]+"/"+splitDate[0];
                        }
                        //skip weight for all treasure types except jewelry
                        if(!value.equals("null Grams (g)"))
                        {
                            TextView tv = new TextView(getApplicationContext());
                            tv.setLayoutParams(lparamsText);
                            String keyValue = key + value;
                            tv.setText(keyValue);
                            tv.setTextColor(R.color.colorPrimaryDark);
                            //Log.d("myTag", ""+tv.getText().toString());
                            mLinearLayoutText.addView(tv);
                        }
                    }
                }
            }
            if(!emptyDetails.isEmpty())
            {
                TextView extra = new TextView(getApplicationContext());
                extra.setLayoutParams(lparamsText);
                extra.setText("");
                mLinearLayoutText.addView(extra);
                for (Map.Entry<String, String> entry : emptyDetails.entrySet()) {
                    String key = entry.getKey();
                    String value = entry.getValue();

                    TextView tv = new TextView(getApplicationContext());
                    tv.setLayoutParams(lparamsText);
                    String keyValue = key + value;
                    tv.setText(keyValue);

                    tv.setTextColor(R.color.colorPrimaryDark);

                    mLinearLayoutText.addView(tv);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.treasure_detailed_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }
        else if(id == R.id.action_edit)
        {
            //Treasure treasure = new Treasure(0, type, coinCountry, coinType, treasureSeries, treasureName, treasureYear, coinMint, treasureMaterial, treasureWeight, treasureLocationFound, treasureFoundDate, treasureInfo, timeAtAdd);
            Intent myIntent = new Intent(getApplicationContext(), AddActivity.class);
            myIntent.putExtra("id", treasure.getTreasureId());
            myIntent.putExtra("type", treasure.getTreasureType());
            myIntent.putExtra("coinCountry", treasure.getTreasureCountry());
            myIntent.putExtra("coinType", treasure.getTreasureDenomination());
            myIntent.putExtra("treasureSeries", treasure.getTreasureSeries());
            myIntent.putExtra("treasureName", treasure.getTreasureName());
            myIntent.putExtra("treasureYear", treasure.getTreasureYear());
            myIntent.putExtra("coinMint", treasure.getTreasureMint());
            myIntent.putExtra("treasureMaterial", treasure.getTreasureMaterial());
            myIntent.putExtra("treasureWeight", treasure.getTreasureWeight());
            myIntent.putExtra("treasureLocationFound", treasure.getTreasureLocationFound());
            myIntent.putExtra("treasureDateFound", treasure.getTreasureDateFound());
            myIntent.putExtra("treasureInfo", treasure.getTreasureInfo());
            myIntent.putExtra("timeAtAdd", treasure.getTreasurePhotoPath());
            myIntent.putExtra("treasureWeightUnit", treasure.getTreasureWeightUnit());
            startActivity(myIntent);
        }
        else if(id == R.id.action_share)
        {
            ArrayList<Uri> imageUris = new ArrayList<>();

            //path to subDir: /data/user/0/com.mdtt.scott.treasuretrackerfordetectorists/files/imageDir
            File directory = getApplication().getFilesDir();
            File subDir = new File(directory, "imageDir");
            boolean isSubDirCreated = subDir.exists();
            if (!isSubDirCreated)
                isSubDirCreated = subDir.mkdir();
            if(isSubDirCreated)
            {
                final String prefix = treasure.getTreasurePhotoPath();

                File [] files = subDir.listFiles((directory1, name) -> name.startsWith(prefix));

                for (File file : files) {
                    File newFile = new File(subDir, file.getName());
                    Uri contentUri = FileProvider.getUriForFile(this, "com.mdtt.scott.fileprovider", newFile);
                    imageUris.add(contentUri);
                }

                //get text of detailed for sharing
                String shareText="";

                for (int i = 0; i < mLinearLayoutText.getChildCount(); i++)
                {
                    Object child = mLinearLayoutText.getChildAt(i);
                    if (child instanceof TextView)
                    {
                        TextView e = (TextView)child;
                        if(e.getText().length() > 0)    // Whatever logic here to determine if valid.
                        {
                            shareText = shareText+e.getText()+"\n";
                        }
                        //we've gotten to the textview called extra which means all empty fields below here
                        else
                        {
                            break;
                        }
                    }
                }

                //add app signature line
                shareText = shareText+"\nShared from Treasure Tracker for Detectorists App";

                Toast.makeText(this, "Treasure info copied to clipboard!", Toast.LENGTH_SHORT).show();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("auto_copy_treasureInfo", shareText);
                clipboard.setPrimaryClip(clip);

                Intent shareIntent = new Intent();
                if(!imageUris.isEmpty())
                {
                    shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
                    shareIntent.setType("image/*");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    startActivity(Intent.createChooser(shareIntent, "Share photos and info to.."));
                }
                else
                {
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
                    startActivity(Intent.createChooser(shareIntent, "Share info to.."));
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}

package com.mdtt.scott.treasuretrackerfordetectorists;


import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("WeakerAccess")
public class AddPhotoFragment extends Fragment {

    private Button addPhotoButton;
    //private static final String IMAGE_DIRECTORY = "/demonuts";
    //private int GALLERY = 1, CAMERA = 2;
    private GridView gridView;
    private String timeAtAdd;
    private final ArrayList<Integer> photoIds = new ArrayList<>();
    private final ArrayList<String> photoNames = new ArrayList<>();
    private final ArrayList<String> photoFilename = new ArrayList<>();
    private final ArrayList<String> photoYears = new ArrayList<>();
    private final ArrayList<String> photoFoundYears = new ArrayList<>();
    private final ArrayList<Bitmap> photoBitmaps = new ArrayList<>();
    private int counter = 0;
    private String type;
    private int id;
    private Bundle bundle;

    public AddPhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            bundle = getArguments();
            type = bundle.getString("type");
            id = bundle.getInt("id");

            if(savedInstanceState == null)
            {
                //contains timeAtAdd because editing the treasure
                if(bundle.containsKey("timeAtAdd"))
                {
                    timeAtAdd = bundle.getString("timeAtAdd");
                }
                //adding a new treasure, generate a timeAtAdd now
                else
                {
                    timeAtAdd = Long.toString(System.currentTimeMillis());
                    //Log.d("myTag", "added to bundle: "+timeAtAdd);
                    bundle.putString("timeAtAdd", timeAtAdd);
                }

            }
            else
            {
                timeAtAdd = savedInstanceState.getString("timeAtAdd");
                //Log.d("myTag", "received from savedInstanceState: "+timeAtAdd);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_photo, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        gridView = view.findViewById(R.id.addtreasure_gridview);
        addPhotoButton = view.findViewById(R.id.addphoto_button);

        //see if there were any photos added previously using timeAtAdd
        if(savedInstanceState != null || id != 0)
        {
            ContextWrapper cw = new ContextWrapper(Objects.requireNonNull(getActivity()).getApplicationContext());
            // path to /data/data/yourapp/app_data/imageDir
            File directory = cw.getFilesDir();
            File subDir = new File(directory, "imageDir");
            boolean isSubDirCreated = subDir.exists();
            if (!isSubDirCreated)
                isSubDirCreated = subDir.mkdir();
            if(isSubDirCreated)
            {
                //restoring from savedInstanceState
                if(savedInstanceState != null)
                {
                    final String prefix = timeAtAdd;
                    final String prefix2 = "temp_" + timeAtAdd;

                    File[] files = subDir.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File directory, String name) {
                            return name.startsWith(prefix);
                        }
                    });

                    //listFiles returns in reverse alphabetical order, so we need to sort to get alphabetical
                    // so that photo order remains the same as when added.
                    Arrays.sort(files);

                    for (File file : files) {

                        counter++;
                        photoNames.add("Photo " + counter);
                        photoYears.add("");
                        photoFoundYears.add("");
                        photoIds.add(counter);
                        photoFilename.add(file.getName());

                        // First decode with inJustDecodeBounds=true to check dimensions
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;

                        // Calculate inSampleSize
                        options.inSampleSize = calculateInSampleSize(options, 100, 100);

                        // Decode bitmap with inSampleSize set
                        options.inJustDecodeBounds = false;

                        //add bitmap to photoBitmaps
                        photoBitmaps.add(BitmapFactory.decodeFile(file.getPath(), options));
                    }

                    File[] files2 = subDir.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File directory, String name) {
                            return name.startsWith(prefix2);
                        }
                    });

                    for (File file : files2) {

                        counter++;
                        photoNames.add("Photo " + counter);
                        photoYears.add("");
                        photoFoundYears.add("");
                        photoIds.add(counter);
                        photoFilename.add(file.getName());

                        // First decode with inJustDecodeBounds=true to check dimensions
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;

                        // Calculate inSampleSize
                        options.inSampleSize = calculateInSampleSize(options, 100, 100);

                        // Decode bitmap with inSampleSize set
                        options.inJustDecodeBounds = false;

                        //add bitmap to photoBitmaps
                        photoBitmaps.add(BitmapFactory.decodeFile(file.getPath(), options));
                    }
                }
                //editing a treasure so load in any stored photos from that treasure
                else
                {
                    if(counter == 0)
                    {
                        final String prefix = timeAtAdd;

                        File[] files = subDir.listFiles(new FilenameFilter() {
                            @Override
                            public boolean accept(File directory, String name) {
                                return name.startsWith(prefix);
                            }
                        });

                        //listFiles returns in reverse alphabetical order, so we need to sort to get alphabetical
                        // so that photo order remains the same as when added.
                        Arrays.sort(files);

                        for (File file : files) {

                            counter++;
                            photoNames.add("Photo " + counter);
                            photoYears.add("");
                            photoFoundYears.add("");
                            photoIds.add(counter);
                            photoFilename.add(file.getName());

                            // First decode with inJustDecodeBounds=true to check dimensions
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;

                            // Calculate inSampleSize
                            options.inSampleSize = calculateInSampleSize(options, 100, 100);

                            // Decode bitmap with inSampleSize set
                            options.inJustDecodeBounds = false;

                            //add bitmap to photoBitmaps
                            photoBitmaps.add(BitmapFactory.decodeFile(file.getPath(), options));
                        }
                    }
                }
            }
        }

        if (counter > 0) {
            addPhotoButton.setText("ADD ANOTHER PHOTO");
            CustomGridViewAdapter adapterViewAndroid = new CustomGridViewAdapter(getActivity(), photoIds, photoNames, photoYears, photoBitmaps);
            gridView.setAdapter(adapterViewAndroid);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int i, long id) { showRemovePictureDialog(i);
                }
            });
        }

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startCropper();
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("timeAtAdd", timeAtAdd);
    }


    private void startCropper() {
        // start picker to get image for cropping and then use the image in cropping activity
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                //.setCropShape(CropImageView.CropShape.OVAL)
                //.setFixAspectRatio(true)
                .start(Objects.requireNonNull(getContext()), this);
    }

    private void showRemovePictureDialog(final int i) {
        final CharSequence[] items = {"Yes", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setTitle("Remove Photo?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Yes")) {

                    //Log.d("TEST", "The value of i is: "+i);

                    photoNames.remove("Photo "+ i+1);
                    photoYears.remove("");
                    photoFoundYears.remove("");
                    photoIds.remove(i);
                    photoBitmaps.remove(i);

                    //Log.d("TEST", "The photopath of file to be removed is: "+photoPaths.get(i));

                    final String photoName = photoFilename.get(i);

                    ContextWrapper cw = new ContextWrapper(Objects.requireNonNull(getActivity()).getApplicationContext());
                    // path to /data/data/yourapp/app_data/imageDir
                    File directory = cw.getFilesDir();
                    File subDir = new File(directory, "imageDir");
                    boolean isSubDirCreated = subDir.exists();
                    if (!isSubDirCreated)
                        isSubDirCreated = subDir.mkdir();
                    if(isSubDirCreated)
                    {
                        File [] files = subDir.listFiles(new FilenameFilter() {
                            @Override
                            public boolean accept(File directory, String name) {
                                return name.equals(photoName);
                            }
                        });

                        for (File file : files) {
                            //in process of editing treasure: rename photo to flag for deletion, but do not actually delete until edit is complete in case user cancels edit
                            if(!file.getName().startsWith("temp_"))
                            {
                                Log.d("TEST", "photo was not temp. file will be renamed instead: "+file.getPath());
                                //add edit_ chars to prefix string
                                String newName = "edit_"+file.getName();
                                File newFile = new File(subDir, newName);
                                //rename file from temp to permanent
                                boolean result = file.renameTo(newFile);
                            }
                            //simply delete the temp image
                            else
                            {
                                Log.d("TEST", "file to be removed is: "+file.getPath());
                                //delete file that was saved in saveImage method
                                boolean result = file.delete();
                            }
                            photoFilename.remove(i);
                        }

                        counter--;
                        if (counter == 0) {
                            addPhotoButton.setText("ADD PHOTO");
                            gridView.setAdapter(null);
                        }
                        else
                        {
                            CustomGridViewAdapter adapterViewAndroid = new CustomGridViewAdapter(getActivity(), photoIds, photoNames, photoYears, photoBitmaps);
                            gridView.setAdapter(adapterViewAndroid);
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view,
                                                        int i, long id) {
                                    showRemovePictureDialog(i);
                                }
                            });
                        }
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == AddActivity.RESULT_OK) {
                Uri resultUri = result.getUri();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), resultUri);
                    String filename = saveImage(bitmap);
                    if (filename.equals("fail")) {
                        Toast.makeText(getActivity(), "Photo failed to save...", Toast.LENGTH_SHORT).show();
                    } else {
                        counter++;
                        if (counter == 1) {
                            addPhotoButton.setText("ADD ANOTHER PHOTO");
                        }
                        photoNames.add("Photo " + counter);
                        photoYears.add("");
                        photoFoundYears.add("");
                        photoIds.add(counter);
                        photoFilename.add(filename);

                        // First decode with inJustDecodeBounds=true to check dimensions
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;

                        // Calculate inSampleSize
                        options.inSampleSize = calculateInSampleSize(options, 100, 100);

                        // Decode bitmap with inSampleSize set
                        options.inJustDecodeBounds = false;

                        //get photo full path
                        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
                        // path to /data/data/yourapp/app_data/imageDir
                        File directory = cw.getFilesDir();
                        File subDir = new File(directory, "imageDir");
                        String photoPath = subDir.getPath() + "/" + filename;

                        //add bitmap to photoBitmaps
                        photoBitmaps.add(BitmapFactory.decodeFile(photoPath, options));

                        //reload UI
                        CustomGridViewAdapter adapterViewAndroid = new CustomGridViewAdapter(getActivity(), photoIds, photoNames, photoYears, photoBitmaps);
                        gridView.setAdapter(adapterViewAndroid);
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int i, long id) {
                                showRemovePictureDialog(i);
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String saveImage(Bitmap myBitmap) {
        ContextWrapper cw = new ContextWrapper(Objects.requireNonNull(getActivity()).getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getFilesDir();
        File subDir = new File(directory, "imageDir");
        boolean isSubDirCreated = subDir.exists();
        if (!isSubDirCreated)
            isSubDirCreated = subDir.mkdir();
        if(isSubDirCreated)
        {
            // Create imageDir
            // File saved as unique temp image name 'temp_timeAtAdd_timeAtPhotoAdd.png'
            //temp prefix will be removed once treasure is officially added to database
            String filename = "temp_" + timeAtAdd + "_" + System.currentTimeMillis() +".jpeg";
            File path=new File(subDir,filename);

            //Log.d("TEST","Path to save image at is:"+path.getPath());

            FileOutputStream out = null;
            try {
                out = new FileOutputStream(path);
                myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
            } catch (FileNotFoundException e) {
                return "fail";
            } finally {
                try {
                    Objects.requireNonNull(out).close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return filename;
        }
        else
        {
            return "fail";
        }

    }

    public void nextButtonClicked() {
        switch (type) {
            case "coin":
                ((AddActivity) Objects.requireNonNull(getActivity())).replaceFragments(AddCoinInfoFragment.class, bundle, "addInfo");
                break;
            case "token":
                ((AddActivity) Objects.requireNonNull(getActivity())).replaceFragments(AddTokenInfoFragment.class, bundle, "addInfo");
                break;
            case "jewelry":
                ((AddActivity) Objects.requireNonNull(getActivity())).replaceFragments(AddJewelryInfoFragment.class, bundle, "addInfo");
                break;
            case "relic":
                ((AddActivity) Objects.requireNonNull(getActivity())).replaceFragments(AddRelicInfoFragment.class, bundle, "addInfo");
                break;
            case "collection":
                ((AddActivity) Objects.requireNonNull(getActivity())).replaceFragments(AddFinalInfoFragment.class, bundle, "addFinal");
                break;
        }
    }

    @SuppressWarnings("SameParameterValue")
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

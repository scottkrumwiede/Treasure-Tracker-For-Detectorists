package com.mdtt.scott.treasuretrackerfordetectorists;


import android.content.ContextWrapper;
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
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
        requireActivity().invalidateOptionsMenu();
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
            ContextWrapper cw = new ContextWrapper(requireActivity().getApplicationContext());
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

                    File[] files = subDir.listFiles((directory13, name) -> name.startsWith(prefix));

                    //listFiles returns in reverse alphabetical order, so we need to sort to get alphabetical
                    // so that photo order remains the same as when added.
                    Arrays.sort(files);

                    for (File file : files) {

                        counter++;
                        photoNames.add("Photo " + counter);
                        photoYears.add("");
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

                    File[] files2 = subDir.listFiles((directory12, name) -> name.startsWith(prefix2));

                    for (File file : files2) {

                        counter++;
                        photoNames.add("Photo " + counter);
                        photoYears.add("");
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

                        File[] files = subDir.listFiles((directory1, name) -> name.startsWith(prefix));

                        //listFiles returns in reverse alphabetical order, so we need to sort to get alphabetical
                        // so that photo order remains the same as when added.
                        Arrays.sort(files);

                        for (File file : files) {

                            counter++;
                            photoNames.add("Photo " + counter);
                            photoYears.add("");
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
            addPhotoButton.setText(R.string.AddAnotherPhoto);
            CustomGridViewAdapter adapterViewAndroid = new CustomGridViewAdapter(getActivity(), photoIds, photoNames, photoYears, photoBitmaps);
            gridView.setAdapter(adapterViewAndroid);
            gridView.setOnItemClickListener((parent, view12, i, id) -> showRemovePictureDialog(i));
        }

        addPhotoButton.setOnClickListener(view1 -> {
            //call a dialog box with choices of from gallery or from camera etc.
            startCropper();
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
                .start(getContext(), this);
    }

    private void showRemovePictureDialog(final int i) {
        final CharSequence[] items = {"Yes", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Remove Photo?");
        builder.setItems(items, (dialog, item) -> {
            if (items[item].equals("Yes")) {

                //Log.d("TEST", "The value of i is: "+i);

                photoNames.remove("Photo "+ i+1);
                photoYears.remove("");
                photoIds.remove(i);
                photoBitmaps.remove(i);

                //Log.d("TEST", "The photopath of file to be removed is: "+photoPaths.get(i));

                final String photoName = photoFilename.get(i);

                ContextWrapper cw = new ContextWrapper(requireActivity().getApplicationContext());
                // path to /data/data/yourapp/app_data/imageDir
                File directory = cw.getFilesDir();
                File subDir = new File(directory, "imageDir");
                boolean isSubDirCreated = subDir.exists();
                if (!isSubDirCreated)
                    isSubDirCreated = subDir.mkdir();
                if(isSubDirCreated)
                {
                    File [] files = subDir.listFiles((directory1, name) -> name.equals(photoName));

                    for (File file : files) {
                        //in process of editing treasure: rename photo to flag for deletion, but do not actually delete until edit is complete in case user cancels edit
                        if(!file.getName().startsWith("temp_"))
                        {
                            Log.d("TEST", "photo was not temp. file will be renamed instead: "+file.getPath());
                            //add edit_ chars to prefix string
                            String newName = "edit_"+file.getName();
                            File newFile = new File(subDir, newName);
                            //rename file from temp to permanent
                            file.renameTo(newFile);
                        }
                        //simply delete the temp image
                        else
                        {
                            Log.d("TEST", "file to be removed is: "+file.getPath());
                            //delete file that was saved in saveImage method
                            file.delete();
                        }
                        photoFilename.remove(i);
                    }

                    counter--;
                    if (counter == 0) {
                        addPhotoButton.setText(R.string.AddPhoto);
                        gridView.setAdapter(null);
                    }
                    else
                    {
                        CustomGridViewAdapter adapterViewAndroid = new CustomGridViewAdapter(getActivity(), photoIds, photoNames, photoYears, photoBitmaps);
                        gridView.setAdapter(adapterViewAndroid);
                        gridView.setOnItemClickListener((parent, view, i1, id) -> showRemovePictureDialog(i1));
                    }
                }
            } else if (items[item].equals("Cancel")) {
                dialog.dismiss();
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
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), resultUri);
                    String filename = saveImage(bitmap);
                    if (filename.equals("fail")) {
                        Toast.makeText(getActivity(), "Photo failed to save...", Toast.LENGTH_SHORT).show();
                    } else {
                        counter++;
                        if (counter == 1) {
                            addPhotoButton.setText(R.string.AddAnotherPhoto);
                        }
                        photoNames.add("Photo " + counter);
                        photoYears.add("");
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
                        gridView.setOnItemClickListener((parent, view, i, id) -> showRemovePictureDialog(i));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String saveImage(Bitmap myBitmap) {
        ContextWrapper cw = new ContextWrapper(requireActivity().getApplicationContext());
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
                ((AddActivity) requireActivity()).replaceFragments(AddCoinInfoFragment.class, bundle, "addInfo");
                break;
            case "token":
                ((AddActivity) requireActivity()).replaceFragments(AddTokenInfoFragment.class, bundle, "addInfo");
                break;
            case "jewelry":
                ((AddActivity) requireActivity()).replaceFragments(AddJewelryInfoFragment.class, bundle, "addInfo");
                break;
            case "relic":
                ((AddActivity) requireActivity()).replaceFragments(AddRelicInfoFragment.class, bundle, "addInfo");
                break;
            case "collection":
                ((AddActivity) requireActivity()).replaceFragments(AddFinalInfoFragment.class, bundle, "addFinal");
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

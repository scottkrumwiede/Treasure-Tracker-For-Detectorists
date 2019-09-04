package com.mdtt.scott.treasuretrackerfordetectorists;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.ImageView;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class AddPhotoFragment extends Fragment {

    private Button addPhotoButton;
    private ImageView imageview;
    //private static final String IMAGE_DIRECTORY = "/demonuts";
    //private int GALLERY = 1, CAMERA = 2;
    private GridView gridView;
    private String timeAtAdd;
    ArrayList<Integer> photoIds = new ArrayList<>();
    ArrayList<String> photoNames = new ArrayList<>();
    ArrayList<String> photoPaths = new ArrayList<>();
    ArrayList<String> photoYears = new ArrayList<>();
    ArrayList<String> photoFoundYears = new ArrayList<>();
    ArrayList<Bitmap> photoBitmaps = new ArrayList<>();
    private int counter = 0;
    String type;
    Bundle bundle;

    public AddPhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = getArguments().getString("type");
        }
        bundle = new Bundle();
        timeAtAdd = Long.toString(System.currentTimeMillis());
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().invalidateOptionsMenu();
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
        gridView = (GridView) view.findViewById(R.id.addtreasure_gridview);
        addPhotoButton = (Button) view.findViewById(R.id.addphoto_button);

        if (counter > 0) {
            CustomGridViewAdapter adapterViewAndroid = new CustomGridViewAdapter(getActivity(), photoIds, photoNames, photoYears, photoFoundYears, photoBitmaps);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Remove Photo?");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Yes")) {

                    Log.d("TEST", "The value of i is: "+i);

                    photoNames.remove("Photo "+ i+1);
                    photoYears.remove("");
                    photoFoundYears.remove("");
                    photoIds.remove(i);
                    photoBitmaps.remove(i);

                    Log.d("TEST", "The photopath of file to be removed is: "+photoPaths.get(i));

                    final String photoPath = photoPaths.get(i);

                    ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
                    File directory = cw.getDir(getResources().getString(R.string.imageDir), Context.MODE_PRIVATE);

                    File [] files = directory.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File directory, String name) {
                            return name.equals(photoPath);
                        }
                    });

                    for (File file : files) {

                        Log.d("TEST",""+file.getPath());
                        //delete file that was saved in saveImage method
                        file.delete();
                        photoPaths.remove(i);
                    }

                    counter--;
                    if (counter == 0) {
                        addPhotoButton.setText("ADD PHOTO");
                        gridView.setAdapter(null);
                    }
                    else
                    {
                        CustomGridViewAdapter adapterViewAndroid = new CustomGridViewAdapter(getActivity(), photoIds, photoNames, photoYears, photoFoundYears, photoBitmaps);
                        gridView.setAdapter(adapterViewAndroid);
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int i, long id) {
                                showRemovePictureDialog(i);
                            }
                        });
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
            if (resultCode == getActivity().RESULT_OK) {
                Uri resultUri = result.getUri();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                    String photoPath = saveImage(bitmap);
                    if(photoPath.equals("fail"))
                    {
                        Toast.makeText(getActivity(), "Photo failed to save...", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        counter++;
                        if (counter == 1) {
                            addPhotoButton.setText("ADD ANOTHER PHOTO");
                        }
                        photoNames.add("Photo " + counter);
                        photoYears.add("");
                        photoFoundYears.add("");
                        photoIds.add(counter);
                        photoPaths.add(photoPath);
                        photoBitmaps.add(bitmap);
                        //imageview.setImageBitmap(bitmap);
                        CustomGridViewAdapter adapterViewAndroid = new CustomGridViewAdapter(getActivity(), photoIds, photoNames, photoYears, photoFoundYears, photoBitmaps);
                        gridView.setAdapter(adapterViewAndroid);
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int i, long id) {
                                showRemovePictureDialog(i);
                            }
                        });

                        Log.d("TEST","File exists at this photoPath: "+photoPath+"\n"+result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getFilesDir();
        File subDir = new File(directory, "imageDir");
        if( !subDir.exists() )
            subDir.mkdir();
        // Create imageDir
        // File saved as unique temp image name 'temp_timeAtAdd_timeAtPhotoAdd.png'
        //temp prefix will be removed once treasure is officially added to database
        String filename = "temp_" + timeAtAdd + "_" + Long.toString(System.currentTimeMillis())+".jpeg";
        File path=new File(subDir,filename);

        Log.d("TEST","Path to save image at is:"+path.getPath());

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(path);
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);
        } catch (FileNotFoundException e) {
            return "fail";
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filename;
    }

    public void nextButtonClicked() {
        bundle.putString("timeAtAdd", timeAtAdd);
        bundle.putString("type", type);
        if(type.equals("coin"))
        {
            ((AddActivity) getActivity()).replaceFragments(AddCoinInfoFragment.class, bundle, "addInfo");
        }
        else if(type.equals("token"))
        {
            ((AddActivity) getActivity()).replaceFragments(AddTokenInfoFragment.class, bundle, "addInfo");
        }
        else if(type.equals("jewelry"))
        {
            ((AddActivity) getActivity()).replaceFragments(AddJewelryInfoFragment.class, bundle, "addInfo");
        }
        else if(type.equals("relic"))
        {
            ((AddActivity) getActivity()).replaceFragments(AddRelicInfoFragment.class, bundle, "addInfo");
        }
        else if(type.equals("collection"))
        {
            ((AddActivity) getActivity()).replaceFragments(AddFinalInfoFragment.class, bundle, "addFinal");
        }
    }
}

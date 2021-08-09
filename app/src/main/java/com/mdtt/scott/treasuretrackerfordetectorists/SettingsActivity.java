package com.mdtt.scott.treasuretrackerfordetectorists;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class SettingsActivity extends AppCompatActivity {
    private static BackgroundTask bt;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 200;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 201;
    private static Activity activity;
    private static LinearLayout mProgressBarLL;
    private static final int FILE_SELECT_CODE = 0;
    private static InputStream inputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment(getSupportFragmentManager()), "settings_main")
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mProgressBarLL = findViewById(R.id.progressbar_ll);
    }

    @Override
    public void onBackPressed() {
        if(bt != null) {
            //keeps user from hitting back while in middle of backup/export/restore
            if (bt.getStatus() != AsyncTask.Status.RUNNING) {
                super.onBackPressed();
            }
        }
        else
        {
                if (getSupportFragmentManager().findFragmentByTag("settings_fastclear") != null) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(getSupportFragmentManager().findFragmentByTag("settings_fastclear"))
                            .show(getSupportFragmentManager().findFragmentByTag("settings_main"))
                            .commit();
                }
                else
                {
                    super.onBackPressed();
                }
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        final FragmentManager sfm;
        public SettingsFragment(FragmentManager supportFragmentManager) {
            sfm = supportFragmentManager;
        }

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.main_settings, rootKey);
            activity = getActivity();

            Preference backupButton = getPreferenceManager().findPreference("backup");
            if (backupButton != null) {
                backupButton.setOnPreferenceClickListener(arg0 -> {
                    //Toast.makeText(getActivity(), "backup button is clicked",
                    // Toast.LENGTH_SHORT).show();

                    // Here, thisActivity is the current activity
                    if (ContextCompat.checkSelfPermission(requireActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Permission is not granted

                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                    } else {
                        bt = new BackgroundTask();
                        bt.execute("backup");
                        mProgressBarLL.setVisibility(View.VISIBLE);
                    }

                    return true;
                });
            }

            Preference restoreButton = getPreferenceManager().findPreference("restore");
            if (restoreButton != null) {
                restoreButton.setOnPreferenceClickListener(arg0 -> {
                    if (ContextCompat.checkSelfPermission(requireActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Permission is not granted

                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                    } else {
                        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        new AlertDialog.Builder(requireContext())
                                .setMessage("WARNING: Restoring from a backup file will PERMANENTLY DELETE AND REPLACE all your current treasures. Are you sure?")
                                .setNegativeButton(R.string.cancel, null)
                                .setPositiveButton(R.string.restore, (arg01, arg1) -> {
                                    try {
                                        activity.startActivityForResult(
                                                Intent.createChooser(intent, "Select a File to Upload"),
                                                FILE_SELECT_CODE);
                                    } catch (android.content.ActivityNotFoundException ex) {
                                        // Potentially direct the user to the Market with a Dialog
                                        Toast.makeText(activity, "Please install a File Manager.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }).create().show();
                    }
                    return true;
                });
            }

            Preference exportButton = getPreferenceManager().findPreference("export");
            if (exportButton != null) {
                exportButton.setOnPreferenceClickListener(arg0 -> {
                    //Toast.makeText(getActivity(), "export button is clicked",
                    // Toast.LENGTH_SHORT).show();

                    // Here, thisActivity is the current activity
                    if (ContextCompat.checkSelfPermission(requireActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {

                        // Permission is not granted

                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                    } else {
                        bt = new BackgroundTask();
                        bt.execute("export");
                        mProgressBarLL.setVisibility(View.VISIBLE);
                    }

                    return true;
                });
            }

            Preference fastClearButton = getPreferenceManager().findPreference("fastclear");
            if (fastClearButton != null) {
                final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                fastClearButton.setOnPreferenceClickListener(arg0 -> {
                    sfm.beginTransaction()
                            .hide(sfm.findFragmentByTag("settings_main"))
                            .add(R.id.settings, new SettingsFastClearFragment(), "settings_fastclear")
                            .commit();
                    return true;
                });
            }



            Preference versionButton = getPreferenceManager().findPreference("versionName");
            String versionName = BuildConfig.VERSION_NAME;
            versionButton.setSummary(versionName);
            //versionButton.setSelectable(false);

            Preference aboutButton = getPreferenceManager().findPreference("aboutApp");
            if (aboutButton != null) {
                final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                aboutButton.setOnPreferenceClickListener(arg0 -> {
                    new AlertDialog.Builder(requireContext())
                            .setMessage(R.string.aboutapp_info_statement)
                            .create().show();
                    return true;
                });
            }
        }
    }

    public static class SettingsFastClearFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.fastclear_settings, rootKey);
            activity = getActivity();

            Preference clearcoinsButton = getPreferenceManager().findPreference("clearcoins");
            if (clearcoinsButton != null) {
                clearcoinsButton.setOnPreferenceClickListener(arg0 -> {

                    fastClear("Coins");
                    return true;
                });
            }
            Preference cleartokensButton = getPreferenceManager().findPreference("cleartokens");
            if (cleartokensButton != null) {
                cleartokensButton.setOnPreferenceClickListener(arg0 -> {

                    fastClear("Tokens");
                    return true;
                });
            }
            Preference clearjewelryButton = getPreferenceManager().findPreference("clearjewelry");
            if (clearjewelryButton != null) {
                clearjewelryButton.setOnPreferenceClickListener(arg0 -> {

                    fastClear("Jewelry");
                    return true;
                });
            }
            Preference clearrelicsButton = getPreferenceManager().findPreference("clearrelics");
            if (clearrelicsButton != null) {
                clearrelicsButton.setOnPreferenceClickListener(arg0 -> {

                    fastClear("Relics");
                    return true;
                });
            }
            Preference clearcladButton = getPreferenceManager().findPreference("clearclad");
            if (clearcladButton != null) {
                clearcladButton.setOnPreferenceClickListener(arg0 -> {

                    fastClear("Clad");
                    return true;
                });
            }
            Preference clearcollectionsButton = getPreferenceManager().findPreference("clearcollections");
            if (clearcollectionsButton != null) {
                clearcollectionsButton.setOnPreferenceClickListener(arg0 -> {

                    fastClear("Collections");
                    return true;
                });
            }
            Preference clearallButton = getPreferenceManager().findPreference("clearall");
            if (clearallButton != null) {
                clearallButton.setOnPreferenceClickListener(arg0 -> {

                    fastClear("All");
                    return true;
                });
            }
        }
    }

    private static void fastClear(String type)
    {
        String message = "Are you sure you want to PERMANENTLY DELETE all added "+type+"? These items cannot be recovered without a backup file!";
        if(type.equals("All"))
        {
            message = "Are you sure you want to PERMANENTLY DELETE all added in above categories? (Coins, Tokens, Jewelry, Relics, Clad and Collections) These items cannot be recovered without a backup file!";
        }
        new AlertDialog.Builder(activity)
                .setTitle("Confirm?")
                .setMessage(message)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, (arg0, arg1) -> {
                    bt = new BackgroundTask();
                    bt.execute("fastClear", type);
                    mProgressBarLL.setVisibility(View.VISIBLE);
                }).create().show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,  int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted, yay! Do the
                // task you need to do.
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    private static String performBackup() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String backupPath = "Backup-"+currentDateandTime+".file";

        File dataDir = Environment.getDataDirectory();
        String dbPath = "/data/" + "com.mdtt.scott.treasuretrackerfordetectorists" + "/databases/";
        String filesPath = "/data/" + "com.mdtt.scott.treasuretrackerfordetectorists" + "/files/";
        File dbDir = new File(dataDir, dbPath);
        File filesDir = new File(dataDir, filesPath);

        File backupFile = new File(downloadDir, backupPath);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(backupFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        ZipOutputStream zos = new ZipOutputStream(fos);

        //handles writing the database to the zip file
        writeToZip(zos, dbDir);

        //handles writing the images to the zip file
        writeToZip(zos, filesDir);

        try {
            zos.close();
            return backupFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String performRestore() {

        try {
            return unzipFile(inputStream);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_SELECT_CODE) {
            if (resultCode == RESULT_OK) {
                // Get the Uri of the selected file

                Uri uri = data.getData();
                try {
                    inputStream = getContentResolver().openInputStream(Objects.requireNonNull(uri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                    bt = new BackgroundTask();
                    bt.execute("restore");
                    mProgressBarLL.setVisibility(View.VISIBLE);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private static String unzipFile(InputStream inputStream) throws IOException {
        File dataDir = Environment.getDataDirectory();
        String dbPath = "/data/" + "com.mdtt.scott.treasuretrackerfordetectorists" + "/databases/";
        String filesPath = "/data/" + "com.mdtt.scott.treasuretrackerfordetectorists" + "/files/";
        File filesDir = new File(dataDir, filesPath);
        File dbDir = new File(dataDir, dbPath);
        byte[] buffer = new byte[1024];
        ZipInputStream zis = new ZipInputStream(inputStream);
        ZipEntry zipEntry = zis.getNextEntry();

        if (zipEntry != null) {
            if (!zipEntry.getName().contains("androidx.work.workdb") && !zipEntry.getName().contains("findsDB") && !zipEntry.getName().contains("findsDB-journal") && !zipEntry.getName().contains("google_app")) {
                return null;
            }
            else
            {
                File imageDir = new File(filesDir, "/imageDir/");
                //Log.d("mytag,","Imagedir: "+imageDir.getPath());


                //cleanup any old images
                for(File file : imageDir.listFiles())
                {
                    if(file.isFile())
                    {
                        //Log.d("mytag","Deleting file: "+file.getPath());
                        file.delete();
                    }
                }

                while (zipEntry != null) {
                    //Log.d("mytag", "zipentry name: "+zipEntry.getName());
                    //db files
                    if(zipEntry.getName().contains("findsDB")||zipEntry.getName().contains("findsDB-journal")||zipEntry.getName().contains("google_app"))
                    {
                        File newFile;
                        //old format
                        if(zipEntry.getName().contains("/data/data"))
                        {
                            //Log.d("mytag","old format");
                            newFile = new File(zipEntry.getName());
                        }
                        //current format
                        else
                        {
                            //Log.d("mytag","new format");
                            newFile = new File(dbDir, zipEntry.getName());
                        }

                        FileOutputStream fos = new FileOutputStream(newFile);
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                        fos.close();
                        zipEntry = zis.getNextEntry();
                    }
                    //images
                    else
                    {
                        File newFile;
                        //old format
                        if(zipEntry.getName().contains("/data/data"))
                        {
                            //Log.d("mytag","old format");
                            newFile = new File(zipEntry.getName());
                        }
                        //current format
                        else
                        {
                            //Log.d("mytag", "zipentry name: "+zipEntry.getName());
                            String ze = zipEntry.getName();
                            ze = ze.replace("images","imageDir");
                            //Log.d("mytag",ze);
                            newFile = new File(filesDir, "/"+ze);
                        }




                        FileOutputStream fos = new FileOutputStream(newFile);
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                        fos.close();
                        zipEntry = zis.getNextEntry();
                    }

                }
                zis.closeEntry();
                zis.close();
                return "Restore complete.";
            }
        }
        zis.closeEntry();
        zis.close();
        return null;


    }

    private static String performExport() {

        final MySQliteHelper dbHelper = new MySQliteHelper(activity.getApplicationContext());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        //directory where export file will be saved on the user's phone
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //name of the export file that will be created for the user
        String exportPath = "Export-"+currentDateandTime+".zip";

        File dataDir = Environment.getDataDirectory();
        //String filesPath = "/files/";
        String filesPath = "/data/" + "com.mdtt.scott.treasuretrackerfordetectorists" + "/files/";
        File filesDir = new File(dataDir, filesPath);
        //cleanup any old export .csv files
        for(File file : filesDir.listFiles())
        {
            if(file.isFile())
            {
                file.delete();
            }
        }

        //create csv files from database tables
        File csvTreasureFile = new File(activity.getApplicationContext().getFilesDir(), "Export-Treasures-"+currentDateandTime+".csv");
        File csvCladFile = new File(activity.getApplicationContext().getFilesDir(), "Export-Clad-"+currentDateandTime+".csv");


        try {
            //write treasure table to CSV file
            CSVWriter csvWrite = new CSVWriter(new FileWriter(csvTreasureFile));
            Cursor curCSV = dbHelper.raw("treasure");
            //write column headers
            csvWrite.writeNext(curCSV.getColumnNames());
            if(curCSV != null)
            {
                //write each row
                while(curCSV.moveToNext()) {
                    String[] mySecondStringArray = new String[curCSV.getColumnNames().length];
                    for(int i=0;i<curCSV.getColumnNames().length;i++)
                    {
                        mySecondStringArray[i] =curCSV.getString(i);
                    }
                    csvWrite.writeNext(mySecondStringArray);
                }
                curCSV.close();
                csvWrite.close();
            }

            //write clad table to CSV file
            csvWrite = new CSVWriter(new FileWriter(csvCladFile));
            curCSV = dbHelper.raw("clad");
            //write column headers
            csvWrite.writeNext(curCSV.getColumnNames());
            if(curCSV != null)
            {
                //write each row
                while(curCSV.moveToNext()) {
                    String[] mySecondStringArray = new String[curCSV.getColumnNames().length];
                    for(int i=0;i<curCSV.getColumnNames().length;i++)
                    {
                        mySecondStringArray[i] =curCSV.getString(i);
                    }
                    csvWrite.writeNext(mySecondStringArray);
                }
                curCSV.close();
                csvWrite.close();
            }

            dbHelper.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        //file that will be created in Downloads folder
        File exportFile = new File(downloadDir, exportPath);

        FileOutputStream fos;
        try {
            fos = new FileOutputStream(exportFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        ZipOutputStream zos = new ZipOutputStream(fos);

        //handles writing the csv and images to the zip file
        writeToZip(zos, filesDir);

        try {
            zos.close();
            csvTreasureFile.delete();
            csvCladFile.delete();
            return exportFile.getAbsolutePath();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void writeToZip(ZipOutputStream zos, File dir) {

        try {
            for (File file : dir.listFiles()) {
                //recursively explores a subdir
                if(file.isDirectory())
                {
                    //Log.d("mytag", "Directory found: "+file.getPath());
                    writeToZip(zos, file);
                    continue;
                }

                //Log.d("mytag", "\nCurrent file to be zipped: "+file.getPath());

                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(file.getPath());
                try {
                    if(file.getPath().contains("imageDir"))
                    {
                        zos.putNextEntry(new ZipEntry("/images/"+file.getName()));
                    }
                    else
                    {
                        zos.putNextEntry(new ZipEntry(file.getName()));
                    }

                    int length;

                    while ((length = fis.read(buffer)) > 0) {
                        //Log.d("mytag", "Length of current buffer: " + length);
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }



    private static class BackgroundTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String...params) {
            String result = "";
            switch (params[0]) {
                case "backup":
                    result = "Backup created: " + performBackup();
                    break;
                case "restore":
                    result = performRestore();
                    break;
                case "export":
                    result = "Export created: " + performExport();
                    break;
                case "fastClear":
                    //Log.d("mytag", "Type: "+params[1]);
                    MySQliteHelper helper = new MySQliteHelper(activity);
                    ArrayList<String> photoPathsOfRemovedList = helper.deleteAllOfType(params[1]);

                    if(photoPathsOfRemovedList != null)
                    {
                        //path to subDir: /data/user/0/com.mdtt.scott.treasuretrackerfordetectorists/files/imageDir
                        File directory = Objects.requireNonNull(activity.getFilesDir());
                        File subDir = new File(directory, "imageDir");
                        boolean isSubDirCreated = subDir.exists();
                        if (!isSubDirCreated)
                            isSubDirCreated = subDir.mkdir();

                        if(isSubDirCreated)
                        {
                            for(String photoPath : photoPathsOfRemovedList)
                            {
                                final String prefix = photoPath;

                                File [] files = subDir.listFiles((directory1, name) -> name.startsWith(prefix));

                                for (File file : files) {
                                    Log.d("TEST", "Deleting file at path: "+file.getPath());
                                    file.delete();
                                }
                            }
                        }
                    }
                    //result grammar formatting based on type
                    if(params[1].equals("Clad") || params[1].equals("Jewelry"))
                    {
                        result = "All "+params[1]+" has been fast cleared!";
                    }
                    else if(params[1].equals("All"))
                    {
                        result = "All categories have been fast cleared!";
                    }
                    else
                    {
                        result = "All "+params[1]+" have been fast cleared!";
                    }
            }
            return result;
        }

        protected void onPostExecute(String result) {
            mProgressBarLL.setVisibility(View.GONE);
            if(result != null)
            {
                //Toast.makeText(activity, ""+result,
                //   Toast.LENGTH_LONG).show();
                new AlertDialog.Builder(Objects.requireNonNull(activity))
                        .setTitle("Success")
                        .setMessage(result)
                        .setPositiveButton(android.R.string.ok, (arg0, arg1) -> {

                        }).create().show();
            }
            else
            {
                new AlertDialog.Builder(Objects.requireNonNull(activity))
                        .setTitle("Incorrect file format")
                        .setMessage("Please select a proper backup file 'Backup-YYYY_MM_DD_HHMMSS.file'")
                        .setPositiveButton(android.R.string.ok, (arg0, arg1) -> {

                        }).create().show();
            }
            //allows for proper onBackPressed
            bt = null;
        }
    }
}
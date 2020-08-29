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
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class SettingsActivity extends AppCompatActivity {
    private static BackgroundTask bt;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 200;
    private static Activity activity;
    private static LinearLayout mProgressBarLL;
    private static final int FILE_SELECT_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mProgressBarLL = findViewById(R.id.progressbar_ll);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            activity = getActivity();

            Preference backupButton = getPreferenceManager().findPreference("backup");
            if (backupButton != null) {
                backupButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) {
                        //Toast.makeText(getActivity(), "backup button is clicked",
                        // Toast.LENGTH_SHORT).show();

                        // Here, thisActivity is the current activity
                        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
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
                    }
                });
            }

            Preference restoreButton = getPreferenceManager().findPreference("restore");
            if (restoreButton != null) {
                restoreButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) {
                        //Toast.makeText(getActivity(), "Feature coming soon!",
                                //Toast.LENGTH_SHORT).show();
                        performRestore();
                        return true;
                    }
                });
            }

            Preference exportButton = getPreferenceManager().findPreference("export");
            if (exportButton != null) {
                exportButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) {
                        //Toast.makeText(getActivity(), "export button is clicked",
                        // Toast.LENGTH_SHORT).show();

                        // Here, thisActivity is the current activity
                        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()),
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
                    }
                });
            }

            Preference clearcoinsButton = getPreferenceManager().findPreference("clearcoins");
            if (clearcoinsButton != null) {
                clearcoinsButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) {

                        Toast.makeText(getActivity(), "Feature coming soon!",
                              Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
            Preference cleartokensButton = getPreferenceManager().findPreference("cleartokens");
            if (cleartokensButton != null) {
                cleartokensButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) {
                        Toast.makeText(getActivity(), "Feature coming soon!",
                                Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
            Preference clearjewelryButton = getPreferenceManager().findPreference("clearjewelry");
            if (clearjewelryButton != null) {
                clearjewelryButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) {
                        Toast.makeText(getActivity(), "Feature coming soon!",
                                Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
            Preference clearrelicsButton = getPreferenceManager().findPreference("clearrelics");
            if (clearrelicsButton != null) {
                clearrelicsButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) {
                        Toast.makeText(getActivity(), "Feature coming soon!",
                                Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
            Preference clearcladButton = getPreferenceManager().findPreference("clearclad");
            if (clearcladButton != null) {
                clearcladButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) {
                        Toast.makeText(getActivity(), "Feature coming soon!",
                                Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
            Preference clearcollectionsButton = getPreferenceManager().findPreference("clearcollections");
            if (clearcollectionsButton != null) {
                clearcollectionsButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference arg0) {
                        Toast.makeText(getActivity(), "Feature coming soon!",
                                Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });
            }
        }
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

            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            try {
                activity.startActivityForResult(
                        Intent.createChooser(intent, "Select a File to Upload"),
                        FILE_SELECT_CODE);
            } catch (android.content.ActivityNotFoundException ex) {
                // Potentially direct the user to the Market with a Dialog
                Toast.makeText(activity, "Please install a File Manager.",
                        Toast.LENGTH_SHORT).show();
            }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FILE_SELECT_CODE) {
            if (resultCode == RESULT_OK) {
                // Get the Uri of the selected file
                InputStream inputStream = null;
                Uri uri = data.getData();
                try {
                    inputStream = getContentResolver().openInputStream(Objects.requireNonNull(uri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                int fileExtensionIndex = Objects.requireNonNull(uri.getPath()).lastIndexOf(".");
                String fileExtension = uri.getPath().substring(fileExtensionIndex);

                Log.d("mytag", uri.getScheme());


                //Example: .file
                Log.d("myTag", "" + fileExtension);

                if (fileExtension.equalsIgnoreCase(".file")) {
                    //TODO: //User has provider a .file
                    //now check to see if it is really a legitimate backup.file by checking to see if it contains both a dbDir and filesDir
                    //File backupFile = new File(uri.getPath());
                    //readFromZip(backupFile);
                    try {
                        readFromZip(inputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //TODO: //user did not provide a proper backup file
                    //alert them to try again.

                } else if (fileExtension.equalsIgnoreCase(".zip")) {
                    try {
                        readFromZip(inputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //TODO: //user did not provide a proper backup file
                    //alert them to try again.
                }

                //Example: /document/raw:/storage/emulated/0/Download/Backup-2020_02_07_124302.file
                Log.d("myTag", uri.toString());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //write to, write from
    private void readFromZip(InputStream inputStream) throws IOException {

        ZipInputStream zis = new ZipInputStream(inputStream);
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            System.out.println(entry.getName());
            byte[] contents = new byte[4096];
            int direct;
            while ((direct = zis.read(contents, 0, contents.length)) >= 0) {
                Log.d("mytag", ""+direct);
            }
            zis.closeEntry();
        }


    }

    private static String performExport() {

        final MySQliteHelper dbHelper = new MySQliteHelper(activity.getApplicationContext());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HHmmss", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        //directory where export file will be saved on the user's phone
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //name of the export file that will be created for the user
        String exportPath = "Export-"+currentDateandTime+".zip";

        //create csv file from database tables
        File csvFile = new File(activity.getApplicationContext().getFilesDir(), "Export-"+currentDateandTime+".csv");

        try {
            CSVWriter csvWrite = new CSVWriter(new FileWriter(csvFile));
            Cursor curCSV = dbHelper.raw();
            csvWrite.writeNext(curCSV.getColumnNames());
            while(curCSV.moveToNext()) {
                String[] mySecondStringArray = new String[curCSV.getColumnNames().length];
                for(int i=0;i<curCSV.getColumnNames().length;i++)
                {
                    mySecondStringArray[i] =curCSV.getString(i);
                }
                csvWrite.writeNext(mySecondStringArray);
            }
            csvWrite.close();
            curCSV.close();
            dbHelper.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        File dataDir = Environment.getDataDirectory();
        String filesPath = "/data/" + "com.mdtt.scott.treasuretrackerfordetectorists" + "/files/";
        File filesDir = new File(dataDir, filesPath);

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
            boolean iscsvFileDeleted = csvFile.delete();
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
                    writeToZip(zos, file);
                    continue;
                }
                Log.d("mytag", "\nCurrent file to be zipped: "+file.getPath());
                byte[] buffer = new byte[1024];
                FileInputStream fis = new FileInputStream(file.getPath());
                try {
                    zos.putNextEntry(new ZipEntry(file.getPath()));
                    int length;

                    while ((length = fis.read(buffer)) > 0) {
                        Log.d("mytag", "Length of current buffer: " + length);
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
                        .setTitle("Success!")
                        .setMessage(result)
                        .setNegativeButton(android.R.string.ok, null)
                        .create().show();
            }
        }
    }
}
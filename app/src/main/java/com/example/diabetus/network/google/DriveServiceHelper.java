package com.example.diabetus.network.google;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DriveServiceHelper {
    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private final Drive mDriveService;

    public DriveServiceHelper(Drive driveService) {
        mDriveService = driveService;
    }

    public Task<String> UploadDatabase(String filePath, String folderId) {
        return Tasks.call(mExecutor, () -> {
            File myFile = null;

            try {
                java.io.File file = new java.io.File(filePath);
                FileContent fileContent = new FileContent("application/database", file);
                File fileMetaData = new File();
                fileMetaData.setName(file.getName());

                /* Check if exists */
                FileList result = mDriveService.files().list()
                        .setQ("name='" + file.getName() + "' and '" + folderId + "' in parents")
                        .execute();

                if (result.getFiles().size() == 0) {
                    fileMetaData.setParents(Collections.singletonList(folderId));
                    myFile = mDriveService.files().create(fileMetaData, fileContent).execute();
                }
                else
                {
                    myFile = result.getFiles().get(0);
                    mDriveService.files().update(myFile.getId(), fileMetaData, fileContent).execute();
                }
            } catch (GoogleJsonResponseException e) {
                System.err.println("Unable to upload file: " + e.getDetails());
            }

            return (myFile == null) ? "" : myFile.getId();
        });
    }

    public Task<String> createFolder(String name) {
        return Tasks.call(mExecutor, () -> {
            File myFile = null;
            FileList result = mDriveService.files().list()
                    .setQ("name='Diabetus'")
                    .execute();

            /* Create folder if does not exist */
            if (result.getFiles().size() == 0) {
                File fileMetaData = new File();
                fileMetaData.setName(name);
                fileMetaData.setMimeType("application/vnd.google-apps.folder");

                try {
                    myFile = mDriveService.files().create(fileMetaData)
                            .setFields("id")
                            .execute();
                } catch (GoogleJsonResponseException e) {
                    System.err.println("Unable to create folder: " + e.getDetails());
                }
            }
            else {
                myFile = result.getFiles().get(0);
            }

            return (myFile == null) ? "" : myFile.getId();
        });
    }
}

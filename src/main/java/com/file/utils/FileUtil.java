package com.file.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @description: file util
 * @author: FM
 * @create: 2019-01-16 14:29:11
 */
public class FileUtil {

    /**
     * zip file list into one file
     * @param srcFiles
     * @param zipFile
     * @throws IOException
     */
    public static void zipFiles(List<File> srcFiles, File zipFile) throws IOException {
        if (!zipFile.exists()) {
            zipFile.createNewFile();
        }
        FileOutputStream fileOutputStream = null;
        ZipOutputStream zipOutputStream = null;
        FileInputStream fileInputStream = null;
        fileOutputStream = new FileOutputStream(zipFile);
        zipOutputStream = new ZipOutputStream(fileOutputStream);
        ZipEntry zipEntry = null;
        try {
            for (int i = 0; i < srcFiles.size(); i++) {
                File srcFile = srcFiles.get(i);
                fileInputStream = new FileInputStream(srcFile);
                zipEntry = new ZipEntry(srcFile.getName());
                zipOutputStream.putNextEntry(zipEntry);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = fileInputStream.read(buffer)) > 0) {
                    zipOutputStream.write(buffer, 0, len);
                }
                srcFile.delete();
            }
        } finally {
            zipOutputStream.closeEntry();
            zipOutputStream.close();
            fileInputStream.close();
            fileOutputStream.close();
        }
    }
}

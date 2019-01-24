package com.file.utils;

import com.file.constants.StringPool;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @description: pdf util
 * @author: FM
 * @create: 2019-01-14 12:17:22
 */
public class PdfUtil {

    private static final String CHARACTER_ENCODING = "UTF-8";
    public static final String PDF_SUFFIX = ".pdf";
    public static final String ZIP_SUFFIX = ".zip";

    /**
     * split pdf
     *
     * @param srcFile
     * @param response
     * @throws IOException
     */
    public static void splitPdf(File srcFile, HttpServletResponse response,String tempDirectory) throws IOException {
        if (srcFile == null || response == null) {
            return;
        }

        System.out.println("+++++++++++++++++file name :" + srcFile.getName());

        // Creating PDF document object
        OutputStream outputStream = null;
        PDDocument document = new PDDocument();
        List<PDDocument> pages = null;
        //random string
        String uuid = UUID.randomUUID().toString();
        List<File> desFiles = new ArrayList<File>();
        File zipFile = null;
        try {
            document = PDDocument.load(srcFile);
            // Instantiating Splitter class
            Splitter splitter = new Splitter();
            // splitting the pages of a PDF document
            pages = splitter.split(document);
            // Creating an iterator
            Iterator<PDDocument> iterator = pages.listIterator();
            // Saving each page as an individual document
            int i = 1;
            File desFile;
            while (iterator.hasNext()) {
                PDDocument pd = iterator.next();
                desFile = new File(uuid + StringPool.UNDERLINE + i++ + PDF_SUFFIX);
                pd.save(desFile);
                desFiles.add(desFile);
            }
            zipFile = new File(tempDirectory + "temp" + ZIP_SUFFIX);
            //zip the file list into one file
            FileUtil.zipFiles(desFiles, zipFile);
            response.setCharacterEncoding(CHARACTER_ENCODING);
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=".concat(uuid).concat(ZIP_SUFFIX));
            outputStream = response.getOutputStream();
            //download the file
            outputStream.write(FileUtils.readFileToByteArray(zipFile));
        } finally {
            if (document != null) {
                document.close();
            }
            for (int i = 0; pages != null && i < pages.size(); i++) {
                PDDocument doc = pages.get(i);
                doc.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            srcFile.delete();
            if (zipFile != null) {
                zipFile.delete();
            }
        }
    }
}

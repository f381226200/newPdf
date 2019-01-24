package com.file.service;

import com.file.utils.PdfUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @description: file service
 * @author: FM
 * @create: 2019-01-14 12:54:05
 */
@Service
public class FileService {

    private Logger log = LogManager.getLogger(FileService.class);

    @Value("${tempDirectory}")
    private String tempDirectory;

    /**
     * get pdf file and split it
     *
     * @param multipartFile
     * @param response
     * @return
     */
    public Boolean handlePdf(MultipartFile multipartFile, HttpServletResponse response) {
        InputStream inputStream = null;
        try {
            File file = new File(tempDirectory.concat("temp").concat(PdfUtil.PDF_SUFFIX));
            inputStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(inputStream, file);
            PdfUtil.splitPdf(file, response, tempDirectory);
            return true;
        } catch (IOException e) {
            log.warn("io exception:" + e);
        } catch (Exception e) {
            log.warn("excetion:" + e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.warn("io exception:" + e);
            }
        }
        return false;
    }

    /**
     * validate the request parameters
     *
     * @param file
     * @return
     */
    public String validateRequest(MultipartFile file) {
        String msg = null;
        if (file == null || StringUtils.isBlank(file.getOriginalFilename())) {
            msg = "file is empty!";
        }
        return msg;
    }

}

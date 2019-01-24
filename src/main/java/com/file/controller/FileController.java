package com.file.controller;

import com.file.service.FileService;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * @description: file controller
 * @author: FM
 * @create: 2019-01-14 12:19:12
 */
@RestController
@RequestMapping()
public class FileController {

    private Log log = LogFactory.getLog(FileController.class);

    @Autowired
    private FileService fileService;

    /**
     * upload and split pdf file
     *
     * @param file
     * @param response
     * @return
     */
    @RequestMapping("/splitPdf")
    public String splitPdf(@RequestParam(name = "file", required = false) MultipartFile file,
                           HttpServletResponse response) {
        log.info("split start!");
        String msg = fileService.validateRequest(file);
        if (StringUtils.isNotBlank(msg)) {
            return msg;
        }
        Boolean flag = fileService.handlePdf(file, response);
        if (BooleanUtils.isTrue(flag)) {
            msg = "success!!!";
        } else {
            msg = "split failure";
        }
        return msg;
    }

}

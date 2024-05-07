package kr.co.felici.remembering.controller;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.felici.remembering.service.UtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;

/**
 * author: felici
 */
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/util")
public class ExcelController {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UtilService utilService;

    String absolutePath = new File("").getAbsolutePath() + File.separator;

    @GetMapping("/album/upload-form")
    public String showUploadForm() {
        return "util/upload_data_to_db.html";
    }

    @PostMapping("/album/upload-data")
    public String uploadDataToDb(@RequestParam(value = "uploadFile") MultipartFile multipartFile,
                                 MultipartHttpServletRequest request) throws IOException, InvalidFormatException {

//        String excelType = request.getParameter("excelType");
        String excelType = "xlsx";

        // 엑셀 파일이 xls, xlsx일 때 서비스 라우팅
        if (excelType.equals("xlsx")) {
            utilService.uploadFileToDb(multipartFile);
        } else if (excelType.equals("xls")) {
            utilService.uploadFileToDb(multipartFile);
        } else {

        }



        return "/home";
    }



}

package kr.co.felici.remembering.controller;

import kr.co.felici.remembering.service.UtilService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/util")
public class ExcelController {

    private final UtilService utilService;

    @GetMapping("/album/upload-form")
    public String showUploadForm() {
        return "util/upload_data_to_db";
    }

    @PostMapping("/album/upload-data")
    public String uploadDataToDb(@RequestParam("uploadFile") MultipartFile multipartFile)
            throws IOException, InvalidFormatException {
        utilService.uploadFileToDb(multipartFile);
        return "redirect:/";
    }
}
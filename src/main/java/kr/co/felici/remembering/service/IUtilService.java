package kr.co.felici.remembering.service;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * author: felici
 */
public interface IUtilService {
    public void uploadFileToDb(MultipartFile multipartFile) throws IOException, InvalidFormatException;
}

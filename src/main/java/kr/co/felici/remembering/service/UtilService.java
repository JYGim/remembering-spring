package kr.co.felici.remembering.service;

import kr.co.felici.remembering.domain.AlbumPhoto;
import kr.co.felici.remembering.dto.AlbumDto;
import kr.co.felici.remembering.repository.AlbumPhotoRepository;
import kr.co.felici.remembering.repository.MemorialPostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * author: felici
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UtilService implements IUtilService {

    private final AlbumPhotoRepository albumPhotoRepository;

    @Override
    @Transactional
    public void uploadFileToDb(MultipartFile multipartFile) throws IOException, InvalidFormatException {
        log.debug("############### 엑셀 등록 서비스 로직 시작 ###############");
        log.debug("############### Controller에서 넘어온 값 체크 : {} ###############", multipartFile);

        List<AlbumDto> list = new ArrayList<>();

        OPCPackage opcPackage = OPCPackage.open(multipartFile.getInputStream());
        XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);

        // 첫 번째 시트를 불러온다.
        XSSFSheet sheet = workbook.getSheet("photo");
//        XSSFSheet sheet = workbook.getSheetAt(1);

        int lastRowNum = sheet.getLastRowNum();
        log.info("log, lastRowNum: " + lastRowNum);

        // i는 몇 번째 행 부터 체크를 할 것인지 정한다.
        for (int i=2; i < lastRowNum+1; i++) {
            log.info("log, 반복 횟수: " + i);
            AlbumDto albumDto = new AlbumDto();

            XSSFRow row = sheet.getRow(i);

            // 행이 존재하지 않으면 패스한다.
//            if (row == null) {
//                continue;
//            }

            // 행의 첫 번째 열(이름)
            XSSFCell cell = row.getCell(0);
            if (cell != null) {



                if (cell.getCellType() == CellType.NUMERIC) {
                    log.info("log, 셀 타입: " + cell.getCellType());
                    if(((int) cell.getNumericCellValue()) == -1) {
                        return;
                    }
                    albumDto.setTitle(String.valueOf((int) cell.getNumericCellValue()));
                    log.info("albumDto.getTitle: " + albumDto.getTitle());

                } else {
                    log.info("log, 셀 타입: " + cell.getCellType());
                    albumDto.setTitle(cell.getStringCellValue());
                }


            }

            // 행의 두 번째, 세번째, 네번째 열(날짜)
            // 네번째 열(촬영일 특정 여부)이 'y'면 촬영일이 저장되고 'N'이면 추정 연도가 저장된다.
            cell = row.getCell(3);

            if (cell != null) {
                if (cell.getCellType() == CellType.NUMERIC) {
                    log.info("log, 셀 타입: " + cell.getCellType());

                    String cellVal = String.valueOf(cell.getNumericCellValue());

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

                    if(cellVal.equals("Y")) {
                        String yearCell = String.valueOf((int)(row.getCell(1).getNumericCellValue()));
                        String dateCell = String.valueOf(row.getCell(2).getStringCellValue());
                        log.info("info, cell.getCellType(), yearCell: " + cell.getCellType());

                        log.info("info, yearCell: " + yearCell);
                        log.info("info, dateCell: " + dateCell);
                        LocalDate localDate = LocalDate.parse(yearCell+dateCell, formatter);
                        albumDto.setTook_in(localDate);
                    } else if(cellVal.equals("") | cellVal == null |
                            cellVal.equals("N")) {
                        if(row.getCell(4) != null | !row.getCell(4).getStringCellValue().equals("")
                        ) {
                            albumDto.setEstimated_year(row.getCell(4).getStringCellValue());
                            albumDto.setIs_WhenPictureWasTaken_clear("N");
                        }
                    } else if (cellVal == null) {
                        albumDto.setIs_WhenPictureWasTaken_clear("N");
                    }


                } else if(cell.getCellType() == CellType.STRING){
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

                    if(cell.getStringCellValue().equals("Y")) {
                        log.info("info, row.getCell(1).getCellType(), yearCell: " + row.getCell(1).getCellType());

                        String yearCell = String.valueOf((int)row.getCell(1).getNumericCellValue());

                        log.info("info, row.getCell(2).getCellType(), dateCell: " + row.getCell(2).getCellType());

                        String dateCell = String.valueOf(row.getCell(2).getStringCellValue());
                        log.info("info, cell.getCellType(), yearCell: " + cell.getCellType());
                        log.info("info, yearCell: " + yearCell);
                        log.info("info, dateCell: " + dateCell);
                        LocalDate localDate = LocalDate.parse(yearCell+dateCell, formatter);
                        albumDto.setTook_in(localDate);
                    } else if(cell.getStringCellValue().equals("") | cell.getStringCellValue() == null |
                            cell.getStringCellValue().equals("N")) {
                        if(row.getCell(4) != null | !row.getCell(4).getStringCellValue().equals("")
                        ) {
                            albumDto.setEstimated_year(row.getCell(4).getStringCellValue());
                            albumDto.setIs_WhenPictureWasTaken_clear("N");
                        }
                    } else if (row.getCell(3).getStringCellValue() == null) {
                        albumDto.setIs_WhenPictureWasTaken_clear("N");
                    }

                }



                log.info("log, albumDto.getTook_in(): " + albumDto.getTook_in());
                log.info("log, albumDto.getEstimated_year(): " + albumDto.getEstimated_year());
                log.info("log, albumDto.getIs_WhenPictureWasTaken_clear(): " + albumDto.getIs_WhenPictureWasTaken_clear());



            }




            // 행의 5번째 열(인포)
            cell = row.getCell(5);
            if (cell != null) {
                albumDto.setInfo(cell.getStringCellValue());
            }
            log.info("log, albumDto.getInfo(): " + albumDto.getInfo());

            // 행의 여섯번째 열(설명)
            cell = row.getCell(6);
            if (cell != null) {
                albumDto.setDescriptions(cell.getStringCellValue());
            }
            log.info("log, albumDto.getDescriptions(): " + albumDto.getDescriptions());


            // 행의 일곱번째 열(사진경로)
            cell = row.getCell(7);
            if (cell != null) {
                albumDto.setPhoto(cell.getStringCellValue());
            }
            log.info("log, albumDto.getPhoto(): " + albumDto.getPhoto());

            // 행의 여덟번째 열(순서)
            cell = row.getCell(8);
            if (cell != null) {

                if (cell.getCellType() == CellType.NUMERIC) {
                    log.debug("log, 셀 타입: " + cell.getCellType());
//                    break;
                    albumDto.setDisplayOrder((int) cell.getNumericCellValue());

                } else {
                    log.debug("log, 셀 타입: " + cell.getCellType());
//                    break;
                    albumDto.setDisplayOrder(Integer.parseInt(cell.getStringCellValue()));
                }


            }
            log.info("albumDto.getDisplayOrder(): " + albumDto.getDisplayOrder());

            // 행의 아홉번째 열(동영상 경로)
//            cell = row.getCell(9);
//            if (cell != null) {
//                albumDto.setVideo(cell.getStringCellValue());
//            }

            // 리스트에 담는다.
            list.add(albumDto);

            if(albumDto.getPhoto() != null || albumDto.getPhoto().equals("")) {

                albumPhotoRepository.save(
                        AlbumPhoto.builder()
                                .title(albumDto.getTitle())
                                .info(albumDto.getInfo())
                                .descriptions(albumDto.getDescriptions())
                                .photo(albumDto.getPhoto())
                                .is_WhenPictureWasTaken_clear(albumDto.getIs_WhenPictureWasTaken_clear())
                                .estimated_year(albumDto.getEstimated_year())
                                .took_in(albumDto.getTook_in())
                                .displayOrder(albumDto.getDisplayOrder())
                                .build()
                );
            }

        }



        log.debug("리스트 안에 옮겨 담기 : {}", list);

    }




}

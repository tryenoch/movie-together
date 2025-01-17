package com.bitc.full505_team2_project.common;

import com.bitc.full505_team2_project.dto.BoardDto;
import com.bitc.full505_team2_project.dto.BoardFileDto;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// * @Component : 사용자가 직접 생성한 라이브러리
@Component
public class FileUtils {
  public List<BoardFileDto> parseFileInfo(int boardPk, int boardSelect, MultipartHttpServletRequest uploadFiles) throws Exception {
    /* 스프링 프레임워크가 제공하는 ObjectUtils를 사용하여 업로드 된 파일이 있는지 확인, 없으면 true*/
    if (ObjectUtils.isEmpty(uploadFiles)) {
      return null;
    }

    // 업로드 된 파일 정보가 있을 경우 목록 저장
    List<BoardFileDto> fileList = new ArrayList<>();

    // 시간 정보 패턴 생성
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    ZonedDateTime current = ZonedDateTime.now();

    /* 게시판 별 다른 폴더에 파일 넣기 */
    String cateName = "";

    if(boardSelect == 1){
      cateName = "board"; // 1번이면 자유게시판 폴더
    } else if (boardSelect == 2) {
      cateName = "qna"; // 2번이면 문의게시판 폴더
    }

    String path = "file:///home/ec2-user/movietogether/img/upload/" + cateName + "/" + current.format(format);

    // 자바의 File 클래스, 객체 생성, 위에서 생성한 경로로
    File file = new File(path);

    // 해당 경로가 있는지 확인, 있으면 true, 없으면 false
    if (file.exists() == false) {
      file.mkdir();
    }

    /* 업로드된 파일 정보에서 파일 이름 목록 가져옴 */
    Iterator<String> iterator = uploadFiles.getFileNames();

    String newFileName; // 새 파일명을 저장할 변수
    String originalFileExtension; // 원본 파일의 확장자명을 저장할 변수
    String contentType; // 새 파일의 확장자명을 저장할 변수

    // 파일 이름 목록 중 다음 내용이 존재하는지 확인
    while (iterator.hasNext()) {
      List<MultipartFile> fileLists = uploadFiles.getFiles(iterator.next());

      for (MultipartFile multipartFile : fileLists) {
        if (multipartFile.isEmpty() == false) {
          contentType = multipartFile.getContentType(); // 확장자명 가져오기

          if (ObjectUtils.isEmpty(contentType)) {
            break;
          } else {
            // 확장자 명에 따라 변수에 확장자명 저장
            if (contentType.contains("image/jpeg")) {
              originalFileExtension = ".jpg";
            } else if (contentType.contains("image/png")) {
              originalFileExtension = ".png";
            } else if (contentType.contains("image/gif")) {
              originalFileExtension = ".gif";
            } else {
              break; // 해당하는 거 없으면 break}
            }
          }

          // 현재 시간을 기준으로 새 파일명 생성, nanoTime()을 사용하여 동일한 이름이 나올 수 없도록 함
          // 위에서 생성한 확장자명과 문자열을 연결함
          newFileName = Long.toString(System.nanoTime()) + originalFileExtension;

          // DB에 저장하기 위한 BoardDto 클래스 타입의 객체에 파일 정보 데이터 추가
          BoardFileDto boardFile = new BoardFileDto();

          boardFile.setBoardPk(boardPk); // 게시물 번호
          boardFile.setBoardSelect(boardSelect); // 게시판 번호 저장 (1: 자유게시판, 2:문의게시판)
          boardFile.setBoardFileSize(multipartFile.getSize()); // 파일 크기
          boardFile.setBoardOfileName(multipartFile.getOriginalFilename());

          // 원본 파일명
          // 서버에 저장되는 파일 이름, 위에서 생성한 파일 저장 경로와 nanoTime()을 이용하여 생성한 파일 이름을 합하여 파일을 저장할 전체 경로를 생성함

          boardFile.setBoardSfileName(path + "/" + newFileName);

          // 위에서 생성한 List<BoardFileDto> 타입의 변수에 데이터 추가
          fileList.add(boardFile);

          // 자바의 File 클래스 객체에 방금 만든 파일 이름과 경로에 저장
          file = new File(path + "/" + newFileName);

          // 서버의 지정된 위치에 실제 파일을 복사함
          multipartFile.transferTo(file);

        }
      }
    }
    // 파일 정보 목록을 반환
  return fileList;
  }

}
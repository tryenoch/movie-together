package com.bitc.full505_team2_project.service;

import com.bitc.full505_team2_project.common.FileUtils;
import com.bitc.full505_team2_project.dto.QnaDto;
import com.bitc.full505_team2_project.dto.QnaFileDto;
import com.bitc.full505_team2_project.mapper.QnaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.List;

@Service
public class QnaServiceImpl implements QnaService{

  @Autowired
  private QnaMapper qnaMapper;
  @Autowired
  private FileUtils fileUtils;

  @Override
  public List<QnaDto> selectQnaList() throws Exception {
    return qnaMapper.selectQnaList();
  }

  @Override
  public QnaDto selectQnaDetail(int qnaPk) throws Exception {
    return null;
  }

  @Override
  public void insertQna(QnaDto qnaBoard, MultipartHttpServletRequest multipart) throws Exception {

  }

  @Override
  public void updateQna(QnaDto qnaBoard) throws Exception {

  }

  @Override
  public void deleteQna(int qnaPk) throws Exception {

  }

  @Override
  public QnaFileDto selectQnaFileInfo(int qnaFileId, int qnaPk) throws Exception {
    return null;
  }
}

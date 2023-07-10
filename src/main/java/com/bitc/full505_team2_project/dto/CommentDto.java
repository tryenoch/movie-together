package com.bitc.full505_team2_project.dto;

import lombok.Data;

@Data
public class CommentDto {
    /* comment, qna_comment table 모두 동일 요소명 사용 */
    private int commentPk;
    private String commentContent;
    private String commentId;
    private String commentDate;
    private String commentDeleteYn;
    private int commentNum;
}

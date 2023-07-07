package com.bitc.full505_team2_project.dto;

import lombok.Data;

@Data
public class CommentDto {
    private int commentPk;
    private String commentContent;
    private String commentId;
    private String commentDate;
    private String commentDeleteYn;
    private int commentQnaNum;
}

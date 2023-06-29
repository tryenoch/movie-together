package com.bitc.full505_team2_project.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class MovieDto {
    int moviePk;
    String movieTitle;
    int movieLikeCnt;
}

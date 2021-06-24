package com.yf.exam.modules.paper.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="作业题目", description="作业题目")
public class PaperQuDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "ID", required=true)
    private String id;

    @ApiModelProperty(value = "作业ID", required=true)
    private String paperId;

    @ApiModelProperty(value = "题目ID", required=true)
    private String quId;

    @ApiModelProperty(value = "题目类型", required=true)
    private Integer quType;

    @ApiModelProperty(value = "是否已答", required=true)
    private Boolean answered;

    @ApiModelProperty(value = "主观答案", required=true)
    private String answer;

    @ApiModelProperty(value = "问题排序", required=true)
    private Integer sort;

    @ApiModelProperty(value = "单题分分值", required=true)
    private Integer score;

    @ApiModelProperty(value = "实际得分(主观题)", required=true)
    private Integer actualScore;

    @ApiModelProperty(value = "是否答对", required=true)
    private Boolean isRight;
    
}

package com.yf.exam.modules.paper.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="作业试题备选答案", description="作业试题备选答案")
public class PaperQuAnswerDTO implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "自增ID", required=true)
    private String id;

    @ApiModelProperty(value = "作业ID", required=true)
    private String paperId;

    @ApiModelProperty(value = "回答项ID", required=true)
    private String answerId;

    @ApiModelProperty(value = "题目ID", required=true)
    private String quId;

    @ApiModelProperty(value = "是否正确项", required=true)
    private Boolean isRight;

    @ApiModelProperty(value = "是否选中", required=true)
    private Boolean checked;

    @ApiModelProperty(value = "排序", required=true)
    private Integer sort;

    @ApiModelProperty(value = "选项标签", required=true)
    private String abc;
    
}

package com.yf.exam.modules.paper.dto.ext;

import com.yf.exam.modules.paper.dto.PaperQuAnswerDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="作业题目备选答案", description="作业题目备选答案")
public class PaperQuAnswerExtDTO extends PaperQuAnswerDTO {

    private static final long serialVersionUID = 1L;
    
    @ApiModelProperty(value = "答案内容", required=true)
    private String content;

    
}

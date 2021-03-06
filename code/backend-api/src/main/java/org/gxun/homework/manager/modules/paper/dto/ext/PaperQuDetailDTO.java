package com.yf.exam.modules.paper.dto.ext;

import com.yf.exam.modules.paper.dto.PaperQuDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value="作业题目详情类", description="作业题目详情类")
public class PaperQuDetailDTO extends PaperQuDTO {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "题目内容", required=true)
    private String content;

    @ApiModelProperty(value = "答案内容", required=true)
    List<PaperQuAnswerExtDTO> answerList;
}

package com.yf.exam.modules.paper.dto.request;

import com.yf.exam.core.api.dto.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="查找作业题目详情请求类", description="查找作业题目详情请求类")
public class PaperQuQueryDTO extends BaseDTO {

    @ApiModelProperty(value = "作业ID", required=true)
    private String paperId;

    @ApiModelProperty(value = "题目ID", required=true)
    private String quId;

}

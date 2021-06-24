package com.yf.exam.modules.paper.dto.request;

import com.yf.exam.core.api.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="作业创建请求类", description="作业创建请求类")
public class PaperCreateReqDTO extends BaseDTO {

    @JsonIgnore
    private String userId;

    @ApiModelProperty(value = "作业ID", required=true)
    private String examId;

}

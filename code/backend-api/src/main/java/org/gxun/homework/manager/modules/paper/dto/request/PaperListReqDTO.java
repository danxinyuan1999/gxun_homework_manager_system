package com.yf.exam.modules.paper.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="作业", description="作业")
public class PaperListReqDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户ID", required=true)
    private String userId;

    @ApiModelProperty(value = "班级ID", required=true)
    private String departId;

    @ApiModelProperty(value = "规则ID", required=true)
    private String examId;

    @ApiModelProperty(value = "用户昵称", required=true)
    private String realName;

    @ApiModelProperty(value = "作业状态", required=true)
    private Integer state;

    
}

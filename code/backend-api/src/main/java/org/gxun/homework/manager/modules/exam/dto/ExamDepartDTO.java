package orz.gxun.exam.modules.exam.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value="考试部门", description="考试部门")
public class ExamDepartDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    @ApiModelProperty(value = "ID", required=true)
    private String id;
    
    @ApiModelProperty(value = "考试ID", required=true)
    private String examId;
    
    @ApiModelProperty(value = "部门ID", required=true)
    private String departId;
    
}

package orz.gxun.exam.modules.exam.dto.response;

import orz.gxun.exam.modules.exam.dto.ExamDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value="阅卷分页响应类", description="阅卷分页响应类")
public class ExamReviewRespDTO extends ExamDTO {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value = "考试人数", required=true)
    private Integer examUser;

    @ApiModelProperty(value = "待阅试卷", required=true)
    private Integer unreadPaper;



}

package orz.gxun.exam.modules.exam.dto.response;

import orz.gxun.exam.modules.exam.dto.ExamDTO;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="在线考试分页响应类", description="在线考试分页响应类")
public class ExamOnlineRespDTO extends ExamDTO {

    private static final long serialVersionUID = 1L;


}

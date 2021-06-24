package orz.gxun.exam.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import orz.gxun.exam.modules.exam.entity.ExamDepart;

import java.util.List;

public interface ExamDepartService extends IService<ExamDepart> {

    /**
     * 保存全部
     * @param examId
     * @param departs
     */
    void saveAll(String examId, List<String> departs);


    /**
     * 根据考试查找对应的部门
     * @param examId
     * @return
     */
    List<String> listByExam(String examId);
}

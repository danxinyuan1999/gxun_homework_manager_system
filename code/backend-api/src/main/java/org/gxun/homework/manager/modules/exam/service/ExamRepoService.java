package orz.gxun.exam.modules.exam.service;

import com.baomidou.mybatisplus.extension.service.IService;
import orz.gxun.exam.modules.exam.dto.ExamRepoDTO;
import orz.gxun.exam.modules.exam.entity.ExamRepo;

import java.util.List;

public interface ExamRepoService extends IService<ExamRepo> {


    /**
     * 保存全部
     * @param examId
     * @param list
     */
    void saveAll(String examId, List<ExamRepoDTO> list);

    /**
     * 查找考试题库列表
     * @param examId
     * @return
     */
    List<ExamRepoDTO> listByExam(String examId);

    /**
     * 清理脏数据
     * @param examId
     */
    void clear(String examId);

}

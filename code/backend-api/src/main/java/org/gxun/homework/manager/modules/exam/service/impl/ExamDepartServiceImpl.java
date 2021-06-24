package orz.gxun.exam.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import orz.gxun.exam.core.exception.ServiceException;
import orz.gxun.exam.modules.exam.entity.ExamDepart;
import orz.gxun.exam.modules.exam.mapper.ExamDepartMapper;
import orz.gxun.exam.modules.exam.service.ExamDepartService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamDepartServiceImpl extends ServiceImpl<ExamDepartMapper, ExamDepart> implements ExamDepartService {

    @Override
    public void saveAll(String examId, List<String> departs) {

        // 先删除
        QueryWrapper<ExamDepart> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ExamDepart::getExamId, examId);
        this.remove(wrapper);

        // 再增加
        if(CollectionUtils.isEmpty(departs)){
            throw new ServiceException(1, "请至少选择选择一个部门！！");
        }
        List<ExamDepart> list = new ArrayList<>();

        for(String id: departs){
            ExamDepart depart = new ExamDepart();
            depart.setDepartId(id);
            depart.setExamId(examId);
            list.add(depart);
        }

        this.saveBatch(list);
    }

    @Override
    public List<String> listByExam(String examId) {
        // 先删除
        QueryWrapper<ExamDepart> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(ExamDepart::getExamId, examId);
        List<ExamDepart> list = this.list(wrapper);
        List<String> ids = new ArrayList<>();
        if(!CollectionUtils.isEmpty(list)){
            for(ExamDepart item: list){
                ids.add(item.getDepartId());
            }
        }

        return ids;

    }
}

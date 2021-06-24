package orz.gxun.exam.modules.exam.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import orz.gxun.exam.core.api.dto.PagingReqDTO;
import orz.gxun.exam.core.exception.ServiceException;
import orz.gxun.exam.core.utils.BeanMapper;
import orz.gxun.exam.core.utils.StringUtils;
import orz.gxun.exam.modules.enums.JoinType;
import orz.gxun.exam.modules.enums.OpenType;
import orz.gxun.exam.modules.exam.dto.ExamDTO;
import orz.gxun.exam.modules.exam.dto.ExamRepoDTO;
import orz.gxun.exam.modules.exam.dto.request.ExamSaveReqDTO;
import orz.gxun.exam.modules.exam.dto.response.ExamOnlineRespDTO;
import orz.gxun.exam.modules.exam.dto.response.ExamReviewRespDTO;
import orz.gxun.exam.modules.exam.entity.Exam;
import orz.gxun.exam.modules.exam.mapper.ExamMapper;
import orz.gxun.exam.modules.exam.service.ExamDepartService;
import orz.gxun.exam.modules.exam.service.ExamRepoService;
import orz.gxun.exam.modules.exam.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExamServiceImpl extends ServiceImpl<ExamMapper, Exam> implements ExamService {


    @Autowired
    private ExamRepoService examRepoService;

    @Autowired
    private ExamDepartService examDepartService;

    @Override
    public void save(ExamSaveReqDTO reqDTO) {

        // ID
        String id = reqDTO.getId();

        if(StringUtils.isBlank(id)){
            id = IdWorker.getIdStr();
        }

        //复制参数
        Exam entity = new Exam();

        // 计算分值
        this.calcScore(reqDTO);


        // 复制基本数据
        BeanMapper.copy(reqDTO, entity);
        entity.setId(id);

        // 修复状态
        if (reqDTO.getTimeLimit()!=null
                && !reqDTO.getTimeLimit()
                && reqDTO.getState()!=null
                && reqDTO.getState() == 2) {
            entity.setState(0);
        } else {
            entity.setState(reqDTO.getState());
        }

        // 题库组卷
        if(JoinType.REPO_JOIN.equals(reqDTO.getJoinType())){
            try {
                examRepoService.saveAll(id, reqDTO.getRepoList());
            }catch (DuplicateKeyException e){
                throw new ServiceException(1, "不能选择重复的题库！");
            }
        }

        // 开放的部门
        if(OpenType.DEPT_OPEN.equals(reqDTO.getOpenType())){
            examDepartService.saveAll(id, reqDTO.getDepartIds());
        }



        this.saveOrUpdate(entity);

    }

    @Override
    public ExamSaveReqDTO findDetail(String id) {
        ExamSaveReqDTO respDTO = new ExamSaveReqDTO();
        Exam exam = this.getById(id);
        BeanMapper.copy(exam, respDTO);

        // 考试部门
        List<String> departIds = examDepartService.listByExam(id);
        respDTO.setDepartIds(departIds);

        // 题库
        List<ExamRepoDTO> repos = examRepoService.listByExam(id);
        respDTO.setRepoList(repos);

        return respDTO;
    }

    @Override
    public ExamDTO findById(String id) {
        ExamDTO respDTO = new ExamDTO();
        Exam exam = this.getById(id);
        BeanMapper.copy(exam, respDTO);
        return respDTO;
    }

    @Override
    public IPage<ExamDTO> paging(PagingReqDTO<ExamDTO> reqDTO) {

        //创建分页对象
        Page page = new Page(reqDTO.getCurrent(), reqDTO.getSize());

        //转换结果
        IPage<ExamDTO> pageData = baseMapper.paging(page, reqDTO.getParams());
        return pageData;
     }

    @Override
    public IPage<ExamOnlineRespDTO> onlinePaging(PagingReqDTO<ExamDTO> reqDTO) {


        // 创建分页对象
        Page page = new Page(reqDTO.getCurrent(), reqDTO.getSize());

        // 查找分页
        IPage<ExamOnlineRespDTO> pageData = baseMapper.online(page, reqDTO.getParams());

        return pageData;
    }

    @Override
    public IPage<ExamReviewRespDTO> reviewPaging(PagingReqDTO<ExamDTO> reqDTO) {
        // 创建分页对象
        Page page = new Page(reqDTO.getCurrent(), reqDTO.getSize());

        // 查找分页
        IPage<ExamReviewRespDTO> pageData = baseMapper.reviewPaging(page, reqDTO.getParams());

        return pageData;
    }


    /**
     * 计算分值
     * @param reqDTO
     */
    private void calcScore(ExamSaveReqDTO reqDTO){

        // 主观题分数
        int objScore = 0;

        // 题库组卷
        if(JoinType.REPO_JOIN.equals(reqDTO.getJoinType())){
            List<ExamRepoDTO> repoList = reqDTO.getRepoList();

            for(ExamRepoDTO item: repoList){
                if(item.getRadioCount()!=null
                        && item.getRadioCount()>0
                        && item.getRadioScore()!=null
                        && item.getRadioScore()>0){
                    objScore+=item.getRadioCount()*item.getRadioScore();
                }
                if(item.getMultiCount()!=null
                        && item.getMultiCount()>0
                        && item.getMultiScore()!=null
                        && item.getMultiScore()>0){
                    objScore+=item.getMultiCount()*item.getMultiScore();
                }
                if(item.getJudgeCount()!=null
                        && item.getJudgeCount()>0
                        && item.getJudgeScore()!=null
                        && item.getJudgeScore()>0){
                    objScore+=item.getJudgeCount()*item.getJudgeScore();
                }
            }
        }


        reqDTO.setTotalScore(objScore);
    }

}

package com.yf.exam.modules.paper.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yf.exam.core.api.dto.PagingReqDTO;
import com.yf.exam.core.exception.ServiceException;
import com.yf.exam.core.utils.BeanMapper;
import com.yf.exam.core.utils.StringUtils;
import com.yf.exam.modules.enums.JoinType;
import com.yf.exam.modules.exam.dto.ExamDTO;
import com.yf.exam.modules.exam.dto.ExamRepoDTO;
import com.yf.exam.modules.exam.service.ExamRepoService;
import com.yf.exam.modules.exam.service.ExamService;
import com.yf.exam.modules.paper.dto.PaperQuDTO;
import com.yf.exam.modules.paper.dto.ext.PaperQuAnswerExtDTO;
import com.yf.exam.modules.paper.dto.ext.PaperQuDetailDTO;
import com.yf.exam.modules.paper.dto.request.PaperAnswerDTO;
import com.yf.exam.modules.paper.dto.request.PaperListReqDTO;
import com.yf.exam.modules.paper.dto.response.ExamDetailRespDTO;
import com.yf.exam.modules.paper.dto.response.ExamResultRespDTO;
import com.yf.exam.modules.paper.dto.response.PaperListRespDTO;
import com.yf.exam.modules.paper.entity.Paper;
import com.yf.exam.modules.paper.entity.PaperQu;
import com.yf.exam.modules.paper.entity.PaperQuAnswer;
import com.yf.exam.modules.paper.enums.ExamState;
import com.yf.exam.modules.paper.enums.PaperState;
import com.yf.exam.modules.paper.mapper.PaperMapper;
import com.yf.exam.modules.paper.service.PaperQuAnswerService;
import com.yf.exam.modules.paper.service.PaperQuService;
import com.yf.exam.modules.paper.service.PaperService;
import com.yf.exam.modules.qu.entity.Qu;
import com.yf.exam.modules.qu.entity.QuAnswer;
import com.yf.exam.modules.qu.enums.QuType;
import com.yf.exam.modules.qu.service.QuAnswerService;
import com.yf.exam.modules.qu.service.QuService;
import com.yf.exam.modules.sys.user.entity.SysUser;
import com.yf.exam.modules.sys.user.service.SysUserService;
import com.yf.exam.modules.user.book.service.UserBookService;
import com.yf.exam.modules.user.exam.service.UserExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class PaperServiceImpl extends ServiceImpl<PaperMapper, Paper> implements PaperService {


    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private ExamService examService;

    @Autowired
    private QuService quService;

    @Autowired
    private QuAnswerService quAnswerService;

    @Autowired
    private PaperService paperService;

    @Autowired
    private PaperQuService paperQuService;

    @Autowired
    private PaperQuAnswerService paperQuAnswerService;

    @Autowired
    private UserBookService userBookService;

    @Autowired
    private ExamRepoService examRepoService;

    @Autowired
    private UserExamService userExamService;

    private static List<String> ABC = Arrays.asList(new String[]{
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K","L","M","N","O","P","Q","R","S","T","U","V","W","X"
            ,"Y","Z"
    });





    @Override
    public String createPaper(String userId, String examId) {

        ExamDTO exam = examService.findById(examId);

        if(exam == null){
            throw new ServiceException(1, "作业不存在！");
        }

        if(!ExamState.ENABLE.equals(exam.getState())){
            throw new ServiceException(1, "作业状态不正确！");
        }

        List<PaperQu> quList = new ArrayList<>();

        if(JoinType.REPO_JOIN.equals(exam.getJoinType())){

            quList = this.generateByRepo(examId, exam.getLevel());
        }

        if(CollectionUtils.isEmpty(quList)){
            throw new ServiceException(1, "规则不正确，无对应的作业！");
        }

        String paperId = this.savePaper(userId, exam, quList);

        return paperId;
    }

    @Override
    public ExamDetailRespDTO paperDetail(String paperId) {


        ExamDetailRespDTO respDTO = new ExamDetailRespDTO();

        Paper paper = paperService.getById(paperId);
        BeanMapper.copy(paper, respDTO);

        List<PaperQuDTO> list = paperQuService.listByPaper(paperId);

        List<PaperQuDTO> radioList = new ArrayList<>();
        List<PaperQuDTO> multiList = new ArrayList<>();
        List<PaperQuDTO> judgeList = new ArrayList<>();
        for(PaperQuDTO item: list){
            if(QuType.RADIO.equals(item.getQuType())){
                radioList.add(item);
            }
            if(QuType.MULTI.equals(item.getQuType())){
                multiList.add(item);
            }
            if(QuType.JUDGE.equals(item.getQuType())){
                judgeList.add(item);
            }
        }

        respDTO.setRadioList(radioList);
        respDTO.setMultiList(multiList);
        respDTO.setJudgeList(judgeList);
        return respDTO;
    }

    @Override
    public ExamResultRespDTO paperResult(String paperId) {

        ExamResultRespDTO respDTO = new ExamResultRespDTO();

        Paper paper = paperService.getById(paperId);
        BeanMapper.copy(paper, respDTO);

        List<PaperQuDetailDTO> quList = paperQuService.listForPaperResult(paperId);
        respDTO.setQuList(quList);

        return respDTO;
    }

    @Override
    public PaperQuDetailDTO findQuDetail(String paperId, String quId) {

        PaperQuDetailDTO respDTO = new PaperQuDetailDTO();

        Qu qu = quService.getById(quId);

        PaperQu paperQu = paperQuService.findByKey(paperId, quId);
        BeanMapper.copy(paperQu, respDTO);
        respDTO.setContent(qu.getContent());

        List<PaperQuAnswerExtDTO> list = paperQuAnswerService.listForExam(paperId, quId);
        respDTO.setAnswerList(list);

        return respDTO;
    }

    private List<PaperQu> generateByRepo(String examId, Integer level){

        List<ExamRepoDTO> list = examRepoService.listByExam(examId);

        List<PaperQu> quList = new ArrayList<>();

        List<String> excludes = new ArrayList<>();
        excludes.add("none");

        if (!CollectionUtils.isEmpty(list)) {
            for (ExamRepoDTO item : list) {

                if(item.getRadioCount() > 0){
                    List<Qu> radioList = quService.listByRandom(item.getRepoId(), QuType.RADIO, level, excludes, item.getRadioCount());
                    for (Qu qu : radioList) {
                        PaperQu paperQu = this.processPaperQu(item, qu);
                        quList.add(paperQu);
                        excludes.add(qu.getId());
                    }
                }

                if(item.getMultiCount() > 0) {
                    List<Qu> multiList = quService.listByRandom(item.getRepoId(), QuType.MULTI, level, excludes,
                            item.getMultiCount());
                    for (Qu qu : multiList) {
                        PaperQu paperQu = this.processPaperQu(item, qu);
                        quList.add(paperQu);
                        excludes.add(qu.getId());
                    }
                }

                if(item.getJudgeCount() > 0) {
                    List<Qu> judgeList = quService.listByRandom(item.getRepoId(), QuType.JUDGE, level, excludes,
                            item.getJudgeCount());
                    for (Qu qu : judgeList) {
                        PaperQu paperQu = this.processPaperQu(item, qu);
                        quList.add(paperQu);
                        excludes.add(qu.getId());
                    }
                }
            }
        }
        return quList;
    }

    private PaperQu processPaperQu(ExamRepoDTO repo, Qu qu) {

        PaperQu paperQu = new PaperQu();
        paperQu.setQuId(qu.getId());
        paperQu.setAnswered(false);
        paperQu.setIsRight(false);
        paperQu.setQuType(qu.getQuType());

        if (QuType.RADIO.equals(qu.getQuType())) {
            paperQu.setScore(repo.getRadioScore());
            paperQu.setActualScore(repo.getRadioScore());
        }

        if (QuType.MULTI.equals(qu.getQuType())) {
            paperQu.setScore(repo.getMultiScore());
            paperQu.setActualScore(repo.getMultiScore());
        }

        if (QuType.JUDGE.equals(qu.getQuType())) {
            paperQu.setScore(repo.getJudgeScore());
            paperQu.setActualScore(repo.getJudgeScore());
        }

        return paperQu;
    }

    private String savePaper(String userId, ExamDTO exam, List<PaperQu> quList) {

        SysUser user = sysUserService.getById(userId);

        Paper paper = new Paper();
        paper.setDepartId(user.getDepartId());
        paper.setExamId(exam.getId());
        paper.setTitle(exam.getTitle());
        paper.setTotalScore(exam.getTotalScore());
        paper.setTotalTime(exam.getTotalTime());
        paper.setUserScore(0);
        paper.setUserId(userId);
        paper.setCreateTime(new Date());
        paper.setUpdateTime(new Date());
        paper.setQualifyScore(exam.getQualifyScore());
        paper.setState(PaperState.ING);
        paper.setHasSaq(false);

        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(System.currentTimeMillis());
        cl.add(Calendar.MINUTE, exam.getTotalTime());
        paper.setLimitTime(cl.getTime());

        paperService.save(paper);

        if (!CollectionUtils.isEmpty(quList)) {
            this.savePaperQu(paper.getId(), quList);
        }

        return paper.getId();
    }

    private void savePaperQu(String paperId, List<PaperQu> quList){

        List<PaperQu> batchQuList = new ArrayList<>();
        List<PaperQuAnswer> batchAnswerList = new ArrayList<>();

        int sort = 0;
        for (PaperQu item : quList) {

            item.setPaperId(paperId);
            item.setSort(sort);
            item.setId(IdWorker.getIdStr());

            List<QuAnswer> answerList = quAnswerService.listAnswerByRandom(item.getQuId());

            if (!CollectionUtils.isEmpty(answerList)) {

                int ii = 0;
                for (QuAnswer answer : answerList) {
                    PaperQuAnswer paperQuAnswer = new PaperQuAnswer();
                    paperQuAnswer.setId(UUID.randomUUID().toString());
                    paperQuAnswer.setPaperId(paperId);
                    paperQuAnswer.setQuId(answer.getQuId());
                    paperQuAnswer.setAnswerId(answer.getId());
                    paperQuAnswer.setChecked(false);
                    paperQuAnswer.setSort(ii);
                    paperQuAnswer.setAbc(ABC.get(ii));
                    paperQuAnswer.setIsRight(answer.getIsRight());
                    ii++;
                    batchAnswerList.add(paperQuAnswer);
                }
            }

            batchQuList.add(item);
            sort++;
        }

        paperQuService.saveBatch(batchQuList);

        paperQuAnswerService.saveBatch(batchAnswerList);
    }

    @Override
    public void fillAnswer(PaperAnswerDTO reqDTO) {

        if(CollectionUtils.isEmpty(reqDTO.getAnswers())
                && StringUtils.isBlank(reqDTO.getAnswer())){
            return;
        }

        List<PaperQuAnswer> list = paperQuAnswerService.listForFill(reqDTO.getPaperId(), reqDTO.getQuId());

        boolean right = true;

        for (PaperQuAnswer item : list) {

            if (reqDTO.getAnswers().contains(item.getId())) {
                item.setChecked(true);
            } else {
                item.setChecked(false);
            }

            if (item.getIsRight()!=null && !item.getIsRight().equals(item.getChecked())) {
                right = false;
            }
            paperQuAnswerService.updateById(item);
        }

        PaperQu qu = new PaperQu();
        qu.setQuId(reqDTO.getQuId());
        qu.setPaperId(reqDTO.getPaperId());
        qu.setIsRight(right);
        qu.setAnswer(reqDTO.getAnswer());
        qu.setAnswered(true);

        paperQuService.updateByKey(qu);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void handExam(String paperId) {

        Paper paper = paperService.getById(paperId);

        if(!PaperState.ING.equals(paper.getState())){
            throw new ServiceException(1, "作业状态不正确！");
        }

        int objScore = paperQuService.sumObjective(paperId);
        paper.setObjScore(objScore);
        paper.setUserScore(objScore);

        paper.setSubjScore(0);

        if(paper.getHasSaq()) {
            paper.setState(PaperState.WAIT_OPT);
        }else {

            userExamService.joinResult(paper.getUserId(), paper.getExamId(), objScore, objScore>=paper.getQualifyScore());

            paper.setState(PaperState.FINISHED);
        }
        paper.setUpdateTime(new Date());

        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(System.currentTimeMillis());
        int userTime = (int)((System.currentTimeMillis() - paper.getCreateTime().getTime()) / 1000 / 60);
        if(userTime == 0){
            userTime = 1;
        }
        paper.setUserTime(userTime);

        paperService.updateById(paper);

        List<PaperQuDTO> list = paperQuService.listByPaper(paperId);
        for(PaperQuDTO qu: list){

            if(qu.getIsRight()){
                continue;
            }

            userBookService.addBook(paper.getExamId(), qu.getQuId());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void reviewPaper(ExamResultRespDTO reqDTO) {

        List<PaperQuDetailDTO> quList = reqDTO.getQuList();
        List<PaperQu> list = new ArrayList<>();
        for(PaperQuDetailDTO item: quList){
            PaperQu qu = new PaperQu();
            qu.setId(item.getId());
            qu.setActualScore(item.getActualScore());
            qu.setIsRight(true);
            list.add(qu);
        }

        paperQuService.updateBatchById(list);

        int subjScore = paperQuService.sumSubjective(reqDTO.getId());

        Paper paper = this.getById(reqDTO.getId());
        paper.setSubjScore(subjScore);
        paper.setUserScore(paper.getObjScore()+subjScore);
        paper.setState(PaperState.FINISHED);
        paper.setUpdateTime(new Date());
        this.updateById(paper);

        userExamService.joinResult(paper.getUserId(), paper.getExamId(), paper.getUserScore(), paper.getUserScore()>=paper.getQualifyScore());
    }

    @Override
    public IPage<PaperListRespDTO> paging(PagingReqDTO<PaperListReqDTO> reqDTO) {
        return baseMapper.paging(reqDTO.toPage(), reqDTO.getParams());
    }

    @Override
    public List<Paper> findDeadPapers() {

        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(System.currentTimeMillis());
        cl.add(Calendar.MINUTE, -5);

        QueryWrapper<Paper> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .le(Paper::getLimitTime, cl.getTime())
                .eq(Paper::getState, PaperState.ING);


        return this.list(wrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void breakExam(String paperId) {

        Paper paper = new Paper();
        paper.setId(paperId);
        paper.setState(PaperState.BREAK);
        paper.setUpdateTime(new Date());
        this.updateById(paper);
    }
}

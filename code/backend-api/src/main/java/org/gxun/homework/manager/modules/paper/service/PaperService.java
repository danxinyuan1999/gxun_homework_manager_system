package com.yf.exam.modules.paper.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.exam.core.api.dto.PagingReqDTO;
import com.yf.exam.modules.paper.dto.ext.PaperQuDetailDTO;
import com.yf.exam.modules.paper.dto.request.PaperAnswerDTO;
import com.yf.exam.modules.paper.dto.request.PaperListReqDTO;
import com.yf.exam.modules.paper.dto.response.ExamDetailRespDTO;
import com.yf.exam.modules.paper.dto.response.ExamResultRespDTO;
import com.yf.exam.modules.paper.dto.response.PaperListRespDTO;
import com.yf.exam.modules.paper.entity.Paper;

import java.util.List;

public interface PaperService extends IService<Paper> {

    String createPaper(String userId, String examId);

    ExamDetailRespDTO paperDetail(String paperId);

    ExamResultRespDTO paperResult(String paperId);

    PaperQuDetailDTO findQuDetail(String paperId, String quId);

    void fillAnswer(PaperAnswerDTO reqDTO);

    void handExam(String paperId);

    void reviewPaper(ExamResultRespDTO reqDTO);

    IPage<PaperListRespDTO> paging(PagingReqDTO<PaperListReqDTO> reqDTO);

    List<Paper> findDeadPapers();

    void breakExam(String paperId);

}

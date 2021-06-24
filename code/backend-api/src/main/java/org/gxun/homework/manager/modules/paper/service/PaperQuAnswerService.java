package com.yf.exam.modules.paper.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.exam.core.api.dto.PagingReqDTO;
import com.yf.exam.modules.paper.dto.PaperQuAnswerDTO;
import com.yf.exam.modules.paper.dto.ext.PaperQuAnswerExtDTO;
import com.yf.exam.modules.paper.entity.PaperQuAnswer;

import java.util.List;

public interface PaperQuAnswerService extends IService<PaperQuAnswer> {

    IPage<PaperQuAnswerDTO> paging(PagingReqDTO<PaperQuAnswerDTO> reqDTO);

    List<PaperQuAnswerExtDTO> listForExam(String paperId, String quId);

    List<PaperQuAnswer> listForFill(String paperId, String quId);
}

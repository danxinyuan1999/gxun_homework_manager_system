package com.yf.exam.modules.paper.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yf.exam.core.api.dto.PagingReqDTO;
import com.yf.exam.modules.paper.dto.PaperQuDTO;
import com.yf.exam.modules.paper.dto.ext.PaperQuDetailDTO;
import com.yf.exam.modules.paper.entity.PaperQu;

import java.util.List;
public interface PaperQuService extends IService<PaperQu> {

    IPage<PaperQuDTO> paging(PagingReqDTO<PaperQuDTO> reqDTO);

    List<PaperQuDTO> listByPaper(String paperId);

    PaperQu findByKey(String paperId, String quId);

    void updateByKey(PaperQu qu);

    int sumObjective(String paperId);

    int sumSubjective(String paperId);

    List<PaperQuDetailDTO> listForPaperResult(String paperId);
}

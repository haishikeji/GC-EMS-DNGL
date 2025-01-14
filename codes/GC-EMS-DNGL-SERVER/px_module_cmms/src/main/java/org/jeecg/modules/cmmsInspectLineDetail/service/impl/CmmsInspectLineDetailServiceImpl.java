package org.jeecg.modules.cmmsInspectLineDetail.service.impl;

import org.jeecg.modules.cmmsInspectLineDetail.entity.CmmsInspectLineDetail;
import org.jeecg.modules.cmmsInspectLineDetail.entity.CmmsInspectLineDetailVo;
import org.jeecg.modules.cmmsInspectLineDetail.mapper.CmmsInspectLineDetailMapper;
import org.jeecg.modules.cmmsInspectLineDetail.service.ICmmsInspectLineDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 巡检路线详情
 * @@author 品讯科技
 * @Date:   2024-01-22
 * @Version: V1.0
 */
@Service
public class CmmsInspectLineDetailServiceImpl extends ServiceImpl<CmmsInspectLineDetailMapper, CmmsInspectLineDetail> implements ICmmsInspectLineDetailService {

    @Autowired
    @SuppressWarnings("all")
    private CmmsInspectLineDetailMapper inspectLineDetailMapper;

    @Override
    public List<CmmsInspectLineDetailVo> selectCmmsInspectLineDetailList(String id) {
        return inspectLineDetailMapper.selectCmmsInspectLineDetailList(id);
    }
}

package org.jeecg.modules.cmmsSpotcheckContentItem.service.impl;

import org.jeecg.modules.cmmsSpotcheckContentItem.entity.CmmsSpotcheckContentItem;
import org.jeecg.modules.cmmsSpotcheckContentItem.mapper.CmmsSpotcheckContentItemMapper;
import org.jeecg.modules.cmmsSpotcheckContentItem.service.ICmmsSpotcheckContentItemService;
import org.jeecg.modules.cmmsSpotcheckContentItem.vo.CmmsSpotcheckContentItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 点检内容-点检项
 * @@author 品讯科技
 * @Date:   2024-01-15
 * @Version: V1.0
 */
@Service
public class CmmsSpotcheckContentItemServiceImpl extends ServiceImpl<CmmsSpotcheckContentItemMapper, CmmsSpotcheckContentItem> implements ICmmsSpotcheckContentItemService {

    @Autowired
    private CmmsSpotcheckContentItemMapper cmmsSpotcheckContentItemMapper;

    /**
     * 查询点检内容-点检项列表
     *
     * @param cmmsSpotcheckContentItem 点检内容-点检项
     * @return 点检内容-点检项
     */
    @Override
    public List<CmmsSpotcheckContentItemVO> selectCmmsSpotcheckContentItemList(CmmsSpotcheckContentItem cmmsSpotcheckContentItem)
    {
        return cmmsSpotcheckContentItemMapper.selectCmmsSpotcheckContentItemList(cmmsSpotcheckContentItem);
    }

}

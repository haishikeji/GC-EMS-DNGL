package org.jeecg.modules.cmmsOrderNode.service.impl;

import org.jeecg.modules.cmmsOrderNode.entity.CmmsOrderNode;
import org.jeecg.modules.cmmsOrderNode.mapper.CmmsOrderNodeMapper;
import org.jeecg.modules.cmmsOrderNode.service.ICmmsOrderNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 工单操作记录表
 * @@author 品讯科技
 * @Date:   2024-01-09
 * @Version: V1.0
 */
@Service
public class CmmsOrderNodeServiceImpl extends ServiceImpl<CmmsOrderNodeMapper, CmmsOrderNode> implements ICmmsOrderNodeService {

    @Autowired
    @SuppressWarnings("all")
    private CmmsOrderNodeMapper cmmsOrderNodeMapper;

    @Override
    public List<CmmsOrderNode> selectCmmsOrderNodeList(CmmsOrderNode node) {
        return cmmsOrderNodeMapper.selectCmmsOrderNodeList(node);
    }

    /**
     *通过工单id删除操作记录
     */
    public boolean deleteCmmsOrderNodeByRelid(String relid){
        return cmmsOrderNodeMapper.deleteCmmsOrderNodeByRelid(relid);
    }

    /**
     *通过工单id批量删除操作记录
     */
    public boolean deleteCmmsOrderNodeByRelids(List<String> relids){
        return cmmsOrderNodeMapper.deleteCmmsOrderNodeByRelids(relids);
    }
}

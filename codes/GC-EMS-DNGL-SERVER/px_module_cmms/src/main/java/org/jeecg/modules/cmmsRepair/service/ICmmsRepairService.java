package org.jeecg.modules.cmmsRepair.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jeecg.modules.cmmsRepair.entity.CmmsRepair;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cmmsRepair.entity.CmmsRepairVo;
import org.jeecg.modules.cmmsRepair.entity.IndexNum;

import java.util.List;

/**
 * @Description: 维修工单
 * @@author 品讯科技
 * @Date:   2024-01-09
 * @Version: V1.0
 */
public interface ICmmsRepairService extends IService<CmmsRepair> {

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 新增维修工单
    *   date: 2024/1/10
    */
    public boolean addCmmsRepair(CmmsRepairVo cmsRepair);

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 维修工单查看详情
    *   date: 2024/1/10
    */
    CmmsRepairVo selectById(String id);

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 查看我的维修工单
    *   date: 2024/1/10
    */
    List<CmmsRepair> selectList(IPage page,CmmsRepair cmmsRepair);

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 处理、派单
    *   date: 2024/1/10
    */
    int repair(CmmsRepairVo cmmsRepair);

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 维修工单冻结、解冻
    *   date: 2024/1/10
    */
    int updateRepairStatus(CmmsRepair cmmsRepair);

    /**
    *   author: dzc
    *   version: 1.0
    *   des: 首页待维修数
    *   date: 2024/2/27
    */
    IndexNum IndexRepairNum();

    List<CmmsRepair> getPageList(Page<CmmsRepair> page, CmmsRepair cmmsRepair);
}

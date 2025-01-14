package org.jeecg.modules.cmmsRepair.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cmmsRepair.entity.CmmsRepair;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cmmsRepair.entity.IndexNum;

/**
 * @Description: 维修工单
 * @@author 品讯科技
 * @Date:   2024-01-09
 * @Version: V1.0
 */
public interface CmmsRepairMapper extends BaseMapper<CmmsRepair> {

    List<CmmsRepair> selectListByObj(IPage page,@Param("cmmsRepair") CmmsRepair cmmsRepair);

    int updateCmmsRepair(CmmsRepair cmmsRepair);

    int updateRepairStatus(@Param("id") String id,@Param("repairstatus") String repairstatus);

    List<Integer> getIndexRepairNum();

    List<CmmsRepair> getPageList(@Param("page") Page<CmmsRepair> page,@Param("cmmsRepair") CmmsRepair cmmsRepair);
}

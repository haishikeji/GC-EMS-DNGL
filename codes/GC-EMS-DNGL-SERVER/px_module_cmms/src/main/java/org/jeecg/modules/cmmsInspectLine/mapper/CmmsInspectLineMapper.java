package org.jeecg.modules.cmmsInspectLine.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cmmsInspectLine.entity.CmmsInspectLine;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cmmsInspectLine.entity.CmmsInspectLineVo;

/**
 * @Description: 巡检路线
 * @@author 品讯科技
 * @Date:   2024-01-22
 * @Version: V1.0
 */
public interface CmmsInspectLineMapper extends BaseMapper<CmmsInspectLine> {

    String getNameById(@Param("id") String id);

    List<CmmsInspectLineVo> getCmmsInspectLineListDetails(@Param("page") Page<CmmsInspectLineVo> page,@Param("cmmsInspectLine") CmmsInspectLine cmmsInspectLine);

    String getCodeById(@Param("relid") String relid);
}

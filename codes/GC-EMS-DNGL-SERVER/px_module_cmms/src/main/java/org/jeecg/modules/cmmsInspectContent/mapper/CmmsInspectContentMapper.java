package org.jeecg.modules.cmmsInspectContent.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.cmmsInspectContent.entity.CmmsInspectContent;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.cmmsInspectContent.entity.CmmsInspectContentVo;

/**
 * @Description: 巡检内容
 * @@author 品讯科技
 * @Date:   2024-01-15
 * @Version: V1.0
 */
public interface CmmsInspectContentMapper extends BaseMapper<CmmsInspectContent> {

    List<CmmsInspectContentVo> getCmmsInspectContent(@Param("page") Page<CmmsInspectContentVo> page,@Param("cmmsInspectContent") CmmsInspectContent cmmsInspectContent);

    CmmsInspectContentVo getCmmsInspectContentById(@Param("id") String id);

    boolean updateStatua(@Param("id") String id,@Param("status") String status);

    CmmsInspectContentVo getCmmsInspectContentByEqId(@Param("eqid") String eqid);

    boolean updateStatusIs1(@Param("equipmentid") String equipmentid);
}

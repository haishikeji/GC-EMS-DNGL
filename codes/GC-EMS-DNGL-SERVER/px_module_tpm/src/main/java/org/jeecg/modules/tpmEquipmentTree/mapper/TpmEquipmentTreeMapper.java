package org.jeecg.modules.tpmEquipmentTree.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.system.vo.SelectTreeModel;
import org.jeecg.modules.tpmEquipmentTree.entity.TpmEquipmentTree;

import java.util.List;
import java.util.Map;

/**
 * @Description: 设备树
 * @@author 品讯科技
 * @Date:   2024-01-09
 * @Version: V1.0
 */
public interface TpmEquipmentTreeMapper extends BaseMapper<TpmEquipmentTree> {

	/**
	 * 获取设备树及设备列表
	 */
	public List<TpmEquipmentTree> selectTreeAndEquipList(TpmEquipmentTree tpmEquipmentTree);


	/**
	 * 编辑节点状态
	 * @param id
	 * @param status
	 */
	void updateTreeNodeStatus(@Param("id") String id,@Param("status") String status);

	/**
	 * 【vue3专用】根据父级ID查询树节点数据
	 *
	 * @param pid
	 * @param query
	 * @return
	 */
	List<SelectTreeModel> queryListByPid(@Param("pid") String pid, @Param("query") Map<String, String> query);

	/**
	*   author: dzc
	*   version: 1.0
	*   des: 查询设备分类树不含设备
	*   date: 2024/4/17
	*/
    List<TpmEquipmentTree> selectTypeTreeList(TpmEquipmentTree equipmentTree);
}

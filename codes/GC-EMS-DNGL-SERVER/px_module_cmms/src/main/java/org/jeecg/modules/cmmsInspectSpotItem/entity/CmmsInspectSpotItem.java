package org.jeecg.modules.cmmsInspectSpotItem.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 巡检点-巡检项
 * @@author 品讯科技
 * @Date:   2024-01-17
 * @Version: V1.0
 */
@Data
@TableName("cmms_inspect_spot_item")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="cmms_inspect_spot_item对象", description="巡检点-巡检项")
public class CmmsInspectSpotItem implements Serializable {
    private static final long serialVersionUID = 1L;

	/**巡检内容-巡检项ID*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "巡检内容-巡检项ID")
    private java.lang.String id;
	/**巡检内容-设备ID*/
	@Excel(name = "巡检内容-设备ID", width = 15)
    @ApiModelProperty(value = "巡检内容-设备ID")
    private java.lang.String contequiptid;
	/**巡检项ID*/
	@Excel(name = "巡检项ID", width = 15)
    @ApiModelProperty(value = "巡检项ID")
    private java.lang.String inspectitemid;
	/**所属部门*/
    @ApiModelProperty(value = "所属部门")
    private java.lang.String sysOrgCode;
}

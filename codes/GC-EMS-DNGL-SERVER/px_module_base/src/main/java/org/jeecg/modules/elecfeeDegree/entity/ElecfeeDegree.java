package org.jeecg.modules.elecfeeDegree.entity;

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
 * @Description: 电度电费
 * @@author 品讯科技
 * @Date:   2024-01-23
 * @Version: V1.0
 */
@Data
@TableName("base_elecfee_degree")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="base_elecfee_degree对象", description="电度电费")
public class ElecfeeDegree implements Serializable {
    private static final long serialVersionUID = 1L;

	/**电度电费ID*/
	@Excel(name = "电度电费ID", width = 15)
    @ApiModelProperty(value = "电度电费ID")
    private String id;
	/**创建者*/
    @ApiModelProperty(value = "创建者")
    @Dict(dictTable ="sys_user",dicText = "realname",dicCode = "username")
    private String createBy;
	/**创建时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
	/**更新者*/
    @ApiModelProperty(value = "更新者")
    private String updateBy;
	/**更新时间*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
	/**备注*/
	@Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private String remark;
	/**部门ID*/
	@Excel(name = "部门ID", width = 15)
    @Dict(dictTable ="sys_depart",dicText = "depart_name",dicCode = "id")
    @ApiModelProperty(value = "部门ID")
    private String deptid;
	/**计费类型：售电、光伏*/
	@Excel(name = "计费类型：售电、光伏", width = 15)
    @Dict(dicCode = "elecfee_degree_feetype")
    @ApiModelProperty(value = "计费类型：售电、光伏")
    private String feetype;
	/**目录电价(元/kw)*/
	@Excel(name = "目录电价(元/kw)", width = 15)
    @ApiModelProperty(value = "目录电价(元/kw)")
    private Double price;
	/**开始时间*/
	@Excel(name = "开始时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private Date begintime;
	/**结束时间*/
	@Excel(name = "结束时间", width = 15, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    private Date endtime;
}

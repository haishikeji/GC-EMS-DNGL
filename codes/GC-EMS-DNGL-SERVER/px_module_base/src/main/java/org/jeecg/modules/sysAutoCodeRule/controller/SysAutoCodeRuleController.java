package org.jeecg.modules.sysAutoCodeRule.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.sysAutoCodeRule.entity.SysAutoCodeRule;
import org.jeecg.modules.sysAutoCodeRule.service.ISysAutoCodeRuleService;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.def.NormalExcelConstants;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.jeecgframework.poi.excel.view.JeecgEntityExcelView;
import org.jeecg.common.system.base.controller.JeecgController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jeecg.common.aspect.annotation.AutoLog;

 /**
 * @Description: ems_sys_auto_code_rule
 * @@author 品讯科技
 * @Date:   2024-01-10
 * @Version: V1.0
 */
@Api(tags="ems_sys_auto_code_rule")
@RestController
@RequestMapping("/sysAutoCodeRule/sysAutoCodeRule")
@Slf4j
public class SysAutoCodeRuleController extends JeecgController<SysAutoCodeRule, ISysAutoCodeRuleService> {
	@Autowired
	private ISysAutoCodeRuleService sysAutoCodeRuleService;
	
	/**
	 * 分页列表查询
	 *
	 * @param sysAutoCodeRule
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "ems_sys_auto_code_rule-分页列表查询")
	@ApiOperation(value="ems_sys_auto_code_rule-分页列表查询", notes="ems_sys_auto_code_rule-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<SysAutoCodeRule>> queryPageList(SysAutoCodeRule sysAutoCodeRule,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<SysAutoCodeRule> queryWrapper = QueryGenerator.initQueryWrapper(sysAutoCodeRule, req.getParameterMap());
		Page<SysAutoCodeRule> page = new Page<SysAutoCodeRule>(pageNo, pageSize);
		IPage<SysAutoCodeRule> pageList = sysAutoCodeRuleService.page(page, queryWrapper);
		return Result.OK(pageList);
	}
	
	/**
	 *   添加
	 *
	 * @param sysAutoCodeRule
	 * @return
	 */
	@AutoLog(value = "ems_sys_auto_code_rule-添加")
	@ApiOperation(value="ems_sys_auto_code_rule-添加", notes="ems_sys_auto_code_rule-添加")
	//@RequiresPermissions("org.jeecg.modules:ems_sys_auto_code_rule:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody SysAutoCodeRule sysAutoCodeRule) {
		sysAutoCodeRuleService.save(sysAutoCodeRule);
		return Result.OK("添加成功！");
	}
	
	/**
	 *  编辑
	 *
	 * @param sysAutoCodeRule
	 * @return
	 */
	@AutoLog(value = "ems_sys_auto_code_rule-编辑")
	@ApiOperation(value="ems_sys_auto_code_rule-编辑", notes="ems_sys_auto_code_rule-编辑")
	//@RequiresPermissions("org.jeecg.modules:ems_sys_auto_code_rule:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody SysAutoCodeRule sysAutoCodeRule) {
		sysAutoCodeRuleService.updateById(sysAutoCodeRule);
		return Result.OK("编辑成功!");
	}
	
	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "ems_sys_auto_code_rule-通过id删除")
	@ApiOperation(value="ems_sys_auto_code_rule-通过id删除", notes="ems_sys_auto_code_rule-通过id删除")
	//@RequiresPermissions("org.jeecg.modules:ems_sys_auto_code_rule:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		sysAutoCodeRuleService.removeById(id);
		return Result.OK("删除成功!");
	}
	
	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "ems_sys_auto_code_rule-批量删除")
	@ApiOperation(value="ems_sys_auto_code_rule-批量删除", notes="ems_sys_auto_code_rule-批量删除")
	//@RequiresPermissions("org.jeecg.modules:ems_sys_auto_code_rule:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.sysAutoCodeRuleService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}
	
	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "ems_sys_auto_code_rule-通过id查询")
	@ApiOperation(value="ems_sys_auto_code_rule-通过id查询", notes="ems_sys_auto_code_rule-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<SysAutoCodeRule> queryById(@RequestParam(name="id",required=true) String id) {
		SysAutoCodeRule sysAutoCodeRule = sysAutoCodeRuleService.getById(id);
		if(sysAutoCodeRule==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(sysAutoCodeRule);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param sysAutoCodeRule
    */
    //@RequiresPermissions("org.jeecg.modules:ems_sys_auto_code_rule:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, SysAutoCodeRule sysAutoCodeRule) {
        return super.exportXls(request, sysAutoCodeRule, SysAutoCodeRule.class, "ems_sys_auto_code_rule");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("ems_sys_auto_code_rule:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, SysAutoCodeRule.class);
    }

}

package org.jeecg.modules.elecfeeRatio.controller;

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
import org.jeecg.modules.elecfeeRatio.entity.ElecfeeRatio;
import org.jeecg.modules.elecfeeRatio.service.IElecfeeRatioService;

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
 * @Description: 力调系数
 * @@author 品讯科技
 * @Date:   2024-01-23
 * @Version: V1.0
 */
@Api(tags="力调系数")
@RestController
@RequestMapping("/elecfeeRatio/elecfeeRatio")
@Slf4j
public class ElecfeeRatioController extends JeecgController<ElecfeeRatio, IElecfeeRatioService> {
	@Autowired
	private IElecfeeRatioService elecfeeRatioService;

	/**
	 * 分页列表查询
	 *
	 * @param elecfeeRatio
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "力调系数-分页列表查询")
	@ApiOperation(value="力调系数-分页列表查询", notes="力调系数-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<ElecfeeRatio>> queryPageList(ElecfeeRatio elecfeeRatio,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<ElecfeeRatio> queryWrapper = QueryGenerator.initQueryWrapper(elecfeeRatio, req.getParameterMap());
		Page<ElecfeeRatio> page = new Page<ElecfeeRatio>(pageNo, pageSize);
		IPage<ElecfeeRatio> pageList = elecfeeRatioService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param elecfeeRatio
	 * @return
	 */
	@AutoLog(value = "力调系数-添加")
	@ApiOperation(value="力调系数-添加", notes="力调系数-添加")
	//@RequiresPermissions("org.jeecg.modules:base_elecfee_ratio:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody ElecfeeRatio elecfeeRatio) {
		elecfeeRatioService.save(elecfeeRatio);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param elecfeeRatio
	 * @return
	 */
	@AutoLog(value = "力调系数-编辑")
	@ApiOperation(value="力调系数-编辑", notes="力调系数-编辑")
	//@RequiresPermissions("org.jeecg.modules:base_elecfee_ratio:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody ElecfeeRatio elecfeeRatio) {
		elecfeeRatioService.updateById(elecfeeRatio);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "力调系数-通过id删除")
	@ApiOperation(value="力调系数-通过id删除", notes="力调系数-通过id删除")
	//@RequiresPermissions("org.jeecg.modules:base_elecfee_ratio:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		elecfeeRatioService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "力调系数-批量删除")
	@ApiOperation(value="力调系数-批量删除", notes="力调系数-批量删除")
	//@RequiresPermissions("org.jeecg.modules:base_elecfee_ratio:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.elecfeeRatioService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "力调系数-通过id查询")
	@ApiOperation(value="力调系数-通过id查询", notes="力调系数-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<ElecfeeRatio> queryById(@RequestParam(name="id",required=true) String id) {
		ElecfeeRatio elecfeeRatio = elecfeeRatioService.getById(id);
		if(elecfeeRatio==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(elecfeeRatio);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param elecfeeRatio
    */
    //@RequiresPermissions("org.jeecg.modules:base_elecfee_ratio:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, ElecfeeRatio elecfeeRatio) {
        return super.exportXls(request, elecfeeRatio, ElecfeeRatio.class, "力调系数");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("base_elecfee_ratio:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, ElecfeeRatio.class);
    }

}

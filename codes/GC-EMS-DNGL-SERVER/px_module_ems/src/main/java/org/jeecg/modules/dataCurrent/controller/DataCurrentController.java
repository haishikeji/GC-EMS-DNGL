package org.jeecg.modules.dataCurrent.controller;

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
import org.jeecg.modules.dataCurrent.entity.DataCurrent;
import org.jeecg.modules.dataCurrent.service.IDataCurrentService;

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
 * @Description: 设备实时电流
 * @@author 品讯科技
 * @Date:   2024-01-19
 * @Version: V1.0
 */
@Api(tags="设备实时电流")
@RestController
@RequestMapping("/dataCurrent/dataCurrent")
@Slf4j
public class DataCurrentController extends JeecgController<DataCurrent, IDataCurrentService> {
	@Autowired
	private IDataCurrentService dataCurrentService;

	/**
	 * 分页列表查询
	 *
	 * @param dataCurrent
	 * @param pageNo
	 * @param pageSize
	 * @param req
	 * @return
	 */
	//@AutoLog(value = "设备实时电流-分页列表查询")
	@ApiOperation(value="设备实时电流-分页列表查询", notes="设备实时电流-分页列表查询")
	@GetMapping(value = "/list")
	public Result<IPage<DataCurrent>> queryPageList(DataCurrent dataCurrent,
								   @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
								   @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
								   HttpServletRequest req) {
		QueryWrapper<DataCurrent> queryWrapper = QueryGenerator.initQueryWrapper(dataCurrent, req.getParameterMap());
		Page<DataCurrent> page = new Page<DataCurrent>(pageNo, pageSize);
		IPage<DataCurrent> pageList = dataCurrentService.page(page, queryWrapper);
		return Result.OK(pageList);
	}

	/**
	 *   添加
	 *
	 * @param dataCurrent
	 * @return
	 */
	@AutoLog(value = "设备实时电流-添加")
	@ApiOperation(value="设备实时电流-添加", notes="设备实时电流-添加")
	//@RequiresPermissions("org.jeecg.modules:设备实时电流:add")
	@PostMapping(value = "/add")
	public Result<String> add(@RequestBody DataCurrent dataCurrent) {
		dataCurrentService.save(dataCurrent);
		return Result.OK("添加成功！");
	}

	/**
	 *  编辑
	 *
	 * @param dataCurrent
	 * @return
	 */
	@AutoLog(value = "设备实时电流-编辑")
	@ApiOperation(value="设备实时电流-编辑", notes="设备实时电流-编辑")
	//@RequiresPermissions("org.jeecg.modules:设备实时电流:edit")
	@RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
	public Result<String> edit(@RequestBody DataCurrent dataCurrent) {
		dataCurrentService.updateById(dataCurrent);
		return Result.OK("编辑成功!");
	}

	/**
	 *   通过id删除
	 *
	 * @param id
	 * @return
	 */
	@AutoLog(value = "设备实时电流-通过id删除")
	@ApiOperation(value="设备实时电流-通过id删除", notes="设备实时电流-通过id删除")
	//@RequiresPermissions("org.jeecg.modules:设备实时电流:delete")
	@DeleteMapping(value = "/delete")
	public Result<String> delete(@RequestParam(name="id",required=true) String id) {
		dataCurrentService.removeById(id);
		return Result.OK("删除成功!");
	}

	/**
	 *  批量删除
	 *
	 * @param ids
	 * @return
	 */
	@AutoLog(value = "设备实时电流-批量删除")
	@ApiOperation(value="设备实时电流-批量删除", notes="设备实时电流-批量删除")
	//@RequiresPermissions("org.jeecg.modules:设备实时电流:deleteBatch")
	@DeleteMapping(value = "/deleteBatch")
	public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
		this.dataCurrentService.removeByIds(Arrays.asList(ids.split(",")));
		return Result.OK("批量删除成功!");
	}

	/**
	 * 通过id查询
	 *
	 * @param id
	 * @return
	 */
	//@AutoLog(value = "设备实时电流-通过id查询")
	@ApiOperation(value="设备实时电流-通过id查询", notes="设备实时电流-通过id查询")
	@GetMapping(value = "/queryById")
	public Result<DataCurrent> queryById(@RequestParam(name="id",required=true) String id) {
		DataCurrent dataCurrent = dataCurrentService.getById(id);
		if(dataCurrent==null) {
			return Result.error("未找到对应数据");
		}
		return Result.OK(dataCurrent);
	}

    /**
    * 导出excel
    *
    * @param request
    * @param dataCurrent
    */
    //@RequiresPermissions("org.jeecg.modules:设备实时电流:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, DataCurrent dataCurrent) {
        return super.exportXls(request, dataCurrent, DataCurrent.class, "设备实时电流");
    }

    /**
      * 通过excel导入数据
    *
    * @param request
    * @param response
    * @return
    */
    //@RequiresPermissions("设备实时电流:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, DataCurrent.class);
    }

}

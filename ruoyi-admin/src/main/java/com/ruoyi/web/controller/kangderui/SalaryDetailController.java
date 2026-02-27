package com.ruoyi.web.controller.kangderui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.common.exception.ServiceException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.kangderui.domain.SalaryDetail;
import com.ruoyi.kangderui.service.ISalaryDetailService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.domain.model.LoginUser;

/**
 * 员工工资明细Controller
 *
 * @author ruoyi
 * @date 2026-02-02
 */
@RestController
@RequestMapping("/kangderui/detail")
public class SalaryDetailController extends BaseController {

    @Autowired
    private ISalaryDetailService salaryDetailService;

    @Autowired
    ISysUserService iSysUserService;

    private HashMap cells = null;//存放每列index值
    private HashMap usersMap = new HashMap();//存放所有用户
    /**
     * 查询员工工资明细列表
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:list')")
    @GetMapping("/list")
    public TableDataInfo list(SalaryDetail salaryDetail) {
        checkPasswordUpdatedForSalaryView();
        applyUserScope(salaryDetail);
        startPage();
        List<SalaryDetail> list = salaryDetailService.selectSalaryDetailList(salaryDetail);
        return getDataTable(list);
    }

    /**
     * 查询我的工资明细列表
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:my')")
    @GetMapping("/my/list")
    public TableDataInfo myList(SalaryDetail salaryDetail) {
        checkPasswordUpdatedForSalaryView();
        if (salaryDetail == null) {
            salaryDetail = new SalaryDetail();
        }
        salaryDetail.setUserId(getUserId());
        startPage();
        List<SalaryDetail> list = salaryDetailService.selectSalaryDetailList(salaryDetail);
        return getDataTable(list);
    }

    /**
     * 导出员工工资明细列表
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:export')")
    @Log(title = "员工工资明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SalaryDetail salaryDetail) {
        checkPasswordUpdatedForSalaryView();
        applyUserScope(salaryDetail);
        List<SalaryDetail> list = salaryDetailService.selectSalaryDetailList(salaryDetail);
        ExcelUtil<SalaryDetail> util = new ExcelUtil<SalaryDetail>(SalaryDetail.class);
        util.exportExcel(response, list, "员工工资明细数据");
    }

    /**
     * 导入员工工资明细数据
     */
    @Log(title = "员工工资明细", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('kangderui:detail:import')")
    @PostMapping("/importData")
    public AjaxResult importData(MultipartFile file) throws Exception {
        //初始化查询出所有用户ID
        List<SysUser> usersList = iSysUserService.selectUserList(new SysUser());
        if(null != usersList) {
            for (SysUser users : usersList) {
                usersMap.put(users.getNickName(), users);
            }
        }
        Object deptNameValue = getExcelCellValue(file, 0, 0, 0);
        Object salaryPeriod = getExcelCellValue(file, 0, 1, 7);
        List<SalaryDetail> detailList = parseExcelToMapList(file, 4,deptNameValue,salaryPeriod);
//        mapToSalaryDetails(sheet, deptNameValue, salaryPeriod);
        String operName = getUsername();
        String message = salaryDetailService.importSalaryDetail(detailList, operName);
        //清空excel列值缓存
        cells = null;
        return success(message);
    }

    /**
     * 导入模板下载
     */
    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response) {
        ExcelUtil<SalaryDetail> util = new ExcelUtil<SalaryDetail>(SalaryDetail.class);
        util.importTemplateExcel(response, "员工工资明细数据");
    }

    /**
     * 获取员工工资明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:query')")
    @GetMapping(value = "/{salaryDetailId}")
    public AjaxResult getInfo(@PathVariable("salaryDetailId") Long salaryDetailId) {
        checkPasswordUpdatedForSalaryView();
        SalaryDetail detail = salaryDetailService.selectSalaryDetailBySalaryDetailId(salaryDetailId);
        if (detail == null) {
            return error("数据不存在");
        }
        if (!isAdminUser() && detail.getUserId() != null && !detail.getUserId().equals(getUserId())) {
            return error("没有权限查看该工资明细");
        }
        return success(detail);
    }

    /**
     * 新增员工工资明细
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:add')")
    @Log(title = "员工工资明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SalaryDetail salaryDetail) {
        if (!isAdminUser()) {
            salaryDetail.setUserId(getUserId());
        }
        return toAjax(salaryDetailService.insertSalaryDetail(salaryDetail));
    }

    /**
     * 修改员工工资明细
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:edit')")
    @Log(title = "员工工资明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SalaryDetail salaryDetail) {
        if (!isAdminUser()) {
            return error("没有权限修改工资明细");
        }
        return toAjax(salaryDetailService.updateSalaryDetail(salaryDetail));
    }

    /**
     * 员工确认工资无误
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:confirm')")
    @Log(title = "员工工资确认", businessType = BusinessType.UPDATE)
    @PutMapping("/confirm/{salaryDetailId}")
    public AjaxResult confirm(@PathVariable("salaryDetailId") Long salaryDetailId) {
        checkPasswordUpdatedForSalaryView();
        SalaryDetail detail = salaryDetailService.selectSalaryDetailBySalaryDetailId(salaryDetailId);
        if (detail == null) {
            return error("数据不存在");
        }
        if (!isAdminUser() && detail.getUserId() != null && !detail.getUserId().equals(getUserId())) {
            return error("没有权限确认该工资");
        }
        if (StringUtils.equals("1", detail.getRemark())) {
            return success("已确认");
        }
        SalaryDetail update = new SalaryDetail();
        update.setSalaryDetailId(salaryDetailId);
        update.setRemark("1");
        update.setUpdateBy(getUsername());
        return toAjax(salaryDetailService.updateSalaryDetail(update));
    }

    /**
     * 删除员工工资明细
     */
    @PreAuthorize("@ss.hasPermi('kangderui:detail:remove')")
    @Log(title = "员工工资明细", businessType = BusinessType.DELETE)
    @DeleteMapping("/{salaryDetailIds}")
    public AjaxResult remove(@PathVariable Long[] salaryDetailIds) {
        return toAjax(salaryDetailService.deleteSalaryDetailBySalaryDetailIds(salaryDetailIds));
    }

    private List<SalaryDetail>  parseExcelToMapList(MultipartFile file, int titleRowIndex,Object deptNameValue,Object salaryPeriod) throws Exception {
        if(null==cells)
            cells = new HashMap<>();
        List<SalaryDetail> list=new ArrayList<>();
       cells.put("姓名",2);
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
//            if (sheet == null) {
//                return cells;
//            }
            Row headerRow1 = sheet.getRow(titleRowIndex-1);//主要列名称

//            Row headerRow2 = sheet.getRow(titleRowIndex);//列名称2：工资卡号,日工资,工作日,天数,金额,天数,金额,天数,金额
//            if (headerRow2 == null) {
//                return cells;
//            }

            int lastCellNum = headerRow1.getPhysicalNumberOfCells();
//            List<Integer> headers = new ArrayList<>();
            for (int i = 0; i < lastCellNum; i++) {
//                headers.add(i);

                // 检查列是否隐藏
                boolean isColumnHidden = false;
                if (sheet instanceof XSSFSheet) {
                    // 对于 .xlsx 文件
                    isColumnHidden = ((XSSFSheet) sheet).isColumnHidden(i);
                } else if (sheet instanceof HSSFSheet) {
                    // 对于 .xls 文件
                    isColumnHidden = ((HSSFSheet) sheet).isColumnHidden((short) i);
                }

                String cellValue1=headerRow1.getCell(i).getStringCellValue();
//                String cellValue2=headerRow2.getCell(i).getStringCellValue();
                if(!isColumnHidden && null!= cellValue1 && !"".equals(cellValue1)){
                    cellValue1=cellValue1.trim();
//                    System.out.println("j==="+i+" : "+headerRow1.getCell(i).getStringCellValue().trim());
                    if("基本工资".equals(cellValue1)){
                        cells.put("基本工资",i);
                    }
                    if("日工资".equals(cellValue1)){
                        cells.put("日工资",i);
                        cells.put("工作日",i+1);
                        cells.put("应发工资小计",i+2);
                    }
                    if("全勤奖".equals(cellValue1)){
                        cells.put("全勤奖",i);
                    }
                    if("安全奖".equals(cellValue1)){
                        cells.put("安全奖",i);
                    }
                    if("工龄工资".equals(cellValue1)){
                        cells.put("工龄工资",i);
                    }
                    if("职务工资".equals(cellValue1)){
                        cells.put("职务工资",i);
                    }
                    if("浮动工资".equals(cellValue1)){
                        cells.put("浮动工资",i);
                    }
                    if("保密工资".equals(cellValue1)){
                        cells.put("保密工资",i);
                    }
                    if("交通补贴".equals(cellValue1)){
                        cells.put("交通补贴",i);
                    }
                    if("特种作业证补贴".equals(cellValue1)){
                        cells.put("特种作业证补贴",i);
                    }
                    if("节假日补贴".equals(cellValue1)){
                        cells.put("节假日补贴",i);
                    }
                    if("工作表现奖".equals(cellValue1)){
                        cells.put("工作表现奖",i);
                    }
                    if("安全培训补贴".equals(cellValue1)){
                        cells.put("安全培训补贴",i);
                    }
                    if("高温费补贴".equals(cellValue1)){
                        cells.put("高温费补贴",i);
                    }
                    if("绩效考核奖".equals(cellValue1)){
                        cells.put("绩效考核奖",i);
                    }
                    if("加班工资120元/天".equals(cellValue1)){
                        cells.put("加班天数",i);
                        cells.put("加班金额",i+1);
                    }
                    if("中班补贴10元/天".equals(cellValue1)){
                        cells.put("中班天数",i);
                        cells.put("中班金额",i+1);
                    }
                    if("夜班补贴15元/天".equals(cellValue1)){
                        cells.put("夜班天数",i);
                        cells.put("夜班金额",i+1);
                        cells.put("其它应发小计",i+2);
                    }
                    if("应发金额".equals(cellValue1)){
                        cells.put("应发金额",i);
                    }
                    if("违纪扣款".equals(cellValue1)){
                        cells.put("违纪扣款",i);
                    }
                    if("个人所得税".equals(cellValue1)){
                        cells.put("个人所得税",i);
                    }
                    if("代扣公积金".equals(cellValue1)){
                        cells.put("代扣公积金",i);
                    }
                    if("代扣代缴保险".equals(cellValue1)){
                        cells.put("代扣代缴保险",i);
                    }
                    if("暂扣工资".equals(cellValue1)){
                        cells.put("暂扣工资",i);
                        cells.put("应扣部分小计",i+1);
                        cells.put("实发金额",i+2);
                    }
                }
//                if (!isColumnHidden && null != cellValue2 && !"".equals(cellValue2))
//
//                    System.out.println("j="+i+" : "+headerRow2.getCell(i).getStringCellValue().trim());
            }

           list = mapToSalaryDetails(  sheet,   deptNameValue,   salaryPeriod,  titleRowIndex, lastCellNum);
        }
        return list;
    }

    private void applyUserScope(SalaryDetail salaryDetail) {
        if (salaryDetail == null) {
            return;
        }
        if (!isAdminUser()) {
            salaryDetail.setUserId(getUserId());
        }
    }

    private void checkPasswordUpdatedForSalaryView() {
        if (isAdminUser()) {
            return;
        }
        LoginUser loginUser = getLoginUser();
        SysUser user = loginUser != null ? loginUser.getUser() : null;
        if (user == null || user.getPwdUpdateDate() == null) {
            throw new ServiceException("请先修改初始密码后再查看工资");
        }
    }

    private boolean isAdminUser() {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null || loginUser.getUser() == null) {
            return false;
        }
        if (loginUser.getUser().isAdmin()) {
            return true;
        }
        Long deptId = loginUser.getUser().getDeptId();
        return deptId != null && (deptId == 100L || deptId == 109L);
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK && StringUtils.isNotEmpty(Convert.toStr(getCellValue(cell)))) {
                return false;
            }
        }
        return true;
    }

    private Object getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            cellType = cell.getCachedFormulaResultType();
        }
        switch (cellType) {
            case STRING:
                return StringUtils.trimToEmpty(cell.getStringCellValue());
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();
                }
                return cell.getNumericCellValue();
            case BLANK:
                return null;
            default:
                return StringUtils.trimToEmpty(cell.toString());
        }
    }

    private Object getExcelCellValue(MultipartFile file, int sheetIndex, int rowIndex, int columnIndex) throws Exception {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(sheetIndex);
            if (sheet == null) {
                return null;
            }
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                return null;
            }
            Cell cell = row.getCell(columnIndex);
            return getCellValue(cell);
        }
    }

    private List<SalaryDetail> mapToSalaryDetails(Sheet sheet, Object deptNameValue, Object salaryPeriod,int titleRowIndex,int lastCellNum) {
        List<SalaryDetail> details = new ArrayList<>();
        if (null == sheet ) {
            return details;
        }
//        for (Map<Integer, Object> rowMap : rowMaps) {

        int lastRowNum = sheet.getLastRowNum();


//遍历excel存到实体类
        for (int i = titleRowIndex + 1; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (isRowEmpty(row)) {
                continue;
            }
//                Map<Integer, Object> rowMap = new LinkedHashMap<>();
//            for (int j = 0; j < lastCellNum; j++) {
//                    if (row.getCell(j) == null || row.getCell(j).getCellType()== CellType.BLANK) {
//                        continue;
//                    }
//                rowMap.put(j, getCellValue(row.getCell(j)));

//            rows.add(rowMap);


                if ("合计：".equals(String.valueOf(row.getCell(0)))) {
                    break;
                }
                if (String.valueOf(row.getCell(2)).contains("制表")) {
                    continue;
                }
                SalaryDetail detail = new SalaryDetail();
                //姓名
//                detail.setNickName(ObjectUtils.isEmpty(rowMap.get(2)) ? null : Convert.toStr(rowMap.get(2)));
                detail.setNickName( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("姓名")))) ? null :Convert.toStr(row.getCell(Convert.toInt(cells.get("姓名")))));

                if(null!=detail.getNickName()&&!"".equals(detail.getNickName())) {

                    if (null != usersMap) {
                        Object user=usersMap.get(detail.getNickName());
                        if(null!=user) {
                            //用户ID
                            detail.setUserId(((SysUser) user).getUserId());
                            //部门ID
                            detail.setDeptId(((SysUser) user).getDeptId());
                        }else{
                            logger.error("姓名："+detail.getNickName()+"不存在！");
                        }
                    } else {
                        SysUser sysUser = iSysUserService.selectUserByNickName(detail.getNickName());
                        if(null!=sysUser) {
                            detail.setUserId(sysUser.getUserId());
                            detail.setDeptId(sysUser.getDeptId());
                        }
                    }
                }
                //工资卡号
                detail.setBankCardNumber("");

                //部门名称
                detail.setDeptName(ObjectUtils.isEmpty(deptNameValue) ? null : Convert.toStr(deptNameValue).replace("工资计算表", "").replace("人员", ""));
                //工资所属期
                detail.setSalaryPeriod(ObjectUtils.isEmpty(salaryPeriod) ? null : Convert.toStr(salaryPeriod).replace("所属期：", ""));
                //基本工资
//                detail.setBasicSalary(ObjectUtils.isEmpty(rowMap.get(3)) ? null : Convert.toBigDecimal(rowMap.get(3)));
                detail.setBasicSalary( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("基本工资")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("基本工资")))));
                //日工资-基本工资/26
            BigDecimal basicSalary=detail.getBasicSalary();
            if(null!=basicSalary && 0!=basicSalary.doubleValue()) {
                detail.setBasicDailySalary(basicSalary.divide(new BigDecimal(26), 2, RoundingMode.HALF_UP));
            }else {
                detail.setBasicDailySalary(null);
            }
                //工作日
                detail.setBasicWorkDays( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("工作日")))) ? null :Convert.toLong(row.getCell(Convert.toInt(cells.get("工作日")))));
                //基本工资小计
            if(null!=basicSalary && 0!=basicSalary.doubleValue()) {
                detail.setBasicSubtotal(detail.getBasicDailySalary().multiply(new BigDecimal(detail.getBasicWorkDays())));
            }else{
                detail.setBasicSubtotal(null);
            }
                //全勤奖
                detail.setAllowanceFullAttendance( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("全勤奖")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("全勤奖")))));
                //安全奖
                detail.setAllowanceSafety( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("安全奖")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("安全奖")))));
                //工龄工资
                detail.setAllowanceSeniority( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("工龄工资")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("工龄工资")))));
                //职务工资
                detail.setAllowancePosition( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("职务工资")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("职务工资")))));
                //浮动工资
                detail.setAllowanceFloating( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("浮动工资")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("浮动工资")))));
                //保密工资
                detail.setAllowanceConfidentiality( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("保密工资")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("保密工资")))));
                //交通补贴
                detail.setAllowanceTransportation( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("交通补贴")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("交通补贴")))));
                //特种作业证补贴
                detail.setAllowanceSpecialCertificate( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("特种作业证补贴")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("特种作业证补贴")))));
                //节假日补贴
                detail.setAllowanceHoliday( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("节假日补贴")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("节假日补贴")))));
                //工作表现奖
                detail.setAllowancePerformance( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("工作表现奖")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("工作表现奖")))));
                //安全培训补贴
                detail.setAllowanceSafetyTraining( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("安全培训补贴")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("安全培训补贴")))));
                //高温费补贴
                detail.setAllowanceHighTemperature( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("高温费补贴")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("高温费补贴")))));
                //绩效考核奖
                detail.setAllowanceAssessment( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("绩效考核奖")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("绩效考核奖")))));
                //加班天数
                detail.setOvertimeDays( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("加班天数")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("加班天数")))));
                //加班金额
                detail.setOvertimeAmount( ObjectUtils.isEmpty(detail.getOvertimeDays())?null:detail.getOvertimeDays().multiply(new BigDecimal(120)));
                //中班天数
                detail.setOvertimeMidShiftDays( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("中班天数")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("中班天数")))));
                //中班金额
                detail.setOvertimeMidShiftAmount( ObjectUtils.isEmpty(detail.getOvertimeMidShiftDays())?null:detail.getOvertimeMidShiftDays().multiply(new BigDecimal(10)));
                //夜班天数
                detail.setOvertimeNightShiftDays( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("夜班天数")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("夜班天数")))));
                //夜班金额
                detail.setOvertimeNightShiftAmount(ObjectUtils.isEmpty(detail.getOvertimeNightShiftDays())?null:detail.getOvertimeNightShiftDays().multiply(new BigDecimal(15)));
                //其它应发小计
                detail.setAllowanceSubtotal(
                        (ObjectUtils.isEmpty(detail.getOvertimeNightShiftAmount())?new BigDecimal(0):detail.getOvertimeNightShiftAmount())
                        .add(ObjectUtils.isEmpty(detail.getOvertimeMidShiftAmount())?new BigDecimal(0):detail.getOvertimeMidShiftAmount())

                        .add((ObjectUtils.isEmpty(detail.getOvertimeAmount())?new BigDecimal(0):detail.getOvertimeAmount()))
                         .add((ObjectUtils.isEmpty(detail.getAllowanceAssessment())?new BigDecimal(0):detail.getAllowanceAssessment()))
                           .add((ObjectUtils.isEmpty(detail.getAllowanceHighTemperature())?new BigDecimal(0):detail.getAllowanceHighTemperature()))
                            .add((ObjectUtils.isEmpty(detail.getAllowanceSafetyTraining())?new BigDecimal(0):detail.getAllowanceSafetyTraining()))
                               .add((ObjectUtils.isEmpty(detail.getAllowancePerformance())?new BigDecimal(0):detail.getAllowancePerformance()))
                                 .add((ObjectUtils.isEmpty(detail.getAllowanceHoliday())?new BigDecimal(0):detail.getAllowanceHoliday()))
                                 .add((ObjectUtils.isEmpty(detail.getAllowanceSpecialCertificate())?new BigDecimal(0):detail.getAllowanceSpecialCertificate()))
                                .add((ObjectUtils.isEmpty(detail.getAllowanceTransportation())?new BigDecimal(0):detail.getAllowanceTransportation()))
                                .add((ObjectUtils.isEmpty(detail.getAllowanceConfidentiality())?new BigDecimal(0):detail.getAllowanceConfidentiality()))
                                .add((ObjectUtils.isEmpty(detail.getAllowanceFloating())?new BigDecimal(0):detail.getAllowanceFloating()))
                                 .add((ObjectUtils.isEmpty(detail.getAllowancePosition())?new BigDecimal(0):detail.getAllowancePosition()))
                                .add((ObjectUtils.isEmpty(detail.getAllowanceSeniority())?new BigDecimal(0):detail.getAllowanceSeniority()))
                                 .add((ObjectUtils.isEmpty(detail.getAllowanceSafety())?new BigDecimal(0):detail.getAllowanceSafety()))
                                 .add((ObjectUtils.isEmpty(detail.getAllowanceFullAttendance())?new BigDecimal(0):detail.getAllowanceFullAttendance()))

                );
                //应发金额
                detail.setTotalEarnings(
                        (ObjectUtils.isEmpty(detail.getBasicSubtotal())?new BigDecimal(0):detail.getBasicSubtotal())
                                .add((ObjectUtils.isEmpty(detail.getAllowanceSubtotal())?new BigDecimal(0):detail.getAllowanceSubtotal())));
                //违纪扣款
                detail.setDeductionDiscipline( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("违纪扣款")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("违纪扣款")))));
                //个人所得税
                detail.setDeductionTax( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("个人所得税")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("个人所得税")))));
                //代扣公积金
                detail.setDeductionHousingFund( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("代扣公积金")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("代扣公积金")))));
                //代扣代缴保险
                detail.setDeductionInsurance( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("代扣代缴保险")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("代扣代缴保险")))));
                //暂扣工资
                detail.setDeductionWithhold( ObjectUtils.isEmpty(row.getCell(Convert.toInt(cells.get("暂扣工资")))) ? null :Convert.toBigDecimal(row.getCell(Convert.toInt(cells.get("暂扣工资")))));
                //应扣部分小计
                detail.setDeductionSubtotal(
                        (ObjectUtils.isEmpty(detail.getDeductionDiscipline())?new BigDecimal(0):detail.getDeductionDiscipline())
                                .add((ObjectUtils.isEmpty(detail.getDeductionTax())?new BigDecimal(0):detail.getDeductionTax()))
                                .add((ObjectUtils.isEmpty(detail.getDeductionHousingFund())?new BigDecimal(0):detail.getDeductionHousingFund()))
                                 .add((ObjectUtils.isEmpty(detail.getDeductionInsurance())?new BigDecimal(0):detail.getDeductionInsurance()))
                                .add((ObjectUtils.isEmpty(detail.getDeductionWithhold())?new BigDecimal(0):detail.getDeductionWithhold()))

                );
                //实发金额
                detail.setNetSalary(
                        (ObjectUtils.isEmpty(detail.getBasicSubtotal())?new BigDecimal(0):detail.getBasicSubtotal())
                                .add((ObjectUtils.isEmpty(detail.getAllowanceSubtotal())?new BigDecimal(0):detail.getAllowanceSubtotal()))
                                .subtract((ObjectUtils.isEmpty(detail.getDeductionSubtotal())?new BigDecimal(0):detail.getDeductionSubtotal())));
                details.add(detail);
            }
//        }

        return details;
    }
}

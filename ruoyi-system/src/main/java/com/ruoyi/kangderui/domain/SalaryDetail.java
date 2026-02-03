package com.ruoyi.kangderui.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 员工工资明细对象 salary_detail
 * 
 * @author ruoyi
 * @date 2026-02-02
 */
public class SalaryDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long salaryDetailId;

    /** 员工ID（关联员工表，可为空） */
    @Excel(name = "员工ID", readConverterExp = "关=联员工表，可为空")
    private Long userId;

    /** 姓名 */
    @Excel(name = "姓名")
    private String nickName;

    /** 工资卡号 */
    @Excel(name = "工资卡号")
    private String bankCardNumber;

    /** 部门ID */
    @Excel(name = "部门ID")
    private Long deptId;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 工资所属期（如：2025-12） */
    @Excel(name = "工资所属期", readConverterExp = "如=：2025-12")
    private String salaryPeriod;

    /** 基本工资 */
    @Excel(name = "基本工资")
    private BigDecimal basicSalary;

    /** 日工资 */
    @Excel(name = "日工资")
    private BigDecimal basicDailySalary;

    /** 工作日 */
    @Excel(name = "工作日")
    private Long basicWorkDays;

    /** 基本工资小计 */
    @Excel(name = "基本工资小计")
    private BigDecimal basicSubtotal;

    /** 全勤奖 */
    @Excel(name = "全勤奖")
    private BigDecimal allowanceFullAttendance;

    /** 安全奖 */
    @Excel(name = "安全奖")
    private BigDecimal allowanceSafety;

    /** 工龄工资 */
    @Excel(name = "工龄工资")
    private BigDecimal allowanceSeniority;

    /** 职务工资 */
    @Excel(name = "职务工资")
    private BigDecimal allowancePosition;

    /** 浮动工资 */
    @Excel(name = "浮动工资")
    private BigDecimal allowanceFloating;

    /** 保密工资 */
    @Excel(name = "保密工资")
    private BigDecimal allowanceConfidentiality;

    /** 交通补贴 */
    @Excel(name = "交通补贴")
    private BigDecimal allowanceTransportation;

    /** 特种作业证补贴 */
    @Excel(name = "特种作业证补贴")
    private BigDecimal allowanceSpecialCertificate;

    /** 节假日补贴 */
    @Excel(name = "节假日补贴")
    private BigDecimal allowanceHoliday;

    /** 工作表现奖 */
    @Excel(name = "工作表现奖")
    private BigDecimal allowancePerformance;

    /** 安全培训补贴 */
    @Excel(name = "安全培训补贴")
    private BigDecimal allowanceSafetyTraining;

    /** 绩效考核奖 */
    @Excel(name = "绩效考核奖")
    private BigDecimal allowanceAssessment;

    /** 加班天数 */
    @Excel(name = "加班天数")
    private BigDecimal overtimeDays;

    /** 加班金额 */
    @Excel(name = "加班金额")
    private BigDecimal overtimeAmount;

    /** 中班天数 */
    @Excel(name = "中班天数")
    private BigDecimal overtimeMidShiftDays;

    /** 中班补贴金额 */
    @Excel(name = "中班补贴金额")
    private BigDecimal overtimeMidShiftAmount;

    /** 夜班天数 */
    @Excel(name = "夜班天数")
    private BigDecimal overtimeNightShiftDays;

    /** 夜班补贴金额 */
    @Excel(name = "夜班补贴金额")
    private BigDecimal overtimeNightShiftAmount;

    /** 其它应发小计 */
    @Excel(name = "其它应发小计")
    private BigDecimal allowanceSubtotal;

    /** 应发金额 */
    @Excel(name = "应发金额")
    private BigDecimal totalEarnings;

    /** 违纪扣款 */
    @Excel(name = "违纪扣款")
    private BigDecimal deductionDiscipline;

    /** 个人所得税 */
    @Excel(name = "个人所得税")
    private BigDecimal deductionTax;

    /** 代扣公积金 */
    @Excel(name = "代扣公积金")
    private BigDecimal deductionHousingFund;

    /** 代扣代缴保险 */
    @Excel(name = "代扣代缴保险")
    private BigDecimal deductionInsurance;

    /** 暂扣工资 */
    @Excel(name = "暂扣工资")
    private BigDecimal deductionWithhold;

    /** 应扣小计 */
    @Excel(name = "应扣小计")
    private BigDecimal deductionSubtotal;

    /** 实发金额 */
    @Excel(name = "实发金额")
    private BigDecimal netSalary;

    public void setSalaryDetailId(Long salaryDetailId) 
    {
        this.salaryDetailId = salaryDetailId;
    }

    public Long getSalaryDetailId() 
    {
        return salaryDetailId;
    }

    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }

    public void setNickName(String nickName) 
    {
        this.nickName = nickName;
    }

    public String getNickName() 
    {
        return nickName;
    }

    public void setBankCardNumber(String bankCardNumber) 
    {
        this.bankCardNumber = bankCardNumber;
    }

    public String getBankCardNumber() 
    {
        return bankCardNumber;
    }

    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }

    public void setDeptName(String deptName) 
    {
        this.deptName = deptName;
    }

    public String getDeptName() 
    {
        return deptName;
    }

    public void setSalaryPeriod(String salaryPeriod) 
    {
        this.salaryPeriod = salaryPeriod;
    }

    public String getSalaryPeriod() 
    {
        return salaryPeriod;
    }

    public void setBasicSalary(BigDecimal basicSalary) 
    {
        this.basicSalary = basicSalary;
    }

    public BigDecimal getBasicSalary() 
    {
        return basicSalary;
    }

    public void setBasicDailySalary(BigDecimal basicDailySalary) 
    {
        this.basicDailySalary = basicDailySalary;
    }

    public BigDecimal getBasicDailySalary() 
    {
        return basicDailySalary;
    }

    public void setBasicWorkDays(Long basicWorkDays) 
    {
        this.basicWorkDays = basicWorkDays;
    }

    public Long getBasicWorkDays() 
    {
        return basicWorkDays;
    }

    public void setBasicSubtotal(BigDecimal basicSubtotal) 
    {
        this.basicSubtotal = basicSubtotal;
    }

    public BigDecimal getBasicSubtotal() 
    {
        return basicSubtotal;
    }

    public void setAllowanceFullAttendance(BigDecimal allowanceFullAttendance) 
    {
        this.allowanceFullAttendance = allowanceFullAttendance;
    }

    public BigDecimal getAllowanceFullAttendance() 
    {
        return allowanceFullAttendance;
    }

    public void setAllowanceSafety(BigDecimal allowanceSafety) 
    {
        this.allowanceSafety = allowanceSafety;
    }

    public BigDecimal getAllowanceSafety() 
    {
        return allowanceSafety;
    }

    public void setAllowanceSeniority(BigDecimal allowanceSeniority) 
    {
        this.allowanceSeniority = allowanceSeniority;
    }

    public BigDecimal getAllowanceSeniority() 
    {
        return allowanceSeniority;
    }

    public void setAllowancePosition(BigDecimal allowancePosition) 
    {
        this.allowancePosition = allowancePosition;
    }

    public BigDecimal getAllowancePosition() 
    {
        return allowancePosition;
    }

    public void setAllowanceFloating(BigDecimal allowanceFloating) 
    {
        this.allowanceFloating = allowanceFloating;
    }

    public BigDecimal getAllowanceFloating() 
    {
        return allowanceFloating;
    }

    public void setAllowanceConfidentiality(BigDecimal allowanceConfidentiality) 
    {
        this.allowanceConfidentiality = allowanceConfidentiality;
    }

    public BigDecimal getAllowanceConfidentiality() 
    {
        return allowanceConfidentiality;
    }

    public void setAllowanceTransportation(BigDecimal allowanceTransportation) 
    {
        this.allowanceTransportation = allowanceTransportation;
    }

    public BigDecimal getAllowanceTransportation() 
    {
        return allowanceTransportation;
    }

    public void setAllowanceSpecialCertificate(BigDecimal allowanceSpecialCertificate) 
    {
        this.allowanceSpecialCertificate = allowanceSpecialCertificate;
    }

    public BigDecimal getAllowanceSpecialCertificate() 
    {
        return allowanceSpecialCertificate;
    }

    public void setAllowanceHoliday(BigDecimal allowanceHoliday) 
    {
        this.allowanceHoliday = allowanceHoliday;
    }

    public BigDecimal getAllowanceHoliday() 
    {
        return allowanceHoliday;
    }

    public void setAllowancePerformance(BigDecimal allowancePerformance) 
    {
        this.allowancePerformance = allowancePerformance;
    }

    public BigDecimal getAllowancePerformance() 
    {
        return allowancePerformance;
    }

    public void setAllowanceSafetyTraining(BigDecimal allowanceSafetyTraining) 
    {
        this.allowanceSafetyTraining = allowanceSafetyTraining;
    }

    public BigDecimal getAllowanceSafetyTraining() 
    {
        return allowanceSafetyTraining;
    }

    public void setAllowanceAssessment(BigDecimal allowanceAssessment) 
    {
        this.allowanceAssessment = allowanceAssessment;
    }

    public BigDecimal getAllowanceAssessment() 
    {
        return allowanceAssessment;
    }

    public void setOvertimeDays(BigDecimal overtimeDays) 
    {
        this.overtimeDays = overtimeDays;
    }

    public BigDecimal getOvertimeDays() 
    {
        return overtimeDays;
    }

    public void setOvertimeAmount(BigDecimal overtimeAmount) 
    {
        this.overtimeAmount = overtimeAmount;
    }

    public BigDecimal getOvertimeAmount() 
    {
        return overtimeAmount;
    }

    public void setOvertimeMidShiftDays(BigDecimal overtimeMidShiftDays) 
    {
        this.overtimeMidShiftDays = overtimeMidShiftDays;
    }

    public BigDecimal getOvertimeMidShiftDays() 
    {
        return overtimeMidShiftDays;
    }

    public void setOvertimeMidShiftAmount(BigDecimal overtimeMidShiftAmount) 
    {
        this.overtimeMidShiftAmount = overtimeMidShiftAmount;
    }

    public BigDecimal getOvertimeMidShiftAmount() 
    {
        return overtimeMidShiftAmount;
    }

    public void setOvertimeNightShiftDays(BigDecimal overtimeNightShiftDays) 
    {
        this.overtimeNightShiftDays = overtimeNightShiftDays;
    }

    public BigDecimal getOvertimeNightShiftDays() 
    {
        return overtimeNightShiftDays;
    }

    public void setOvertimeNightShiftAmount(BigDecimal overtimeNightShiftAmount) 
    {
        this.overtimeNightShiftAmount = overtimeNightShiftAmount;
    }

    public BigDecimal getOvertimeNightShiftAmount() 
    {
        return overtimeNightShiftAmount;
    }

    public void setAllowanceSubtotal(BigDecimal allowanceSubtotal) 
    {
        this.allowanceSubtotal = allowanceSubtotal;
    }

    public BigDecimal getAllowanceSubtotal() 
    {
        return allowanceSubtotal;
    }

    public void setTotalEarnings(BigDecimal totalEarnings) 
    {
        this.totalEarnings = totalEarnings;
    }

    public BigDecimal getTotalEarnings() 
    {
        return totalEarnings;
    }

    public void setDeductionDiscipline(BigDecimal deductionDiscipline) 
    {
        this.deductionDiscipline = deductionDiscipline;
    }

    public BigDecimal getDeductionDiscipline() 
    {
        return deductionDiscipline;
    }

    public void setDeductionTax(BigDecimal deductionTax) 
    {
        this.deductionTax = deductionTax;
    }

    public BigDecimal getDeductionTax() 
    {
        return deductionTax;
    }

    public void setDeductionHousingFund(BigDecimal deductionHousingFund) 
    {
        this.deductionHousingFund = deductionHousingFund;
    }

    public BigDecimal getDeductionHousingFund() 
    {
        return deductionHousingFund;
    }

    public void setDeductionInsurance(BigDecimal deductionInsurance) 
    {
        this.deductionInsurance = deductionInsurance;
    }

    public BigDecimal getDeductionInsurance() 
    {
        return deductionInsurance;
    }

    public void setDeductionWithhold(BigDecimal deductionWithhold) 
    {
        this.deductionWithhold = deductionWithhold;
    }

    public BigDecimal getDeductionWithhold() 
    {
        return deductionWithhold;
    }

    public void setDeductionSubtotal(BigDecimal deductionSubtotal) 
    {
        this.deductionSubtotal = deductionSubtotal;
    }

    public BigDecimal getDeductionSubtotal() 
    {
        return deductionSubtotal;
    }

    public void setNetSalary(BigDecimal netSalary) 
    {
        this.netSalary = netSalary;
    }

    public BigDecimal getNetSalary() 
    {
        return netSalary;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("salaryDetailId", getSalaryDetailId())
            .append("userId", getUserId())
            .append("nickName", getNickName())
            .append("bankCardNumber", getBankCardNumber())
            .append("deptId", getDeptId())
            .append("deptName", getDeptName())
            .append("salaryPeriod", getSalaryPeriod())
            .append("basicSalary", getBasicSalary())
            .append("basicDailySalary", getBasicDailySalary())
            .append("basicWorkDays", getBasicWorkDays())
            .append("basicSubtotal", getBasicSubtotal())
            .append("allowanceFullAttendance", getAllowanceFullAttendance())
            .append("allowanceSafety", getAllowanceSafety())
            .append("allowanceSeniority", getAllowanceSeniority())
            .append("allowancePosition", getAllowancePosition())
            .append("allowanceFloating", getAllowanceFloating())
            .append("allowanceConfidentiality", getAllowanceConfidentiality())
            .append("allowanceTransportation", getAllowanceTransportation())
            .append("allowanceSpecialCertificate", getAllowanceSpecialCertificate())
            .append("allowanceHoliday", getAllowanceHoliday())
            .append("allowancePerformance", getAllowancePerformance())
            .append("allowanceSafetyTraining", getAllowanceSafetyTraining())
            .append("allowanceAssessment", getAllowanceAssessment())
            .append("overtimeDays", getOvertimeDays())
            .append("overtimeAmount", getOvertimeAmount())
            .append("overtimeMidShiftDays", getOvertimeMidShiftDays())
            .append("overtimeMidShiftAmount", getOvertimeMidShiftAmount())
            .append("overtimeNightShiftDays", getOvertimeNightShiftDays())
            .append("overtimeNightShiftAmount", getOvertimeNightShiftAmount())
            .append("allowanceSubtotal", getAllowanceSubtotal())
            .append("totalEarnings", getTotalEarnings())
            .append("deductionDiscipline", getDeductionDiscipline())
            .append("deductionTax", getDeductionTax())
            .append("deductionHousingFund", getDeductionHousingFund())
            .append("deductionInsurance", getDeductionInsurance())
            .append("deductionWithhold", getDeductionWithhold())
            .append("deductionSubtotal", getDeductionSubtotal())
            .append("netSalary", getNetSalary())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}

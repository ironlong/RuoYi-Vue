<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="90px">
      <el-form-item label="工资所属期" prop="salaryPeriod">
        <el-date-picker
          v-model="queryParams.salaryPeriod"
          type="month"
          value-format="yyyy年MM月"
          format="yyyy年MM月"
          placeholder="请选择工资所属期"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table
      v-loading="loading"
      :data="detailList"
      border
      stripe
      size="small"
      :max-height="520"
    >
      <el-table-column label="基础信息" align="center">
        <el-table-column label="主键ID" align="center" prop="salaryDetailId" v-if="false" />
        <el-table-column label="员工ID" align="center" prop="userId" v-if="false" />
        <el-table-column label="姓名" align="center" prop="nickName" fixed="left" width="100" />
        <el-table-column label="部门名称" align="center" prop="deptName" fixed="left" width="140" show-overflow-tooltip />
        <el-table-column label="工资所属期" align="center" prop="salaryPeriod" fixed="left" width="110" />
        <el-table-column label="确认状态" align="center" min-width="100">
          <template slot-scope="scope">
            <span>{{ isConfirmed(scope.row) ? "已确认" : "未确认" }}</span>
          </template>
        </el-table-column>
      </el-table-column>
      <el-table-column label="基本工资" align="center">
        <el-table-column label="基本工资" align="center" prop="basicSalary" />
        <el-table-column label="日工资" align="center" prop="basicDailySalary" />
        <el-table-column label="工作日" align="center" prop="basicWorkDays" />
        <el-table-column label="基本工资小计" align="center" prop="basicSubtotal" />
      </el-table-column>
      <el-table-column label="补贴奖金" align="center">
        <el-table-column label="全勤奖" align="center" prop="allowanceFullAttendance" />
        <el-table-column label="安全奖" align="center" prop="allowanceSafety" />
        <el-table-column label="工龄工资" align="center" prop="allowanceSeniority" />
        <el-table-column label="职务工资" align="center" prop="allowancePosition" />
        <el-table-column label="浮动工资" align="center" prop="allowanceFloating" />
        <el-table-column label="保密工资" align="center" prop="allowanceConfidentiality" />
        <el-table-column label="交通补贴" align="center" prop="allowanceTransportation" />
        <el-table-column label="特种作业证补贴" align="center" prop="allowanceSpecialCertificate" />
        <el-table-column label="节假日补贴" align="center" prop="allowanceHoliday" />
        <el-table-column label="工作表现奖" align="center" prop="allowancePerformance" />
        <el-table-column label="安全培训补贴" align="center" prop="allowanceSafetyTraining" />
        <el-table-column label="绩效考核奖" align="center" prop="allowanceAssessment" />
        <el-table-column label="其它应发小计" align="center" prop="allowanceSubtotal" />
      </el-table-column>
      <el-table-column label="加班" align="center">
        <el-table-column label="加班天数" align="center" prop="overtimeDays" />
        <el-table-column label="加班金额" align="center" prop="overtimeAmount" />
        <el-table-column label="中班天数" align="center" prop="overtimeMidShiftDays" />
        <el-table-column label="中班补贴金额" align="center" prop="overtimeMidShiftAmount" />
        <el-table-column label="夜班天数" align="center" prop="overtimeNightShiftDays" />
        <el-table-column label="夜班补贴金额" align="center" prop="overtimeNightShiftAmount" />
      </el-table-column>
      <el-table-column label="扣款" align="center">
        <el-table-column label="违纪扣款" align="center" prop="deductionDiscipline" />
        <el-table-column label="个人所得税" align="center" prop="deductionTax" />
        <el-table-column label="代扣公积金" align="center" prop="deductionHousingFund" />
        <el-table-column label="代扣代缴保险" align="center" prop="deductionInsurance" />
        <el-table-column label="暂扣工资" align="center" prop="deductionWithhold" />
        <el-table-column label="应扣小计" align="center" prop="deductionSubtotal" />
      </el-table-column>
      <el-table-column label="汇总" align="center">
        <el-table-column label="应发金额" align="center" prop="totalEarnings" min-width="100" />
        <el-table-column label="实发金额" align="center" prop="netSalary" min-width="100" />
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="120">
        <template slot-scope="scope">
          <el-button
            v-if="!isConfirmed(scope.row)"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handleConfirm(scope.row)"
            v-hasPermi="['kangderui:detail:confirm']"
          >确认</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { listMyDetail, confirmDetail } from "@/api/kangderui/detail"

export default {
  name: "MySalary",
  data() {
    return {
      loading: true,
      showSearch: true,
      total: 0,
      detailList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        salaryPeriod: null
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listMyDetail(this.queryParams).then(response => {
        this.detailList = response.rows
        this.total = response.total
        this.loading = false
      }).catch(error => {
        this.loading = false
        this.handlePasswordRedirect(error)
      })
    },
    handlePasswordRedirect(error) {
      const message = error && error.message ? error.message : error
      if (message === "请先修改初始密码后再查看工资") {
        this.$router.push({ name: "Profile", params: { activeTab: "resetPwd" } })
        return true
      }
      return false
    },
    isConfirmed(row) {
      return String(row.remark) === "1"
    },
    handleConfirm(row) {
      const salaryDetailId = row.salaryDetailId
      this.$modal.confirm("确认工资无误后将无法撤销，是否继续？").then(() => {
        return confirmDetail(salaryDetailId)
      }).then(() => {
        this.$modal.msgSuccess("确认成功")
        this.getList()
      }).catch(() => {})
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    }
  }
}
</script>

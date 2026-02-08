<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="员工姓名" prop="nickName">
        <el-select
          v-model="queryParams.nickName"
          filterable
          remote
          clearable
          reserve-keyword
          placeholder="请输入姓名"
          :remote-method="queryUserOptions"
          :loading="userSearchLoading"
          @change="handleQuery"
          @clear="clearUserSearch"
        >
          <el-option
            v-for="item in userSearchOptions"
            :key="item.userId"
            :label="item.nickName"
            :value="item.nickName"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="部门名称" prop="deptName">
        <el-select
          v-model="queryParams.deptName"
          filterable
          remote
          clearable
          reserve-keyword
          placeholder="请输入部门名称"
          :remote-method="queryDeptOptions"
          :loading="deptSearchLoading"
          @change="handleQuery"
          @clear="clearDeptSearch"
        >
          <el-option
            v-for="item in deptSearchOptions"
            :key="item.deptId"
            :label="item.deptName"
            :value="item.deptName"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="工资所属期" prop="salaryPeriod" label-width="110px">
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

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['kangderui:detail:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single || !isAdminRole"
          @click="handleUpdate"
          v-hasPermi="['kangderui:detail:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['kangderui:detail:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['kangderui:detail:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['kangderui:detail:import']"
        >导入</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="detailList"
      border
      stripe
      size="small"
      :max-height="520"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" fixed="left" />
      <el-table-column label="基础信息" align="center">
        <el-table-column label="主键ID" align="center" prop="salaryDetailId" v-if=false />
        <el-table-column label="员工ID" align="center" prop="userId" v-if=false />
        <el-table-column label="姓名" align="center" prop="nickName" fixed="left" width="100" />
<!--        <el-table-column label="工资卡号" align="center" prop="bankCardNumber" fixed="left" width="160" show-overflow-tooltip />-->
        <el-table-column label="部门ID" align="center" prop="deptId" v-if=false />
        <el-table-column label="部门名称" align="center" prop="deptName" fixed="left" width="140" show-overflow-tooltip />
        <el-table-column label="工资所属期" align="center" prop="salaryPeriod" fixed="left" width="110" />
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
        <el-table-column label="备注" align="center" prop="remark" min-width="160" show-overflow-tooltip />
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" fixed="right" width="120">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            :disabled="!isAdminRole"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['kangderui:detail:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['kangderui:detail:remove']"
          >删除</el-button>
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

    <!-- 添加或修改员工工资明细对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1200px" append-to-body class="salary-detail-dialog">
      <el-form ref="form" :model="form" :rules="rules" label-width="90px" class="four-col-form">
        <div class="form-group-title full-width">基础信息</div>
        <el-form-item label="员工ID" prop="userId" v-show="false">
          <el-input v-model="form.userId" placeholder="请输入员工ID" />
        </el-form-item>
        <el-form-item label="姓名" prop="nickName">
          <el-select
            v-model="form.userId"
            placeholder="请选择员工"
            filterable
            clearable
            :disabled="isEdit"
            @change="handleUserChange"
          >
            <el-option
              v-for="item in userOptions"
              :key="item.userId"
              :label="item.nickName"
              :value="item.userId"
            />
          </el-select>
        </el-form-item>
<!--        <el-form-item label="工资卡号" prop="bankCardNumber">-->
<!--          <el-input v-model="form.bankCardNumber" placeholder="请输入工资卡号" />-->
<!--        </el-form-item>-->
        <el-form-item label="部门ID" prop="deptId" v-show="false">
          <el-input v-model="form.deptId" placeholder="请输入部门ID" />
        </el-form-item>
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="form.deptName" placeholder="" :disabled="true" />
        </el-form-item>
        <el-form-item label="工资所属期" prop="salaryPeriod">
          <el-date-picker
            v-model="form.salaryPeriod"
            type="month"
            value-format="yyyy年MM月"
            format="yyyy年MM月"
            placeholder="请选择工资所属期"
            clearable
            :disabled="isEdit"
          />
        </el-form-item>
        <div class="form-group-title full-width">基本工资</div>
        <el-form-item label="基本工资" prop="basicSalary">
          <el-input v-model="form.basicSalary" placeholder="请输入基本工资" />
        </el-form-item>
        <el-form-item label="日工资" prop="basicDailySalary">
          <el-input v-model="form.basicDailySalary" placeholder="请输入日工资" />
        </el-form-item>
        <el-form-item label="工作日" prop="basicWorkDays">
          <el-input v-model="form.basicWorkDays" placeholder="请输入工作日" />
        </el-form-item>
        <el-form-item label="基本工资小计" prop="basicSubtotal" label-width="110px">
          <el-input v-model="form.basicSubtotal" placeholder="请输入基本工资小计" :disabled="true" />
        </el-form-item>
        <div class="form-group-title full-width">补贴奖金</div>
        <el-form-item label="全勤奖" prop="allowanceFullAttendance">
          <el-input v-model="form.allowanceFullAttendance" placeholder="请输入全勤奖" />
        </el-form-item>
        <el-form-item label="安全奖" prop="allowanceSafety">
          <el-input v-model="form.allowanceSafety" placeholder="请输入安全奖" />
        </el-form-item>
        <el-form-item label="工龄工资" prop="allowanceSeniority">
          <el-input v-model="form.allowanceSeniority" placeholder="请输入工龄工资" />
        </el-form-item>
        <el-form-item label="职务工资" prop="allowancePosition">
          <el-input v-model="form.allowancePosition" placeholder="请输入职务工资" />
        </el-form-item>
        <el-form-item label="浮动工资" prop="allowanceFloating">
          <el-input v-model="form.allowanceFloating" placeholder="请输入浮动工资" />
        </el-form-item>
        <el-form-item label="保密工资" prop="allowanceConfidentiality">
          <el-input v-model="form.allowanceConfidentiality" placeholder="请输入保密工资" />
        </el-form-item>
        <el-form-item label="交通补贴" prop="allowanceTransportation">
          <el-input v-model="form.allowanceTransportation" placeholder="请输入交通补贴" />
        </el-form-item>
        <el-form-item label="特种作业证补贴" prop="allowanceSpecialCertificate" label-width="110px">
          <el-input v-model="form.allowanceSpecialCertificate" placeholder="请输入特种作业证补贴" />
        </el-form-item>
        <el-form-item label="节假日补贴" prop="allowanceHoliday">
          <el-input v-model="form.allowanceHoliday" placeholder="请输入节假日补贴" />
        </el-form-item>
        <el-form-item label="工作表现奖" prop="allowancePerformance">
          <el-input v-model="form.allowancePerformance" placeholder="请输入工作表现奖" />
        </el-form-item>
        <el-form-item label="安全培训补贴" prop="allowanceSafetyTraining" label-width="110px">
          <el-input v-model="form.allowanceSafetyTraining" placeholder="请输入安全培训补贴" />
        </el-form-item>
        <el-form-item label="绩效考核奖" prop="allowanceAssessment">
          <el-input v-model="form.allowanceAssessment" placeholder="请输入绩效考核奖" />
        </el-form-item>
        <div class="form-group-title full-width">加班</div>
        <el-form-item label="加班天数" prop="overtimeDays">
          <el-input v-model="form.overtimeDays" placeholder="请输入加班天数" />
        </el-form-item>
        <el-form-item label="加班金额" prop="overtimeAmount">
          <el-input v-model="form.overtimeAmount" placeholder="请输入加班金额" />
        </el-form-item>
        <el-form-item label="中班天数" prop="overtimeMidShiftDays">
          <el-input v-model="form.overtimeMidShiftDays" placeholder="请输入中班天数" />
        </el-form-item>
        <el-form-item label="中班补贴金额" prop="overtimeMidShiftAmount" label-width="110px">
          <el-input v-model="form.overtimeMidShiftAmount" placeholder="请输入中班补贴金额" />
        </el-form-item>
        <el-form-item label="夜班天数" prop="overtimeNightShiftDays">
          <el-input v-model="form.overtimeNightShiftDays" placeholder="请输入夜班天数" />
        </el-form-item>
        <el-form-item label="夜班补贴金额" prop="overtimeNightShiftAmount" label-width="110px">
          <el-input v-model="form.overtimeNightShiftAmount" placeholder="请输入夜班补贴金额" />
        </el-form-item>
        <div class="form-group-title full-width">应发汇总</div>
        <el-form-item label="其它应发小计" prop="allowanceSubtotal" label-width="110px">
          <el-input v-model="form.allowanceSubtotal" placeholder="请输入其它应发小计" :disabled="true" />
        </el-form-item>
        <el-form-item label="应发金额" prop="totalEarnings">
          <el-input v-model="form.totalEarnings" placeholder="请输入应发金额" :disabled="true" />
        </el-form-item>
        <div class="form-group-title full-width">扣款</div>
        <el-form-item label="违纪扣款" prop="deductionDiscipline">
          <el-input v-model="form.deductionDiscipline" placeholder="请输入违纪扣款" />
        </el-form-item>
        <el-form-item label="个人所得税" prop="deductionTax">
          <el-input v-model="form.deductionTax" placeholder="请输入个人所得税" />
        </el-form-item>
        <el-form-item label="代扣公积金" prop="deductionHousingFund">
          <el-input v-model="form.deductionHousingFund" placeholder="请输入代扣公积金" />
        </el-form-item>
        <el-form-item label="代扣代缴保险" prop="deductionInsurance" label-width="110px">
          <el-input v-model="form.deductionInsurance" placeholder="请输入代扣代缴保险" />
        </el-form-item>
        <el-form-item label="暂扣工资" prop="deductionWithhold">
          <el-input v-model="form.deductionWithhold" placeholder="请输入暂扣工资" />
        </el-form-item>
        <el-form-item label="应扣小计" prop="deductionSubtotal">
          <el-input v-model="form.deductionSubtotal" placeholder="请输入应扣小计" :disabled="true" />
        </el-form-item>
        <div class="form-group-title full-width">实发与备注</div>
        <el-form-item label="实发金额" prop="netSalary">
          <el-input v-model="form.netSalary" placeholder="请输入实发金额" :disabled="true" />
        </el-form-item>
        <el-form-item label="备注" prop="remark" class="full-width">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 员工工资明细导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <span>仅允许导入xls、xlsx格式文件。</span>
<!--            <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">下载模板</el-link>-->
          </div>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listDetail, getDetail, delDetail, addDetail, updateDetail } from "@/api/kangderui/detail"
import { listUser } from "@/api/system/user"
import { listDept } from "@/api/system/dept"
import { deptTreeSelect } from "@/api/system/user"
import { getToken } from "@/utils/auth"
import Treeselect from "@riophae/vue-treeselect"
import "@riophae/vue-treeselect/dist/vue-treeselect.css"

export default {
  name: "Detail",
  components: { Treeselect },
  computed: {
    isAdminRole() {
      const roles = this.$store.getters.roles || []
      return roles.includes("admin")
    },
    isEdit() {
      return this.form && this.form.salaryDetailId != null
    }
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 员工工资明细表格数据
      detailList: [],
      // 弹出层标题
      title: "",
      // 部门树选项
      deptOptions: [],
      // 可用部门树选项
      enabledDeptOptions: [],
      // 员工下拉选项
      userOptions: [],
      // 搜索下拉选项
      userSearchOptions: [],
      deptSearchOptions: [],
      userSearchLoading: false,
      deptSearchLoading: false,
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        nickName: null,
        bankCardNumber: null,
        deptId: null,
        deptName: null,
        salaryPeriod: null,
        basicSalary: null,
        basicDailySalary: null,
        basicWorkDays: null,
        basicSubtotal: null,
        allowanceFullAttendance: null,
        allowanceSafety: null,
        allowanceSeniority: null,
        allowancePosition: null,
        allowanceFloating: null,
        allowanceConfidentiality: null,
        allowanceTransportation: null,
        allowanceSpecialCertificate: null,
        allowanceHoliday: null,
        allowancePerformance: null,
        allowanceSafetyTraining: null,
        allowanceAssessment: null,
        overtimeDays: null,
        overtimeAmount: null,
        overtimeMidShiftDays: null,
        overtimeMidShiftAmount: null,
        overtimeNightShiftDays: null,
        overtimeNightShiftAmount: null,
        allowanceSubtotal: null,
        totalEarnings: null,
        deductionDiscipline: null,
        deductionTax: null,
        deductionHousingFund: null,
        deductionInsurance: null,
        deductionWithhold: null,
        deductionSubtotal: null,
        netSalary: null,
      },
      // 表单参数
      form: {},
      // 导入参数
      upload: {
        // 是否显示弹出层（导入）
        open: false,
        // 弹出层标题（导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/kangderui/detail/importData"
      },
      // 表单校验
      rules: {
        nickName: [
          { required: true, message: "姓名不能为空", trigger: "blur" }
        ],
        deptId: [
          { required: true, message: "部门ID不能为空", trigger: "blur" }
        ],
      }
    }
  },
  watch: {
    form: {
      handler() {
        this.recalcFormTotals()
      },
      deep: true
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询员工工资明细列表 */
    getList() {
      this.loading = true
      listDetail(this.queryParams).then(response => {
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
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        salaryDetailId: null,
        userId: null,
        nickName: null,
        bankCardNumber: null,
        deptId: null,
        deptName: null,
        salaryPeriod: null,
        basicSalary: null,
        basicDailySalary: null,
        basicWorkDays: null,
        basicSubtotal: null,
        allowanceFullAttendance: null,
        allowanceSafety: null,
        allowanceSeniority: null,
        allowancePosition: null,
        allowanceFloating: null,
        allowanceConfidentiality: null,
        allowanceTransportation: null,
        allowanceSpecialCertificate: null,
        allowanceHoliday: null,
        allowancePerformance: null,
        allowanceSafetyTraining: null,
        allowanceAssessment: null,
        overtimeDays: null,
        overtimeAmount: null,
        overtimeMidShiftDays: null,
        overtimeMidShiftAmount: null,
        overtimeNightShiftDays: null,
        overtimeNightShiftAmount: null,
        allowanceSubtotal: null,
        totalEarnings: null,
        deductionDiscipline: null,
        deductionTax: null,
        deductionHousingFund: null,
        deductionInsurance: null,
        deductionWithhold: null,
        deductionSubtotal: null,
        netSalary: null,
        createTime: null,
        createBy: null,
        updateBy: null,
        updateTime: null,
        remark: null
      }
      this.resetForm("form")
    },
    hasValue(value) {
      return value !== null && value !== undefined && value !== ""
    },
    toNumber(value) {
      if (!this.hasValue(value)) {
        return 0
      }
      const num = Number(value)
      return Number.isNaN(num) ? 0 : num
    },
    calcSum(fields) {
      return fields.reduce((sum, field) => sum + this.toNumber(this.form[field]), 0)
    },
    recalcFormTotals() {
      if (!this.form) {
        return
      }
      const basicSalary = this.form.basicSalary
      const basicSubtotal = this.hasValue(basicSalary) ? this.toNumber(basicSalary) : null

      const allowanceFields = [
        "allowanceFullAttendance",
        "allowanceSafety",
        "allowanceSeniority",
        "allowancePosition",
        "allowanceFloating",
        "allowanceConfidentiality",
        "allowanceTransportation",
        "allowanceSpecialCertificate",
        "allowanceHoliday",
        "allowancePerformance",
        "allowanceSafetyTraining",
        "allowanceAssessment",
        "overtimeAmount",
        "overtimeMidShiftAmount",
        "overtimeNightShiftAmount"
      ]
      const hasAllowance = allowanceFields.some(field => this.hasValue(this.form[field]))
      const allowanceSubtotal = hasAllowance ? this.calcSum(allowanceFields) : null

      const deductionFields = [
        "deductionDiscipline",
        "deductionTax",
        "deductionHousingFund",
        "deductionInsurance",
        "deductionWithhold"
      ]
      const hasDeduction = deductionFields.some(field => this.hasValue(this.form[field]))
      const deductionSubtotal = hasDeduction ? this.calcSum(deductionFields) : null

      const hasTotalEarnings = this.hasValue(basicSubtotal) || this.hasValue(allowanceSubtotal)
      const totalEarnings = hasTotalEarnings
        ? this.toNumber(basicSubtotal) + this.toNumber(allowanceSubtotal)
        : null

      const hasNetSalary = this.hasValue(totalEarnings) || this.hasValue(deductionSubtotal)
      const netSalary = hasNetSalary
        ? this.toNumber(totalEarnings) - this.toNumber(deductionSubtotal)
        : null

      this.form.basicSubtotal = basicSubtotal
      this.form.allowanceSubtotal = allowanceSubtotal
      this.form.deductionSubtotal = deductionSubtotal
      this.form.totalEarnings = totalEarnings
      this.form.netSalary = netSalary
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    queryUserOptions(query) {
      if (!query) {
        this.userSearchOptions = []
        return
      }
      this.userSearchLoading = true
      listUser({ pageNum: 1, pageSize: 20, nickName: query, status: "0" }).then(response => {
        this.userSearchOptions = (response.rows || []).map(item => ({
          userId: item.userId,
          nickName: item.nickName
        }))
        this.userSearchLoading = false
      }).catch(() => {
        this.userSearchLoading = false
      })
    },
    clearUserSearch() {
      this.userSearchOptions = []
    },
    queryDeptOptions(query) {
      if (!query) {
        this.deptSearchOptions = []
        return
      }
      this.deptSearchLoading = true
      listDept({ deptName: query }).then(response => {
        this.deptSearchOptions = (response.data || []).map(item => ({
          deptId: item.deptId,
          deptName: item.deptName
        }))
        this.deptSearchLoading = false
      }).catch(() => {
        this.deptSearchLoading = false
      })
    },
    clearDeptSearch() {
      this.deptSearchOptions = []
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.salaryDetailId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      const now = new Date()
      this.form.salaryPeriod = `${now.getFullYear()}年${String(now.getMonth() + 1).padStart(2, "0")}月`
      this.getUserOptions()
      this.getDeptTree()
      this.open = true
      this.title = "添加员工工资明细"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const salaryDetailId = row.salaryDetailId || this.ids
      getDetail(salaryDetailId).then(response => {
        this.form = response.data
        this.getUserOptions().then(() => {
          if (this.form.userId && !this.userOptions.find(item => item.userId === this.form.userId)) {
            this.userOptions.unshift({ userId: this.form.userId, nickName: this.form.nickName })
          }
        })
        this.open = true
        this.title = "修改员工工资明细"
      }).catch(error => {
        this.handlePasswordRedirect(error)
      })
    },
    /** 查询员工下拉列表 */
    getUserOptions() {
      return listUser({ pageNum: 1, pageSize: 10000, status: "0" }).then(response => {
        this.userOptions = (response.rows || []).map(item => ({
          userId: item.userId,
          nickName: item.nickName,
          deptId: item.deptId || (item.dept ? item.dept.deptId : null),
          deptName: item.dept ? item.dept.deptName : null
        }))
      })
    },
    handleUserChange(userId) {
      const selected = this.userOptions.find(item => item.userId === userId)
      this.form.userId = userId || null
      this.form.nickName = selected ? selected.nickName : null
      this.form.deptId = selected ? selected.deptId : null
      this.form.deptName = selected ? selected.deptName : null
    },
    /** 查询部门下拉树结构 */
    getDeptTree() {
      return deptTreeSelect().then(response => {
        this.deptOptions = response.data || []
        this.enabledDeptOptions = this.filterDisabledDept(JSON.parse(JSON.stringify(this.deptOptions)))
      })
    },
    // 过滤禁用的部门
    filterDisabledDept(deptList) {
      return deptList.filter(dept => {
        if (dept.disabled) {
          return false
        }
        if (dept.children && dept.children.length) {
          dept.children = this.filterDisabledDept(dept.children)
        }
        return true
      })
    },
    findDeptLabel(list, deptId) {
      if (!list || deptId === null || deptId === undefined) {
        return null
      }
      for (const item of list) {
        if (item.id === deptId) {
          return item.label
        }
        if (item.children && item.children.length) {
          const label = this.findDeptLabel(item.children, deptId)
          if (label) {
            return label
          }
        }
      }
      return null
    },
    handleDeptChange(deptId) {
      this.form.deptId = deptId
      this.form.deptName = this.findDeptLabel(this.enabledDeptOptions, deptId)
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.salaryDetailId != null) {
            updateDetail(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addDetail(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const salaryDetailIds = row.salaryDetailId || this.ids
      this.$modal.confirm('是否确认删除员工工资明细编号为"' + salaryDetailIds + '"的数据项？').then(function() {
        return delDetail(salaryDetailIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('kangderui/detail/export', {
        ...this.queryParams
      }, `detail_${new Date().getTime()}.xlsx`)
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "员工工资明细导入"
      this.upload.open = true
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('kangderui/detail/importTemplate', {
      }, `salary_detail_template_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress() {
      this.upload.isUploading = true
    },
    // 文件上传成功处理
    handleFileSuccess(response) {
      this.upload.open = false
      this.upload.isUploading = false
      this.$refs.upload.clearFiles()
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true })
      this.getList()
    },
    // 提交上传文件
    submitFileForm() {
      const file = this.$refs.upload.uploadFiles
      if (!file || file.length === 0 || !file[0].name.toLowerCase().endsWith('.xls') && !file[0].name.toLowerCase().endsWith('.xlsx')) {
        this.$modal.msgError("请选择后缀为 “xls”或“xlsx”的文件。")
        return
      }
      this.$refs.upload.submit()
    }
  }
}
</script>

<style scoped>
.four-col-form {
  display: flex;
  flex-wrap: wrap;
}

.four-col-form .el-form-item {
  width: 25%;
  min-width: 240px;
  box-sizing: border-box;
  margin-right: 0;
  vertical-align: top;
  padding: 0 8px;
}

.four-col-form .el-form-item.full-width {
  width: 100%;
}

.four-col-form .el-form-item .el-form-item__content {
  width: calc(100% - 90px);
}

.four-col-form .el-form-item .el-input,
.four-col-form .el-form-item .el-textarea {
  width: 100%;
}

.four-col-form .form-group-title {
  font-weight: 600;
  color: #303133;
  padding: 10px 0 6px;
  border-bottom: 1px solid #ebeef5;
  margin: 6px 0 12px;
  background: #f9fafc;
  border-radius: 4px;
  padding-left: 8px;
}

.four-col-form .form-group-title.full-width {
  width: 100%;
  flex-basis: 100%;
  display: block;
}

@media (max-width: 1400px) {
  .four-col-form .el-form-item {
    width: 33.3333%;
  }
}

@media (max-width: 1100px) {
  .four-col-form .el-form-item {
    width: 50%;
  }
}

@media (max-width: 760px) {
  .four-col-form .el-form-item {
    width: 100%;
  }
}

::v-deep .salary-detail-dialog .el-dialog__body {
  padding: 16px 20px 8px;
}

::v-deep .salary-detail-dialog .el-form-item__label {
  color: #606266;
}
</style>

<template>
  <a-card :bordered="false">
    <div class="table-page-search-wrapper">
      <a-form layout="inline">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <!-- <j-dict-select-tag placeholder="请选择设备名称" v-model="queryParam.result" dictCode="spotcheck_result"/> -->
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="11" :md="12" :sm="24">
            <a-form-item label="查询日期">
              <a-range-picker date-format="YYYY-MM-DD" v-model="queryParam.date" @change="onChangeDate" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="loadData" icon="search">查询</a-button>
              <!-- <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button> -->
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>

    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        :scroll="{ x: true }"
        bordered
        rowKey="equipmentid"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{ selectedRowKeys: selectedRowKeys, onChange: onSelectChange }"
        class="j-table-force-nowrap"
        @change="handleTableChange"
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text, record">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无图片</span>
          <img
            v-else
            :src="getImgView(text)"
            :preview="record.equipmentid"
            height="25px"
            alt=""
            style="max-width: 80px; font-size: 12px; font-style: italic"
          />
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
          <a-button v-else :ghost="true" type="primary" icon="download" size="small" @click="downloadFile(text)">
            下载
          </a-button>
        </template>
      </a-table>
    </div>
  </a-card>
</template>

<script>
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
// import SelectDialogEq from "@/components/SelectDialogEq/index.vue";
import { httpAction, getAction } from '@/api/manage'

export default {
  name: 'Tpmequiponoffsection',
  mixins: [JeecgListMixin, mixinDevice],
  components: {
    // SelectDialogEq,
  },
  data() {
    return {
      // 查询参数
      // queryParam: {
      //   equipmentid: '',
      //   day_begin: '',
      //   day_end: '',
      // },
      url: {
        list: '/equipmentOnoffSection/equipmentOnoffSection/listtransverse',
      },
      dataSource: [],
      // 表头
      columns: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1
          },
        },
        {
          title: '设备名称',
          align: 'center',
          dataIndex: 'equipmentid_dictText',
        },
        {
          title: '设备编号',
          align: 'center',
          dataIndex: 'equipmentcode',
        },
        {
          title: '日期',
          align: 'center',
          dataIndex: 'day',
        },
        {
          title: '总时长(分钟)',
          align: 'center',
          dataIndex: 'duration',
        },
        {
          title: '运行时长(分钟)',
          align: 'center',
          dataIndex: 'onduration',
        },
        {
          title: '待机时长(分钟)',
          align: 'center',
          dataIndex: 'pauseduration',
        },
        {
          title: '关机时长(分钟)',
          align: 'center',
          dataIndex: 'offduration',
        },
      ],
    }
  },
  created() {},
  methods: {
    // 时间处理
    onChangeDate(date, dateString) {
      console.log(date, dateString)
      this.queryParam.day_begin = dateString[0]
      this.queryParam.day_end = dateString[1]
    },
    // 选择框上，选择设备
    // chooseEquipment1(params) {
    //   // 返回对象
    //   this.queryParam.equipmentid = params.equipmentid
    //   this.queryParam.equipmentcode = params.equipmentcode
    //   this.queryParam.equipmentname = params.equipmentname
    //   this.loadData()
    // },
    /** 查询设备运行状态时间段列表 */
    loadData(arg) {
      if (!this.url.list) {
        this.$message.error('请设置url.list属性!')
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1
      }
      var params = this.getQueryParams() //查询条件
      params.equipmentid = !params.equipmentid ? '' : params.equipmentid
      params.day_begin = !params.day_begin ? '' : params.day_begin
      params.day_end = !params.day_end ? '' : params.day_end
      this.loading = true
      getAction(this.url.list, params).then((res) => {
        if (res.success) {
          //update-begin---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
          this.dataSource = res.result.records || res.result
          if (res.result.total) {
            this.ipagination.total = res.result.total
          } else {
            this.ipagination.total = 0
          }
          //update-end---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
        } else {
          this.$message.warning(res.message)
        }

        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParam.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.onoffsectionid)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 导出按钮操作 */
    handleExport() {
      if (this.dateRange == null || this.dateRange.length < 1) {
        this.$message({
          type: 'error',
          message: '请先选择日期！',
        })
        return false
      }
      this.download(
        'tpm/tpmequipmentonoffsection/exporttransverse',
        {
          ...this.queryParam,
        },
        `设备运行状态_${new Date().getTime()}.xlsx`
      )
    },
  },
}
</script>

<style scoped>
@import '~@assets/less/common.less';
</style>
<template>
  <div>
    <div class="table-page-search-wrapper">
      <a-form layout="inline" :model="queryParams" ref="queryForm">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag v-model="queryParams.equipmentid" dict="tpm_equipment,equipmentname,id"  style="width: 100%"/>
            </a-form-item>
          </a-col>
          <a-col :xl="4" :lg="7" :md="8" :sm="24">
            <a-form-item label="参数类型">
              <j-dict-select-tag v-model="queryParams.tagtype"  placeholder="请输入优先级" dictCode="tpm_tag_type"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left; overflow: hidden" class="table-page-search-submitButtons">
              <a-button type="primary" @click="loadData" icon="search">查询</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>    
    <div class="trend-charts" ref="trendCharts" :style="{ height: echartsHeight + 'px' }"></div>

    <!-- table区域-begin -->
    <div>
      <a-table
        ref="table"
        size="middle"
        :scroll="{ x: true }"
        bordered
        rowKey="id"
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
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mixinDevice } from '@/utils/mixin'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
import { httpAction, getAction } from '@/api/manage'
import { emsdata } from '@/views/module_ems/energyAnalyse/emsdata'
export default {
  name: '',
  mixins: [JeecgListMixin, mixinDevice],
  data() {
    return {
      loading: true,
      dataSource: [],
      dataInfo: null,
      times: [],
      myChart: null,
      echartsHeight: window.innerHeight - 450,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        equipmentid: '301156882513921',
        day: null,
        tagtype: "5",
      },
      url: {
        list: '/equipmentStatistics/equipmentStatistics/listday',
        listinfo: '/equipmentStatistics/equipmentStatistics/listdayinfo',
      },
      // 时间
      timeList: [],
      // 表头
      columns: [],
      columns1: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          },
        },
        {
          title: '点位名称',
          align: 'center',
          dataIndex: 'tagname',
        },
        {
          title: '时间',
          align: 'center',
          dataIndex: 'logtime',
        },
        {
          title: '值',
          align: 'center',
          dataIndex: 'tagvalue',
        },
      ],
      columns2: [
        {
          title: '序号',
          dataIndex: '',
          key: 'rowIndex',
          width: 60,
          align: 'center',
          customRender: function (t, r, index) {
            return parseInt(index) + 1;
          },
        },
        {
          title: '点位名称',
          align: 'center',
          dataIndex: 'tagname',
        },
        {
          title: '时间',
          align: 'center',
          dataIndex: 'logtime',
        },
        {
          title: '值',
          align: 'center',
          dataIndex: 'tagvalue2',
        },
      ],
    }
  },
  created() {},
  mounted() {
    var now = this.dateformat(new Date()).substring(0, 10)
    this.queryParams.day = now
    this.$nextTick(() => {
    })
  },
  methods: {
    dateformat(date) {
      var year = date.getFullYear()
      var month = date.getMonth() + 1
      month = month < 10 ? '0' + month : month
      var strDate = date.getDate()
      strDate = strDate < 10 ? '0' + strDate : strDate
      var hour = date.getHours()
      hour = hour < 10 ? '0' + hour : hour
      var minute = date.getMinutes()
      minute = minute < 10 ? '0' + minute : minute
      var second = date.getSeconds()
      second = second < 10 ? '0' + second : second

      return year + '-' + month + '-' + strDate + ' ' + hour + ':' + minute + ':' + second
    },
    /** 查询设备运行状态时间段列表 */
    loadData(arg) {
      var _this = this;
      if (!this.url.list) {
        this.$message.error('请设置url.list属性!')
        return
      }
      if (!this.queryParams.equipmentid) {
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg === 1) {
        this.ipagination.current = 1
      }
      console.log("tagtype", this.queryParams.tagtype)
      var params = this.getQueryParams() //查询条件
      params.equipmentid = !this.queryParams.equipmentid ? '' : this.queryParams.equipmentid
      params.day = !this.queryParams.day ? '' : this.queryParams.day
      params.tagtype = !this.queryParams.tagtype ? '' : this.queryParams.tagtype
      this.loading = true

      // 电量用另外一个列定义
      if (params.tagtype == "7") {
        this.columns = this.columns2;
      }
      else {
        this.columns = this.columns1;
      }

      // 获取图表信息
      getAction(_this.url.listinfo, params).then((res) => {
        if (res.success) {
          _this.dataInfo = res.result.records || res.result;
          _this.getList(params);
          _this.initEcharts();
        } else {
          this.$message.warning(res.message)
        }
      });

      this.loading = false
    },
    
    // 获取表格信息
    getList(params) {
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
      })
    },
    initEcharts() {
      var _this = this
      var chartDom = _this.$refs.trendCharts;
      _this.myChart && _this.myChart.dispose();
      _this.myChart = echarts.init(chartDom)

      var legends = [];
      var seriess = [];
      _this.times = [];
      var rec = 0;
      _this.dataInfo.tag.forEach(item => {
        legends.push(item.tagname);
        var realdatas = [];
        _this.dataInfo.list[rec].forEach(realdata => {
          // 电量获取值的方法不一样
          if (_this.queryParams.tagtype == "7") {
            realdatas.push(realdata.tagvalue2);
          }
          else {
            realdatas.push(realdata.tagvalue);
          }
        });
        seriess.push({
          name: item.tagname,
          type: 'line',
          symbol: 'none',
          sampling: 'lttb',
          markPoint: {
            data: [
              { type: 'max', name: 'Max' },
              { type: 'min', name: 'Min' },
            ],
          },
          data: realdatas,
        });
        console.log(rec, seriess);
        rec++;
      });

      _this.dataInfo.list[0].forEach(item => {
        _this.times.push(item.logtime);
      });

      var option = {
        tooltip: {
          trigger: 'axis',
          position: function (pt) {
            return [pt[0], '20%']
          },
        },
        title: {
          left: 'left',
          text: '实时数据',
        },
        legend: {
          data: legends,
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: _this.times,
        },
        yAxis: {
          type: 'value',
          boundaryGap: [0, '100%'],
        },
        dataZoom: [
          {
            type: 'inside',
            start: 0,
            end: 100,
            xAxisIndex: 0,
          },
          {
            start: 0,
            end: 10,
          },
          {
            show: true,
            yAxisIndex: 0,
            filterMode: "empty",
            width: 30,
            height: "80%",
            right: "7%",
          },
        ],
        series: seriess,
      }

      option && _this.myChart.setOption(option)

      window.addEventListener('resize', function () {
        _this.myChart.resize()
      })
    },
  },
}
</script>

<style lang="less" scoped>
@import '~@/assets/less/uStyle.less';
</style>
<style lang="less">
.trend-charts {
  padding: 10px;
  margin-top: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>

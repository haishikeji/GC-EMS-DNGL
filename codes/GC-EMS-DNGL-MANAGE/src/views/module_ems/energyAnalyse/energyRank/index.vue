<template>
  <a-spin :spinning="loading">
    <div class="energy-analysis ranking" :loading="loading">
      <commonSearch ref="commonSearch" type="daterange" @search="handleSearch"></commonSearch>
      <div class="item-echarts" ref="echarts"></div>
      <a-table
        rowKey="id"
        size="middle"
        bordered
        :columns="columns"
        :dataSource="tableData"
        :pagination="false"
      >
        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
  
        <template slot="imgSlot" slot-scope="text, record">
          <div style="font-size: 12px; font-style: italic">
            <span v-if="!text">无图片</span>
            <img v-else :src="getImgView(text)" :preview="record.id" alt="" style="max-width: 80px; height: 25px" />
          </div>
        </template>
  
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px; font-style: italic">无文件</span>
          <a-button v-else ghost type="primary" icon="download" size="small" @click="downloadFile(text)">
            <span>下载</span>
          </a-button>
        </template>
      </a-table>
    </div>
  </a-spin>
</template>

<script>
import * as echarts from 'echarts'
import commonSearch from '../components/commonSearch.vue'
import { httpAction, getAction } from '@/api/manage'
import { JeecgListMixin } from '@/mixins/JeecgListMixin'
export default {
  name: 'energyRank',
  components: {
    commonSearch,
    JeecgListMixin,
  },
  data() {
    return {
      description: '能耗排名',
      loading: true,
      columns: [
        {
          title: '设备',
          align: 'center',
          dataIndex: 'equipmentname',
        },
        {
          title: '总用电(KWH)',
          align: 'center',
          dataIndex: 'howManyValue',
        },
        {
          title: '占比(%)',
          align: 'center',
          dataIndex: 'otherValue',
        },
        {
          title: '去年同期(KWH)',
          align: 'center',
          dataIndex: 'howManyValue2',
        },
        {
          title: '同比增长率(%)',
          align: 'center',
          dataIndex: 'otherValue2',
        },
      ],
      tableData: [],
      yuanquName: [],
      numbers: [],
      url: {
        list: '/emsStatistics/ranking',
      },
    }
  },
  created() {},
  mounted() {
    this.$nextTick(() => {
      this.getRanking()
    })
  },
  methods: {
    getRanking() {
      this.loading = true
      var data = this.$refs.commonSearch.queryParams
      getAction(this.url.list, data).then((response) => {
        if(response.success){
          this.tableData = response.result.tableData
          this.yuanquName = response.result.yuanquName
          this.numbers = response.result.numbers
          // var yuanquName, numbers
          // yuanquName = this.tableData.map((obj) => {
          //   return obj.equipmentname
          // })
          // numbers = this.tableData.map((obj) => {
          //   return obj.howManyValue
          // })
          // this.yuanquName = yuanquName.filter((item, index) => index < 10)
          // this.numbers = numbers.filter((item, index) => index < 10)

          this.yuanquName.reverse()
          this.numbers.reverse()
          // this.$message.success(response.message);
        }else{
          this.$message.warning(response.message);
        }
      }).finally(() => {
        this.initEcharts()
        this.loading = false
      })
    },
    initEcharts() {
      var _this = this
      var chartDom = this.$refs.echarts
      var myChart = echarts.init(chartDom)
      var option

      option = {
        title: {
          text: '能耗排名TOP10',
          left: 'center',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
          },
        },
        legend: {
          top: 'bottom',
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '15px',
          top: '20px',
          containLabel: true,
        },
        toolbox: {
          show: true,
          feature: {
            saveAsImage: {},
          },
        },
        xAxis: {
          type: 'value',
          boundaryGap: [0, 0.01],
        },
        yAxis: {
          type: 'category',
          axisTick: {
            show: false,
          },
          axisLine: {
            show: false,
          },
          data: this.yuanquName,
        },
        series: [
          {
            name: '本期',
            type: 'bar',
            // barWidth: 17,
            data: this.numbers,
          },
        ],
      }

      option && myChart.setOption(option)
      window.addEventListener('resize', function () {
        myChart.resize()
      })
    },
    /** 搜索按钮操作 */
    handleSearch(param) {
      this.getRanking()
    },
  },
}
</script>

<style lang="less" scoped>
@import '~@/assets/less/uStyle.less';
</style>
<style lang="less" scoped>
.ranking {
  padding: 0 10px;
  .item-echarts {
    padding: 10px;
    min-height: calc(100vh - 464px);
    box-shadow: 0 2px 10px rgba(153, 153, 153, 0.1);
    background: #fff;
  }
  .ant-table-wrapper {
    // padding: 10px;
    margin-top: 10px;
    box-shadow: 0 2px 10px rgba(153, 153, 153, 0.1);
    background: #fff;
  }
}
</style>

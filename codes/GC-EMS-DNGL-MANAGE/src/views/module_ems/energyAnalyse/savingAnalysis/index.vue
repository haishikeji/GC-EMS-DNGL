<template>
  <a-spin :spinning="loading">
    <div class="saving-analysis" :loading="loading">
      <commonSearch ref="commonSearch" :type="'month'" @search="handleSearch"></commonSearch>
      <div class="text-detail-container u-flex">
        <div class="u-flex-cjac item">
          <div class="name">实际用能(KWh)</div>
          <div class="value">{{ shijiAll }}</div>
        </div>
        <div class="u-flex-cjac item">
          <div class="name">计划用能(KWh)</div>
          <div class="value">{{ jihuaAll }}</div>
        </div>
        <div class="u-flex-cjac item">
          <div class="name">累计节能(KWh)</div>
          <div class="value">{{ leijiAll }}</div>
        </div>
        <div class="u-flex-cjac item analysis-text">
          <div class="name">数据分析</div>
          <div class="text">{{ anlysis }}</div>
        </div>
      </div>
      <div class="item-echarts" ref="echarts"></div>
    </div>
  </a-spin>
</template>

<script>
import * as echarts from 'echarts'
import commonSearch from '../components/commonSearch.vue'
import { httpAction, getAction } from '@/api/manage'
export default {
  name: 'savingAnalysis',
  components: {
    commonSearch,
  },
  data() {
    return {
      description: '节能分析',
      loading: true,
      anlysis: null,
      shijiAll: null,
      jihuaAll: null,
      leijiAll: null,
      monthDate: [],
      // monthDate: ['08/01', '08/02','08/03', '04', '05', '06', '07','08', '09', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29' ,'30', '31'],
      jienengData: [],
      // jienengData: [ 6256,13954,14856,14236,14368,14000,0,0,14056,14265,5999,0,14666,5258,6489,14888,0,14257,13999,14879,8899,8159,14148,14942,14025,14356,14943,0,0,14678,14977 ],
      chaobiaoData: [],
      // chaobiaoData: [ 0,0,0,0,0,0,9204,9015,0,0,0,18999,0,0,0,0,15111,0,0,0,0,0,0,0,0,0,0,10056,9752,0,0 ],
      jihuaData: [],
      // jihuaData: [ 9000, 15000, 15000, 15000, 15000, 15000, 9000, 9000, 15000, 15000, 15000, 15000, 15000, 9000, 9000, 15000, 15000, 15000, 15000, 15000, 9000, 9000, 15000, 15000, 15000, 15000, 15000, 9000, 9000, 15000, 15000],
      leijiData: [],
      // leijiData: [2743,4100,4828,5768,6670,7929,7424,7315,8338,9193,18252,14124,14547.7,18245,20942.6,22420,22314.5,23049.2,24237.6,24572.8,24638.3,25072.3,25846.9,26014.3,26956.7,27639.2,27859.5,27213.1,26854,27254,27440.8],
      url: {
        list: "/emsStatistics/savinganalysis",
      },
    }
  },
  created() {},
  mounted() {
    this.$nextTick(() => {
      this.getSavinganalysis()
    })
  },
  methods: {
    getSavinganalysis() {
      this.loading = true
      var data = this.$refs.commonSearch.queryParams
      console.log(data)
      getAction(this.url.list, data).then((response) => {
        if(response.success){
          this.jienengData = response.result.jienengData
          this.chaobiaoData = response.result.chaobiaoData
          this.jihuaData = response.result.jihuaData
          this.leijiData = response.result.leijiData
          this.monthDate = response.result.monthDate
          this.anlysis = response.result.anlysis
          this.jihuaAll = response.result.jihuaAll
          this.shijiAll = response.result.shijiAll
          this.leijiAll = response.result.leijiAll
          // this.$message.success(response.message);
        }else{
          this.$message.warning(response.message);
        }
      }).finally(() => {
        this.initEcharts()
        this.loading = false
      })
    },
    // 计算最大值
    _calcMaxNumber(arr) {
      let max = Math.max(...arr)
      let maxInt = Math.ceil(max / 9.5)
      let maxVal = maxInt * 10
      return maxVal
    },
    // 计算最小值
    _calcMinNumber(arr) {
      let min = Math.min(...arr)
      let minInt = Math.floor(min / 10)
      let minVal = minInt * 10
      return minVal
    },
    initEcharts() {
      var _this = this
      var chartDom = this.$refs.echarts
      var myChart = echarts.init(chartDom)
      var option

      // let uvMax = this._calcMaxNumber(this.uvDet);
      // let uvMin = this._calcMinNumber(this.uvDet);
      // let cliMax = this._calcMaxNumber(this.cliDet);
      // let cliMin = this._calcMinNumber(this.cliDet);

      //yMax1 第一条y轴的最大值   yMax2 第二条y轴的最大值
      // var divisor = 5;
      // var lcmVal = this.chartlcm(yMax1, yMax2)//获取两条y轴的最大公约数
      // var Ymaxval_interval = this.YmaxvalAndinterval(yMax1, yMax2, lcmVal, divisor);//计算y轴最大值和间隔值

      option = {
        title: {
          text: '节能分析趋势',
          left: 'center',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            // 横纵坐标虚线链接显示
            type: 'cross',
            crossStyle: {
              color: '#999',
            },
          },
          formatter: function (params) {
            // console.log(params)
            // var res = `${params[0].name} <br/>`
            var res = ''
            for (const item of params) {
              if (item.value === 0 && item.seriesType === 'bar') {
              } else {
                res += `<span style="background: ${item.color}; height:10px; width: 10px; border-radius: 50%;display: inline-block;margin-right:10px;"></span> ${item.seriesName} ：${item.value} KWh<br/>`
              }
            }
            return res
          },
        },
        toolbox: {
          show: true,
          feature: {
            saveAsImage: {},
          },
        },
        legend: {
          top: 'bottom',
          // data: ['Evaporation', 'Precipitation', 'Temperature']
        },
        xAxis: [
          {
            type: 'category',
            data: this.monthDate,
            axisPointer: {
              type: 'shadow',
            },
          },
        ],
        yAxis: [
          {
            type: 'value',
            name: '实际能耗(KWh)',
            // min:uvMin,
            // max:uvMax,
            // splitNumber:5,
            // interval:(uvMax-uvMin)/5,

            // min: 0,//最小值
            // max: Ymaxval_interval.max1,//最大值
            // interval: Ymaxval_interval.interval1,//每个刻度的间隔值
          },
          {
            type: 'value',
            name: '累计节能(KWh)',
            // min:cliMin,
            // max:cliMax,
            // splitNumber:5,
            // interval:(cliMax-cliMin)/5,

            // min: 0,
            // max: Ymaxval_interval.max2,
            // interval: Ymaxval_interval.interval2,
          },
        ],
        series: [
          {
            name: '实际能耗(节能)',
            type: 'bar',
            stack: 'total', // 设置堆叠图，因为后端会分开返回两个数据，而必有一个为0
            barWidth: '55%',
            itemStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  {
                    offset: 0,
                    color: '#248ff7',
                  },
                  {
                    offset: 1,
                    color: '#6851f1',
                  },
                ]),
                barBorderRadius: 12,
              },
            },
            data: this.jienengData,
          },
          {
            name: '实际能耗(超标)',
            type: 'bar',
            stack: 'total',
            barWidth: '55%',
            itemStyle: {
              normal: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  {
                    offset: 0,
                    color: '#fb734e',
                    // color: '#fccb05'
                  },
                  {
                    offset: 1,
                    color: '#e32f46',
                    // color: '#f5804d'
                  },
                ]),
                barBorderRadius: 11,
              },
            },
            data: this.chaobiaoData,
          },
          {
            name: '计划用能',
            type: 'line',
            step: 'middle',
            symbol: 'none',
            // tooltip: { // 单独设置显示的样式
            //   valueFormatter: function (value) {
            //     return value + ' KWh';
            //   }
            // },
            itemStyle: {
              color: '#FAC858',
            },
            lineStyle: {
              join: 'round',
            },
            data: this.jihuaData,
          },
          {
            name: '累计节能',
            type: 'line',
            yAxisIndex: 1, // 依据哪儿个y周坐标显示数据
            symbol: 'circle',
            symbolSize: 8,
            // tooltip: { // 单独设置显示的样式
            //   valueFormatter: function (value) {
            //     return value + ' KWh';
            //   }
            // },
            itemStyle: {
              normal: {
                width: 12,
                color: '#8bd46e',
                // color: '#09bcb7'
              },
            },
            data: this.leijiData,
          },
        ],
      }

      option && myChart.setOption(option)
      window.addEventListener('resize', function () {
        myChart.resize()
      })
    },
    //echarts专用求最大公约数 不含小数
    chartlcm(a, b) {
      var result = 1
      for (var i = 1; i <= a && i <= b; i++) {
        if (a % i == 0 && b % i == 0) {
          result = i
        }
        if (result > 1 && i >= 10)
          //公约数大于10的时候 直接跳出 避免y轴刻度太多  (如果不介意刻度太多可以把这一段去掉）
          break
      }
      return result
    },
    //获取echarts  多Y轴的最大值和间隔值 lcmval 最大公约数   divisor 间隔数量
    YmaxvalAndinterval(m, n, lcmval, divisor) {
      var interval1 = Math.ceil(m / lcmval)
      var interval2 = Math.ceil(n / lcmval)
      if (lcmval != 1 && lcmval != 0 && lcmval <= 10) {
        return { max1: m, max2: n, interval1: interval1, interval2: interval2 }
      }
      if (divisor == undefined || divisor == null) divisor = 5
      //var mval = m % divisor;
      //if (mval > 0) {
      //    m = ((m - mval) / divisor + 1) * divisor
      //}
      m = Math.ceil(m / divisor) * divisor
      n = Math.ceil(n / divisor) * divisor
      interval1 = Math.ceil(m / divisor)
      interval2 = Math.ceil(n / divisor)
      return { max1: m, max2: n, interval1: interval1, interval2: interval2 }
    },
    /** 搜索按钮操作 */
    handleSearch(param) {
      this.getSavinganalysis()
    },
  },
}
</script>

<style lang="less" scoped>
@import '~@/assets/less/uStyle.less';
</style>
<style lang="less" scoped>
.saving-analysis {
  min-height: calc(100vh - 84px);
  padding: 0 10px;
  .text-detail-container {
    .item {
      height: 100px;
      box-shadow: 0 2px 10px rgba(153, 153, 153, 0.1);
      flex: 1;
      margin-right: 10px;
      padding: 10px;
      background: #fff;
      .name {
        font-size: 14px;
      }
      .value {
        margin-top: 8px;
        font-size: 1.5vw;
      }
    }
    .analysis-text {
      flex: 4;
      margin-right: 0;
      .text {
        margin-top: 8px;
        font-size: 1.1vw;
      }
    }
  }
  .item-echarts {
    min-height: calc(100vh - 284px);
    padding: 10px;
    margin-top: 10px;
    box-shadow: 0 2px 10px rgba(153, 153, 153, 0.1);
    background: #fff;
  }
}
</style>

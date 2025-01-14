<template>
  <a-spin :spinning="loading">
    <div class="station-analysis">
      <commonSearch hasAreaSearch ref="commonSearch" type="nodate" @search="handleSearch"></commonSearch>
      <div class="choose-zhibiao">
        <a-checkbox-group
          v-model="zhibiaoValue"
          name="checkboxgroup"
          :options="zhibiaoOptions"
          @change="zhibiaoChange"
        />
      </div>
      <div class="u-flex-jab content-container">
        <template v-if="zhibiaoValue.findIndex(item=>item==='hdlqsh')!== -1">
          <div class="common-container">
            <div class="common-title">总电量趋势</div>
            <div class="common-echart-area" ref="totalEleTrend"></div>
          </div>
        </template>
        <template v-if="zhibiaoValue.findIndex(item=>item==='ssxl')!== -1">
          <div class="common-container">
            <div class="common-title">输送效率</div>
            <div class="common-echart-area" ref="shusongXiaolv"></div>
          </div>
        </template>
        <template v-if="zhibiaoValue.findIndex(item=>item==='lqshwch')!== -1">
          <div class="common-container">
            <div class="common-title">冷却水温差</div>
            <div class="common-echart-area" ref="waterWencha"></div>
          </div>
        </template>
        <template v-if="zhibiaoValue.findIndex(item=>item==='hdlzhb')!== -1">
          <div class="common-container">
            <div class="common-title">耗电量占比</div>
            <div class="eleRatio-chart" ref="eleRatio"></div>
            <div class="eleRatio-legend">
              <div class="u-flex-jaa item" v-for="(item, index) in dataLists" :key="index">
                <div class="u-flex-jac">
                  <div class="example" :style="{background: item.color}"></div>
                  <div class="text" :style="{color: item.color}">{{item.name}}</div>
                </div>
                <div class="number">{{item.number}}KWh</div>
                <div class="pieValue">{{item.pieValue}}%</div>
              </div>
            </div>
          </div>
        </template>
        <template v-if="zhibiaoValue.findIndex(item=>item==='yxshjphl')!== -1">
          <div class="common-container lqshwch-container">
            <div class="common-title">运行时间平衡率</div>
            <div class="common-echart-area" ref="timeRate"></div>
          </div>
        </template>
      </div>
    </div>
  </a-spin>
</template>

<script>
import * as echarts from 'echarts'
import commonSearch from '../components/commonSearch'
import { httpAction, getAction } from '@/api/manage'
export default {
  name: '',
  components: {
    commonSearch,
  },
  data () {
    return {
      description: '设备能效分析',
      loading: false,
      zhibiaoValue: ['hdlqsh', 'hdlzhb', 'ssxl', 'yxshjphl', 'lqshwch'],
      zhibiaoOptions: [
        { label: '耗电量趋势', value: 'hdlqsh' },
        { label: '输送效率', value: 'ssxl' },
        { label: '冷却水温差', value: 'lqshwch' },
        { label: '耗电量占比', value: 'hdlzhb' },
        { label: '运行时间平衡率', value: 'yxshjphl' },
      ],
      dataLists: [
        { name: '#1冷却泵耗电量', number: 11514.45, pieValue: 34.37, color: '#5470C6' },
        { name: '#2冷却泵耗电量', number: 9485.32, pieValue: 28.32, color: '#91CC75' },
        { name: '#3冷却泵耗电量', number: 7969.53, pieValue: 23.79, color: '#FAC858' },
        { name: '#4冷却泵耗电量', number: 4527.72, pieValue: 13.52, color: '#EE6666' }
      ],
    }
  },
  created () {
  },
  mounted () {
    this.$nextTick(() => {
      this.initTotalEleTrend()
      this.initShusongXiaolv()
      this.initWaterWencha()
      this.initPieDianliang()
      this.initTimeRate()
      // this.getEnergyoverview()
    })
  },
  methods: {
    getEnergyoverview() {
      // {spaceId: 277797283102721}
      this.loading = true
      var data = this.$refs.commonSearch.queryParams
      getAction(this.url.list, data).then((res) => {
        if(res.success){
          // 获取数据
        }else{
          this.$message.warning(res.message);
        }
      }).finally(() => {
        this.$nextTick(() => {
          // this.initSortTop5()
          // this.initPieDianliang()
          // this.initTotalEleTrend()
        })
        this.loading = false;
      })
    },
    handleSearch(param) {
      this.getEnergyoverview()
    },
    zhibiaoChange(checkedValue){
      console.log(checkedValue)
    },
    // 总电量趋势-柱状图
    initTotalEleTrend() {
      // console.log(this.totalEleTrend.daysplit)
      var _this = this
      var chartDom = this.$refs.totalEleTrend
      var myChart = echarts.init(chartDom)
      var option

      option = {
        // title: {
        //   text: '用能趋势',
        //   left: 'center'
        // },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
          },
        },
        legend: {
          bottom: 0,
        },
        grid: {
          top: '10%',
          left: '3%',
          right: '4%',
          bottom: '25px',
          containLabel: true,
        },
        xAxis: {
          type: 'category',
          data: ['6月','7月','8月','9月','10月','11月','12月','1月','2月','3月','4月','5月',]
          // data: this.totalEleTrend.daysplit,
        },
        yAxis: {
          type: 'value',
        },
        toolbox: {
          show: true,
          feature: {
            saveAsImage: {},
          },
        },
        series: [
          {
            name: '#1冷却泵',
            type: 'bar',
            stack: 'total',
            color: '#5470C6',
            emphasis: {
              focus: 'series',
            },
            data: [150, 212, 201, 154, 190, 330, 410, 320, 199, 254, 324, 225]
            // data: this.totalEleTrend.dongli,
          },
          {
            name: '#2冷却泵',
            type: 'bar',
            stack: 'total',
            color: '#91CC75',
            // label: { // 是否显示柱形图上数字
            //   show: true
            // },
            // emphasis: { // 悬浮时是否模糊其他柱形图
            //   focus: 'series'
            // },
            data: [120, 132, 101, 134, 90, 230, 210, 209, 150, 140, 152, 143]
            // data: this.totalEleTrend.kongtiao,
          },
          {
            name: '#3冷却泵',
            type: 'bar',
            stack: 'total',
            color: '#EE6666',
            // emphasis: {
            //   focus: 'series'
            // },
            data: [220, 182, 191, 234, 290, 330, 310, 320, 145, 222, 245, 301]
            // data: this.totalEleTrend.zhaoming,
          },
          {
            name: '#4冷却泵',
            type: 'bar',
            stack: 'total',
            color: '#FAC858',
            // barWidth: '50%',
            // emphasis: {
            //   focus: 'series'
            // },
            data: [320, 302, 301, 334, 390, 330, 320, 300, 310, 316, 304, 322]
            // data: this.totalEleTrend.qita,
          },
        ],
      }

      option && myChart.setOption(option)

      window.addEventListener('resize', function () {
        myChart.resize()
      })
    },
    // 输送效率
    initShusongXiaolv() {
      var _this = this
      var chartDom = this.$refs.shusongXiaolv
      var myChart = echarts.init(chartDom)
      var option

      option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
          },
        },
        legend: {
          bottom: 0,
        },
        grid: {
          top: '10%',
          left: '3%',
          right: '4%',
          bottom: '25px',
          containLabel: true,
        },
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
          type: 'value'
        },
        toolbox: {
          show: true,
          feature: {
            saveAsImage: {},
          },
        },
        series: [
          {
            name: '输送效率',
            type: 'line',
            data: [150, 230, 224, 218, 135, 147, 260],
          }
        ]
      }

      option && myChart.setOption(option)

      window.addEventListener('resize', function () {
        myChart.resize()
      })
    },
    // 冷却水温差
    initWaterWencha() {
      var _this = this
      var chartDom = this.$refs.waterWencha
      var myChart = echarts.init(chartDom)
      var option

      option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
          },
        },
        legend: {
          bottom: 0,
        },
        grid: {
          top: '10%',
          left: '3%',
          right: '4%',
          bottom: '25px',
          containLabel: true,
        },
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
          type: 'value'
        },
        toolbox: {
          show: true,
          feature: {
            saveAsImage: {},
          },
        },
        series: [
          {
            name: '温差值',
            type: 'line',
            color: '#FAC858',
            data: [150, 230, 224, 218, 135, 147, 260],
          }
        ]
      }

      option && myChart.setOption(option)

      window.addEventListener('resize', function () {
        myChart.resize()
      })
    },
    // 耗电量占比
    initPieDianliang(){
      var chartDom = this.$refs.eleRatio
      var myChart = echarts.init(chartDom);
      var option;

      option = {
        series: [
          {
            type: 'gauge',
            radius: '90%',
            center: ['50%', '50%'],
            clockwise: true, // 仪表盘刻度是否是顺时针增长
            startAngle: '90', // 开始角度
            endAngle: '-269.9999', // 结束角度
            splitNumber: 36, // 仪表盘刻度的分割段数
            detail: {
              offsetCenter: [0, -20],
              formatter: ' '
            },
            detail: { // 仪表盘详情，用于显示数据-value
              show: true, // 是否显示详情,默认 true
              offsetCenter: [0, -4],// 相对于仪表盘中心的偏移位置
              color: '#000',
              fontWeight: '600',
              fontSize: '18',
              formatter: "{value}",	// 格式化函数或者字符串
            },
            title: {	//仪表盘标题-name。
              show: true, //是否显示标题,默认 true
              offsetCenter: [0, 16], // 相对于仪表盘中心的偏移位置
              color: '#000',
              fontWeight: '100',
              fontSize: '14',
            },
            pointer: { // 指针
              show: false
            },
            axisLine: {
              show: true,
              lineStyle: {
                color: [
                  [0.3437, '#5470C6'],
                  [0.6269, '#91CC75'],
                  [0.8648, '#FAC858'],
                  [1, '#EE6666']
                  // [0.3437, '#e2e14f'],
                  // [0.6269, '#00a7fb'],
                  // [0.8648, '#00e0fb'],
                  // [1, '#00ffb4']
                ],
                width: 18
              }
            },
            axisTick: { // 刻度线
              show: false
            },
            splitLine: { // 分割线
              show: true,
              distance: -18, // 分割线与轴线的距离（在哪儿显示）
              length: 18,
              lineStyle: {
                color: '#f1f1f1',
                width: 2
              }
            },
            axisLabel: {
              show: false
            },
            data: [
              {
                value: 33497.02,
                name: 'KWh'
              }
            ]
          },
          {
            type: 'pie',
            name: '内层细圆环',
            radius: ['72%', '73%'],
            center: ['50%', '50%'],
            hoverAnimation: false,
            startAngle: 200,
            itemStyle: {
              color: '#E3EBFA'
            },
            label: {
              show: false
            },
            data: [100]
          },
          {
            type: 'pie',
            name: '外层细圆环',
            radius: ['92%', '91%'],
            center: ['50%', '50%'],
            hoverAnimation: false,
            startAngle: 200,
            itemStyle: {
              color: '#E3EBFA'
            },
            label: {
              show: false
            },
            data: [100]
          }
        ]
      };

      option && myChart.setOption(option);
      window.addEventListener("resize",function (){
        myChart.resize();
      });
    },
    // 运行时间平衡率
    initTimeRate() {
      var _this = this
      var chartDom = this.$refs.timeRate
      var myChart = echarts.init(chartDom)
      var option

      option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
          },
        },
        legend: {
          bottom: 0,
        },
        grid: {
          top: '10%',
          left: '3%',
          right: '4%',
          bottom: '25px',
          containLabel: true,
        },
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
          type: 'value'
        },
        toolbox: {
          show: true,
          feature: {
            saveAsImage: {},
          },
        },
        series: [
          {
            name: '运行时间平衡率',
            type: 'bar',
            color: '#91CC75',
            data: [150, 230, 224, 218, 135, 147, 260],
          }
        ]
      }

      option && myChart.setOption(option)

      window.addEventListener('resize', function () {
        myChart.resize()
      })
    },
  }
}
</script>

<style lang="less" scoped>
  @import '~@/assets/less/uStyle.less';
</style>
<style lang="less" scoped>
.station-analysis{
  .choose-zhibiao{
    background-color: #fff;
    padding: 12px;
  }
  .content-container{
    flex-wrap: wrap;
    .common-container{
      width: 33%;
      height: 40vh;
      background-color: #fff;
      margin-top: 10px;
    }
    .lqshwch-container{
      width: 66.5%;
    }
    .common-title{
      text-align: center;
      font-weight: 600;
      height: 30px;
      line-height: 30px;
    }
    .common-echart-area{
      height: calc(100% - 30px);
    }
    .eleRatio-chart{
      width: 100%;
      height: 50%;
    }
    .eleRatio-legend{
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      // align-items: start;
      // align-items: flex-start;
      padding: 20px;
      font-size: 14px;
      .item{
        width: 100%;
        align-items: center;
        .example{
          width: 20px;
          height: 10px;
          margin-right: 10px;
        }
        .text{
          min-width: 50px;
        }
        .number{
          min-width: 140px;
          // margin-right: 22px;
        }
        .pieValue{

        }
      }
    }
  }
}
</style>

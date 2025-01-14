<template>
  <div class="peakValley-statistics">
    <commonSearch :showSearch="showSearch" @search="handleSearch"></commonSearch>
    <div class="peakValley-charts" ref="peakValleyCharts"></div>
    <div class="statistics-container u-flex-jab">
      <div class="dianfei-container item-common f1">
        <!-- chart图表 -->
        <div class="common-title">峰平谷电量占比</div>
        <!-- 右侧图例 -->
        <div class="u-flex common-container">
          <div class="chart" ref="dianfei"></div>
          <div class="legend">
            <div class="u-flex">
              <div>尖时</div>
              <div>11514.45KWh</div>
              <div>34.37%</div>
            </div>
            <div class="u-flex">
              <div>峰时</div>
              <div>9485.32KWh</div>
              <div>28.32%</div>
            </div>
            <div class="u-flex">
              <div>平时</div>
              <div>7969.53KWh</div>
              <div>23.79%</div>
            </div>
            <div class="u-flex">
              <div>谷时</div>
              <div>4527.72KWh</div>
              <div>13.52%</div>
            </div>
          </div>
        </div>
      </div>
      <div class="dianliang-container item-common f1">
        <!-- chart图表 -->
        <div class="common-title">峰平谷电量占比</div>
        <!-- 右侧图例 -->
        <div class="u-flex common-container">
          <div class="chart" ref="dianliang"></div>
          <div class="legend">
            <div class="u-flex item" v-for="(item, index) in dataLists" :key="index">
              <div class="example"></div>
              <div class="text">{{item.name}}</div>
              <div class="number">{{item.number}}KWh</div>
              <div class="pieValue">{{item.pieValue}}%</div>
            </div>
          </div>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
  import * as echarts from 'echarts';
  import commonSearch from '../components/commonSearch.vue'
  import { httpAction, getAction } from '@/api/manage'
  import { emsdata } from '../emsdata';
  export default {
    name: 'peakValleyAnalysis',
    components: {
      commonSearch
    },
    data () {
      return {
        description: '用能平衡分析',
        showSearch: true,
        dataLists: [
          { name: '尖时', number: 11514.45, pieValue: 34.37 },
          { name: '峰时', number: 9485.32, pieValue: 28.32 },
          { name: '平时', number: 7969.53, pieValue: 23.79 },
          { name: '谷时', number: 4527.72, pieValue: 13.52 }
        ]
      }
    },
    created () {
    },
    mounted() {
      this.$nextTick(() => {
        this.initPieDianfei()
        this.initPieDianliang()
        this.initEcharts()
      })
    },
    methods: {
      initEcharts(){
        var _this = this
        var chartDom = this.$refs.peakValleyCharts
        var myChart = echarts.init(chartDom);
        var option;

        option = {
          title: {
            text: '峰平谷电量统计',
            left: 'center'
          },
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          legend: {
            bottom: 0
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '6%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: ['11/29 00:00', '01:00', '02:00', '03:00', '04:00', '05:00', '06:00', '07:00', '08:00', '09:00', '10:00', '11:00', '12:00', '13:00', '14:00', '15:00']
          },
          yAxis: {
            type: 'value',
          },
          toolbox: {
            show: true,
            feature: {
              saveAsImage: {},
              mySearch: {
                show: true,
                title: '搜索',
                icon: 'path://M953.474215 908.234504l-152.576516-163.241391c61.92508-74.48211 95.81186-167.36973 95.81186-265.073744 0-229.294809-186.63531-415.930119-416.102133-415.930119-229.294809 0-415.930119 186.63531-415.930119 415.930119s186.63531 415.930119 415.930119 415.930119c60.032925 0 118.00168-12.55703 172.186125-37.327062 16.169326-7.396607 23.221905-26.318159 15.825298-42.315471-7.396607-16.169326-26.318159-23.221905-42.315471-15.825298-45.927768 20.813707-94.951789 31.478582-145.695952 31.478582-194.031917 0-351.94087-157.908953-351.94087-351.94087 0-194.031917 157.908953-351.94087 351.94087-351.94087 194.031917 0 351.94087 157.908953 351.94087 351.94087 0 91.339493-34.918864 177.86259-98.048043 243.743995-12.213002 12.729044-11.868974 33.026709 0.860071 45.239711 1.032085 0.860071 2.236183 1.204099 3.268268 2.064169 0.860071 1.204099 1.376113 2.752226 2.408198 3.956325l165.477574 177.00252c6.192508 6.70855 14.793214 10.148833 23.393919 10.148833 7.912649 0 15.653284-2.92424 21.845792-8.600706C964.827146 941.433227 965.515202 921.135562 953.474215 908.234504z',
                onclick: function (params) {
                  _this.showSearch = !_this.showSearch
                  console.log(window.innerHeight, _this.$refs.peakValleyCharts.offsetTop)
                  _this.echartsHeight = window.innerHeight - 84 - 60 - 40 + _this.$refs.peakValleyCharts.offsetTop
                  console.log(_this.echartsHeight)
                  setTimeout(() => { // 延迟绘制，不然浏览器反应不过来
                    myChart.resize(); // 会出现抖动,待解决
                    // _this.myChart.clear();_this.myChart.setOption(option);
                    // this.myChart.clear()
                  }, 1)
                }
              }
            }
          },
          series: [
            {
              name: '峰时',
              type: 'bar',
              stack: 'total',
              barWidth: '30%',
              // emphasis: {
              //   focus: 'series'
              // },
              data: [320, 302, 301, 334, 390, 330, 320]
            },
            {
              name: '尖时',
              type: 'bar',
              stack: 'total',
              // label: { // 是否显示柱形图上数字
              //   show: true
              // },
              // emphasis: { // 悬浮时是否模糊其他柱形图
              //   focus: 'series'
              // },
              data: [120, 132, 101, 134, 90, 230, 210]
            },
            {
              name: '平时',
              type: 'bar',
              stack: 'total',
              // emphasis: {
              //   focus: 'series'
              // },
              data: [220, 182, 191, 234, 290, 330, 310]
            },
            {
              name: '谷时',
              type: 'bar',
              stack: 'total',
              emphasis: {
                focus: 'series'
              },
              data: [150, 212, 201, 154, 190, 330, 410]
            }
          ]
        };
        

        option && myChart.setOption(option);
        
        window.addEventListener("resize",function (){
          myChart.resize();
        });

      },
      initPieDianfei(){
        var chartDom = this.$refs.dianfei
        var myChart = echarts.init(chartDom);
        var option;

        option = {
          series: [
            {
              type: 'gauge',
              radius: '70%',
              center: ['68%', '50%'],
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
                    // [0.3437, '#5470C6'],
                    // [0.6269, '#91CC75'],
                    // [0.8648, '#FAC858'],
                    // [1, '#EE6666']
                    [0.3437, '#e2e14f'],
                    [0.6269, '#00a7fb'],
                    [0.8648, '#00e0fb'],
                    [1, '#00ffb4']
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
                  color: '#fff',
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
              radius: ['52%', '53%'],
              center: ['68%', '50%'],
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
              radius: ['72%', '71%'],
              center: ['68%', '50%'],
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
      initPieDianliang(){
        var chartDom = this.$refs.dianliang
        var myChart = echarts.init(chartDom);
        var option;

        option = {
          series: [
            {
              type: 'gauge',
              radius: '70%',
              center: ['68%', '50%'],
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
              radius: ['52%', '53%'],
              center: ['68%', '50%'],
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
              radius: ['72%', '71%'],
              center: ['68%', '50%'],
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
      /** 搜索按钮操作 */
      handleSearch(param) {
        this.getTest()
      },
    }
  }
</script>

<style lang="less" scoped>
  @import '~@/assets/less/uStyle.less';
</style>
<style lang="less" scoped>
.peakValley-statistics{
  min-height: calc(100vh - 84px);
  padding: 10px;
  .peakValley-charts{
    min-height: calc(100vh - 434px);
    padding: 10px;
    margin-top: 10px;
    box-shadow: 0 2px 10px rgba(0,0,0,.1);
  }
  .statistics-container{
    margin-top: 10px;
    .item-common{
      width: 100%;
      height: 100%;
      box-shadow: 0 2px 10px rgba(0,0,0,.1);
      padding: 20px 0;
      .common-title{
        font-size: 18px;
        font-weight: 600;
        text-align: center;
        color: #464646;
      }
      .common-container{
        width: 100%;
        height: 100%;
      }
      .chart{
        // width: 100%;
        height: 240px;
        flex: 1;
      }
      .legend{
        flex: 1;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
        // align-items: start;
        align-items: flex-start;
        padding: 20px;
        font-size: 14px;
        .item{
          width: 100%;
          align-items: center;
          .example{
            width: 20px;
            height: 10px;
            background: #000;
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
    .dianfei-container{
      margin-right: 10px;
    }
    .dianliang-container{
    }
  }
}
</style>

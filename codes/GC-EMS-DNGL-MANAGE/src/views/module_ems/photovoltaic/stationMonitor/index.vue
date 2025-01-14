<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="8" :style="{ marginBottom: '12px' }">
        <a-card
          :loading="loading"
          :body-style="{ padding: '0px 8px 8px' }"
          class="topcard"
          :bordered="false"
          style="text-align: center"
        >
          <a-row :gutter="24">
            <!-- <a-form layout="inline">
                <a-col :span="24">
                  <a-form-item label="站点名称">
                    <a-input placeholder="请输入站点名称"></a-input>
                  </a-form-item>
                  <a-button type="primary" icon="search">查询</a-button>
                </a-col>
            </a-form> -->
            <a-col :span="12">
              <div class="totalEle-trend" ref="realPowerChart" style="padding-top:15%;"></div>
              <!-- <div style="font-size:20px;">{{stationInfo.realPower}}</div> -->
              <span>实时发电功率(kW)</span>
            </a-col>
            <a-col :span="12">
              <div class="head-info" style="text-align: left; margin-top: 30px">
                <span style="line-height: 32px">并网电价：{{ stationInfo.fSubsidyprice }} 元/kW·h</span>
              </div>
              <div class="head-info" style="text-align: left">
                <span style="line-height: 32px">电站装机容量：{{ stationInfo.fInstalledcapacity }} kW</span>
              </div>
              <div class="head-info" style="text-align: left">
                <span style="line-height: 32px">逆变器数量：{{ stationInfo.inverterNum }} 个</span>
              </div>
              <div class="head-info" style="text-align: left">
                <span style="line-height: 32px">并网电压：{{ stationInfo.fGridvoltage }} V</span>
              </div>
              <div class="head-info" style="text-align: left">
                <span style="line-height: 32px">年利用小时数：{{ elecdetail.effectiveHours }} h</span>
              </div>
              <div class="head-info" style="text-align: left">
                <span style="line-height: 32px">总发电量：{{ totalEpiValue }} kWh</span>
              </div>
            </a-col>
          </a-row>
        </a-card>
      </a-col>

      <a-col :sm="24" :md="12" :xl="8" :style="{ padding: '0px 12px 0px 0px' }">
        <a-row>
          <a-col :span="24">
            <a-card
              :loading="loading"
              :body-style="{ padding: '5px 8px 8px' }"
              class="topcard"
              size="small"
              :bordered="false"
              title="发电量信息"
            >
              <a-row>
                <a-col :span="8">
                  <div class="head-info" :class="center && 'center'">
                    <div style="height:46px; width:130%; display: flex; align-items: center;">
                      <div style="height:50%; width:15%;" class="img4"></div>
                      <span>日发电量</span>
                    </div>
                    <p>{{ elecdetail.powerByDay }} kW·h</p>
                    <!-- <span>日发电量</span>
                    <p>{{ elecdetail.powerByDay }} kW·h</p> -->
                  </div>
                </a-col>
                <a-col :span="8">
                  <div class="head-info" :class="center && 'center'">
                    <div style="height:46px; width:130%; display: flex; align-items: center;">
                      <div style="height:50%; width:15%;" class="img5"></div>
                      <span>月发电量</span>
                    </div>
                    <p>{{elecdetail.powerByMonth}} kW·h</p>
                    <!-- <span>月发电量</span>
                    <p>{{ elecdetail.powerByMonth }} kW·h</p> -->
                  </div>
                </a-col>
                <a-col :span="8">
                  <div class="head-info" :class="center && 'center'">
                    <div style="height:46px; width:130%; display: flex; align-items: center;">
                      <div style="height:50%; width:15%;" class="img6"></div>
                      <span>年发电量</span>
                    </div>
                    <p>{{elecdetail.powerByYear}} kW·h</p>
                    <!-- <span>年发电量</span>
                    <p>{{ elecdetail.powerByYear }} kW·h</p> -->
                  </div>
                </a-col>
              </a-row>
              <a-divider style="margin: 8px 0" />
              <span class="leftHeader"></span>
              <span class="head-title">收益信息</span>
              <a-row>
                <a-col :span="8">
                  <div class="head-info" :class="center && 'center'">
                    <div style="height:46px; width:130%; display: flex; align-items: center;">
                      <div style="height:50%; width:15%;" class="img7"></div>
                      <span>日收益</span>
                    </div>
                    <p>{{elecdetail.powerByDayProfit}} 元</p>
                    <!-- <span>日收益</span>
                    <p>{{ elecdetail.powerByDayProfit }} 元</p> -->
                  </div>
                </a-col>
                <a-col :span="8">
                  <div class="head-info" :class="center && 'center'">
                    <div style="height:46px; width:130%; display: flex; align-items: center;">
                      <div style="height:50%; width:15%;" class="img8"></div>
                      <span>月收益</span>
                    </div>
                    <p>{{elecdetail.powerByMonthProfit}} 元</p>
                    <!-- <span>月收益</span>
                    <p>{{ elecdetail.powerByMonthProfit }} 元</p> -->
                  </div>
                </a-col>
                <a-col :span="8">
                  <div class="head-info" :class="center && 'center'">
                    <div style="height:46px; width:130%; display: flex; align-items: center;">
                      <div style="height:50%; width:15%;" class="img9"></div>
                      <span>年收益</span>
                    </div>
                    <p>{{elecdetail.powerByYearProfit}} 元</p>
                    <!-- <span>年收益</span>
                    <p>{{ elecdetail.powerByYearProfit }} 元</p> -->
                  </div>
                </a-col>
              </a-row>
            </a-card>
          </a-col>
        </a-row>
      </a-col>
      <a-col :sm="24" :md="12" :xl="8" :style="{ padding: '0px 12px 0px 0px' }">
        <a-row>
          <a-card
            :loading="loading"
            :body-style="{ padding: '10px 8px 8px' }"
            class="topcard"
            size="small"
            :bordered="false"
            title="视频监控"
          >
          </a-card>
        </a-row>
      </a-col>
    </a-row>
    <a-row>
      <a-col :sm="24" :md="12" :xl="8" :style="{ padding: '0px 16px 0px 0px' }">
        <a-card
          :loading="loading"
          :body-style="{ padding: '10px 8px 8px' }"
          class="middlecard"
          size="small"
          :bordered="false"
          title="气象信息"
        >
          <a-row>
            <a-col :span="8">
              <div class="head-info" :class="center && 'center'">
                <div class="i fuzhao">
                  <div style="height:90%; width:90%;" class="img1"></div>
                </div>
                <span>辐照度</span>
                <p>{{ envirInfo.irradiance }} {{ envirInfo.irradianceUnit }}</p>
              </div>
            </a-col>
            <a-col :span="8">
              <div class="head-info" :class="center && 'center'">
                <div class="i wenshi">
                  <div style="height:90%; width:90%;" class="img2"></div>
                </div>
                <span>环境温湿度</span>
                <p>{{ envirInfo.temp }} {{ envirInfo.tempUnit }} / {{ envirInfo.humi }} {{ envirInfo.humiUnit }}</p>
              </div>
            </a-col>
            <a-col :span="8">
              <div class="head-info" :class="center && 'center'">
                <div class="i fengsu">
                  <div style="height:90%; width:90%;" class="img3"></div>
                </div>
                <span>风速</span>
                <p>{{ envirInfo.windSpeed }} {{ envirInfo.windSpeedUnit }}</p>
              </div>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="16">
        <a-card
          :body-style="{ padding: '0px 8px 8px' }"
          class="middlecard"
          :bordered="false"
          size="small"
          title="功率及发电量趋势"
          style="margin-left: -4px"
        >
          <div class="totalEle-trend2" ref="powerChart"></div>
        </a-card>
      </a-col>
    </a-row>
    <a-row>
      <a-col :sm="24" :md="12" :xl="24" :style="{ padding: '12px 0px 0px 0px' }">
        <a-card
          :body-style="{ padding: '0px 8px 8px' }"
          class="middlecard"
          :bordered="false"
          size="small"
          title="逆变器信息"
          style="height: 700px;"
        >
          <div class="tri-colorLight" ref="flexDivGroup">
            <!-- <div class="item-title item-title1" style="padding: 16px 0px 0px 0px; font-size: 16px; margin-left: 26px">
              逆变器信息
            </div> -->
            <!-- <a-divider style="margin: 16px 0" /> -->
            <div class="item-content" ref="itemFlexDiv" v-for="(item, index) in invInfo" :key="index" cl>
              <div class="item-title">{{ item.fInvertername }}</div>
              <div style="width: 100%">
                <div class="item-inv"></div>
                <div class="item-cc">
                  <div class="item-title"></div>
                  <div>厂家：{{ item.fManufacturername }}</div>
                  <div>设备型号：{{ item.fProductmodel }}</div>
                  <div>状态：{{ item.state }}</div>
                  <div>额定交流功率：{{ item.fRatedoutputpower }} kW</div>
                  <div>实时发电功率：{{ item.realGeneratingPower }} kW</div>
                  <div>当日发电量：{{ item.powerByDay }} kW·h</div>
                  <div>组件峰值功率：{{ item.fComponentpeakpower }} kWp</div>
                </div>
              </div>
              <router-link :to="'/equipment/info/detail/' + item.fInverterid">
                <!-- <router-link :to="'/system/dict-data/index/' + item.equipmentid"> -->
                <a-button type="success" plain style="margin-top: 10px">查看详情 </a-button>
              </router-link>
            </div>
            <!-- 优化布局 -->
            <div ref="completion" v-for="item in completionNumber" :key="'com' + item" class="completion"></div>
          </div>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { httpAction, getAction } from '@/api/manage'
import { pvdata } from '../pvdata'

export default {
  name: 'stationMonitor',
  components: {},
  data() {
    return {
      description: '电站运行监控',
      loading: true,
      center: null,
      completionNumber: 0, // 布局使用参数
      elecdetail: {}, // 发电量信息
      envirInfo: {}, // 气象信息
      invInfo: [], // 逆变器信息
      stationInfo: {}, // 电站信息
      totalEpiValue: 0, // 总发电量
      times: [], // 功率及发电量趋势-时间
      powers: [], // 功率及发电量趋势-功率
      irradiances: [], // 功率及发电量趋势-辐照强度
    }
  },
  created() {
    setTimeout(() => {
      this.loading = !this.loading
    }, 1000)
  },
  mounted() {
    // 监听窗口改变设置最后一行布局
    window.addEventListener('resize', () => {
      this.WindowOnresize()
    })
    this.$nextTick(() => {
      setTimeout(() => {
        this.getTest()
      }, 1000)
      // this.getEnergyoverview()
    })
  },
  methods: {
    getTest() {
      var listData = pvdata.stationMonitorData
      console.log(listData)
      this.totalEpiValue = listData.totalEpiValue
      this.elecdetail = pvdata.pvDashboardData.elecdetail
      this.envirInfo = listData.envirInfo
      this.invInfo = listData.invInfo
      this.stationInfo = listData.stationInfo
      listData.dayPowerValue.forEach((i) => {
        this.times.push(i.fTime)
        this.powers.push(i.fP)
        this.irradiances.push(i.irradianceValue)
      })

      this.initRealPowerChart()
      this.initPowerChart()

      this.$nextTick(() => {
        this.WindowOnresize()
      })
    },
    initRealPowerChart() {
      var _this = this
      var chartDom = _this.$refs.realPowerChart
      var myChart = echarts.init(chartDom)

      var option = {
        tooltip: {
          formatter: '{a} <br/>{b} : {c}%',
        },
        series: [
          {
            name: 'Pressure',
            type: 'gauge',
            min: 0,
            max: 30,
            progress: {
              show: true,
            },
            detail: {
              // 仪表盘展示文字
              // show: false,
              formatter: '{value}',
              fontSize: '18',
              color: "#838383"
            },
            radius: '100%', // 仪表盘放大/缩小
            axisLine: { lineStyle: {
                color: [[1, '#1FD09D']],
                // width: 8
            } },
            axisLabel: {
                textStyle: {
                    color: "#1FD09D"
                }
            },
            // 分隔线
              splitLine: {
                show: true,
                // 这里刻度线长短
                // length: 12,
                lineStyle: {
                  width: 2,
                  color: '#1FD09D'
                }
              },
              // 刻度线
              axisTick: {
                show: true,
                splitNumber: 5,
                length: 10,
                lineStyle: {
                  color: '#B0B0B0',
                  width: 1,
                },
              },
              // 刻度标签(数字)
              axisLabel: {
                // distance: 2,
                fontSize: '10',
                color: '#B0B0B0',
              },
              // 仪表盘指针
              pointer: {
                show: true,
                length: '65%',
                width: 5 // 指针粗细
              },
              // 仪表盘指针样式
              itemStyle: {
                color: '#1FD09D',
              },
            data: [
              {
                value: _this.stationInfo.realPower,
                // name: '功率',
              },
            ],
          },
        ],
      }

      option && myChart.setOption(option)

      window.addEventListener('resize', function () {
        myChart.resize()
      })
    },
    initPowerChart() {
      var _this = this
      var chartDom = _this.$refs.powerChart
      var myChart = echarts.init(chartDom)

      var option = {
        tooltip: {
          trigger: 'axis',
          position: function (pt) {
            return [pt[0], '20%']
          },
        },
        legend: {
          data: ['功率', '辐照度'],
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: _this.times,
        },
        yAxis: [
          {
            type: 'value',
            name: '功率',
            boundaryGap: [0, '30%'],
          },
          {
            type: 'value',
            name: '辐照度',
            boundaryGap: [0, '30%'],
          },
        ],
        // dataZoom: [
        //   {
        //     type: 'inside',
        //     start: 0,
        //     end: 100,
        //     xAxisIndex: 0,
        //   },
        //   {
        //     start: 0,
        //     end: 10,
        //   },
        //   {
        //     show: true,
        //     yAxisIndex: 0,
        //     filterMode: "empty",
        //     width: 30,
        //     height: "80%",
        //     right: "7%",
        //   },
        // ],
        series: [
          {
            name: '功率',
            type: 'line',
            symbol: 'none',
            sampling: 'lttb',
            itemStyle: {
              color: '#FBB13C',
            },
            markPoint: {
              data: [
                { type: 'max', name: 'Max' },
                { type: 'min', name: 'Min' },
              ],
            },
            data: _this.powers,
          },
          {
            name: '辐照度',
            type: 'line',
            symbol: 'none',
            sampling: 'lttb',
            yAxisIndex: 1, // 依据哪儿个y周坐标显示数据
            itemStyle: {
              color: '#57B8FF',
            },
            markPoint: {
              data: [
                { type: 'max', name: 'Max' },
                { type: 'min', name: 'Min' },
              ],
            },
            data: _this.irradiances,
          },
        ],
      }

      option && myChart.setOption(option)

      window.addEventListener('resize', function () {
        myChart.resize()
      })
    },
    // 设置最后一行布局
    WindowOnresize() {
      const equipmentGroupWidth = this.$refs.flexDivGroup.offsetWidth - 10
      // console.log("整个页面宽度:--->"+equipmentGroupWidth);
      const itemEquipmentList = this.$refs.itemFlexDiv
      const itemEquipmentCount = itemEquipmentList.length // 现有item数量
      // console.log("现有的item的数量:--->"+itemEquipmentCount);
      const itemEquipmentWidth = itemEquipmentList[0].offsetWidth
      // console.log("宽度:--->"+itemEquipmentWidth);
      // const itemEquipmentWidth = itemEquipmentList[0].getBoundingClientRect().width + 6;
      const rowEquipmentCount = parseInt(equipmentGroupWidth / itemEquipmentWidth) // 每一行有几个item
      // console.log("每一行有几个item:--->"+rowEquipmentCount);
      const lastItemLenth = itemEquipmentCount % rowEquipmentCount // 最后一行有几个
      // console.log(equipmentGroupWidth,itemEquipmentWidth, itemEquipmentCount, rowEquipmentCount, lastItemLenth)
      // console.log("最后一行有几个item:--->"+lastItemLenth);
      this.completionNumber = lastItemLenth === 0 ? 0 : rowEquipmentCount - lastItemLenth
      // console.log("需要补上的空白格子的个数:"+this.completionNumber)
    },
  },
}
</script>

<style lang="less" scoped>
.chart-card-header {
  position: relative;
  overflow: hidden;
  width: 100%;
  height: 50px;

  .meta {
    position: relative;
    overflow: hidden;
    //   width: 100%;
    color: #ffffff;
    font-size: 20px;
    line-height: 38px;
    float: left;
  }
}

.chart-card-action {
  cursor: pointer;
  position: absolute;
  top: 0;
  right: 0;
}

.chart-card-footer {
  border-top: 1px solid #e8e8e8;
  padding-top: 9px;
  margin-top: 8px;

  > * {
    position: relative;
  }

  .field {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    margin: 0;
  }
}

.chart-card-content {
  margin-bottom: 12px;
  position: relative;
  height: 46px;
  width: 100%;

  .content-fix {
    position: absolute;
    left: 0;
    bottom: 0;
    width: 100%;
  }
}

.total {
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
  white-space: nowrap;
  color: #ffffff;
  // margin-top: 4px;
  margin-bottom: 0;
  font-size: 30px;
  line-height: 38px;
  height: 38px;
  float: right;
}
.circle-cust {
  position: relative;
  top: 28px;
  left: -100%;
}
.extra-wrapper {
  line-height: 55px;
  padding-right: 24px;

  .extra-item {
    display: inline-block;
    margin-right: 24px;

    a {
      margin-left: 24px;
    }
  }
}

/* 首页访问量统计 */
.head-info {
  position: relative;
  text-align: center;
  padding: 0 32px 0 0;
  min-width: 15px;

  &.center {
    text-align: center;
    padding: 0 32px;
  }

  span {
    color: rgba(0, 0, 0, 0.45);
    display: inline-block;
    font-size: 0.95rem;
    line-height: 42px;
    margin-bottom: 1px;
  }
  p {
    line-height: 32px;
    margin: 0;
    a {
      font-weight: 600;
      font-size: 1rem;
    }
  }
  .bm-view {
    width: 100%;
    height: 300px;
  }

  .img1 {
    background: url('../../../../assets/images/fuzhao.png') no-repeat center center;
    background-size: 70%;
  }
  .img2 {
    background: url('../../../../assets/images/wenshi.png') no-repeat center center;
    margin-left: 5%;
    background-size: 70%;
  }
  .img3 {
    background: url('../../../../assets/images/fengsu.png') no-repeat center center;
    background-size: 70%;
  }
  .img4 {
    background: url('../../../../assets/images/Rd.png') no-repeat center center;
    background-size: 70%;
    margin-left: 10%;
    margin-right: 1%;
  }
  .img5 {
    background: url('../../../../assets/images/Yd.png') no-repeat center center;
    background-size: 70%;
    margin-left: 9%;
    margin-right: 1%;
  }
  .img6 {
    background: url('../../../../assets/images/Nd.png') no-repeat center center;
    background-size: 70%;
    margin-left: 9%;
    margin-right: 1%;
  }
  .img7 {
    background: url('../../../../assets/images/Rs.png') no-repeat center center;
    background-size: 80%;
    margin-left: 15%;
    // margin-right: 1%;
  }
  .img8 {
    background: url('../../../../assets/images/Ys.png') no-repeat center center;
    background-size: 80%;
    margin-left: 13%;
    margin-right: 1%;
  }
  .img9 {
    background: url('../../../../assets/images/Ns.png') no-repeat center center;
    background-size: 80%;
    margin-left: 13%;
    margin-right: 1%;
  }
  .i{
    font-size: 2vw;
    border-radius: 100%;
    width: 80px;
    height: 80px;
    display: flex;
    justify-content: center;
    align-items: center;
    // text-align: center;
    // line-height: 60px;
  }
  .fuzhao{
    margin: 15% 0 7% 22%;
    background-image: linear-gradient(240deg, #5eb2f77c, #8998F6);
  }
  .wenshi{
    margin: 15% 0 7% 22%;
    background-image: linear-gradient(240deg, #e5beff, #C680FD);
  }
  .fengsu{
    margin: 15% 0 7% 22%;
    background-image: linear-gradient(240deg, #56E2CE, #07B79F);
  }
}
.head-title {
  font-size: 15px;
  margin-left: 10px;
}

.totalEle-trend {
  height: 200px;
  // width: 200px;
}
.totalEle-trend2 {
  height: 220px;
}
.topcard {
  height: 245px;
}
.middlecard {
  height: 245px;
}
// 三色灯
.tri-colorLight {
  display: flex;
  background: #fff;
  justify-content: space-around;
  align-items: center;
  flex-wrap: wrap;
  // width: 20%;
  .item-container {
    padding: 10px;
    box-sizing: border-box;
  }
  .item-content {
    width: 424px;
    // border: 2px solid #c3c3c3;
    color: rgba(0, 0, 0, 0.45);
    // padding: 16px 50px 20px;
    // padding-top: 16px;
    padding-bottom: 20px;
    margin: 10px 6px;
    // box-shadow: 0 2px 10px 2px rgba(59, 78, 128, 0.5) inset;
    text-align: center;
    box-sizing: border-box;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    .item-inv {
      background-image: url(../../../../assets/inv.png);
      background-repeat: no-repeat;
      background-size: 75%;
      height: 181px;
      width: 132px;
      margin: 20px 0px 0px 0px;
      background-position: 50% 0;
      float: left;
    }
    .item-title {
      width: 100%;
      font-size: 18px;
      margin-top: 10px;
      margin-bottom: 10px;
      font-weight: bold;
      background-color: #E5F6F4;
      color: #666666;
      // display: flex;
      // justify-content: center;
      // align-items: center;
    }
    .item-cc {
      float: right;
      text-align: left;
    }
  }
  .item_content_rad {
    border-radius: 18px;
  }
  .completion {
    width: 424px;
    margin: 10px 6px;
  }
}
.leftHeader{
  height: 100%;
  // width: 100%;
  // background-image: linear-gradient(150deg, #01ADA8, #80D6D3);
  border: 2px solid #01ADA8;
  margin-left: 2%;
  // margin-right: 3%;
}
::v-deep .ant-card{
  border-radius: 5px;
  box-shadow: -1px 0 2px rgba(180, 180, 180, 0.156), /*左侧阴影*/
                1px 0 2px rgba(180, 180, 180, 0.156), /*右侧阴影*/
                -2px 2px 2px rgba(180, 180, 180, 0.156), /*上方阴影*/
                2px 2px 2px rgba(180, 180, 180, 0.156);
}
::v-deep .ant-card-head{
  background-color: #01ADA8 !important;
  color: #fff;
}
</style>
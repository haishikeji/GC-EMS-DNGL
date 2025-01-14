<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="18" :style="{ marginBottom: '12px' }">
        <a-row :gutter="24">
          <a-col :sm="24" :md="12" :xl="8" :style="{ marginBottom: '12px', padding: '0px 0px 0px 12px' }">
            <a-card
              :body-style="{ padding: '10px 8px 8px' }"
              :bordered="false"
              style="background: linear-gradient(45deg, #8897f5, #9eaafd)"
            >
              <div class="chart-card-header">
                <div class="meta">
                  <div class="img"></div>
                  <span class="chart-card-title" style="display: flex; align-items: center;">电站数量</span>
                </div>
                <div class="total">
                  <span style="display: flex; align-items: center;">2 个</span>
                </div>
              </div>
            </a-card>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8" :style="{ marginBottom: '12px', padding: '0px 0px 0px 12px' }">
            <a-card
              :body-style="{ padding: '10px 8px 8px' }"
              :bordered="false"
              style="background: linear-gradient(45deg, #c67ffd, #d9a0fd)"
            >
              <div class="chart-card-header">
                <div class="meta">
                  <div class="img2"></div>
                  <span class="chart-card-title" style="display: flex; align-items: center;">电站装机容量</span>
                </div>
                <div class="total">
                  <span style="display: flex; align-items: center;">{{elecdetail.fInstalledCapacity}} kW</span>
                </div>
              </div>
            </a-card>
          </a-col>
          <a-col :sm="24" :md="12" :xl="8" :style="{ marginBottom: '12px' }">
            <a-card
              :body-style="{ padding: '10px 8px 8px' }"
              :bordered="false"
              style="background: linear-gradient(45deg, #07b59d, #09d5b8)"
            >
              <div class="chart-card-header">
                <div class="meta">
                  <div class="img3"></div>
                  <span class="chart-card-title" style="display: flex; align-items: center;">当前发电功率</span>
                </div>
                <div class="total">
                  <span style="display: flex; align-items: center;">{{elecdetail.realGeneratingPower}} kwh</span>
                </div>
              </div>
            </a-card>
          </a-col>
        </a-row>
        <a-row :gutter="24">
          <a-col :sm="24" :md="12" :xl="24">
            <div id="map" style="width: 100%; height: 775px"></div>
          </a-col>
        </a-row>
      </a-col>

      <a-col :sm="24" :md="12" :xl="6" :style="{ padding: '0px 12px 0px 0px' }">
        <a-row>
          <a-col :span="24">
            <a-card :loading="loading" :body-style="{ padding: '10px 8px 8px' }" :bordered="false">
              <span class="leftHeader"></span>
              <span class="head-title">发电量信息</span>
              <a-row>
                <a-col :span="8">
                  <div class="head-info" :class="center && 'center'">
                    <div style="height:46px; width:130%; display: flex; align-items: center;">
                      <div style="height:50%; width:15%;" class="img4"></div>
                      <span>日发电量</span>
                    </div>
                    <p>{{elecdetail.powerByDay}} kW·h</p>
                  </div>
                </a-col>
                <a-col :span="8">
                  <div class="head-info" :class="center && 'center'">
                    <div style="height:46px; width:130%; display: flex; align-items: center;">
                      <div style="height:50%; width:15%;" class="img5"></div>
                      <span>月发电量</span>
                    </div>
                    <p>{{elecdetail.powerByMonth}} kW·h</p>
                  </div>
                  <!-- <div class="head-info" :class="center && 'center'">
                    <span>月发电量</span>
                    <p>{{elecdetail.powerByMonth}} kW·h</p>
                  </div> -->
                </a-col>
                <a-col :span="8">
                  <div class="head-info" :class="center && 'center'">
                    <div style="height:46px; width:130%; display: flex; align-items: center;">
                      <div style="height:50%; width:15%;" class="img6"></div>
                      <span>年发电量</span>
                    </div>
                    <p>{{elecdetail.powerByYear}} kW·h</p>
                  </div>
                  <!-- <div class="head-info" :class="center && 'center'">
                    <span>年发电量</span>
                    <p>{{elecdetail.powerByYear}} kW·h</p>
                  </div> -->
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
                  </div>
                  <!-- <div class="head-info" :class="center && 'center'">
                    <span>日收益</span>
                    <p>{{elecdetail.powerByDayProfit}} 元</p>
                  </div> -->
                </a-col>
                <a-col :span="8">
                  <div class="head-info" :class="center && 'center'">
                    <div style="height:46px; width:130%; display: flex; align-items: center;">
                      <div style="height:50%; width:15%;" class="img8"></div>
                      <span>月收益</span>
                    </div>
                    <p>{{elecdetail.powerByMonthProfit}} 元</p>
                  </div>
                  <!-- <div class="head-info" :class="center && 'center'">
                    <span>月收益</span>
                    <p>{{elecdetail.powerByMonthProfit}} 元</p>
                  </div> -->
                </a-col>
                <a-col :span="8">
                  <div class="head-info" :class="center && 'center'">
                    <div style="height:46px; width:130%; display: flex; align-items: center;">
                      <div style="height:50%; width:15%;" class="img9"></div>
                      <span>年收益</span>
                    </div>
                    <p>{{elecdetail.powerByYearProfit}} 元</p>
                  </div>
                  <!-- <div class="head-info" :class="center && 'center'">
                    <span>年收益</span>
                    <p>{{elecdetail.powerByYearProfit}} 元</p>
                  </div> -->
                </a-col>
              </a-row>
            </a-card>
          </a-col>
        </a-row>
        <a-row>
          <a-card
            :loading="loading"
            :body-style="{ padding: '10px 8px 8px' }"
            :bordered="false"
            style="margin-top: 12px;height:266px;"
          >
            <span class="leftHeader"></span>
            <span class="head-title">当年社会效益</span>
            <div class="chart-card-header">
              <div class="societyTree">
                <div class="societyTreeFont Cleft">
                  <span class="treeF">节约标煤</span><br />
                  <span class="treeF2">{{elecdetail.coalAmount}} t</span>
                </div>
                <div class="societyTreeFont Cright">
                  <span class="treeF">减少二氧化碳排放</span><br />
                  <span class="treeF2">{{elecdetail.co2Amount}} t</span>
                </div>
              </div>
            </div>
          </a-card>
        </a-row>
        <a-row>
          <a-card
            :body-style="{ padding: '10px 8px 8px' }"
            :bordered="false"
            style="margin-top: 12px;height:300px;"
          >
            <span class="leftHeader"></span>
            <span class="head-title">年发电量</span>
            <div class="totalEle-trend" ref="yearElec"></div>
          </a-card>
        </a-row>
      </a-col>
    </a-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { httpAction, getAction } from '@/api/manage'
import { pvdata } from '../pvdata';

export default {
  name: 'pvDashboard',
  components: {},
  data() {
    return {
        description: '光伏综合看板',
      loading: true,
      center: null,
      elecdetail: {},
      yearElecList: {}
    }
  },
  created() {
    setTimeout(() => {
      this.loading = !this.loading
    }, 1000)
  },
  mounted() {
    this.initMap()
    this.$nextTick(() => {
    setTimeout(() => {
      this.getTest();
    }, 1000)
      // this.getEnergyoverview()
    })
  },
  methods: {
    getTest() {
      var listData = pvdata.pvDashboardData;
      console.log(listData)
      this.elecdetail = listData.elecdetail;
      this.yearElecList = listData.yearElecList;
      
      this.initChart();
    },
    initMap() {
      // 百度地图API功能
      var map = new BMap.Map('map') // 创建Map实例
      map.centerAndZoom(new BMap.Point(121.455333, 37.471718), 15) // 初始化地图,设置中心点坐标和地图级别
      //添加地图类型控件
      map.addControl(
        new BMap.MapTypeControl({
          mapTypes: [BMAP_NORMAL_MAP],
        })
      )
      map.setCurrentCity('烟台') // 设置地图显示的城市 此项是必须设置的
      map.enableScrollWheelZoom(true) //开启鼠标滚轮缩放
    },
    initChart() {
      var _this = this;
      var chartDom = _this.$refs.yearElec;
      var myChart = echarts.init(chartDom);
      var option

      option = {
        grid: {
          top: '25%',
          left: '15%',
          // right: '2%',
          // bottom: '14%',
        },
        toolbox: {
          show: true,
          feature: {
              mark: {show: true},
              saveAsImage: {show: true},
          }
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'],
          axisLine: {
              lineStyle: {
                  color: '#3FA7DA',
              },
          },
          axisLabel: {
              textStyle: {
                  color: '#3FA7DA',
                  fontSize: 12,
              },
          },
        },
        yAxis: {
          type: 'value',
          name: '电量(kW·h)',
          axisLine: {
              lineStyle: {
                  color: '#3FA7DA',
              },
          },
          axisLabel: {
              textStyle: {
                  color: '#3FA7DA',
                  fontSize: 12,
              },
          },
        },
        series: [
          {
            data: _this.yearElecList,
            type: 'bar',
            itemStyle: {
                        normal: {
                            color: '#1FD09D'
                        },
                    },
          },
        ],
      }

      option && myChart.setOption(option);
      
      window.addEventListener('resize', function () {
        myChart.resize()
      })
    },
  },
}
</script>

<style lang="less" scoped>
.chart-card-header {
  // position: relative;
  // overflow: hidden;
  width: 100%;
  height: 50px;
  // border: 1px solid #148cb1;

  .meta {
    // position: relative;
    // overflow: hidden;
      width: 52%;
      height: 100%;
    // border: 1px solid #148cb1;
    color: #ffffff;
    font-size: 20px;
    // line-height: 40px;
    float: left;
    display: flex;
    // justify-items: center;
  }

  .img {
    width: 40%;
    background: url('../../../../assets/images/dianZhan.png') no-repeat center center;
    background-size: 68%;
  }
  .img2 {
    width: 40%;
    background: url('../../../../assets/images/dianRong.png') no-repeat center center;
    background-size: 70%;
  }
  .img3 {
    width: 40%;
    background: url('../../../../assets/images/gongLv.png') no-repeat center center;
    background-size: 70%;
  }
}

.leftHeader{
  height: 100%;
  // width: 100%;
  // background-image: linear-gradient(150deg, #01ADA8, #80D6D3);
  border: 2px solid #01ADA8;
  margin-left: 2%;
  margin-right: 3%;
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
  // margin-bottom: 0;
  font-size: 30px;
  // line-height: 38px;
  // height: 38px;
  height: 100%;
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
  min-width: 125px;
  height: 100%;
  // border: 1px solid #148cb1;

  &.center {
    text-align: center;
    padding: 0 32px;
  }

  span {
    color: rgba(0, 0, 0, 0.45);
    display: inline-block;
    font-size: 0.95rem;
    line-height: 42px;
    margin-bottom: 4px;
    // font-weight: bold;
  }
  p {
    line-height: 42px;
    margin: 0 0 0 5%;
    font-weight: bold;
    a {
      font-weight: 600;
      font-size: 1rem;
    }
  }
  .bm-view {
    width: 100%;
    height: 300px;
  }
  .img4 {
    background: url('../../../../assets/images/Rd.png') no-repeat center center;
    background-size: 90%;
    margin-left: 10%;
    margin-right: 3%;
  }
  .img5 {
    background: url('../../../../assets/images/Yd.png') no-repeat center center;
    background-size: 90%;
    margin-left: 3%;
    margin-right: 3%;
  }
  .img6 {
    background: url('../../../../assets/images/Nd.png') no-repeat center center;
    background-size: 90%;
    margin-left: 3%;
    margin-right: 3%;
  }
  .img7 {
    background: url('../../../../assets/images/Rs.png') no-repeat center center;
    background-size: 90%;
    margin-left: 10%;
    margin-right: 2%;
  }
  .img8 {
    background: url('../../../../assets/images/Ys.png') no-repeat center center;
    background-size: 90%;
    margin-left: 4%;
    margin-right: 3%;
  }
  .img9 {
    background: url('../../../../assets/images/Ns.png') no-repeat center center;
    background-size: 90%;
    margin-left: 4%;
    margin-right: 3%;
  }
}
.head-title {
  font-size: 18px;
}
.societyTree {
  background-image: url(../../../../assets/societyTree_light.png);
  background-repeat: no-repeat;
  background-size: 75%;
  height: 190px;
  background-position: 50% 0;

  .societyTreeFont {
    position: absolute;
    font-size: small;
    text-align: center;
    .treeF {
      color: #989c9f !important;
    }
    .treeF2 {
      color: #333 !important;
    }
  }
  .Cleft {
    top: 63%;
    left: 22%;
  }
  .Cright {
    top: 52%;
    left: 46%;
    font-size: 14px;
  }
}

.totalEle-trend {
  height: 260px;
}
::v-deep .ant-card{
  border-radius: 5px;
  box-shadow: -1px 0 2px rgba(180, 180, 180, 0.156), /*左侧阴影*/
                1px 0 2px rgba(180, 180, 180, 0.156), /*右侧阴影*/
                -2px 2px 2px rgba(180, 180, 180, 0.156), /*上方阴影*/
                2px 2px 2px rgba(180, 180, 180, 0.156);
}
</style>
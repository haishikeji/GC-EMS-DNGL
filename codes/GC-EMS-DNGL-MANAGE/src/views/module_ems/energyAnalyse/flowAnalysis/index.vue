<template>
  <a-spin :spinning="loading">
    <div class="energyFlow-analysis" :loading="loading">
      <commonSearch ref="commonSearch" type="daterange" @search="handleSearch"></commonSearch>
      <div class="sankey-chart" ref="sankeyChart" style="height:1800px"></div>
    </div>
  </a-spin>
</template>

<script>
  import * as echarts from 'echarts';
  import commonSearch from '../components/commonSearch.vue'
  import { httpAction, getAction } from '@/api/manage'
  export default {
    name: 'flowAnalysis',
    components: {
      commonSearch
    },
    data () {
      return {
      description: '能流分析页面',
        loading: true,
        echartsHeight: window.innerHeight - 84 - 60 - 30,
        // 每一个数据及当前总数据（可能多条路线的和）
        sourceData: [],
        linksData: [],
        url: {
          list: "/emsStatistics/energyflowAnalysis",
        },
      }
    },
    created () {
    },
    mounted() {
      this.$nextTick(() => {
        this.getEnergyflowAnalysis()
      })
    },
    methods: {
      getEnergyflowAnalysis(){
        var _this = this
        _this.loading = true
        var data = _this.$refs.commonSearch.queryParams
        getAction(this.url.list, data).then(response => {
          if(response.success){
            _this.sourceData = response.result.sourceData
            _this.linksData = response.result.linksData
            // this.$message.success(response.message);
          }else{
            this.$message.warning(response.message);
          }
        }).finally(() => {
          _this.initEcharts()
          _this.loading = false
        })
      },
      initEcharts(){
        var _this = this
        var chartDom = _this.$refs.sankeyChart
        var myChart = echarts.init(chartDom);
        var option;

        var sangjiColor = ['#f7a365', '#44eda1', '#00ffff', '#00baff', '#f8b551', '#7ecef4', '#7ecef4', '#7ecef4'];
        var itemStyleSource = [];
        for(let d = 0; d < _this.sourceData.length; d++) {
          _this.sourceData[d].itemStyle = {
            normal: {
              color: sangjiColor[d]
            }
          }
          itemStyleSource.push(_this.sourceData[d]);
        }

        option = {
          // backgroundColor:"#013d5a",
          toolbox: {
            show: true,
            feature: {
              saveAsImage: {},
            }
          },
          series: [{
            type: 'sankey',
            layout: 'none',
            // top:"12%",
            // bottom: '21%',
            // left:'3%',
            // focusNodeAdjacency: 'allEdges',
            // focus: 'adjacency',
            // nodeAligh: 'left',
            // layoutIterations: 32,
            data: itemStyleSource,
            links: _this.linksData,
            label: {
              normal:{
                color:"#666",
                fontSize:12,
                formatter: function(params, i) {
                  // console.log(params);
                  return "{white|"+params.data.name +"}"+params.data.nameValue+" "+params.data.valueUnit;
                },
                rich:{
                  white:{
                    fontSize:12,
                    padding:[10,0,0,0]
                  }
                }
              }
            },
            lineStyle: {
              normal: {
                color: 'source',
                curveness: 0.7
              }
            },
            itemStyle: {
              normal: {
                borderWidth: 1,
                borderColor: 'transparent'
              }
            }
          }]
        }
        
        option && myChart.setOption(option);
        // window.addEventListener("resize",function (){
        //   myChart.resize();
        // });
      },
      /** 搜索按钮操作 */
      handleSearch(param) {
        this.getEnergyflowAnalysis()
      }
    },
  }
</script>

<style lang="less" scoped>
  @import '~@/assets/less/uStyle.less';
</style>
<style lang="less" scoped>
.energyFlow-analysis{
  min-height: calc(100vh - 84px);
  padding: 0 10px;
  .sankey-chart{
    // min-height: calc(100vh - 104px);
    padding: 10px;
    box-shadow: 0 2px 10px rgba(0,0,0,.1);
    background: #fff;
  }
}
</style>

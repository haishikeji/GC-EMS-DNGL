<template>
  <a-card :bordered="false" bodyStyle="padding: 0px 10px 0px 10px;">
    <a-tabs v-model="curtab" @tab-click="tabClick">
      <a-tab-pane tab="实时数据" key="realtime">
        <realtime v-if="curtab === 'realtime'"></realtime>
      </a-tab-pane>
      <a-tab-pane tab="历史数据" key="history">
        <history v-if="curtab === 'history'"></history>
      </a-tab-pane>
    </a-tabs>
  </a-card>
</template>

<script>
import realtime from './realtime.vue'
import history from './history.vue'
export default {
  name: 'equipmentRunStatis',
  components: {
    realtime, //实时数据
    history, //历史数据
  },
  data() {
    return {
      curtab: 'realtime',
      // 遮罩层
      loading: true,
    }
  },
  created() {},
  methods: {
    // 选择tab标签
    tabClick(tab, event) {
      this.curtab = tab.name
    },
  },
}
</script>
<style lang="less" scoped>
@import '~@/assets/less/uStyle.less';
</style>
<style lang="less" scoped>
</style>
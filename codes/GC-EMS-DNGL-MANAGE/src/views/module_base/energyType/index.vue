<template>
    <a-tabs default-active-key="1" @change="callback" class="card-container">
        <a-tab-pane key="1" tab="能源分类" force-render>
            <energy-type-list :key="timer1"></energy-type-list>
        </a-tab-pane>
        <a-tab-pane key="2" tab="能耗分项" force-render>
            <energy-item-list :key="timer2"></energy-item-list>
        </a-tab-pane>
    </a-tabs>
</template>

<script>

import EnergyTypeList from './EnergyTypeList.vue'
import EnergyItemList from '../energyItem/EnergyItemList.vue'

export default ({
    components: {
        EnergyTypeList,
        EnergyItemList
    },
    data() {
        return {
            timer1: '',
            timer2: '',
        }
    },
    methods: {
        callback(key) {
            if (key == 1) {
                // 我的下发
                this.timer1 = new Date().getTime()
            } else if (key == 2) {
                // 我的操作
                this.timer2 = new Date().getTime()
            } 
        },
    },
})
</script>

<style scoped>
::v-deep .ant-card-body{
    padding-top: 10px !important;
}
</style>

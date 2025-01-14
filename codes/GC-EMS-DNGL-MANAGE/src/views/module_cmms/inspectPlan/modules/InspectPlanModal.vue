<template>
  <u-modal
    :title="title"
    :visible.sync="visible"
    contentFull
    @ok="handleOk"
    @cancel="handleCancel">
    <template slot="centerContent">
      <div class="tabs">
        <span v-for="(item, index) in tabs" :key="index" @click="handleTab(item.key)" class="item" :class="item.key === chooseTab ? 'choose':''">{{ item.name }}</span>
      </div>
    </template>
    <inspect-plan-form-set v-show="chooseTab==='set'" ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></inspect-plan-form-set>
    <inspect-plan-form-list v-show="chooseTab==='list'" ref="realList" @ok="submitCallback"></inspect-plan-form-list>
  </u-modal>
</template>

<script>
  import InspectPlanFormList from './InspectPlanFormList.vue'
  import InspectPlanFormSet from './InspectPlanFormSet'
  export default {
    name: 'InspectPlanModal',
    components: {
      InspectPlanFormSet,
      InspectPlanFormList
    },
    data () {
      return {
        title:'',
        width:800,
        visible: false,
        disableSubmit: false,
        tabs: [
          { name: '计划设置', key: 'set' },
          { name: '计划内容', key: 'list' },
        ],
        chooseTab: 'set'
      }
    },
    methods: {
      handleTab(key){
        console.log(key)
        this.chooseTab = key
      },
      add () {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.add();
          this.$refs.realList.add();
        })
      },
      edit (record) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.edit(record);
          this.$refs.realList.edit(record);
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleOk () {
        this.$refs.realForm.submitForm(this.$refs.realList.tableData);
      },
      submitCallback(){
        this.$emit('ok');
        this.visible = false;
      },
      handleCancel () {
        this.close()
      },
    }
  }
</script>
<style lang="less" scoped>
  .tabs{
    .item{
      font-size: 15px;
      cursor: pointer;
    }
    .item:first-child{
      margin-right: 22px;
    }
    .choose{
      color: #1890FF;
    }
  }
</style>
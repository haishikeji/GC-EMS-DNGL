<template>
  <div class="all u-flex">
    <div class="left">
      <u-equipment-tree @select="selectEquipment"></u-equipment-tree>
    </div>
    <div class="right">
      <inspect-content-list></inspect-content-list>
    </div>
  </div>
</template>

<script>
  import InspectContentList from "./InspectContentList.vue";
  export default({
    components: {
      InspectContentList
    },
    data () {
      return {
      }
    },
    created() {
      
    },
    methods: {
      selectEquipment(selectedKeys, e){
        console.log(selectedKeys, e)
      },
    },
  })
</script>

<style scoped>
  .all{
    height: calc(100vh - 135px);
    width: 100%;
    display: flex;
  }
  .left{
    min-width: 200px;
    max-width: 240px;
    height: 100%;
    margin-right: 10px;
    margin-left: 5px;
  }
  .right{
    flex: 1;
    height: 100%;
  }
</style>

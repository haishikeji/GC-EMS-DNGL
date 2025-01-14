<template>
  <div class="equipment-tree-container">
    <a-input-search style="margin-bottom: 8px" placeholder="Search" @change="onChange" />
    <div class="tree-container">
      <a-tree
        :expanded-keys="expandedKeys"
        auto-expand-parent
        :tree-data="tpmTreeData"
        @expand="onExpand"
        :replace-fields="replaceFields"
        @select="selectNode"
        >
        <!-- :filter-tree-node="filterTreeNode" -->
      </a-tree>
    </div>
  </div>
</template>

<script>
  import { getAction } from '@api/manage'
  export default({
    name: 'UEquipmentTree',
    components: {
    },
    data () {
      return {
        expandedKeys: [],
        searchValue: '',
        autoExpandParent: true,
        tpmListData: [], // 原始数据
        tpmTreeData: [], // 树形列表使用数据
        replaceFields: {
          children: 'children',
          title: 'name',
        },
      }
    },
    created() {
      this.getTpmTreeData()
    },
    methods: {
      getTpmTreeData(){
        getAction(`/tpmEquipmentTree/tpmEquipmentTree/listtreeandequip`).then(res=>{
          if (res.success) {
            // 防止res.result对tpmListData造成地址赋值的问题
            this.tpmListData = JSON.parse(JSON.stringify(res.result))
            this.tpmTreeData = this.handleTree(res.result, "id", "parentid")
            console.log(this.tpmTreeData)
          } else {
            
          }
        })
      },
      filterTreeNode(node) {
        // console.log(node)
        // if (!inputValue) return true;
        return node.title.indexOf('设备') !== -1;
      },
      onExpand(expandedKeys) {
        this.expandedKeys = expandedKeys;
        this.autoExpandParent = false;
      },
      onChange(e) {
        const value = e.target.value;
        // 筛选后数据
        var filterData = []
        // 筛选符合条件的数据：包含当前搜索的项
        console.log(this.tpmListData)
        filterData = this.tpmListData.filter(item => (item.name.indexOf(value) !== -1))
        // var data = []
        // filterData.forEach(item => {
        //   var arr = this.tpmListData.filter(data => item.parentid === data.id)
        //   console.log('父级', arr)
        //   // filterData = [...filterData, ...arr]
        // })
        // 循环寻找父级
        // console.log(this.findParents(this.tpmTreeData, '287813167808513'))

        

        
        this.tpmTreeData = this.handleTree(filterData, "id", "parentid")
        // const expandedKeys = dataList.map(item => {
        //   if (item.name.indexOf(value) > -1) {
        //       // return getParentKey(item.key, gData);
        //       return this.filterNode(item.key, gData);
        //   }
        //   return null;
        // }).filter((item, i, self) => item && self.indexOf(item) === i);
        // Object.assign(this, {
        //   expandedKeys,
        //   searchValue: value,
        //   autoExpandParent: true,
        // });
      },
      // findParents(treeData,id){
      //   let allparents = []
      //   if(treeData.length==0){
      //     return 
      //   }

      //   let findele = (data,id) => {
      //     if(!id) return
      //     data.forEach((item,index) => {
      //       if(item.id == id){
      //         allparents.unshift(item.id)
      //         findele(treeData,item.parentid)

      //       }else{
      //         if(!!item.children){
      //             findele(item.children,id)
      //         }
              
      //       }
      //     })
      //   }

      //   findele(treeData,id)
      //   return allparents

      // },
      selectNode(selectedKeys, e){
        this.$emit('select', selectedKeys, e)
      },
    },
  })
</script>

<style lang="less" scoped>
.equipment-tree-container{
  width: 100%;
  height: 100%;
  background-color: #fff;
  padding: 15px;
  .tree-container{
    width: 100%;
    height: calc(100% - 40px);
    overflow: auto;
  }
}
</style>

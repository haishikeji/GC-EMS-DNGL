<template>
  <a-card :bordered="false">
    <!-- <div class="tree">
    </div> -->
    <div>
      <a-input-search style="margin-bottom: 8px" placeholder="Search" @change="onChange" />
      <a-tree
        :expanded-keys="expandedKeys"
        auto-expand-parent
        :tree-data="tpmTreeData"
        @expand="onExpand"
        :replace-fields="replaceFields"
        :filter-tree-node="filterTreeNode"
        >
        <!-- <template slot="title" slot-scope="{ title }">
          <span v-if="name.indexOf(searchValue) > -1">
          {{ name.substr(0, name.indexOf(searchValue)) }}
          <span style="color: #f50">{{ searchValue }}</span>
          {{ name.substr(name.indexOf(searchValue) + searchValue.length) }}
          </span>
          <span v-else>{{ title }}</span>
        </template> -->
      </a-tree>
    </div>
  </a-card>
</template>

<script>
  import { getAction } from '@api/manage'
  export default({
    name: 'DeviceList',
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
        console.log(node)
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
    },
  })
</script>

<style scoped>
.tree{
  height: calc(100vh - 185px);
  width: 100%;
}
</style>

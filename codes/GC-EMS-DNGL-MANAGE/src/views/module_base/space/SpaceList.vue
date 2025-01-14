<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="区域名称">
              <a-input placeholder="请输入设备区域名称" v-model="queryParam.name"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <!-- <a-button @click="expandClose" type="primary" :icon="tableTreeStatus == 1 ? 'down-circle' : 'up-circle'">{{tableTreeStatus == 1 ? '一键展开':'一键折叠'}}</a-button> -->
      <a-button @click="expandClose" type="primary">{{tableTreeStatus == 1 ? '一键展开':'一键折叠'}}</a-button>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('空间管理')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload> -->
      <!-- 高级查询区域 -->
      <!-- <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query> -->
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        rowKey="id"
        class="j-table-force-nowrap"
        :scroll="{x:true}"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="false"
        :loading="loading"
        :expandedRowKeys="expandedRowKeys"
        @change="handleTableChange"
        @expand="handleExpand"
        v-bind="tableProps">

        <template slot="imgSlot" slot-scope="text,record">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" :preview="record.id" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a @click="handleAddChild(record)">添加下级</a>
          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDeleteNode(record.id)" placement="topLeft">
            <a>删除</a>
          </a-popconfirm>

          <!-- <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleAddChild(record)">添加下级</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDeleteNode(record.id)" placement="topLeft">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown> -->
        </span>

      </a-table>
    </div>

    <space-modal ref="modalForm" @ok="modalFormOk"></space-modal>
  </a-card>
</template>

<script>

  import { getAction, deleteAction } from '@/api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { filterObj } from '@/utils/util';
  import SpaceModal from './modules/SpaceModal.vue'

  export default {
    name: "SpaceList",
    mixins:[JeecgListMixin],
    components: {
        SpaceModal
    },
    data () {
      return {
        description: '空间管理管理页面',
        // 表头
        columns: [
          {
            title:'区域名称',
            align:"left",
            dataIndex: 'name'
          },
          // {
          //   title:'编号',
          //   align:"left",
          //   dataIndex: 'serialnum'
          // },
          // {
          //   title:'类型',
          //   align:"left",
          //   dataIndex: 'spacetype'
          // },
          // {
          //   title:'管理单位/部门',
          //   align:"left",
          //   dataIndex: 'deptid'
          // },
          // {
          //   title:'建筑面积（平方米）',
          //   align:"left",
          //   dataIndex: 'builtarea'
          // },
          // {
          //   title:'空调系统',
          //   align:"left",
          //   dataIndex: 'aircondsys'
          // },
          // {
          //   title:'采暖系统',
          //   align:"left",
          //   dataIndex: 'heatsys'
          // },
          // {
          //   title:'空调面积',
          //   align:"left",
          //   dataIndex: 'aircondarea'
          // },
          // {
          //   title:'采暖面积',
          //   align:"left",
          //   dataIndex: 'heatarea'
          // },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' },
          }
        ],
        url: {
          // list: "/space/space/rootList",
          list: "/space/space/treeList",
          childList: "/space/space/childList",
          getChildListBatch: "/space/space/getChildListBatch",
          delete: "/space/space/delete",
          deleteBatch: "/space/space/deleteBatch",
          exportXlsUrl: "/space/space/exportXls",
          importExcelUrl: "space/space/importExcel",
        },
        expandedRowKeys:[],
        hasChildrenField:"hasChild",
        pidField:"parentid",
        dictOptions: {},
        loadParent: false,
        superFieldList:[],
        // 展开/折叠
        tableTreeStatus: 1,
      }
    },
    created() {
      this.getSuperFieldList();
    },
    computed: {
      importExcelUrl(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
      tableProps() {
        let _this = this
        return {
          // 列表项是否可选择
          rowSelection: {
            selectedRowKeys: _this.selectedRowKeys,
            onChange: (selectedRowKeys) => _this.selectedRowKeys = selectedRowKeys
          }
        }
      }
    },
    methods: {
      loadData(arg){
        if(arg==1){
          this.ipagination.current=1
        }
        this.loading = true
        let params = this.getQueryParams()
        params.hasQuery = 'true'
        getAction(this.url.list,params).then(res=>{
          if(res.success){
            this.dataSource = res.result;
            // let result = res.result
            // if(Number(result.total)>0){
            //   this.ipagination.total = Number(result.total)
            //   this.dataSource = this.getDataByResult(res.result.records)
            //   return this.loadDataByExpandedRows(this.dataSource)
            // }else{
            //   this.ipagination.total=0
            //   this.dataSource=[]
            // }
          }else{
            this.$message.warning(res.message)
          }
        }).finally(()=>{
          this.loading = false
        })
      },
      // 根据已展开的行查询数据（用于保存后刷新时异步加载子级的数据）
      loadDataByExpandedRows(dataList) {
        if (this.expandedRowKeys.length > 0) {
          return getAction(this.url.getChildListBatch,{ parentIds: this.expandedRowKeys.join(',') }).then(res=>{
            if (res.success && res.result.records.length>0) {
              //已展开的数据批量子节点
              let records = res.result.records
              const listMap = new Map();
              for (let item of records) {
                let pid = item[this.pidField];
                if (this.expandedRowKeys.join(',').includes(pid)) {
                 let mapList = listMap.get(pid);
                  if (mapList == null) {
                    mapList = [];
                  }
                  mapList.push(item);
                  listMap.set(pid, mapList);
                }
              }
              let childrenMap = listMap;
              let fn = (list) => {
                if(list) {
                  list.forEach(data => {
                    if (this.expandedRowKeys.includes(data.id)) {
                      data.children = this.getDataByResult(childrenMap.get(data.id))
                      fn(data.children)
                    }
                  })
                }
              }
              fn(dataList)
            }
          })
        } else {
          return Promise.resolve()
        }
      },
      getQueryParams(arg) {
        //获取查询条件
        let sqp = {}
        let param = {}
        if(this.superQueryParams){
          sqp['superQueryParams']=encodeURI(this.superQueryParams)
          sqp['superQueryMatchType'] = this.superQueryMatchType
        }
        if(arg){
          param = Object.assign(sqp, this.isorter ,this.filters);
        }else{
          param = Object.assign(sqp, this.queryParam, this.isorter ,this.filters);
        }
        if(JSON.stringify(this.queryParam) === "{}" || arg){
          param.hasQuery = 'false'
        }else{
          param.hasQuery = 'true'
        }
        param.field = this.getQueryField();
        param.pageNo = this.ipagination.current;
        param.pageSize = this.ipagination.pageSize;
        return filterObj(param);
      },
      searchReset() {
        //重置
        this.expandedRowKeys = []
        this.queryParam = {}
        this.loadData(1);
      },
      getDataByResult(result){
        if(result){
          return result.map(item=>{
            //判断是否标记了带有子节点
            if(item[this.hasChildrenField]=='1'){
              let loadChild = { id: item.id+'_loadChild', name: 'loading...', isLoading: true }
              item.children = [loadChild]
            }
            return item
          })
        }
      },
      handleExpand(expanded, record){
        // 判断是否是展开状态
        if (expanded) {
          this.expandedRowKeys.push(record.id)
          if (record.children.length>0 && record.children[0].isLoading === true) {
            let params = this.getQueryParams(1);//查询条件
            params[this.pidField] = record.id
            params.hasQuery = 'false'
            params.superQueryParams=""
            getAction(this.url.childList,params).then((res)=>{
              if(res.success){
                if(res.result.records){
                  record.children = this.getDataByResult(res.result.records)
                  this.dataSource = [...this.dataSource]
                }else{
                  record.children=''
                  record.hasChildrenField='0'
                }
              }else{
                this.$message.warning(res.message)
              }
            })
          }
        }else{
          let keyIndex = this.expandedRowKeys.indexOf(record.id)
          if(keyIndex>=0){
            this.expandedRowKeys.splice(keyIndex, 1);
          }
        }
      },
      handleAddChild(record){
        this.loadParent = true
        let obj = {}
        obj[this.pidField] = record['id']
        this.$refs.modalForm.add(obj);
      },
      handleDeleteNode(id) {
        if(!this.url.delete){
          this.$message.error("请设置url.delete属性!")
          return
        }
        var that = this;
        deleteAction(that.url.delete, {id: id}).then((res) => {
          if (res.success) {
             that.loadData(1)
          } else {
            that.$message.warning(res.message);
          }
        });
      },
      batchDel(){
        if(this.selectedRowKeys.length<=0){
          this.$message.warning('请选择一条记录！');
          return false;
        }else{
          let ids = "";
          let that = this;
          that.selectedRowKeys.forEach(function(val) {
            ids+=val+",";
          });
          that.$confirm({
            title:"确认删除",
            content:"是否删除选中数据?",
            onOk: function(){
              that.handleDeleteNode(ids)
              that.onClearSelected();
            }
          });
        }
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'parentid',text:'上级id',dictCode:''})
        fieldList.push({type:'string',value:'name',text:'名称',dictCode:''})
        fieldList.push({type:'string',value:'serialnum',text:'编号',dictCode:''})
        fieldList.push({type:'string',value:'spacetype',text:'类型',dictCode:''})
        fieldList.push({type:'string',value:'deptid',text:'管理单位/部门',dictCode:''})
        fieldList.push({type:'int',value:'builtarea',text:'建筑面积（平方米）',dictCode:''})
        fieldList.push({type:'string',value:'aircondsys',text:'空调系统',dictCode:''})
        fieldList.push({type:'string',value:'heatsys',text:'采暖系统',dictCode:''})
        fieldList.push({type:'int',value:'aircondarea',text:'空调面积',dictCode:''})
        fieldList.push({type:'int',value:'heatarea',text:'采暖面积',dictCode:''})
        this.superFieldList = fieldList
      },
      expandClose(){
        this.tableTreeStatus = - this.tableTreeStatus
        if (this.tableTreeStatus == 1) {
          this.expandedRowKeys = []
        } else {
          this.expandedRowKeys = this.dataSource.map(item =>{
            return item.id
          })
        }
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
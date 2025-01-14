<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="点检内容名称">
              <a-input v-model="queryParam.contentname" placeholder="请输入点检内容名称"></a-input>
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
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('点检内容')">导出</a-button>
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
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="status"  slot-scope="text, record">
          <!-- {{ record.status }} -->
          <!-- <a-popconfirm title="确定删除吗?" @confirm="() => statusChange(record)">
            <a-switch :default-checked= "record.status == 0 ? true : false" @change="statusChange(record,$event)" />
          </a-popconfirm> -->
          <a-switch :checked= "record.status == 0 ? true : false" @click="statusChange(record,$event)" />
          <!-- <a-switch checkedChildren="禁用" unCheckedChildren="启用" :default-checked= "record.status == 0 ? true : false" @change="statusChange(record,$event)" /> -->
        </template>

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
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
          <a @click="handleDetail(record)">详情</a>
          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>

          <!-- <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown> -->
        </span>

      </a-table>
    </div>

    <!-- <cmms-spotcheck-content-modal ref="modalForm" @ok="modalFormOk"></cmms-spotcheck-content-modal> -->
    <spotcheck-content-modal ref="modalForm" @ok="modalFormOk"></spotcheck-content-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import { httpAction, getAction } from '@/api/manage'
  import SpotcheckContentModal from './modules/SpotcheckContentModal.vue'

  export default {
    name: 'SpotcheckContentList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SpotcheckContentModal
    },
    props: {
      selectData: {
        type: Object,
        default: function(){
            return {} // 使用工厂函数返回默认值
        }
      },
    },
    data () {
      return {
        description: '点检内容管理页面',
        // 查询参数
        queryParam: {},
        // 表头
        columns: [
          {
            title: '序号',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'点检内容编号',
            align:"center",
            sorter: true,
            dataIndex: 'contentcode'
          },
          {
            title:'点检内容名称',
            align:"center",
            dataIndex: 'contentname'
          },
          {
            title:'设备',
            align:"center",
            dataIndex: 'equipmentid_dictText'
          },
          {
            title:'状态',
            align:"center",
            dataIndex: 'status_dictText',
            scopedSlots: { customRender: 'status' }
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/cmmsSpotcheckContent/cmmsSpotcheckContent/list",
          delete: "/cmmsSpotcheckContent/cmmsSpotcheckContent/delete",
          deleteBatch: "/cmmsSpotcheckContent/cmmsSpotcheckContent/deleteBatch",
          exportXlsUrl: "/cmmsSpotcheckContent/cmmsSpotcheckContent/exportXls",
          importExcelUrl: "cmmsSpotcheckContent/cmmsSpotcheckContent/importExcel",
          // edit: "/cmmsSpotcheckContent/cmmsSpotcheckContent/edit",
          edit: "/cmmsSpotcheckContent/cmmsSpotcheckContent/editStatus",
        },
        dictOptions:{},
        superFieldList:[],
        model: {}
      }
    },
    created() {
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      loadData(arg) {
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        //加载数据 若传入参数1则加载第一页的内容
        if (arg === 1) {
          this.ipagination.current = 1;
        }
        var params = this.getQueryParams();//查询条件
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            //update-begin---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
            this.dataSource = res.result.records||res.result;
            if(res.result.total)
            {
              this.ipagination.total = res.result.total;
            }else{
              this.ipagination.total = 0;
            }
            //update-end---author:zhangyafei    Date:20201118  for：适配不分页的数据列表------------
          }else{
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.loading = false
        })
      },
      // 处理父组件传过来的数据
      getDataList(){
        // console.log(111,this.selectData)
        this.queryParam.equipmentid = this.selectData.id
        this.loadData()
      },
      modalFormOk() {
        // 新增/修改 成功时，重载列表
        this.loadData();
        //清空列表选中
        this.onClearSelected()
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'contentcode',text:'点检内容编号',dictCode:''})
        fieldList.push({type:'string',value:'contentname',text:'点检内容名称',dictCode:''})
        fieldList.push({type:'sel_search',value:'equipmentid',text:'设备id',dictTable:"tpm_equipment", dictText:'equipmentname', dictCode:'id'})
        fieldList.push({type:'string',value:'status',text:'状态：启用：0、禁用:1',dictCode:'spotcheck_content_status'})
        this.superFieldList = fieldList
      },
      statusChange(record,$event){
        console.log(record.status,$event)
        let text1 = record.status === "0" ? "是否确认停用“" : "是否确认开启“";
        let text2 = record.status === "0" ? "”，请确保该设备有可使用的点检内容" : "”，若启用当前点检内容则该设备其他点检内容默认关闭";
        const that = this
        that.$confirm({
          title: "提示",
          content: text1 + record.contentname + text2,
          onOk() {
            let httpurl = '';
            let method = 'put';
            httpurl+=that.url.edit;
            if (record.status == 1) {
              that.model = record
              that.model.status = 0
            } else {
              that.model = record
              that.model.status = 1
            }
            httpAction(httpurl,that.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.modalFormOk()
              }else{
                that.$message.warning(res.message);
              }
            })
          },
          onCancel() {
            // that.modalFormOk()
          }
        });
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
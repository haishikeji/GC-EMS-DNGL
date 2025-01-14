<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="巡检计划名称">
              <a-input placeholder="请输入巡检计划名称" v-model="queryParam.planname"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="是否开启时间设置">
                <j-dict-select-tag v-model="queryParam.iftimeset" dictCode="common_yes_no" placeholder="请选择是否开启时间设置" />
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="状态">
                <j-dict-select-tag v-model="queryParam.status" dictCode="common_status" placeholder="请选择状态" />
              </a-form-item>
            </a-col>
          </template>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('巡检计划')">导出</a-button>
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

        <span slot="iftimeset" slot-scope="text, record">
          <a-tag v-if="record.iftimeset === 'Y'" color="blue">是</a-tag>
          <a-tag v-if="record.iftimeset === 'N'">否</a-tag>
        </span>
        <template slot="status" slot-scope="text, record">
          <a-switch :checked="record.status === '0' ? true : false" checked-children="启动" un-checked-children="停用" @click="changeStatus(record)"/>
          <!--  checked-children="启动" un-checked-children="停用" -->
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
          <a-divider type="vertical" />
          <a @click="handleOneOrder(record)">生成一次工单</a>

          <!-- <a-dropdown>
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

    <inspect-plan-modal ref="modalForm" @ok="modalFormOk"></inspect-plan-modal>
  </a-card>
</template>

<script>
  import { httpAction, getAction } from '@/api/manage'
  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import InspectPlanModal from './modules/InspectPlanModal'

  export default {
    name: 'InspectPlanList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      InspectPlanModal
    },
    data () {
      return {
        description: '巡检计划管理页面',
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
            title:'巡检计划编号',
            align:"center",
            sorter: true,
            dataIndex: 'plancode'
          },
          {
            title:'巡检计划名称',
            align:"center",
            dataIndex: 'planname'
          },
          {
            title:'是否开启时间设置',
            align:"center",
            dataIndex: 'iftimeset',
            scopedSlots: { customRender: 'iftimeset' }
          },
          {
            title:'待办人',
            align:"center",
            dataIndex: 'chargeruser'
          },
          {
            title:'任务数量',
            align:"center",
            sorter: true,
            dataIndex: 'tasknum'
          },
          {
            title:'状态',
            align:"center",
            dataIndex: 'status',
            scopedSlots: { customRender: 'status' }
          },
          {
            title:'开始时间',
            align:"center",
            sorter: true,
            dataIndex: 'begintime',
            customRender:function (text) {
              return !text?"-":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'重复周期',
            align:"center",
            dataIndex: 'repeatnum',
            customRender:function (t, r, index) {
              return r.iftimeset === 'Y' ? r.repeatnum + r.repeattype : '-'
            }
          },
          // {
          //   title:'重复类型：年、月、周、日、时、分',
          //   align:"center",
          //   dataIndex: 'repeattype'
          // },
          {
            title:'提前通知时间',
            align:"center",
            dataIndex: 'noticenum',
            customRender:function (t, r, index) {
              return r.iftimeset === 'Y' ? r.noticenum + r.noticetype : '-'
            }
          },
          // {
          //   title:'通知单位',
          //   align:"center",
          //   dataIndex: 'noticetype'
          // },
          {
            title:'下次执行时间',
            align:"center",
            sorter: true,
            dataIndex: 'nexttime',
            customRender:function (text) {
              return !text?"-":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'下次通知时间',
            align:"center",
            sorter: true,
            dataIndex: 'noticetime',
            customRender:function (text) {
              return !text?"-":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'是否已通知',
            align:"center",
            dataIndex: 'ifnotice'
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
          list: "/cmmsInspectPlan/cmmsInspectPlan/list",
          delete: "/cmmsInspectPlan/cmmsInspectPlan/delete",
          deleteBatch: "/cmmsInspectPlan/cmmsInspectPlan/deleteBatch",
          exportXlsUrl: "/cmmsInspectPlan/cmmsInspectPlan/exportXls",
          importExcelUrl: "cmmsInspectPlan/cmmsInspectPlan/importExcel",
          updateStatus: "cmmsInspectPlan/cmmsInspectPlan/updateStatus",
          generateinspect: "/cmmsInspectPlan/cmmsInspectPlan/generateinspect"
        },
        dictOptions:{},
        superFieldList:[],
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
      changeStatus(record){
        // console.log(record.status)
        const that = this
        that.$confirm({
          title: "提示",
          content: record.status ==='0'?'确定停用吗？':'确定启用吗？',
          onOk() {
            var status = {
              id: record.id,
              status: record.status ==='0' ? '1' : '0',
            }
            httpAction(that.url.updateStatus,status,'put').then((res)=>{
              if(res.success){
                that.loadData()
                that.$message.success(res.message);
              }else{
                that.$message.warning(res.message);
              }
            })
          },
          onCancel() {
          }
        });
      },
      // 生成一次工单
      handleOneOrder(record){
        getAction(this.url.generateinspect, {id: record.id}).then((res)=>{
          if(res.success){
            this.$message.success(res.message);
          }else{
            this.$message.warning(res.message);
          }
        })
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'remark',text:'备注',dictCode:''})
        fieldList.push({type:'string',value:'plancode',text:'巡检计划编号',dictCode:''})
        fieldList.push({type:'string',value:'planname',text:'巡检计划名称',dictCode:''})
        fieldList.push({type:'string',value:'iftimeset',text:'是否开启时间设置：Y是、N否',dictCode:''})
        fieldList.push({type:'date',value:'begintime',text:'开始时间'})
        fieldList.push({type:'int',value:'repeatnum',text:'重复',dictCode:''})
        fieldList.push({type:'string',value:'repeattype',text:'重复类型：年、月、周、日、时、分',dictCode:''})
        fieldList.push({type:'int',value:'noticenum',text:'提前通知时间',dictCode:''})
        fieldList.push({type:'string',value:'noticetype',text:'提前通知类型：天、时、分',dictCode:''})
        fieldList.push({type:'string',value:'status',text:'状态（0启用 1停用）',dictCode:''})
        fieldList.push({type:'string',value:'chargeruser',text:'待办人',dictCode:''})
        fieldList.push({type:'date',value:'nexttime',text:'下次执行时间'})
        fieldList.push({type:'int',value:'tasknum',text:'任务数量',dictCode:''})
        fieldList.push({type:'date',value:'noticetime',text:'下次通知时间'})
        fieldList.push({type:'string',value:'ifnotice',text:'是否已通知：是/否',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备">
              <j-search-select-tag placeholder="请选择设备" v-model="queryParam.equipmentid" dict="tpm_equipment,equipmentname,id"/>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="检定结果">
                <j-dict-select-tag placeholder="请选择检定结果" v-model="queryParam.submissionResult" dictCode="device_verification_result"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="送检状态">
                <j-dict-select-tag placeholder="请选择送检状态" v-model="queryParam.submissionStatus" dictCode="submission_status"/>
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
      <a-button type="primary" icon="download" @click="handleExportXls('送检工单设备表')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
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

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
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
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <submission-equipment-modal ref="modalForm" @ok="modalFormOk"></submission-equipment-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SubmissionEquipmentModal from './modules/SubmissionEquipmentModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'SubmissionEquipmentList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SubmissionEquipmentModal
    },
    data () {
      return {
        description: '送检工单设备表管理页面',
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
            title:'送检工单id',
            align:"center",
            dataIndex: 'cmmsSubmissionId'
          },
          {
            title:'设备id',
            align:"center",
            dataIndex: 'equipmentid'
          },
          {
            title:'检定日期',
            align:"center",
            dataIndex: 'submissionDate'
          },
          {
            title:'检定结果',
            align:"center",
            dataIndex: 'submissionResult_dictText'
          },
          {
            title:'有效日期',
            align:"center",
            dataIndex: 'submissionEffectiveDate'
          },
          {
            title:'班组确认',
            align:"center",
            dataIndex: 'teamConfirm'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark'
          },
          {
            title:'送检状态',
            align:"center",
            dataIndex: 'submissionStatus_dictText'
          },
          {
            title:'送回日期',
            align:"center",
            dataIndex: 'submissionReturnDate'
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
          list: "/cmmsSubmissionEquipment/cmmsSubmissionEquipment/list",
          delete: "/cmmsSubmissionEquipment/cmmsSubmissionEquipment/delete",
          deleteBatch: "/cmmsSubmissionEquipment/cmmsSubmissionEquipment/deleteBatch",
          exportXlsUrl: "/cmmsSubmissionEquipment/cmmsSubmissionEquipment/exportXls",
          importExcelUrl: "cmmsSubmissionEquipment/cmmsSubmissionEquipment/importExcel",

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
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'cmmsSubmissionId',text:'送检工单id',dictCode:''})
        fieldList.push({type:'string',value:'equipmentid',text:'设备id',dictCode:''})
        fieldList.push({type:'datetime',value:'submissionDate',text:'检定日期'})
        fieldList.push({type:'string',value:'submissionResult',text:'检定结果',dictCode:'device_verification_result'})
        fieldList.push({type:'datetime',value:'submissionEffectiveDate',text:'有效日期'})
        fieldList.push({type:'string',value:'teamConfirm',text:'班组确认',dictCode:''})
        fieldList.push({type:'string',value:'remark',text:'备注',dictCode:''})
        fieldList.push({type:'string',value:'submissionStatus',text:'送检状态',dictCode:'submission_status'})
        fieldList.push({type:'datetime',value:'submissionReturnDate',text:'送回日期'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
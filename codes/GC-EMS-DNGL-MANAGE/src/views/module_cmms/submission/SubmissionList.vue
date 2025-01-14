<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="送检负责人">
              <!-- <a-input placeholder="请输入送检负责人" v-model="queryParam.verificatePerson"></a-input> -->
              <j-search-select-tag v-model="queryParam.verificatePerson" placeholder="请选择负责人" dict="sys_user,realname,username"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="送检编号">
              <a-input placeholder="请输入送检编号" v-model="queryParam.sjcode"></a-input>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="检定方式">
                <j-dict-select-tag placeholder="请选择检定方式" v-model="queryParam.verificateMethod" dictCode="verificate_method"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="检定单位">
                <j-search-select-tag placeholder="请选择检定单位" v-model="queryParam.verificateClientId" dict="base_client,client_name,id"/>
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
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('送检工单表')">导出</a-button>
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

        <span slot="sjcode" slot-scope="text, record">
          <a @click="handleDetailXQ(record)">{{ record.sjcode}}</a>
        </span>

        <!-- 版本1：弹框（未做if判断） -->
        <!-- <span slot="submissionGdstatus_dictText" slot-scope="text, record">
          <a @click="statusEdit(record)">{{ record.submissionGdstatus_dictText}}</a>
        </span> -->
        <!-- 版本二（加if判断） -->
        <span slot="submissionGdstatus_dictText" slot-scope="text, record">
          <!-- <a @click="statusEdit(record)" v-if="record.submissionGdstatus != 2">{{ record.submissionGdstatus_dictText}}</a> -->
          <a-popconfirm  v-if="record.submissionGdstatus != 2" :title="record.submissionGdstatus == 0 ? '确定将工单状态改为“进行中”吗':'确定将工单状态改为“已完成”吗'" @confirm="() => statusEdit(record)">
            <a>{{ record.submissionGdstatus_dictText}}</a>
          </a-popconfirm>
          <span v-if="record.submissionGdstatus == 2">{{ record.submissionGdstatus_dictText}}</span>
        </span>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>
          <a-divider type="vertical" />
          <a @click="fankui(record)">反馈</a>
          <a-divider type="vertical" />
          <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
            <a>删除</a>
          </a-popconfirm>
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

    <cmms-submission-modal ref="modalForm" @ok="modalFormOk"></cmms-submission-modal>
    <SubmissionEquipmentModalFK ref="modalFormFK" @ok="modalFormOk"> </SubmissionEquipmentModalFK>
    <edit-status-modal ref="modalFormEdit" @ok="modalFormOk"></edit-status-modal>
    <handle-detail-xq ref="modalFormXq"></handle-detail-xq>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { httpAction, getAction } from '@/api/manage'
  import CmmsSubmissionModal from './modules/SubmissionModal'
  import SubmissionEquipmentModalFK from "./modulesFK/SubmissionEquipmentModalFK.vue";
  import EditStatusModal from './modules/editStatusModal.vue'
  import HandleDetailXq from './modules/handleDetailXq.vue'

  export default {
    name: 'CmmsSubmissionList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      CmmsSubmissionModal,
      SubmissionEquipmentModalFK,
        EditStatusModal,
        HandleDetailXq,
    },
    data () {
      return {
        description: '送检工单表管理页面',
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
            title:'送检编号',
            align:"center",
            sorter: true,
            dataIndex: 'sjcode',
            scopedSlots: { customRender: 'sjcode' }
          },
          {
            title:'送检负责人',
            align:"center",
            dataIndex: 'verificatePerson_dictText'
          },
          {
            title:'检定方式',
            align:"center",
            dataIndex: 'verificateMethod_dictText'
          },
          {
            title:'检定单位',
            align:"center",
            dataIndex: 'verificateClientId_dictText'
          },
          {
            title:'送检设备数量',
            align:"center",
            dataIndex: 'totalNum'
          },
          {
            title:'返回设备数量',
            align:"center",
            dataIndex: 'returnNum'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark'
          },
          {
            title:'送检工单状态',
            align:"center",
            dataIndex: 'submissionGdstatus_dictText',
            scopedSlots: { customRender: 'submissionGdstatus_dictText' }
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
          list: "/cmmsSubmission/cmmsSubmission/list",
          delete: "/cmmsSubmission/cmmsSubmission/delete",
          deleteBatch: "/cmmsSubmission/cmmsSubmission/deleteBatch",
          exportXlsUrl: "/cmmsSubmission/cmmsSubmission/exportXls",
          importExcelUrl: "cmmsSubmission/cmmsSubmission/importExcel",
          editStatus: "/cmmsSubmission/cmmsSubmission/editSJStatus"
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
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'verificatePerson',text:'送检负责人',dictCode:''})
        fieldList.push({type:'string',value:'verificateMethod',text:'检定方式',dictCode:'verificate_method'})
        fieldList.push({type:'sel_search',value:'verificateClientId',text:'检定单位',dictTable:"base_client", dictText:'client_name', dictCode:'id'})
        fieldList.push({type:'string',value:'totalNum',text:'送检设备数量',dictCode:''})
        fieldList.push({type:'string',value:'returnNum',text:'返回设备数量',dictCode:''})
        fieldList.push({type:'string',value:'remark',text:'备注',dictCode:''})
        this.superFieldList = fieldList
      },
      fankui(record) {
        this.$refs.modalFormFK.title = "送检信息反馈";
        this.$refs.modalFormFK.disableSubmit = false;
        this.$refs.modalFormFK.fankui(record.id);
      },
      // 修改工单状态
      statusEdit(record){
        // 版本1：弹框
        // this.$refs.modalFormEdit.title = "修改工单状态";
        // this.$refs.modalFormEdit.disableSubmit = false;
        // this.$refs.modalFormEdit.edit(record.id);
        // 版本2：按钮直接进行下一步状态
        console.log(record.submissionGdstatus)
        let httpurl = '';
        let method = '';
        httpurl+=this.url.editStatus;
        method = 'put';
        if (record.submissionGdstatus == 0) {
          this.model = {
            id: record.id,
            submissionGdstatus : "1",
          }
        }
        if (record.submissionGdstatus == 1) {
          this.model = {
            id: record.id,
            submissionGdstatus : "2",
          }
        }
        httpAction(httpurl,this.model,method).then((res)=>{
          if(res.success){
            this.$message.success(res.message);
            this.modalFormOk()
          }else{
            this.$message.warning(res.message);
            this.modalFormOk()
          }
        })
      },
      handleDetailXQ(record){
        this.$refs.modalFormXq.title = "送检信息详情";
        this.$refs.modalFormXq.disableSubmit = false;
        this.$refs.modalFormXq.findHandle(record.id);
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
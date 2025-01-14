<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备编号">
              <a-input v-model="queryParam.equipmentcode" placeholder="请输入设备编号"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <j-search-select-tag v-model="queryParam.equipmentid" placeholder="请选择设备" dict="tpm_equipment,equipmentname,id" />
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="计划日期">
                <!-- <j-date placeholder="请选择计划日期" v-model="queryParam.plandate"  style="width: 100%" /> -->
                <a-range-picker date-format="YYYY-MM-DD" v-model="queryParam.date" @change="onChangeDate" />
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="能源分类">
                <j-search-select-tag v-model="queryParam.energytypeid" placeholder="请选择能源分类" dict="base_energy_type,name,id"  />
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
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('base_energy_plan')">导出</a-button>
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

    <energy-plan-modal ref="modalForm" @ok="modalFormOk"></energy-plan-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import EnergyPlanModal from './modules/EnergyPlanModal'

  export default {
    name: 'EnergyPlanList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      EnergyPlanModal
    },
    data () {
      return {
        description: 'base_energy_plan管理页面',
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
            title:'设备编号',
            align:"center",
            dataIndex: 'equipmentcode',
            width: 150,
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'equipmentid_dictText',
            width: 300,
          },
          {
            title:'计划日期',
            align:"center",
            sorter: true,
            dataIndex: 'plandate',
            // customRender:function (text) {
            //   return !text?"":(text.length>10?text.substr(0,10):text)
            // },
            width: 150,
          },
          {
            title:'能源分类',
            align:"center",
            dataIndex: 'energytypeid_dictText',
            width: 150,
          },
          {
            title:'计划值',
            align:"center",
            sorter: true,
            dataIndex: 'planvalue',
            width: 150,
          },
          {
            title:'录入人',
            align:"center",
            dataIndex: 'createBy_dictText',
          },
          {
            title:'录入日期',
            align:"center",
            sorter: true,
            dataIndex: 'createTime',
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
          list: "/energyPlan/energyPlan/list",
          delete: "/energyPlan/energyPlan/delete",
          deleteBatch: "/energyPlan/energyPlan/deleteBatch",
          exportXlsUrl: "/energyPlan/energyPlan/exportXls",
          importExcelUrl: "energyPlan/energyPlan/importExcel",

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
      // 时间处理
      onChangeDate(date, dateString){
        console.log(date, dateString);
        this.queryParam.plandate_begin= dateString[0]
        this.queryParam.plandate_end= dateString[1]
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'int',value:'energyplanid',text:'能源计划ID'})
        fieldList.push({type:'string',value:'remark',text:'备注'})
        fieldList.push({type:'int',value:'equipmentid',text:'设备ID'})
        fieldList.push({type:'string',value:'equipmentcode',text:'设备编号'})
        fieldList.push({type:'date',value:'plandate',text:'计划日期'})
        fieldList.push({type:'int',value:'energytypeid',text:'能源分类ID'})
        fieldList.push({type:'number',value:'planvalue',text:'计划值'})
        fieldList.push({type:'int',value:'year',text:'所属年'})
        fieldList.push({type:'int',value:'month',text:'所属月'})
        fieldList.push({type:'string',value:'day',text:'日期'})
        fieldList.push({type:'string',value:'yearmonth',text:'所属年月'})
        fieldList.push({type:'int',value:'week',text:'所属周'})
        fieldList.push({type:'int',value:'dayofweek',text:'周几'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
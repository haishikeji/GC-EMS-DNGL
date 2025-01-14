<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="工单编号">
              <a-input placeholder="请输入工单编号" v-model="queryParam.repaircode"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="工单名称">
              <a-input placeholder="请输入工单名称" v-model="queryParam.repairname"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备名称">
              <!-- <j-search-select-tag v-model="queryParam.equipmentid" placeholder="请选择设备" dict="tpm_equipment,equipmentname,id" /> -->
              <a-select
              v-model="queryParam.equipmentid"
              placeholder="请输入设备名称或设备编号"
              show-search
              :filterOption="filterOptions"
              @search="searchDevice"
              allowClear>
                <a-select-option v-for="(item, index) in deviceOptions" :key="index" :value="item.id" :label="item.equipmentname">
                  <span>{{item.equipmentname}}</span>
                  <span style="position: absolute;right: 2%;">{{ item.equipmentcode }}</span>
                </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <template v-if="toggleSearchStatus">
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="优先级">
                <j-dict-select-tag  v-model="queryParam.priority"  placeholder="请输入优先级" dictCode="repair_priority"/>
              </a-form-item>
            </a-col>
            <!-- <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="待办人">
                <j-dict-select-tag v-model="queryParam.repairuser" placeholder="请选择待办人" dictCode="sys_user,realname,id"/>
              </a-form-item>
            </a-col> -->
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="维修状态">
                <j-dict-select-tag  v-model="queryParam.status"  placeholder="请选择维修状态" dictCode="repair_handle_status"/>
              </a-form-item>
            </a-col>
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="工单状态">
                <j-dict-select-tag  v-model="queryParam.repairstatus"  placeholder="请选择工单状态" dictCode="repair_order_status"/>
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
      <!-- <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button> -->
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('维修工单')">导出</a-button> -->
      <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload> -->
      <!-- 高级查询区域 -->
      <!-- <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query> -->
      <!-- <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown> -->
    </div>

    <!-- table区域-begin -->
    <div>
      <!-- <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div> -->

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
        class="j-table-force-nowrap"
        @change="handleTableChange">
        <!-- :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}" -->

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

        <span slot="priority" slot-scope="text, record">
          <a-tag v-if="record.priority === '0'">{{ text }}</a-tag>
          <a-tag v-if="record.priority === '1'" color="orange">{{ text }}</a-tag>
          <a-tag v-if="record.priority === '2'" color="red">{{ text }}</a-tag>
        </span>
        <span slot="status" slot-scope="text, record">
          <a-tag v-if="record.status === '0'" color="orange">{{ text }}</a-tag>
          <a-tag v-if="record.status === '1' || record.status === '2'" color="blue">{{ text }}</a-tag>
          <a-tag v-if="record.status === '3' || record.status === '4'">{{ text }}</a-tag>
        </span>

        <span slot="action" slot-scope="text, record">
          <a @click="handleFeedback(record)" :disabled="record.repairstatus === '1'">维修反馈</a>
          <!-- <a-divider type="vertical" /> -->
          <!-- <a @click="handleDetail(record)">详情</a> -->
        </span>

      </a-table>
    </div>

    <repair-my-modal ref="modalForm" @ok="modalFormOk"></repair-my-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import RepairMyModal from './modules/RepairMyModal'
  import { httpAction, getAction } from '@/api/manage'

  export default {
    name: 'RepairMyList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      RepairMyModal
    },
    data () {
      return {
        description: '维修工单管理页面',
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
            title:'优先级',
            align:"center",
            sorter: true,
            dataIndex: 'priority_dictText',
            scopedSlots: { customRender: 'priority' }
          },
          {
            title:'工单编号',
            align:"center",
            sorter: true,
            dataIndex: 'repaircode',
            // customCell: (record, index)=>{ return this.customCellDetail(record, index, 'customerName')}
            // customCell: this.customCellDetail
          },
          {
            title:'工单名称',
            align:"center",
            dataIndex: 'repairname'
          },
          {
            title:'设备名称',
            align:"center",
            dataIndex: 'equipmentname'
          },
          {
            title:'设备编号',
            align:"center",
            sorter: true,
            dataIndex: 'equipmentcode'
          },
          {
            title:'故障日期',
            align:"center",
            sorter: true,
            dataIndex: 'faultdate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'故障描述',
            align:"center",
            dataIndex: 'faultdesc'
          },
          {
            title:'录入人',
            align:"center",
            dataIndex: 'createBy'
          },
          {
            title:'待办人',
            align:"center",
            dataIndex: 'repairuser_dictText'
          },
          {
            title:'维修时间',
            align:"center",
            sorter: true,
            dataIndex: 'repairdate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'维修反馈',
            align:"center",
            dataIndex: 'feedback'
          },

          // {
          //   title:'设备ID',
          //   align:"center",
          //   dataIndex: 'equipmentid'
          // },
          // {
          //   title:'维修结果',
          //   align:"center",
          //   dataIndex: 'result'
          // },
          // {
          //   title:'关联ID',
          //   align:"center",
          //   dataIndex: 'relid'
          // },
          // {
          //   title:'创建时间',
          //   align:"center",
          //   dataIndex: 'createTime',
          //   customRender:function (text) {
          //     return !text?"":(text.length>10?text.substr(0,10):text)
          //   }
          // },
          // {
          //   title:'备注',
          //   align:"center",
          //   dataIndex: 'remark'
          // },
          {
            title:'工单状态',
            align:"center",
            dataIndex: 'repairstatus_dictText'
          },
          {
            title:'维修状态',
            align:"center",
            fixed:"right",
            width:80,
            dataIndex: 'status_dictText',
            scopedSlots: { customRender: 'status' }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:152,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/cmmsRepair/cmmsRepair/listMy",
          // exportXlsUrl: "/cmmsRepairMy/cmmsRepairMy/exportXls",

        },
        dictOptions:{},
        superFieldList:[],
        deviceOptions: [],
        deviceOptionsAll: [],
      }
    },
    created() {
    this.getSuperFieldList();
    this.getDeviceOption();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      // 获取设备下拉列表
      getDeviceOption(){
        getAction(`/tpmEquipment/tpmEquipment/selectEquipmentList`).then(res=>{
          console.log(111,res.result)
          this.deviceOptions = res.result.map((res) => {
            return {
              id: res.id,
              equipmentname: res.equipmentname,
              equipmentcode: res.equipmentcode,
            }
          })
          // 存一个完整的设备表
          this.deviceOptionsAll = this.deviceOptions
          console.log(7878,this.deviceOptionsAll)
        })
      },
      // 筛选设备
      searchDevice(value) {
        console.log(1212,value,value.trim().length)
        // 若输入的值删除，则重新赋完整的设备列表
        if (value.trim().length === 0) {
          this.deviceOptions = this.deviceOptionsAll
        }
        // 通过判断字符串中是数字还是文字进而判断是通过设备名筛选还是设备编号筛选
        let panDuan = isNaN(parseFloat(value))
        if (!panDuan) {
          // 数字
          console.log(777)
          let filteredArray = this.deviceOptionsAll.filter(item => item.equipmentcode.includes(value));
          this.deviceOptions = filteredArray
        } else {
          console.log(888)
          let filteredArray = this.deviceOptionsAll.filter(item => item.equipmentname.includes(value));
          this.deviceOptions = filteredArray
        }
        console.log(999,this.deviceOptions)
      },
      // 解决筛选后option不回显问题
      filterOptions(input, option) {
        return this.deviceOptions
      },
      handleFeedback(record){
        this.$refs.modalForm.title = "维修反馈";
        this.$refs.modalForm.modalType = "feedback";
        this.$refs.modalForm.feedback(record);
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'id',text:'维修工单ID',dictCode:''})
        fieldList.push({type:'string',value:'createBy',text:'创建者',dictCode:''})
        fieldList.push({type:'date',value:'createTime',text:'创建时间'})
        fieldList.push({type:'string',value:'remark',text:'备注',dictCode:''})
        fieldList.push({type:'string',value:'repaircode',text:'维修工单编号',dictCode:''})
        fieldList.push({type:'string',value:'repairname',text:'工单名称',dictCode:''})
        fieldList.push({type:'string',value:'equipmentid',text:'设备ID',dictCode:''})
        fieldList.push({type:'string',value:'equipmentcode',text:'设备编号',dictCode:''})
        fieldList.push({type:'string',value:'equipmentname',text:'设备名称',dictCode:''})
        fieldList.push({type:'string',value:'priority',text:'优先级：0普通，1紧急，2特急',dictCode:''})
        fieldList.push({type:'date',value:'faultdate',text:'故障日期'})
        fieldList.push({type:'string',value:'faultdesc',text:'故障描述',dictCode:''})
        fieldList.push({type:'string',value:'repairuser',text:'待办人',dictCode:''})
        fieldList.push({type:'date',value:'repairdate',text:'维修时间'})
        fieldList.push({type:'string',value:'feedback',text:'维修反馈',dictCode:''})
        fieldList.push({type:'string',value:'result',text:'维修结果',dictCode:''})
        fieldList.push({type:'string',value:'status',text:'状态：0待处理，1维修检查，2设备维修，3维修结束',dictCode:''})
        fieldList.push({type:'string',value:'relid',text:'关联ID：巡检、点检、保养的ID',dictCode:''})
        fieldList.push({type:'string',value:'repairstatus',text:'维修工单状态: 0正常、1冻结、2拒绝',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
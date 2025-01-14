<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="计划编号">
              <a-input placeholder="请输入计划编号" v-model="queryParam.plancode"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="计划名称">
              <a-input placeholder="请输入计划名称" v-model="queryParam.planname"></a-input>
            </a-form-item>
          </a-col>
          <!-- <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备类型">
              <j-tree-select
                ref="treeSelect"
                placeholder="请选择设备类型"
                v-model="queryParam.equipdefid"
                dict="tpm_equipment_tree,name,id"
                pidField="parentid"
                pidValue="0"
                hasChildField="has_child"
                >
              </j-tree-select>
            </a-form-item>
          </a-col> -->
          <template v-if="toggleSearchStatus">
            <!-- <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="保养项编号">
                <a-input placeholder="请输入保养项编号" v-model="queryParam.itemcode"></a-input>
              </a-form-item>
            </a-col> -->
            <a-col :xl="6" :lg="7" :md="8" :sm="24">
              <a-form-item label="设备">
                <!-- <j-search-select-tag v-model="queryParam.equipmentid" dict="tpm_equipment,equipmentname,id"  placeholder="请选择设备"/> -->
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
                <!-- <a-input placeholder="请输入保养项编号" v-model="queryParam.itemcode"></a-input> -->
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
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('保养计划')">导出</a-button>
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
          <a-switch :checked= "record.status == 0 ? true : false" @click="statusChange(record,$event)" />
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

    <upkeep-plan-modal ref="modalForm" @ok="modalFormOk"></upkeep-plan-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { httpAction, getAction } from '@/api/manage'
  import UpkeepPlanModal from './modules/UpkeepPlanModal'

  export default {
    name: 'UpkeepPlanList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      UpkeepPlanModal
    },
    data () {
      return {
        description: '保养计划管理页面',
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
            title:'计划编号',
            align:"center",
            sorter: true,
            dataIndex: 'plancode'
          },
          {
            title:'计划名称',
            align:"center",
            dataIndex: 'planname'
          },
          {
            title:'设备',
            align:"center",
            dataIndex: 'equipmentid_dictText'
          },
          // {
          //   title:'保养工作项目ID',
          //   align:"center",
          //   dataIndex: 'upkeepprojid'
          // },
          // {
          //   title:'工作项目名称',
          //   align:"center",
          //   dataIndex: 'projectname'
          // },
          {
            title:'地点',
            align:"center",
            dataIndex: 'address'
          },
          // {
          //   title:'厂商名称',
          //   align:"center",
          //   dataIndex: 'supplier'
          // },
          // {
          //   title:'厂商电话',
          //   align:"center",
          //   dataIndex: 'suppliertel'
          // },
          // {
          //   title:'联系人',
          //   align:"center",
          //   dataIndex: 'linker'
          // },
          // {
          //   title:'联系人电话',
          //   align:"center",
          //   dataIndex: 'linkertel'
          // },
          {
            title:'负责人',
            align:"center",
            dataIndex: 'chargeruser_dictText'
          },
          // {
          //   title:'描述',
          //   align:"center",
          //   dataIndex: 'plandesc'
          // },
          {
            title:'开始时间',
            align:"center",
            sorter: true,
            dataIndex: 'begintime',
            // customRender:function (text) {
            //   return !text?"":(text.length>10?text.substr(0,10):text)
            // }
          },
          {
            title:'重复间隔',
            align:"center",
            sorter: true,
            dataIndex: 'repeatnum'
          },
          {
            title:'间隔单位',
            align:"center",
            dataIndex: 'repeattype'
          },
          {
            title:'提前通知时长',
            align:"center",
            sorter: true,
            dataIndex: 'noticenum'
          },
          {
            title:'时长单位',
            align:"center",
            dataIndex: 'noticetype'
          },
          // 状态（0启用 1停用）
          {
            title:'状态',
            align:"center",
            dataIndex: 'status',
            scopedSlots: { customRender: 'status' }
          },
          {
            title:'下次执行时间',
            align:"center",
            sorter: true,
            dataIndex: 'nexttime',
            // customRender:function (text) {
            //   return !text?"":(text.length>10?text.substr(0,10):text)
            // }
          },
          {
            title:'下次通知时间',
            align:"center",
            sorter: true,
            dataIndex: 'noticetime',
            // customRender:function (text) {
            //   return !text?"":(text.length>10?text.substr(0,10):text)
            // }
          },
          {
            title:'通知状态',
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
          list: "/cmmsUpkeepPlan/cmmsUpkeepPlan/list",
          delete: "/cmmsUpkeepPlan/cmmsUpkeepPlan/delete",
          deleteBatch: "/cmmsUpkeepPlan/cmmsUpkeepPlan/deleteBatch",
          exportXlsUrl: "/cmmsUpkeepPlan/cmmsUpkeepPlan/exportXls",
          importExcelUrl: "cmmsUpkeepPlan/cmmsUpkeepPlan/importExcel",
          edit: "/cmmsUpkeepPlan/cmmsUpkeepPlan/updateCmmsUpkeepPlanStatusById",
        },
        dictOptions:{},
        superFieldList:[],
        model: {},
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
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'remark',text:'备注',dictCode:''})
        fieldList.push({type:'string',value:'plancode',text:'保养计划编号',dictCode:''})
        fieldList.push({type:'string',value:'planname',text:'保养计划名称',dictCode:''})
        fieldList.push({type:'string',value:'upkeepprojid',text:'保养工作项目ID',dictCode:''})
        fieldList.push({type:'string',value:'projectname',text:'工作项目名称',dictCode:''})
        fieldList.push({type:'string',value:'address',text:'地点',dictCode:''})
        fieldList.push({type:'string',value:'supplier',text:'厂商名称',dictCode:''})
        fieldList.push({type:'string',value:'suppliertel',text:'厂商电话',dictCode:''})
        fieldList.push({type:'string',value:'linker',text:'联系人',dictCode:''})
        fieldList.push({type:'string',value:'linkertel',text:'联系人电话',dictCode:''})
        fieldList.push({type:'string',value:'chargeruser',text:'负责人',dictCode:''})
        fieldList.push({type:'string',value:'plandesc',text:'描述',dictCode:''})
        fieldList.push({type:'date',value:'begintime',text:'开始时间'})
        fieldList.push({type:'int',value:'repeatnum',text:'重复',dictCode:''})
        fieldList.push({type:'string',value:'repeattype',text:'重复类型：年、月、周、日、时、分',dictCode:''})
        fieldList.push({type:'int',value:'noticenum',text:'提前通知时间',dictCode:''})
        fieldList.push({type:'string',value:'noticetype',text:'提前通知类型：天、时、分',dictCode:''})
        fieldList.push({type:'string',value:'status',text:'状态（0启用 1停用）',dictCode:''})
        fieldList.push({type:'date',value:'nexttime',text:'下次执行时间'})
        fieldList.push({type:'date',value:'noticetime',text:'下次通知时间'})
        fieldList.push({type:'string',value:'ifnotice',text:'是否已通知：是/否',dictCode:''})
        fieldList.push({type:'string',value:'equipmentid',text:'设备id（根据设备id查找保养项内容）',dictCode:''})
        this.superFieldList = fieldList
      },
      statusChange(record,$event){
        console.log(record.status,$event)
        let text1 = record.status === "0" ? "是否确认停用“" : "是否确认使用“";
        let text2 = record.status === "0" ? "”，请确保该设备有可使用的保养计划" : "”，若启用当前保养计划则该设备其他保养计划默认禁用";
        const that = this
        that.$confirm({
          title: "提示",
          content: text1 + record.planname + text2,
          onOk() {
            let httpurl = '';
            let method = 'put';
            httpurl+=that.url.edit;
            if (record.status == 1) {
              that.model.id = record.id
              that.model.equipmentid = record.equipmentid
              that.model.status = 0
            } else {
              that.model.id = record.id
              that.model.equipmentid = record.equipmentid
              that.model.status = 1
            }
            console.log("0101",that.model)
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
<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="名称">
              <a-input placeholder="请输入名称" v-model="queryParam.itemname"></a-input>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="设备类型">
              <!-- <a-input placeholder="请输入设备类型" v-model="queryParam.equipdefid"></a-input> -->
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
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="巡检标准类型">
              <j-dict-select-tag type="list" v-model="queryParam.classification" dictCode="inspect_item_type" placeholder="请选择巡检类型" />
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <!-- <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a> -->
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <!-- <a-button type="primary" icon="download" @click="handleExportXls('巡检标准')">导出</a-button> -->
      <!-- <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
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

        <template slot="tooltip" slot-scope="text" >
          <a-tooltip placement="top" :title="text" trigger="hover">
            <div style="cusor:pointer; overflow: hidden; white-space: nowrap; text-overflow: ellipsis; width: 80px;">
              {{ text }}
            </div>
          </a-tooltip>
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

    <inspect-item-modal ref="modalForm" @ok="modalFormOk"></inspect-item-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import InspectItemModal from './modules/InspectItemModal'

  export default {
    name: 'InspectItemList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      InspectItemModal
    },
    data () {
      return {
        description: '巡检标准管理页面',
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
            title:'巡检标准编号',
            align:"center",
            sorter: true,
            dataIndex: 'itemcode'
          },
          {
            title:'名称',
            align:"center",
            dataIndex: 'itemname'
          },
          {
            title:'设备类型',
            align:"center",
            dataIndex: 'equipdefid_dictText'
          },
          // {
          //   title:'条件(字典中定义)',
          //   align:"center",
          //   dataIndex: 'conditions'
          // },
          // {
          //   title:'参考值',
          //   align:"center",
          //   dataIndex: 'refervalue'
          // },
          // {
          //   title:'标签(多个用逗号隔开)',
          //   align:"center",
          //   dataIndex: 'tag'
          // },
          {
            title:'巡检标准',
            align:"center",
            dataIndex: 'inspectionstandards'
          },
          {
            title:'巡检标准类型',
            align:"center",
            dataIndex: 'classification_dictText'
          },
          {
            title:'备注',
            align:"center",
            width: 80,
            dataIndex: 'remark',
            scopedSlots: { customRender: 'tooltip' }
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          },
        ],
        url: {
          list: "/cmmsInspectItem/cmmsInspectItem/list",
          delete: "/cmmsInspectItem/cmmsInspectItem/delete",
          deleteBatch: "/cmmsInspectItem/cmmsInspectItem/deleteBatch",
          exportXlsUrl: "/cmmsInspectItem/cmmsInspectItem/exportXls",
          importExcelUrl: "cmmsInspectItem/cmmsInspectItem/importExcel",

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
        fieldList.push({type:'string',value:'remark',text:'备注',dictCode:''})
        fieldList.push({type:'string',value:'itemcode',text:'巡检标准编号',dictCode:''})
        fieldList.push({type:'string',value:'itemname',text:'名称',dictCode:''})
        fieldList.push({type:'string',value:'equipdefid',text:'设备类型ID(设备定义)',dictCode:''})
        fieldList.push({type:'string',value:'conditions',text:'条件(字典中定义)',dictCode:''})
        fieldList.push({type:'string',value:'refervalue',text:'参考值',dictCode:''})
        fieldList.push({type:'string',value:'tag',text:'标签(多个用逗号隔开)',dictCode:''})
        fieldList.push({type:'string',value:'inspectionstandards',text:'巡检标准',dictCode:''})
        fieldList.push({type:'string',value:'classification',text:'分类:0环境1设备2其他',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
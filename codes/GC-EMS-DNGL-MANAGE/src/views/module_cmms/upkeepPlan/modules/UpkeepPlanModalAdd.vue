<template>
    <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">

        <a-table
            ref="table"
            size="middle"
            :scroll="{x:true}"
            bordered
            rowKey="id"
            :columns="columns"
            :dataSource="dataSource"
            :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
            :pagination="false"
            class="j-table-force-nowrap">
        </a-table>

    </j-modal>
</template>

<script>

import { httpAction, getAction } from '@/api/manage'
  
  export default {
    name: 'UpkeepPlanModalAdd',
    components: {
        
    },
    props: {
      modelForm: {
        type: Object,
        default: function(){
            return {} // 使用工厂函数返回默认值
        }
      },
      dataList: {
        type: Array,
        default: function(){
            return [] // 使用工厂函数返回默认值
        }
      },
    },
    data () {
      return {
        title:'',
        width:1000,
        visible: false,
        /* table选中keys*/
        selectedRowKeys: [],
        /* table选中records*/
        selectionRows: [],
        columns: [
          // {
          //   title:'保养项ID',
          //   align:"center",
          //   dataIndex: 'id'
          // },
          {
            title:'保养项编号',
            align:"center",
            dataIndex: 'itemcode'
          },
          {
            title:'保养项名称',
            align:"center",
            dataIndex: 'itemname'
          },
          // {
          //   title:'设备类型',
          //   align:"center",
          //   dataIndex: 'equiptypeName'
          // },
          {
            title:'保养项标准',
            align:"center",
            dataIndex: 'itemcontent'
          },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark'
          },
        ],
        dataSource: [],
        url: {
            getList: "/cmmsUpkeepItem/cmmsUpkeepItem/getCmmsUpkeepItemByEqid"
        },
      }
    },
    methods: {
      add () {
        this.visible = true;
        const a = {
            id: this.modelForm.equipmentid
        }
        console.log(111,a)
        getAction(this.url.getList,a).then((res) => {
            console.log(res)
            this.dataSource = res.result
        })
        this.selectList()
      },
      // 将以选中的值重新在列表中选中
      selectList() {
        console.log(111,this.dataList)
        this.selectionRows = this.dataList
        this.selectedRowKeys = this.dataList.map((res) => {
            return res.id
        })
      },  
      close () {
        this.$emit('close');
        this.visible = false;
        this.onClearSelected()
        this.selectionRows = this.dataList
        this.selectedRowKeys = this.dataList.map((res) => {
            return res.id
        })
      },
      handleOk () {
        console.log(this.selectionRows)
        this.$emit('customEvent', this.selectionRows);
        this.visible = false;
      },
      submitCallback(){
        this.$emit('ok');
        this.visible = false;
      },
      handleCancel () {
        this.close()
      },
      onSelectChange(selectedRowKeys, selectionRows) {
        this.selectedRowKeys = selectedRowKeys;
        this.selectionRows = selectionRows;
        console.log(this.selectedRowKeys,this.selectionRows)
      },
      onClearSelected() {
        this.selectedRowKeys = [];
        this.selectionRows = [];
      },
    }
  }
</script>
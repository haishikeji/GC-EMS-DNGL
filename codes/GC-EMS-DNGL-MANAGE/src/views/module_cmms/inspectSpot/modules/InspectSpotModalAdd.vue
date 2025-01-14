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
      :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange, onSelect: onSelect}"
      :pagination="ipagination"
      @change="handleTableChange"
      class="j-table-force-nowrap">
    </a-table>
  </j-modal>
</template>

<script>
  import { httpAction, getAction } from '@/api/manage'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  
  export default {
    name: 'SpotcheckContentModalAdd',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
        
    },
    props: {
      //表单禁用
      equipmentId: {
        type: String,
        default: ''
      },
      selectData: {
        type: Array,
        default: function(){
          return [] // 使用工厂函数返回默认值
        }
      },
    },
    data () {
      return {
        title:'',
        width: 1000,
        visible: false,
        /* table选中keys*/
        selectedRowKeys: [],
        /* table选中records*/
        selectionRows: [],
        columns: [
          // {
          //   title: '',
          //   dataIndex: '',
          //   key:'rowIndex',
          //   width:60,
          //   align:"center",
          //   customRender:function (t,r,index) {
          //     return parseInt(index)+1;
          //   }
          // },
          {
            title:'巡检项目编号',
            align:"center",
            dataIndex: 'contentcode'
          },
          {
            title:'巡检项目名称',
            align:"center",
            dataIndex: 'contentname'
          },
          {
            title:'设备',
            align:"center",
            dataIndex: 'equipmentname'
          },
          // {
          //   title:'状态',
          //   align:"center",
          //   dataIndex: 'status'
          // },
          {
            title:'备注',
            align:"center",
            dataIndex: 'remark'
          },
        ],
        dataSource: [],
        chooseData: [],
        url: {
          list: "/cmmsInspectContent/cmmsInspectContent/listDetails",
        },
      }
    },
    watch: {
      selectData: {
        handler(newV, oldV){
          console.log(newV, oldV)
          if(newV.length > 0){
            this.selectList()
          }
        },
      }
    },
    methods: {
      add(){
        this.visible = true;
        this.loadData()
      },
      loadData () {
        //  {status: '0'}
        var params = this.getQueryParams();//查询条件
        params.status = '0'
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records||res.result;
            if(res.result.total)
            {
              this.ipagination.total = res.result.total;
            }else{
              this.ipagination.total = 0;
            }
            // this.selectList()
          }else{
            this.$message.warning(res.message)
          }
        }).finally(() => {
          this.loading = false
        })
      },
      // 将以选中的值重新在列表中选中
      selectList() {
        this.chooseData = [...this.selectData]
        // console.log(this.selectData)
        this.selectedRowKeys = this.selectData.map((res) => {
          return res.id
        })
      },
      close () {
        this.visible = false;
      },
      handleOk () {
        this.$emit('ok', this.chooseData);
        console.log(this.chooseData)
        this.visible = false;
      },
      handleCancel () {
        this.close()
      },
      onSelect(selectedRow, selected){
        // console.log(selectedRow, selected)
        if(selected){
          // 新增
          this.chooseData.push(selectedRow)
        } else {
          // 删除(过滤)
          var index = this.chooseData.findIndex(item => item.id === selectedRow.id)
          this.chooseData.splice(index, 1)
        }
      },





      // 将以选中的值重新在列表中选中
      // selectList() {
      //   this.selectionRows = this.selectData
      //   console.log(this.selectData)
      //   this.selectedRowKeys = this.selectData.map((res) => {
      //     return res.id
      //   })
      // },
      // close () {
      //   this.$emit('close');
      //   this.visible = false;
      //   this.onClearSelected()
      //   this.selectionRows = this.selectData
      //   this.selectedRowKeys = this.selectData.map((res) => {
      //     return res.id
      //   })
      //   this.dataSource = []
      // },
      // handleOk () {
      //   console.log(this.selectionRows)
      //   this.$emit('ok', this.selectionRows);
      //   this.visible = false;
      // },
      // handleCancel () {
      //   this.close()
      // },
      // onSelectChange(selectedRowKeys, selectionRows) {
      //   this.selectedRowKeys = selectedRowKeys;
      //   this.selectionRows = selectionRows;
      //   console.log(this.selectedRowKeys,this.selectionRows)
      // },
      // onClearSelected() {
      //   this.selectedRowKeys = [];
      //   this.selectionRows = [];
      // },
    }
  }
</script>
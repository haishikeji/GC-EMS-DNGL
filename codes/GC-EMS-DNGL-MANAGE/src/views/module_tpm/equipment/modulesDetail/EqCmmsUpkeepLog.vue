<template>
  <div>
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
    </a-table>
  </div>
</template>

<script>
  import { httpAction, getAction } from '@/api/manage'
  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: 'InspectItemList',
    mixins:[JeecgListMixin, mixinDevice],
    props: {
      id: {
        type: String,
        default: ''
      }
    },
    data () {
      return {
        // 表头
        columns: [
          // {
          //   title: '序号',
          //   dataIndex: '',
          //   key:'rowIndex',
          //   width:60,
          //   align:"center",
          //   customRender:function (t,r,index) {
          //     return parseInt(index)+1;
          //   }
          // },
          {
            title:'创建日期',
            align:"center",
            dataIndex: 'createTime',
          },
          {
            title:'负责人',
            align:"center",
            dataIndex: 'chargeruser_dictText'
          },
          {
            title:'处理状态',
            align:"center",
            dataIndex: 'status_dictText'
          },
          {
            title:'处理意见',
            align:"center",
            dataIndex: 'opinion'
          },
          {
            title:'所属保养工单',
            align:"center",
            dataIndex: 'upkeepname'
          },
        ],
        url: {
          list: "/cmmsUpkeep/cmmsUpkeep/selectEquipUpkeepDetailByEqid",
          // equipmentId
          // delete: "/cmmsInspectItem/cmmsInspectItem/delete",
          // deleteBatch: "/cmmsInspectItem/cmmsInspectItem/deleteBatch",
          // exportXlsUrl: "/cmmsInspectItem/cmmsInspectItem/exportXls",
          // importExcelUrl: "cmmsInspectItem/cmmsInspectItem/importExcel",
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    },
    computed: {
    },
    methods: {
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
        params.equipmentid = this.id
        this.loading = true;
        getAction(this.url.list, params).then((res) => {
          // this.dataSource = res;
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
      initDictConfig(){
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>
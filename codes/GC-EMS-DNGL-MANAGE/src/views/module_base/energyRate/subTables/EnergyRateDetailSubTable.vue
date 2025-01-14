<template>
  <a-table
    rowKey="id"
    size="middle"
    bordered
    :loading="loading"
    :columns="columns"
    :dataSource="dataSource"
    :pagination="false"
  >

    <template slot="htmlSlot" slot-scope="text">
      <div v-html="text"></div>
    </template>

    <template slot="imgSlot" slot-scope="text,record">
      <div style="font-size: 12px;font-style: italic;">
        <span v-if="!text">无图片</span>
        <img v-else :src="getImgView(text)" :preview="record.id" alt="" style="max-width:80px;height:25px;"/>
      </div>
    </template>

    <template slot="fileSlot" slot-scope="text">
      <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
      <a-button
              v-else
              ghost
              type="primary"
              icon="download"
              size="small"
              @click="downloadFile(text)"
      >
        <span>下载</span>
      </a-button>
    </template>

  </a-table>
</template>

<script>
  import { getAction } from '@api/manage'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'

  export default {
    name: 'EnergyRateDetailSubTable',
    mixins: [JeecgListMixin],
    props: {
      record: {
        type: Object,
        default: null,
      }
    },
    data() {
      return {
        description: '能源费率内嵌列表',
        disableMixinCreated: true,
        loading: false,
        dataSource: [],
        columns: [
          {
            title: '分时开始时间',
            align: 'center',
            dataIndex: 'begintime',
          },
          {
            title: '分时结束时间',
            align: 'center',
            dataIndex: 'endtime',
          },
          {
            title: '峰平谷',
            align: 'center',
            dataIndex: 'pandv_dictText',
          },
          {
            title: '价格',
            align: 'center',
            dataIndex: 'price',
          },
          {
            title: '起始量',
            align: 'center',
            dataIndex: 'beginamount',
          },
          {
            title: '结束量',
            align: 'center',
            dataIndex: 'endamount',
          },
        ],
        url: {
          listByMainId: '/energyRate/energyRate/queryDetailByMainId',
        },
      }
    },
    watch: {
      record: {
        immediate: true,
        handler() {
          if (this.record != null) {
            this.loadData(this.record)
          }
        }
      }
    },
    methods: {

      loadData(record) {
        this.loading = true
        this.dataSource = []
        getAction(this.url.listByMainId, {
          id: record.id
        }).then((res) => {
          if (res.success) {
            this.dataSource = res.result.records
          }
        }).finally(() => {
          this.loading = false
        })
      },

    },
  }
</script>

<style scoped>

</style>

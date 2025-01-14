<template>
  <div>
    <a-table
      ref="table"
      size="middle"
      :scroll="{x:true}"
      bordered
      rowKey="id"
      :columns="columns"
      :dataSource="logData"
      class="equipment-cmms-table">

      <span slot="status" slot-scope="text, record">
        <a-tag v-if="record.status === '0'" color="orange">{{ text }}</a-tag>
        <a-tag v-if="record.status === '1' || record.priority === '2'" color="blue">{{ text }}</a-tag>
        <a-tag v-if="record.status === '3'">{{ text }}</a-tag>
      </span>

    </a-table>
  </div>
</template>

<script>
export default {
  name: '',
  data () {
    return {
    }
  },
  created () {
  },
  mounted () {
  },
  methods: {
  }
}
</script>

<style lang="less" scoped>
</style>

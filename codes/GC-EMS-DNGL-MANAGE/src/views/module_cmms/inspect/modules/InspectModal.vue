<template>
  <u-modal
    :title="title"
    :visible.sync="visible"
    contentFull
    :bottons="false"
    @cancel="handleCancel">

    <inspect-form ref="realForm" :disabled="disableSubmit"></inspect-form>
  </u-modal>
</template>

<script>

  import InspectForm from './InspectForm'
  export default {
    name: 'InspectModal',
    components: {
      InspectForm
    },
    data () {
      return {
        title:'',
        width:800,
        visible: false,
        disableSubmit: false
      }
    },
    methods: {
      edit (record) {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.edit(record);
        })
      },
      close () {
        this.$emit('close');
        this.visible = false;
      },
      handleCancel () {
        this.close()
      }
    }
  }
</script>
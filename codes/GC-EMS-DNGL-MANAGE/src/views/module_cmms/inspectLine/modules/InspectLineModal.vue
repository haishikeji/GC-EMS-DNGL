<template>
  <u-modal
    :title="title"
    :visible.sync="visible"
    contentFull
    @ok="handleOk"
    @cancel="handleCancel">
    <inspect-line-form ref="realForm" @ok="submitCallback" :disabled="disableSubmit"></inspect-line-form>
  </u-modal>
</template>

<script>

  import InspectLineForm from './InspectLineForm'
  export default {
    name: 'InspectLineModal',
    components: {
      InspectLineForm
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
      add () {
        this.visible=true
        this.$nextTick(()=>{
          this.$refs.realForm.add();
        })
      },
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
      handleOk () {
        this.$refs.realForm.submitForm();
      },
      submitCallback(){
        this.$emit('ok');
        this.visible = false;
      },
      handleCancel () {
        this.close()
      }
    }
  }
</script>
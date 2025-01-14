<template>
  <u-modal
    :title="title"
    :visible.sync="visible"
    contentFull
    @ok="handleOk"
    :okButtonProps="{ class:{'jee-hidden': disableSubmit} }"
    @cancel="handleCancel"
    cancelText="关闭">
    <!-- <TpmTagFormPL1 ref="realForm" @ok="submitCallback"></TpmTagFormPL1> -->
    <TagFormPL1 ref="realForm" @ok="submitCallback"></TagFormPL1>
  </u-modal>
</template>

<script>
import TagFormPL1 from './TagFormPL1.vue';
  export default {
    name: 'TagModalPL',
    components: {
        TagFormPL1
    },
    data () {
      return {
        title:'',
        visible: false,
        disableSubmit: false
      }
    },
    methods: {
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
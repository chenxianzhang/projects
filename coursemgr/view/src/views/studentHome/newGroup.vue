<template>
  <div class="group-add-container">
      <div>组长：{{leader}}</div>
      <div>请选择组员：</div>

  </div>
</template>

<script>
    export default {
        name: "newGroup",
      data(){
          return{
            leader:'张三',
          }
      },
    }
</script>

<style scoped>
  .group-add-container{
    width: 100%;
  }
</style>

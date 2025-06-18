<template>


  <router-view/>
</template>
<script>
import { defineComponent } from "vue";
// 移除单独导入和注册 Element Plus 组件
export default defineComponent({
  name: "App",
});
</script>
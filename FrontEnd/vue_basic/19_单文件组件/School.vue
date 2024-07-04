<!-- 组件的结构 -->
<template>
  <div class="demo">
    <h2>学校名称: {{ name }}</h2>
    <h2>学校地址: {{ address }}</h2>
    <button @click="showName">点我提示学校名</button>
  </div>
</template>

<!-- 组件交互相关的代码(数据、方法等) -->
<script>
export default {
  name: "School",
  data() {
    return {
      name: "尚硅谷",
      address: "北京昌平",
    };
  },
  methods: {
    showName() {
      alert(this.name);
    },
  },
};
</script>

<!-- 组件的样式 -->
<style>
.demo {
  background-color: orange;
}
</style>
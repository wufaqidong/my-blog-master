<template>
  <div>
    <svg-icon class="giteeSvg" icon-class="giteeSvg" @click.native="goto"
      style="height: 2.5em;font-size: 18px;cursor: pointer" />
  </div>
</template>

<script>
export default {
  name: 'ShiYiGit',
  data() {
    return {
      url: 'https://gitee.com/quequnlong/shiyi-blog'
    }
  },
  methods: {
    goto() {
      window.open(this.url)
    }
  }
}
</script>
<style scoped>
svg {
  transition: all .5s;
}

svg:hover {
  transform: scale(1.1);
}
</style>

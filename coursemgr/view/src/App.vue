<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<script>

import { getCourseId } from '@/utils/auth'

export default {
  name: 'App',
  created() {
    this.variables.courseId = getCourseId()
  }
}
</script>

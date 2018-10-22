<template>
  <div id="app">
    <transition name="fade-transform" mode="out-in">
      <router-view />
    </transition>
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

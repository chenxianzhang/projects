<template>
  <div class="tags-view">
    <div class="tags">
      <el-tag v-for="tag in localTags" :key="tag.index" :closable="tag.closable"
              :class="tag.index === activeTag ? 'active' : ''">
        <!--{{ tag.label | ellipsis(72, 12) }}-->
        {{ tag.label }}
        <span style="display:none">{{ tag.index }}</span>
      </el-tag>
    </div>
    <div class="bottom-box"></div>
  </div>
</template>
/**
 * tags: {
 *     label: '',
 *     closable: false,
 *     index: ''
 */
<script>
export default {
  name: "tags-view",
  props: ["tags", "activeTag", "router"],
  data() {
    return {
      localTags: []
    }
  },
  created() {
    this.localTags = this.tags;
  },
  moutend() {
    this.initTags();
  },
  watch: {
    tags(value) {
      this.localTags = value;
    }
  },
  updated() {
    this.initTags();
  },
  methods: {
    initTags() {
      let tags = document.getElementsByClassNome("el-tag");
      this.updateTagLeft(tags);
      this.bindEvent(tags);
    },
    updateTagLeft(tags) {
      if (tags.length === 1) {
        return;
      }
      for (let i = 0; i < tags.length; i++) {
        tags[i].style.left = 35 + 34 * i + this.sumTagsWidth(tags, i - 1) + "px";
      }
    },
    sumTagsWidth(tags, index) {
      let sum = 0;
      for (let i = 0; i <= index; i++) {
        sum += tags[i].clientWidth;
      }
      return sum;
    },
    bindEvent(tags) {
      let self = this;
      for (let i = 0; i < tags.length; i++) {
        tags[i].onclick = function (e) {
          self.removeActive(tags);
          e.target.classList.add("active");
          if (self.router) {
            self.$router.push(e.target.getElementsByTagName("span")[0].innerText);
          } else {
            self.$emit("tagClk", e.target.getElementsByTagName("span")[0].innerText);
          }
        };
      }
    },
    removeActive(tags) {
      [].slice.call(tags).forEach(function (tag) {
        if (tag.classList.contains("active")) {
          tag.classList.remove("active");
        }
      });
    }
  }
}
</script>
<style>
  .tags-view {
    padding-top: 20px;
    padding-left: 35px;
    background-color: rgba(203, 212, 219, 0.6);
    position: relative;
  }
  .tags-view .active {
    z-index: 10 !important;
  }
  .tags-view .tags {
    height: 24px;
  }
  .tags-view .el-tag {
    border: 0;
    border-radius: 0;
    padding: 0 15px;
    position: absolute;
    color: #333;
    background: #fff;
    margin-right: 10px;
    z-index: 4;
    -webkit-filter: drop-shadow(1px -2px 0 #ccc);
    cursor: pointer;
    max-width: 100px;
  }
  .tags-view .el-tag:hover {
    color: #ccc;
  }
  .tags-view .el-tag:before {
    width: 0;
    height: 0;
    content: "";
    border: 12px solid;
    position: absolute;
    border-color: transparent #fff #fff transparent;
    top: 0;
    left: -24px;
  }
  .tags-view .el-tag:after {
    width: 0;
    height: 0;
    content: "";
    border: 12px solid;
    position: absolute;
    border-color: transparent transparent #fff #fff;
    top: 0;
    left: -24px;
  }
  .tags-view .bottom-box {
    height: 6px;
    width: 100%;
    border: 1px solid #ccc;
    margin-left: -35px;
    margin-top: -1px;
    background-color: #fff;
    z-index: 8;
    position: absolute;
    -webkit-filter: drop-shadow(0 -1px 0 #ccc);
    border-bottom: 0;
  }
</style>

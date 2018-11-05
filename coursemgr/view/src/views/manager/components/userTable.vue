<template>
  <div class="user-table">
    <el-table :data="data" v-if="role!=='admin'">
      <!-- <el-table-column v-if="editable" type="selection" width="55" align="center"></el-table-column> -->
      <el-table-column align="center" prop="name" label="姓名" width="180"></el-table-column>
      <el-table-column align="center" prop="serialNo" :label="getSerialText()"></el-table-column>
      <el-table-column v-if="role==='teacher'" align="center" prop="college" label="学院"></el-table-column>
      <el-table-column v-else align="center" prop="profession" label="专业"></el-table-column>
      <el-table-column align="center" label="操作" width="200">
        <template slot-scope="scope">
          <div class="operator">
            <i class="operator-icon-edit"  @click="handleClk(scope.row, 'edit')"></i>
            <i class="operator-icon-course"  @click="handleClk(scope.row, 'course')"></i>
            <i class="operator-icon-reset-pwd"  @click="handleClk(scope.row, 'reset')"></i>
            <i class="operator-icon-delete"  @click="handleClk(scope.row, 'delete')"></i>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-table :data="data" v-if="role==='admin'">
      <!-- <el-table-column v-if="editable" type="selection" width="55" align="center"></el-table-column> -->
      <el-table-column align="center" prop="courseName" label="课程名称" width="180"></el-table-column>
      <el-table-column align="center" prop="credit" label="学分数"></el-table-column>
      <el-table-column align="center" prop="teacher" label="任课教师"></el-table-column>
      <el-table-column align="center" prop="teacherNo" label="教师工号"></el-table-column>
      <el-table-column align="center" label="操作" width="80">
        <template slot-scope="scope">
          <div class="operator">
            <i class="operator-icon-grade"  @click="handleClk(scope.row, 'viewGrade')"></i>
            <i class="operator-icon-delete"  @click="handleClk(scope.row, 'deleteCourse')"></i>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                   :current-page="currPage" :page-sizes="[10, 15, 20, 30]" :page-size="pageSize"
                   layout="total, sizes, prev, pager, next, jumper" :total="totalCount" style="float: right">
    </el-pagination>
  </div>
</template>

<script>
export default {
  name: 'userTable',
  data() {
    return {}
  },
  props: {
    data: {
      type: Array,
      default: function() {
        return []
      }
    },
    currPage: {
      type: Number,
      default: 1
    },
    pageSize: {
      type: Number,
      default: 10
    },
    totalCount: {
      type: Number,
      default: 0
    },
    role: {
      type: String,
      default: 'teacher'
    }
  },
  created() {},
  mounted() {},
  methods: {
    handleSizeChange(val) {
      this.$emit('handlePage', {
        type: 'PAGE_SIZE_CHANGE',
        value: val
      })
    },
    handleCurrentChange(val) {
      this.$emit('handlePage', {
        type: 'PAGE_INDEX_CHANGE',
        value: val
      })
    },
    getSerialText() {
      return this.role === 'teacher' ? '教工号' : '学号'
    },
    handleClk(row, commond) {
      this.$emit('handleCommond', {
        commond: commond,
        value: row
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.user-table {
  .el-table {
    margin-top: 15px;
  }
  .el-pagination {
    margin-top: 10px;
  }
  .operator {
    display: flex;
    justify-content: space-around;
  }
}
</style>

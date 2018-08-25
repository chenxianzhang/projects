<template>
    <div>
      <h4>课程名:{{courseName}}</h4>
      <el-table :data="tasks" style="width: 100%">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="name" label="任务名称"> </el-table-column>
        <el-table-column prop="publishTime" label="发布日期" :formatter="dateFormat"> </el-table-column>
        <el-table-column prop="deadline" label="截止日期" :formatter="dateFormat"> </el-table-column>
        <!--<el-table-column prop="finishState" label="完成状态"> </el-table-column>-->
        <el-table-column fixed="right" label="操作" width="140">
          <template slot-scope="scope">
            <el-button @click="handleDetailClick(scope.row)" type="text" size="small">详情</el-button>
            <el-button @click="handleModifyClick(scope.row)" type="text" size="small">修改</el-button>
            <el-button @click="handleDeleteClick(scope.row)" type="text" size="small">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
</template>

<script>
  import {getCourseTasksByCourseId} from '@/api/course'
    export default {
        name: "taskInfo",
      data(){
          return {
            courseName:'xxx课程',
            tasks:[{taskId:'1', taskName:'1', publishDate:'1', expiryDate:'1', finishState:'1'},
              {taskId:'2', taskName:'2', publishDate:'2', expiryDate:'2', finishState:'2'}],
          }
      },
      created() {
        let self = this;
        let cId = this.$store.getters.courseId;
        if (!cId || cId === '') {
          this.$message.warning('无效的课程id');
        }
        getCourseTasksByCourseId({courseId: cId}).then(response => {
          if (response.status === 0) {
            self.$message({
              showClose: true,
              type: 'warning',
              message: response.msg
            });
            return;
          }
          self.tasks = response.data;
        });
      },
      methods: {
        /**
         * 查看详情
         * */
        handleDetailClick(row) {
          console.log(row);
        },
        /**
         * 修改任务
         * */
        handleModifyClick(row) {
          console.log(row);
        },
        /**
         * 删除任务
         * */
        handleDeleteClick(row) {
          console.log(row);
        },
        /**
         * 时间格式转换
         * */
        dateFormat:function(row, column) {
          let date = row[column.property];
          if (date == undefined) {
            return "";
          }
          return new Date(date).toLocaleDateString();
        },
      },
    }
</script>

<style scoped>

</style>

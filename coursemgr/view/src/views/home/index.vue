<template>
  <div class="container">
    <div class="flex-center">
      <div class="container-left">
        <div class="module-title">
          <span>课程列表</span>
        </div>
        <div class="course-list">
          <div class="card-panel" v-for="(item, index) in courseList" :key="index">
            <span>{{ item.name }}</span>
          </div>
          <div class="card-panel">
            <i class="el-icon-plus"></i>
          </div>
        </div>
      </div>
      <div class="container-right">
        <!--<div class="userinfo">-->
          <div class="module-title">
            <span>用户信息</span>
            <!--<svg-icon icon-class="edit"></svg-icon>-->
            <i class="el-icon-edit-outline"></i>
          </div>
          <div class="content">
            <div class="info-row">
              <span class="label">姓名</span>
              <span class="value">陈献章</span>
            </div>
            <div class="info-row">
              <span class="label">教工号</span>
              <span class="value">陈献章</span>
            </div>
            <div class="info-row">
              <span class="label">学院</span>
              <span class="value">陈献章</span>
            </div>
            <div class="info-row">
              <span class="label">电话</span>
              <span class="value">陈献章</span>
            </div>
            <div class="info-row">
              <span class="label">邮箱</span>
              <span class="value">陈献章</span>
            </div>
          </div>
        <!--</div>-->
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: "home",
  data() {
    return {
      courseList: []
    };
  },
  mounted () {
    for (let i = 0; i < 7; i++) {
      this.courseList.push({name: "数学"});
    }
  }
}


</script>
<style rel="stylesheet/scss" lang="scss" scoped>

.container {
  background-color: #567;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  padding: 10px 50px;
  .flex-center {
    width: 84%;
    height: 100%;
    display: flex;
     justify-content: center;
    .container-left {
      border-radius: 4px;
      height: 100%;
      width: calc(100% - 400px);
      background-color: white;
      .course-list {
        width: 100%;
        height: calc(100% - 44px);
        display: flex;
        flex-wrap: wrap;
        justify-content: flex-start;
        //justify-content: space-around;
        align-content: flex-start;
        overflow: auto;
        .card-panel {
          display: flex;
          justify-content: center;
          flex-wrap: wrap;
          align-items: center;
          width: 100px;
          height: 100px;
          background-color: #87a035;
          border-radius: 4px;
          margin-top: 10px;
          margin-left: 5px;
          cursor: pointer;
          span {
            text-align: center;
            font-size: 18px;
            font-weight: 800;
            color: #ccc;
          }
          .el-icon-plus {
            color: #ccc;
          }
        }
        .card-panel:hover {
          background-color: rgba(135, 160, 53, 0.8);
          span {
            color: #fff;
          }
        }
      }
    }
    .container-right {
      width: 400px;
      height: 400px;
      border-radius: 4px;
      background-color: #fff;
      margin-left: 10px;
      .content {
        width: 100%;
        height: calc(100% - 44px);
        .info-row {
          border-bottom: 1px solid #ccc;
          padding: 10px;
          font-size:  13px;
          span {
            display: inline-block;
          }
          .label {
            width: 70px;
            text-align: right;
            color: #555;
            font-weight: 600;
          }
          .value {
            padding-left: 15px;
            color: #888;
          }
        }
      }
    }
    .module-title {
      color: #303133;
      font-weight: 800;
      transition: color .2s cubic-bezier(.645,.045,.355,1);
      border-bottom: 1px solid #ccc;
      box-shadow: 0 1px 0 #ccc;
      display: flex;
      justify-content: space-between;
      align-items: center;
      span {
        padding: 10px;
        display: inline-block;
      }
      .el-icon-edit-outline {
        cursor: pointer;
        margin-right: 10px;
      }
      .el-icon-edit-outline:hover {
        opacity: 0.8;
      }
    }
  }

}



</style>



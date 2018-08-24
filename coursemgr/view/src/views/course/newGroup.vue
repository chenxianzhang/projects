<template>
  <div class="group-add-container">
    <div class="leader-span">组长：{{leader}}</div>
    <div>
      <span>组员选择：</span>
      <el-transfer
        filterable
        :titles="['所有学生', '组队学生']"
        :filter-method="filterMethod"
        filter-placeholder="请输入姓名"
        v-model="students2Group"
        :data="allStudentsSelection">
      </el-transfer>
    </div>
  </div>
</template>

<script>
    export default {
        name: "newGroup",
      data() {
        const generateData2 = _ => {
          const data = [];
          const cities = ['上海', '北京', '广州', '深圳', '南京', '西安', '成都'];
          cities.forEach((city, index) => {
            data.push({
              label: city,
              key: index,
            });
          });
          return data;
        };

        return {
          allStudents:[],//所有可选学生  原始列表
          allStudentsSelection: generateData2(),
          students2Group: [],//选中的学生
          filterMethod(query, item) {
            return item.label.indexOf(query) > -1;
          }
        }
      },
      methods: {
        getStudents(){
          //todo 后台获取所有学生列表
          this.allStudents = [];
        },
      },
    }
</script>

<style scoped>
  .group-add-container{
    width: 100%;
    margin-top: 10px;
    box-shadow: 0px 0px 5px 5px #3276bd;
    border-radius: 1px;
    padding: 10px;
  }
  .leader-span{
    margin: 10px;
    height: 30px;
    line-height: 30px;
  }
  .member-choose{

  }
</style>

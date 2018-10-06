<template>
    <div class="user-container">
        <div class="operation">
            <div class="left">
                <el-button type="primary">添加</el-button>
                <el-button type="primary">批量导入</el-button>
            </div>
            <div class="right">
                <el-input :placeholder="placeholder" v-model="nameOfNo" @input="queryChange">
                    <el-button slot="append" icon="el-icon-search"></el-button>
                </el-input>
            </div>
        </div>
        <router-view ref="child" />
    </div>
</template>

<script>
export default {
    name: 'index',
    data() {
        return {
            placeholder: '输入姓名或者教工号',
            nameOfNo: ''
        }
    },
    mounted () {
        if (this.$route.path.indexOf('student') >= 0) {
            this.placeholder = '输入姓名或者学号'
        } else {
            this.placeholder = '输入姓名或者教工号'
        }
    },
    methods: {
        queryChange(value) {
            this.$refs.child.$emit('query', this.nameOfNo)
        }
    }
}
</script>

<style lang="scss" scoped>
.user-container {
    .operation {
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: 1px solid gray;
        padding-bottom: 5px;
    }
}
</style>


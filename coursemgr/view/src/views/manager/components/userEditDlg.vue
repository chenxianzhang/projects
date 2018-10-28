<template>
    <drag-dialog :title="title" width="36%" :dialogVisible="visible" @close="handleClose" @confirm="handleConfirm">
        <el-form ref="editForm" :model="user" :rules="rules" label-position="right" label-width="80px">
            <el-form-item label="用户名" prop="name">
                <el-input v-model="user.name" placeholder="请输入姓名"></el-input>
            </el-form-item>
            <el-form-item label="学院" prop="college">
                <el-input v-model="user.college" placeholder="请输入学院名称"></el-input>
            </el-form-item>
            <el-form-item :label="serialText" prop="serialNo">
                <el-input v-model="user.serialNo" placeholder="请输入学号" :disabled="from && from === 'admin_edit'"></el-input>
            </el-form-item>
            <el-form-item v-if="role === 'student'" label="专业" prop="profession">
                <el-input v-model="user.profession" placeholder="请输入专业名称"></el-input>
            </el-form-item>
            <el-form-item label="性别" prop="sex">
                <el-radio-group v-model="user.sex">
                    <el-radio label="男">男</el-radio>
                    <el-radio label="女">女</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="电话" prop="cellphone">
                <el-input v-model="user.cellphone" placeholder="请输入电话"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
                <el-input v-model="user.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>
        </el-form>
    </drag-dialog>
</template>

<script>
import { validateEmail, validatePhone } from '@/utils/validate'

export default {
  name: 'userEditDlg',
  data() {
    return {
      serialText: '',
      rules: {}
    }
  },
  props: {
    from:{
      type: String,
      default:''
    },
    title: {
      type: String,
      default: ''
    },
    visible: {
      type: Boolean,
      default: false
    },
    user: {
      type: Object,
      default: function() {
        return {}
      }
    },
    role: {
      type: String,
      default: 'student'
    }
  },
  components: {

  },
  mounted() {
    this.serialText = this.role === 'teacher' ? '教工号' : '学号'
    this.initRoles()
  },
  watch: {
    role(val) {
      this.serialText = this.role === 'teacher' ? '教工号' : '学号'
    }
  },
  methods: {
    initRoles() {
      this.rules = {
        name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        // college: [
        //   { required: true, message: '请输入学院名称', trigger: 'blur' }
        // ],
        serialNo: [
          {
            required: true,
            message: '请输入' + this.serialText,
            trigger: 'blur'
          },
          {
            min: 7,
            message: '长度不能小于 7',
            trigger: 'blur'
          }
        ],
        profession: [],
        sex: [{ required: true, message: '请选择性别', trigger: 'change' }],
        cellphone: [{ trigger: 'blur', validator: this.validatePhone }],
        email: [{ trigger: 'blur', validator: this.validateEmail }]
      }
      if (this.role === 'student') {
          this.rules.profession.push({ required: true, message: '请输入专业名称', trigger: 'blur' })
      }
    },
    handleClose() {
      this.$emit('close')
    },
    handleConfirm() {
        this.$refs.editForm.validate(valid => {
            if (valid) {
                this.$emit('confirm', this.user)
            }
        })
    },
    validateEmail(rule, value, callback) {
      if (value && value.trim() != '' && !validateEmail(value)) {
        callback(new Error('请输入正确的邮箱'))
      } else {
        callback()
      }
    },
    validatePhone(rule, value, callback) {
      if (value && value.trim() != '' && !validatePhone(value)) {
        callback(new Error('请输入正确的电话号码'))
      } else {
        callback()
      }
    }
  }
}
</script>

<style>
</style>

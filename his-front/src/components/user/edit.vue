<template>
  <div>
    <el-form ref="form" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" :disabled="isupdate"></el-input>
      </el-form-item>
      <el-form-item label="密码" >
        <el-input type="password" v-model="form.password" show-password></el-input>
      </el-form-item>
      <el-form-item label="真实姓名" prop="realname">
        <el-input  v-model="form.realname" ></el-input>
      </el-form-item>
      <el-form-item label="电话号码" prop="telephone">
        <el-input v-model="form.telephone" ></el-input>
      </el-form-item>
      <el-form-item label="用户类型">
        <el-select v-model="form.userType" placeholder="请选择" :popper-append-to-body="false">
          <el-option
            v-for="(role, id) in roles"
            :key="id"
            :label="role.name"
            :value="id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="归属科室">
        <el-select v-model="form.deptId" placeholder="请选择" :popper-append-to-body="false">
          <el-option
            v-for="(value, id) in department"
            :key="id"
            :label="value.name"
            :value="id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" plain @click="save">保存数据</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { mapState } from 'vuex'
export default {
  name: 'UserEdit',
  props: ['editid'],
  data () {
    var checkName = (rule, value, cb) => {
      if (this.editid) cb()
      const promise = this.$axios.http.get('/users/check', { params: { name: value } })
      promise.then(
        response => {
          if (response.data.status !== 200) {
            cb(new Error('该用户名已存在'))
          }
          cb()
        }
      )
    }
    // 验证手机号的规则
    var checkMobile = (rule, value, cb) => {
      // 验证手机号的正则表达式
      const regMobile = /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/
      if (regMobile.test(value)) {
        return cb()
      }
      cb(new Error('请输入合法的手机号'))
    }
    return {
      form: {
        username: '',
        password: '',
        realname: '',
        telephone: '',
        userType: '',
        deptId: ''
      },
      isupdate: false,
      rules: {
        // 验证用户名是否合法
        username: [
          { required: true, message: '请输入登录名称', trigger: 'blur' },
          { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' },
          { validator: checkName, trigger: 'blur' }
        ],
        // 验证手机号是否合法
        telephone: [
          { required: true, message: '请输入11位手机号', trigger: 'blur' },
          { validator: checkMobile, trigger: 'blur' }
        ],
        // 验证真实姓名
        realname: [
          { required: true, message: '请输入11位手机号', trigger: 'blur' }
        ]
      }
    }
  },
  created () {
    if (this.editid) {
      // 如果是修改 用户名输入框禁用
      this.isupdate = true
      // 通过id 读原始数据
      this.$axios.get(`/users/${this.editid}`, response => {
        this.form.username = response.data.username
        this.form.realname = response.data.realname
        this.form.telephone = response.data.telephone
        this.form.userType = String(response.data.userType)
        if (response.data.deptId !== 0) {
          this.form.deptId = String(response.data.deptId)
        }
      })
    }
    this.$store.dispatch('getRoles')
    this.$store.dispatch('getDepartment')
  },
  computed: {
    ...mapState(['roles', 'department'])
  },
  methods: {
    save () {
      this.$refs.form.validate(valid => {
        if (!valid) return
        if (this.editid) {
          this.$axios.put(`/users/${this.editid}`, response => {
            // 子组件要改变父组件的值
            this.$emit('update:show', false)
            this.$emit('getData')
          }, this.form)
        } else {
          this.$axios.post('/users', response => {
            // 子组件要改变父组件的值
            this.$emit('update:show', false)
            this.$emit('getData')
          }, this.form)
        }
      })
    }
  }
}
</script>

<style scoped>
.el-select-dropdown {
  position: absolute;
  top: 38px !important;
  left: 0 !important;
}
</style>

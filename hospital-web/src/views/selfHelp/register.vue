<template>
  <div>
    <el-button type="success" @click="appointment" v-if="visitNumber == 0" plain>挂号</el-button>
    <p v-if="visitNumber != 0">当前就诊号:{{visitNumber}}</p>
  </div>
</template>

<script>
  export default {
    name: 'Appointment',
    data() {
      return {
        visitNumber: 0,
      }
    },
    created: function() {
      this.getVisitNumber()
    },
    methods: {
      getVisitNumber: function() {
        this.$api.visit().then((res) => {
          this.visitNumber = res.data
        }).catch((res) => {
          this.$message.error('访问失败')
        })
      },

      //挂号
      appointment: function() {
        this.$api.appointment().then((res) => {
          if (res.data) {
            this.visitNumber = res.data
          } else {
            this.open()
          }
        }).catch((res) => {
          this.$message.error('挂号失败')
        })
      },

      //提示框
      open() {
        this.$confirm('存在尚未缴费的医疗单或者尚未使用', '挂号失败', {
          confirmButtonText: '前往自助缴费',
          cancelButtonText: '查看挂号记录',
          type: 'warning'
        }).then(() => {
          this.$router.push("/pay")
        }).catch(() => {
          console.log("catch")
        });
      }
    }
  }
</script>

<style>
</style>

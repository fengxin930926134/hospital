<template>
  <el-card class="box-card" style="width: 100%; height:90vh;">
    <el-scrollbar style="height:100%">
      <div class="block" style="height: 100vh;">
        <el-timeline>
          <el-timeline-item timestamp="2018/4/12" placement="top">
            <el-card>
              <h4>更新 Github 模板</h4>
              <p>王小虎 提交于 2018/4/12 20:46</p>
            </el-card>
          </el-timeline-item>
          <el-timeline-item timestamp="2018/4/3" placement="top">
            <el-card>
              <h4>更新 Github 模板</h4>
              <p>王小虎 提交于 2018/4/3 20:46</p>
            </el-card>
          </el-timeline-item>
          <el-timeline-item timestamp="2018/4/2" placement="top">
            <el-card>
              <el-form :inline="true" :model="formInline" class="demo-form-inline">
                <el-form-item label="审批人">
                  <el-input v-model="formInline.user" placeholder="审批人"></el-input>
                </el-form-item>
                <el-form-item label="活动区域">
                  <el-select v-model="formInline.region" placeholder="活动区域">
                    <el-option label="区域一" value="shanghai"></el-option>
                    <el-option label="区域二" value="beijing"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="onSubmit">查询</el-button>
                </el-form-item>
              </el-form>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>
    </el-scrollbar>
  </el-card>
</template>

<script>
  export default {
    name: 'Record',
    data() {
      return {
        count: 0,
        formInline: {
          user: '',
          region: ''
        }
      }
    },
    methods: {
      load () {
        this.count += 2
      },
      onSubmit() {
        console.log('submit!');
      }
    }
  }
</script>

<style>
  .el-scrollbar__wrap{
    overflow-x: hidden;
  }
</style>

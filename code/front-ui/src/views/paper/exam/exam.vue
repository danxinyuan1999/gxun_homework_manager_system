<template>

  <div v-visibility-change="visibleChange" class="app-container">

    <el-row :gutter="24">

      <el-col :span="24">
        <el-card style="margin-bottom: 10px">

          距离作答结束还有：<span style="color: #ff0000;">{{ min }}分钟{{ sec }}秒</span>
          <el-button style="float: right; margin-top: -10px" type="primary" icon="el-icon-plus" :loading="loading" @click="handHandExam()">
            {{ handleText }}
          </el-button>

        </el-card>
      </el-col>

      <el-col :span="6" :xs="24" style="margin-bottom: 10px">

        <el-card>

          <p class="card-title">答题卡</p>
          <el-row :gutter="24" class="card-line" style="padding-left: 10px">
            <el-tag type="info">未作答</el-tag>
            <el-tag type="success">已作答</el-tag>
          </el-row>

          <div v-if="paperData.radioList!==undefined && paperData.radioList.length > 0">
            <p class="card-title">单选题</p>
            <el-row :gutter="24" class="card-line">
              <el-tag v-for="item in paperData.radioList" :type="cardItemClass(item.answered, item.quId)" @click="handSave(item)"> {{ item.sort+1 }}</el-tag>
            </el-row>
          </div>

          <div v-if="paperData.multiList!==undefined && paperData.multiList.length > 0">
            <p class="card-title">多选题</p>
            <el-row :gutter="24" class="card-line">
              <el-tag v-for="item in paperData.multiList" :type="cardItemClass(item.answered, item.quId)" @click="handSave(item)">{{ item.sort+1 }}</el-tag>
            </el-row>
          </div>

          <div v-if="paperData.judgeList!==undefined && paperData.judgeList.length > 0">
            <p class="card-title">判断题</p>
            <el-row :gutter="24" class="card-line">
              <el-tag v-for="item in paperData.judgeList" :type="cardItemClass(item.answered, item.quId)" @click="handSave(item)">{{ item.sort+1 }}</el-tag>
            </el-row>
          </div>

        </el-card>

      </el-col>

      <el-col :span="18" :xs="24">

        <el-card class="qu-content">
          <p v-if="quData.content">{{ quData.sort + 1 }}.{{ quData.content }}</p>
          <div v-if="quData.quType === 1 || quData.quType===3">
            <el-radio-group v-model="radioValue">
              <el-radio v-for="item in quData.answerList" :label="item.id">{{ item.abc }}.{{ item.content }} <div v-if="item.image" style="clear: both" /></el-radio>
            </el-radio-group>
          </div>

          <div v-if="quData.quType === 2">

            <el-checkbox-group v-model="multiValue">
              <el-checkbox v-for="item in quData.answerList" :label="item.id">{{ item.abc }}.{{ item.content }} <div v-if="item.image" style="clear: both" /></el-checkbox>
            </el-checkbox-group>

          </div>

        </el-card>

        <div style="margin-top: 20px">
          <el-button v-if="showPrevious" type="primary" icon="el-icon-back" @click="handPrevious()">
            上一题
          </el-button>

          <el-button v-if="showNext" type="warning" icon="el-icon-right" @click="handNext()">
            下一题
          </el-button>

        </div>

      </el-col>

    </el-row>
  </div>

</template>

<script>
import { Loading } from 'element-ui'

export default {
  name: 'ExamProcess',

  data() {
    return {
      isFullscreen: false,
      showPrevious: false,
      showNext: true,
      loading: false,
      handleText: '交作业',
      pageLoading: false,
      // 作业ID
      paperId: '',
      // 当前答题卡
      cardItem: {},
      allItem: [],
      // 当前题目内容
      quData: {
        answerList: []
      },
      // 作业信息
      paperData: {
        leftSeconds: 99999,
        radioList: [],
        multiList: [],
        judgeList: []
      },
      radioValue: '',
      multiValue: [],
      answeredIds: [],
      min: '00',
      sec: '00'
    }
  },
  created() {
    const id = this.$route.params.id
    if (typeof id !== 'undefined') {
      this.paperId = id
      this.fetchData(id)
    }
  },
  methods: {
    countdown() {
      const leftSeconds = this.paperData.leftSeconds
      if (leftSeconds < 0) {
        this.doHandler()
        return
      }
      this.min = min > 9 ? min : '0' + min
      const that = this
      this.paperData.leftSeconds -= 1
      setTimeout(function() {
        that.countdown()
      }, 1000)
    },
    cardItemClass(answered, quId) {
      if (quId === this.cardItem.quId) {
        return 'warning'
      }
      if (answered) {
        return 'success'
      }
      if (!answered) {
        return 'info'
      }
    },
    countNotAnswered() {
      let notAnswered = 0
      this.paperData.radioList.forEach(function(item) {
        if (!item.answered) {
          notAnswered += 1
        }
      })
      this.paperData.multiList.forEach(function(item) {
        if (!item.answered) {
          notAnswered += 1
        }
      })
      this.paperData.judgeList.forEach(function(item) {
        if (!item.answered) {
          notAnswered += 1
        }
      })
      return notAnswered
    },
    handNext() {
      const index = this.cardItem.sort + 1
      this.handSave(this.allItem[index])
    },
    handPrevious() {
      const index = this.cardItem.sort - 1
      this.handSave(this.allItem[index])
    },
    doHandler() {
      this.handleText = '正在上交，请等待...'
      this.loading = true
      const params = { id: this.paperId }
      handExam(params).then(() => {
        this.$message({
          message: '作业提交成功，即将进入作业详情！',
          type: 'success'
        })
        this.$router.push({ name: 'ShowExam', params: { id: this.paperId }})
      })
    },
    handHandExam() {
      const that = this

      this.handSave(this.cardItem, function() {
        const notAnswered = that.countNotAnswered()
        let msg = '确认要交作业吗？'
        if (notAnswered > 0) {
          msg = '您还有' + notAnswered + '题未作答，确认要交作业吗?'
        }
        that.$confirm(msg, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          that.doHandler()
        }).catch(() => {
          that.$message({
            type: 'info',
            message: '作业提交已取消，您可以继续作答！'
          })
        })
      })
    },
    handSave(item, callback) {
      if (item.id === this.allItem[0].id) {
        this.showPrevious = false
      } else {
        this.showPrevious = true
      }
      const last = this.allItem.length - 1
      if (item.id === this.allItem[last].id) {
        this.showNext = false
      } else {
        this.showNext = true
      }
      const answers = this.multiValue
      if (this.radioValue !== '') {
        answers.push(this.radioValue)
      }
      const params = { paperId: this.paperId, quId: this.cardItem.quId, answers: answers, answer: '' }
      fillAnswer(params).then(() => {
        if (answers.length > 0) {
          this.cardItem.answered = true
        }
        if (callback) {
          callback()
        }
        this.fetchQuData(item)
      })
    },
    fetchQuData(item) {
      const loading = Loading.service({
        text: '拼命加载中',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      this.cardItem = item
      const params = { paperId: this.paperId, quId: item.quId }
      quDetail(params).then(response => {
        console.log(response)
        this.quData = response.data
        this.radioValue = ''
        this.multiValue = []
        this.quData.answerList.forEach((item) => {
          if ((this.quData.quType === 1 || this.quData.quType === 3) && item.checked) {
            this.radioValue = item.id
          }
          if (this.quData.quType === 2 && item.checked) {
            this.multiValue.push(item.id)
          }
        })
        loading.close()
      })
    },
    fetchData(id) {
      const params = { id: id }
      paperDetail(params).then(response => {
        this.paperData = response.data
        if (this.paperData.radioList) {
          this.cardItem = this.paperData.radioList[0]
        } else if (this.paperData.multiList) {
          this.cardItem = this.paperData.multiList[0]
        } else if (this.paperData.judgeList) {
          this.cardItem = this.paperData.judgeList[0]
        }
        const that = this
        this.paperData.radioList.forEach(function(item) {
          that.allItem.push(item)
        })
        this.paperData.multiList.forEach(function(item) {
          that.allItem.push(item)
        })
        this.paperData.judgeList.forEach(function(item) {
          that.allItem.push(item)
        })
        this.fetchQuData(this.cardItem)
        this.countdown()
      })
    }

  }
}
</script>

<style scoped>

  .qu-content div{
    line-height: 30px;
  }

  .el-checkbox-group label,.el-radio-group label{
    width: 100%;
  }

  .card-title{
    background: #eee;
    line-height: 35px;
    text-align: center;
    font-size: 14px;
  }
  .card-line{
    padding-left: 10px
  }
  .card-line span {
    cursor: pointer;
    margin: 2px;
  }

</style>


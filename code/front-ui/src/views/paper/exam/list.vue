<template>

  <data-table
    ref="pagingTable"
    :options="options"
    :list-query="listQuery"
  >
    <template slot="filter-content">

      <el-select v-model="listQuery.params.openType" class="filter-item" placeholder="开放类型" clearable>
        <el-option
          v-for="item in openTypes"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>

      <el-input v-model="listQuery.params.title" placeholder="搜索作业名称" style="width: 200px;" class="filter-item" />

    </template>

    <template slot="data-columns">

      <el-table-column
        label="作业名称"
        prop="title"
        show-overflow-tooltip
      />

      <el-table-column
        label="作业类型"
        align="center"
      >
        <template slot-scope="scope">
          {{ scope.row.openType | examOpenType }}
        </template>

      </el-table-column>

      <el-table-column
        label="作业时间"
        width="220px"
        align="center"
      >

        <template slot-scope="scope">
          <span v-if="scope.row.timeLimit">
            {{ scope.row.startTime }} ~ {{ scope.row.endTime }}
          </span>
          <span v-else>不限时</span>
        </template>

      </el-table-column>

      <el-table-column
        label="作业时长"
        align="center"
      >

        <template slot-scope="scope">
          {{ scope.row.totalTime }}分钟
        </template>

      </el-table-column>

      <el-table-column
        label="作业总分"
        prop="totalScore"
        align="center"
      />

      <el-table-column
        label="及格线"
        prop="qualifyScore"
        align="center"
      />

      <el-table-column
        label="操作"
        align="center"
      >
        <template slot-scope="scope">
          <el-button v-if="scope.row.state===0" icon="el-icon-caret-right" type="primary" size="mini" @click="handlePre(scope.row.id)">去作答</el-button>
          <el-button v-if="scope.row.state===1" icon="el-icon-s-release" size="mini" disabled>已禁用</el-button>
          <el-button v-if="scope.row.state===2" icon="el-icon-s-fold" size="mini" disabled>待开始</el-button>
          <el-button v-if="scope.row.state===3" icon="el-icon-s-unfold" size="mini" disabled>已结束</el-button>
        </template>

      </el-table-column>

    </template>

  </data-table>

</template>

<script>

export default {
  name: 'OnlineList',
  data() {
    return {

      openTypes: [
        {
          value: 1,
          label: '完全开放'
        },
        {
          value: 2,
          label: '定向作业'
        }
      ],

      listQuery: {
        current: 1,
        size: 10,
        params: {
        }
      },

      options: {
        // 可批量操作
        multi: false,
        // 列表请求URL
        listUrl: '/exam/api/exam/exam/online-paging'
      }
    }
  },
  methods: {

    // 开始作答
    handlePre(examId) {
      this.$router.push({ name: 'PreExam', params: { examId: examId }})
    }
  }
}
</script>

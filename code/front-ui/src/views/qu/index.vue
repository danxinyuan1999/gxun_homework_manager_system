<template>

  <div>

    <data-table
      ref="pagingTable"
      :options="options"
      :list-query="listQuery"
      @multi-actions="handleMultiAction"
    >
      <template slot="filter-content">

        <el-row>
          <el-col :span="24">

            <el-select v-model="listQuery.params.quType" class="filter-item" clearable>
              <el-option
                v-for="item in quTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>

            <repo-select v-model="listQuery.params.repoIds" :multi="true" />

            <el-input v-model="listQuery.params.content" placeholder="题目内容" style="width: 200px;" class="filter-item" />

            <el-button-group class="filter-item" style="float:  right">
              <el-button size="mini" icon="el-icon-upload2" @click="showImport">导入</el-button>
              <el-button size="mini" icon="el-icon-download" @click="exportExcel">导出</el-button>
            </el-button-group>

          </el-col>
        </el-row>

      </template>

      <template slot="data-columns">

        <el-table-column
          label="题目类型"
          align="center"
          width="100px"
        >
          <template slot-scope="scope">
            {{ scope.row.quType }}
          </template>
        </el-table-column>

        <el-table-column
          label="题目内容"
          show-overflow-tooltip
        >
          <template slot-scope="scope">
            <router-link :to="{ name: 'UpdateQu', params:{ id: scope.row.id}}">
              {{ scope.row.content }}
            </router-link>
          </template>
        </el-table-column>

        <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          width="180px"
        />

      </template>

    </data-table>

    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="30%"
    >

      <el-form label-position="left" label-width="100px">

        <el-form-item label="操作题库" prop="repoIds">
          <repo-select v-model="dialogRepos" :multi="true" />
        </el-form-item>

        <el-row>
          <el-button type="primary" @click="handlerRepoAction">保存</el-button>
        </el-row>

      </el-form>

    </el-dialog>

    <el-dialog
      title="导入试题"
      :visible.sync="importVisible"
      width="30%"
    >

      <el-row>
        <el-button type="primary" >上传导入</el-button>
        <el-button type="warning" >下载导入模板</el-button>
        <input ref="upFile" class="file" name="file" type="file" style="display: none"/>
      </el-row>

    </el-dialog>
  </div>
</template>
<script>
import { batchAction } from '@/api/qu/repo'
export default {
  name: 'QuList',
  data() {
    return {
      dialogTitle: '加入题库',
      dialogVisible: false,
      importVisible: false,
      dialogRepos: [],
      dialogQuIds: [],
      dialogFlag: false,

      listQuery: {
        current: 1,
        size: 10,
        params: {
          content: '',
          quType: '',
          repoIds: []
        }
      },

      quTypes: [
        {
          value: 1,
          label: '单选题'
        },
        {
          value: 2,
          label: '多选题'
        },
        {
          value: 3,
          label: '判断题'
        }
      ],
      options: {
        multi: true,
        multiActions: [
          {
            value: 'delete',
            label: '删除'
          }, {
            value: 'enable',
            label: '启用'
          },
          {
            value: 'disable',
            label: '禁用'
          },
          {
            value: 'add-repo',
            label: '加入题库..'
          },
          {
            value: 'remove-repo',
            label: '从..题库移除'
          }
        ],
        listUrl: '/exam/api/qu/qu/paging',
        deleteUrl: '/exam/api/qu/qu/delete',
        stateUrl: '/exam/api/qu/qu//state',
        addRoute: 'AddQu'
      }
    }
  },
  methods: {

    handleMultiAction(obj) {
      if (obj.opt === 'add-repo') {
        this.dialogTitle = '加入题库'
        this.dialogFlag = false
      }

      if (obj.opt === 'remove-repo') {
        this.dialogTitle = '从题库移除'
        this.dialogFlag = true
      }

      this.dialogVisible = true
      this.dialogQuIds = obj.ids
    },

    handlerRepoAction() {
      const postForm = {repoIds: this.dialogRepos, quIds: this.dialogQuIds, remove: this.dialogFlag}

      batchAction(postForm).then(() => {
        this.$notify({
          title: '成功',
          message: '批量操作成功！',
          type: 'success',
          duration: 2000
        })

        this.dialogVisible = false
      })
    },

    showImport() {
      this.importVisible = true
    },
  }
}
</script>

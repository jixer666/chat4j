<template>
  <div class="dict-container">
    <div>
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="字典名称">
          <el-input v-model="searchForm.dictName" placeholder="请输入字典名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getList" size="medium">搜索</el-button>
          <el-button @click="searchForm = {}" size="medium">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="btn-div">
     <div>
       <el-button plain size="mini" type="primary" @click="handleAdd">新增</el-button>
       <el-button plain size="mini" type="danger" @click="handleDelete(multipleSelectionIds)" :disabled="multipleSelectionIds.length === 0">批量删除</el-button>
     </div>
      <div>

      </div>
    </div>
    <div>
      <el-table v-loading="loading" :data="tableList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="字典名称" align="center" key="dictName" prop="dictName" :show-overflow-tooltip="true" />
        <el-table-column label="字典Key" align="center" key="dictKey" prop="dictKey" :show-overflow-tooltip="true" >
          <template slot-scope="scope">
            <el-link type="primary" @click="handleDictData(scope.row)">{{ scope.row.dictKey }}</el-link>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" key="status" width="100">
          <template slot-scope="scope">
            <dict-tag :options="dict.type.common_status" :value="scope.row.status"/>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        </el-table-column>
        <el-table-column label="更新时间" align="center" prop="updateTime" width="160">
        </el-table-column>
        <el-table-column
          label="操作"
          align="center"
          width="250"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete([scope.row.dictId])"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <pagination
      v-show="searchForm.total > 0"
      :total="searchForm.total"
      :page.sync="searchForm.pageNum"
      :limit.sync="searchForm.pageSize"
      @pagination="getList"
    />
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose">
      <el-form ref="form" :model="form" label-width="80px" :rules="rules">
        <el-form-item label="字典名称" prop="dictName">
          <el-input v-model="form.dictName"></el-input>
        </el-form-item>
        <el-form-item label="字典Key" prop="dictKey">
          <el-input v-model="form.dictKey"></el-input>
        </el-form-item>
        <el-form-item label="字典状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="parseInt(item.value)" v-for="(item, index) in dict.type.common_status" :key="index">{{ item.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmit" :loading="submitLoading">确 定</el-button>
      </span>
    </el-dialog>
    <el-drawer
      :title="dialogDictDataTitle"
      :visible.sync="drawerDictDataVisible"
      direction="rtl"
      size="45%"
      :before-close="handleClose">
      <div class="drawer-div">
        <el-button size="mini" type="primary" plain @click="handleDictDataAdd">新增</el-button>
        <el-table v-loading="dictDataLoading" :data="dictDataList">
          <el-table-column label="字典标签" align="center" key="dictLabel" prop="dictLabel" :show-overflow-tooltip="true" />
          <el-table-column label="字典值" align="center" key="dictValue" prop="dictValue" :show-overflow-tooltip="true" />
          <el-table-column label="状态" align="center" key="status" width="100">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.common_status" :value="scope.row.status"/>
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="250"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdateDictData(scope.row)"
              >修改</el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDeleteDictData([scope.row.dictDataId])"
              >删除</el-button>
            </template>
          </el-table-column>
        </el-table>

      </div>

    </el-drawer>
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogDictDataVisible"
      width="30%"
      :before-close="handleCloseDictData">
      <el-form ref="form" :model="form" label-width="80px" :rules="dictDataRules">
        <el-form-item label="字典标签" prop="dictLabel">
          <el-input v-model="form.dictLabel"></el-input>
        </el-form-item>
        <el-form-item label="字典值" prop="dictValue">
          <el-input v-model="form.dictValue"></el-input>
        </el-form-item>
        <el-form-item label="字典状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="parseInt(item.value)" v-for="(item, index) in dict.type.common_status" :key="index">{{ item.label }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleCloseDictData">取 消</el-button>
        <el-button type="primary" @click="onDictDataSubmit" :loading="submitLoading">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import {getDictPage, addDict, updateDict, deleteDict} from '@/api/system/dict'
import {getDictDataPage, addDictData, updateDictData, deleteDictData} from '@/api/system/dictData'

export default {
  name: 'Dict',
  dicts: ['common_status'],
  data() {
    return {
      searchForm: {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      },
      form: {},
      tableList: [],
      dictDataList: [],
      loading: false,
      submitLoading: false,
      dictDataLoading: false,
      dialogVisible: false,
      dialogDictDataVisible: false,
      drawerDictDataVisible: false,
      dialogTitle: null,
      dialogDictDataTitle: null,
      rules: {
        dictName: [
          { required: true, message: "字典名称不能为空", trigger: "blur" }
        ],
        dictKey: [
          { required: true, message: "字典字符不能为空", trigger: "blur" }
        ],
      },
      dictDataRules: {
        dictLabel: [
          { required: true, message: "字典标签不能为空", trigger: "blur" }
        ],
        dictValue: [
          { required: true, message: "字典值不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "blur" }
        ],
      },
      multipleSelectionIds: [],
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      getDictPage(this.searchForm).then(res => {
        this.tableList = res.data.list;
        this.searchForm.total = parseInt(res.data.total);
        this.loading = false;
      }).catch(error => {
        this.loading = false;
      })
    },
    onSubmit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.submitLoading = true;
          if (this.form.dictId != undefined) {
            updateDict(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.submitLoading = false;
              this.dialogVisible = false;
              this.getList();
            }).catch(error => {
              this.submitLoading = false;
            });
          } else {
            addDict(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.submitLoading = false;
              this.dialogVisible = false;
              this.getList();
            }).catch(error => {
              this.submitLoading = false;
            });
          }
        }
      });
    },
    handleUpdate(item) {
      this.form = item;
      this.dialogTitle = "修改字典";
      this.dialogVisible = true;
    },
    handleDelete(ids) {
      if (ids === null || ids.length === 0) {
        this.$modal.msgWarning("未选中字典列表");
        return;
      }
      this.$modal.confirm('是否确认删除字典编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return deleteDict({
          dictIds: ids
        });
      }).then(() => {
        this.$modal.msgSuccess("删除成功");
        this.loading = false;
        this.getList();
      }).catch((error) => {
        this.loading = false;
      });
    },
    handleAdd() {
      this.dialogTitle = "新增字典";
      this.dialogVisible = true;
    },
    handleClose() {
      this.form = {};
      this.dialogVisible = false;
      this.drawerDictDataVisible = false;
      this.dialogDictDataVisible = false
    },
    handleCloseDictData() {
      this.dialogDictDataVisible = false;
    },
    handleSelectionChange(val) {
      this.multipleSelectionIds = val.map(item => item.dictId);
    },
    handleDictData(item) {
      this.dialogDictDataTitle = `${item.dictName} - 字典数据`;
      this.form.dictId = item.dictId;
      this.getDictDataList();
    },
    handleUpdateDictData(item) {
      this.form = item;
      this.dialogTitle = "修改字典数据";
      this.dialogDictDataVisible = true;
    },
    handleDeleteDictData(ids) {
      if (ids === null || ids.length === 0) {
        this.$modal.msgWarning("未选中字典列表");
        return;
      }
      this.$modal.confirm('是否确认删除字典数据编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return deleteDictData({
          dictDataIds: ids
        });
      }).then(() => {
        this.$modal.msgSuccess("删除成功");
        this.loading = false;
        this.getDictDataList();
      }).catch((error) => {
        this.loading = false;
      });
    },
    getDictDataList() {
      getDictDataPage({
        dictId: this.form.dictId
      }).then(res => {
        this.dictDataList = res.data.list;
        this.drawerDictDataVisible = true;
      })
    },
    handleDictDataAdd() {
      this.dialogTitle = "新增字典数据";
      this.dialogDictDataVisible = true;
    },
    onDictDataSubmit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.submitLoading = true;
          if (this.form.dictDataId != undefined) {
            updateDictData(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.submitLoading = false;
              this.getDictDataList();
              this.handleCloseDictData();
            });
          } else {
            addDictData(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.submitLoading = false;
              this.getDictDataList();
              this.handleCloseDictData();
            });
          }
        }
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.dict-container {
  padding: 20px;
}

.btn-div {
  display: flex;
  justify-content: space-between;
  align-content: center;
}

.tree-border {
  margin-top: 5px;
  border: 1px solid #e5e6e7;
  background: #FFFFFF none;
  border-radius: 4px;
}

.drawer-div {
  padding: 0 10px;
}
</style>

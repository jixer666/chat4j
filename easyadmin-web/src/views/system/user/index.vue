<template>
  <div class="user-container">
    <div>
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="用户账号">
          <el-input v-model="searchForm.username" placeholder="请输入用户账号"></el-input>
        </el-form-item>
        <el-form-item label="用户昵称">
          <el-input v-model="searchForm.nickname" placeholder="请输入用户昵称"></el-input>
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
        <el-table-column label="账号" align="center" key="username" prop="username" :show-overflow-tooltip="true" />
        <el-table-column label="昵称" align="center" key="nickname" prop="nickname" :show-overflow-tooltip="true" />
        <el-table-column label="头像" align="center" key="avatar" prop="avatar" :show-overflow-tooltip="true" width="100">
          <template slot-scope="scope">
            <el-avatar size="medium" :src="scope.row.avatar"></el-avatar>
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
              @click="handleDelete([scope.row.userId])"
            >删除</el-button>
            <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)">
              <el-button size="mini" type="text" icon="el-icon-d-arrow-right">更多</el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="resetPwd" icon="el-icon-refresh-left">重置密码</el-dropdown-item>
                <el-dropdown-item command="authRole" icon="el-icon-circle-check">分配角色</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
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
        <el-form-item label="用户昵称" prop="nickname">
          <el-input v-model="form.nickname"></el-input>
        </el-form-item>
        <el-form-item label="用户头像">
          <file-upload v-model="form.avatar" :isDrag="true" :fileType="[]" class="update-div" :fileSize="5" ref="fileUploadRef" :limit="1" />
        </el-form-item>
        <el-form-item label="用户状态">
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
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogUserRoleVisible"
      width="30%"
      :before-close="handleClose">
      <el-form ref="form" :model="form" label-width="80px" :rules="rules">
        <el-form-item label="角色">
          <el-select v-model="form.roleIds" placeholder="请选择角色" multiple>
            <el-option
              v-for="item in roleList"
              :key="item.roleId"
              :label="item.roleName"
              :value="item.roleId">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogUserRoleVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmitUserRole" :loading="submitLoading">确 定</el-button>
      </span>
    </el-dialog>
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogResetPwdVisible"
      width="30%"
      :before-close="handleClose">
      <el-form ref="form" :model="form" label-width="80px" :rules="resetRules">
        <el-form-item label="新密码" prop="newPassword">
          <el-input v-model="form.newPassword"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogResetPwdVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitResetPwd" :loading="submitLoading">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {getUserPage, addUser, updateUser, deleteUser, getUserRole, saveUserRole, resetPassword} from '@/api/system/user'
import FileUpload from '@/components/FileUpload';

export default {
  name: 'User',
  dicts: ['common_status'],
  components: {
    FileUpload
  },
  data() {
    return {
      searchForm: {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      },
      form: {
        roleIds: []
      },
      tableList: [],
      loading: false,
      submitLoading: false,
      dialogVisible: false,
      dialogResetPwdVisible: false,
      dialogTitle: null,
      rules: {
        nickname: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" }
        ],
      },
      resetRules: {
        newPassword: [
          { required: true, message: "密码不能为空", trigger: "blur" }
        ],
      },
      multipleSelectionIds: [],
      roleList: [],
      dialogUserRoleVisible: false,
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      getUserPage(this.searchForm).then(res => {
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
          const fileList = this.$refs.fileUploadRef.fileList
          if (!fileList || fileList.length === 0) {
            this.$message.warning("文件未上传");
          }
          this.form.avatar = fileList[0].url;
          if (this.form.userId != undefined) {
            updateUser(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.submitLoading = false;
              this.dialogVisible = false;
              this.getList();
            }).catch(error => {
              this.submitLoading = false;
            });
          } else {
            addUser(this.form).then(response => {
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
      this.dialogTitle = "修改用户";
      this.dialogVisible = true;
    },
    handleDelete(ids) {
      if (ids === null || ids.length === 0) {
        this.$modal.msgWarning("未选中用户列表");
        return;
      }
      this.$modal.confirm('是否确认删除用户编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return deleteUser({
          userIds: ids
        });
      }).then(() => {
        this.$modal.msgSuccess("删除成功");
        this.loading = false;
        this.getList();
      }).catch(() => {
        this.loading = false;
      });
    },
    handleAdd() {
      this.dialogTitle = "新增用户";
      this.dialogVisible = true;
    },
    handleClose() {
      this.form = {
        menuCheckStrictly: true,
        roleIds: []
      };
      this.dialogVisible = false;
      this.dialogUserRoleVisible = false;
      this.dialogResetPwdVisible = false;
      const fileUploadRef = this.$refs.fileUploadRef;
      if (fileUploadRef) {
        fileUploadRef.fileList = [];
        const elUploadRef = fileUploadRef.$refs.imageUpload;
        if (elUploadRef && elUploadRef.clearFiles) {
          elUploadRef.clearFiles();
        }
      }
    },
    handleSelectionChange(val) {
      this.multipleSelectionIds = val.map(item => item.userId);
    },
    handleUserRole(userId) {
      this.dialogTitle = "分配用户角色";
      this.form.userId = userId;
      getUserRole(userId).then(res => {
        this.roleList = res.data.roles;
        this.form.roleIds = res.data.roleIds;
        this.dialogUserRoleVisible = true;
      })
    },
    onSubmitUserRole() {
      this.submitLoading = true;
      saveUserRole(this.form).then(res => {
        this.$modal.msgSuccess("操作成功");
        this.submitLoading = false;
        this.dialogUserRoleVisible = false;
      }).catch(error => {
        this.submitLoading = false;
      })
    },
    handleResetPwd(userId) {
      this.form.userId = userId;
      this.dialogTitle = "重置密码";
      this.dialogResetPwdVisible = true;
    },
    submitResetPwd() {
      this.submitLoading = true;
      resetPassword(this.form).then(res => {
        this.$modal.msgSuccess("操作成功");
        this.submitLoading = false;
        this.dialogResetPwdVisible = false;
      }).catch(error => {
        this.submitLoading = false;
      })
    },
    handleCommand(command, row) {
      switch (command) {
        case "resetPwd":
          this.handleResetPwd(row.userId);
          break;
        case "authRole":
          this.handleUserRole(row.userId);
          break;
        default:
          break;
      }
    },
  }
}
</script>

<style lang="scss" scoped>
.user-container {
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

</style>

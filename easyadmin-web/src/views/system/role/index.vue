<template>
  <div class="role-container">
    <div>
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="角色名称">
          <el-input v-model="searchForm.roleName" placeholder="请输入角色名称"></el-input>
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
        <el-table-column label="角色名称" align="center" key="roleName" prop="roleName" :show-overflow-tooltip="true" />
        <el-table-column label="角色字符" align="center" key="roleKey" prop="roleKey" :show-overflow-tooltip="true" />
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
              @click="handleDelete([scope.row.roleId])"
            >删除</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-circle-check"
              @click="handleMenus(scope.row)"
            >分配菜单</el-button>
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
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="form.roleName"></el-input>
        </el-form-item>
        <el-form-item label="角色字符" prop="roleKey">
          <el-input v-model="form.roleKey"></el-input>
        </el-form-item>
        <el-form-item label="角色状态">
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
      :visible.sync="dialogMenuVisible"
      width="30%"
      :before-close="handleClose">
      <el-form ref="form" :model="form">
        <el-form-item>
          <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event, 'menu')">展开/折叠</el-checkbox>
          <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event, 'menu')">全选/全不选</el-checkbox>
          <el-checkbox v-model="form.menuCheckStrictly" @change="handleCheckedTreeConnect($event, 'menu')">父子联动</el-checkbox>
          <el-tree
            class="tree-border"
            :data="menuOptions"
            show-checkbox
            ref="menu"
            node-key="menuId"
            :check-strictly="!form.menuCheckStrictly"
            empty-text="加载中，请稍候"
            :props="defaultProps"
          ></el-tree>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogMenuVisible = false">取 消</el-button>
        <el-button type="primary" @click="onSubmitRoleMenu" :loading="submitLoading">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {getRolePage, addRole, updateRole, getRoleMenuTree, addRoleMenu, deleteRole} from '@/api/system/role'

export default {
  name: 'Role',
  dicts: ['common_status'],
  data() {
    return {
      searchForm: {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      },
      form: {
        menuCheckStrictly: true,
      },
      tableList: [],
      loading: false,
      submitLoading: false,
      dialogVisible: false,
      dialogMenuVisible: false,
      dialogTitle: null,
      rules: {
        roleName: [
          { required: true, message: "角色名称不能为空", trigger: "blur" }
        ],
        roleKey: [
          { required: true, message: "角色字符不能为空", trigger: "blur" }
        ],
      },
      defaultProps: {
        children: "children",
        label: "menuName"
      },
      menuExpand: false,
      menuNodeAll: false,
      menuOptions: [],
      multipleSelectionIds: []
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      getRolePage(this.searchForm).then(res => {
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
          if (this.form.roleId != undefined) {
            updateRole(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.submitLoading = false;
              this.dialogVisible = false;
              this.getList();
            }).catch(error => {
              this.submitLoading = false;
            });
          } else {
            addRole(this.form).then(response => {
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
      this.dialogTitle = "修改角色";
      this.dialogVisible = true;
    },
    handleDelete(ids) {
      if (ids === null || ids.length === 0) {
        this.$modal.msgWarning("未选中角色列表");
        return;
      }
      this.$modal.confirm('是否确认删除角色编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return deleteRole({
          roleIds: ids
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
      this.dialogTitle = "新增角色";
      this.dialogVisible = true;
    },
    handleClose() {
      this.form = {
        menuCheckStrictly: true,
      };
      this.dialogVisible = false;
      this.dialogMenuVisible = false;
    },
    handleMenus(item) {
      this.dialogTitle = "分配菜单权限";
      this.form.roleId = item.roleId;
      getRoleMenuTree(item.roleId).then(res => {
        this.menuOptions = res.data.menus;
        res.data.checkMenuIds.forEach((v) => {
          this.$nextTick(()=>{
            this.$refs.menu.setChecked(v, true ,false);
          })
        })
        this.dialogMenuVisible = true;
      })
    },
    // 树权限（展开/折叠）
    handleCheckedTreeExpand(value, type) {
      if (type == 'menu') {
        let treeList = this.menuOptions;
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.menu.store.nodesMap[treeList[i].menuId].expanded = value;
        }
      } else if (type == 'dept') {
        let treeList = this.deptOptions;
        for (let i = 0; i < treeList.length; i++) {
          this.$refs.dept.store.nodesMap[treeList[i].id].expanded = value;
        }
      }
    },
    // 树权限（全选/全不选）
    handleCheckedTreeNodeAll(value, type) {
      if (type == 'menu') {
        this.$refs.menu.setCheckedNodes(value ? this.menuOptions: []);
      } else if (type == 'dept') {
        this.$refs.dept.setCheckedNodes(value ? this.deptOptions: []);
      }
    },
    // 树权限（父子联动）
    handleCheckedTreeConnect(value, type) {
      if (type == 'menu') {
        this.form.menuCheckStrictly = value ? true: false;
      } else if (type == 'dept') {
        this.form.deptCheckStrictly = value ? true: false;
      }
    },
    getMenuAllCheckedKeys() {
      // 目前被选中的菜单节点
      let checkedKeys = this.$refs.menu.getCheckedKeys();
      // 半选中的菜单节点
      let halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      return checkedKeys;
    },
    onSubmitRoleMenu() {
      this.form.menuIds = this.getMenuAllCheckedKeys();
      this.submitLoading = true;
      addRoleMenu(this.form).then(res => {
        this.$modal.msgSuccess("操作成功");
        this.dialogMenuVisible = false;
        this.submitLoading = false;
      }).catch(error => {
        this.submitLoading = false;
      })
    },
    handleSelectionChange(val) {
      this.multipleSelectionIds = val.map(item => item.roleId);
    }
  }
}
</script>

<style lang="scss" scoped>
.role-container {
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

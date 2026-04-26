<template>
  <div class="generateTable-container">
    <div>
      <el-form :inline="true" :model="searchForm" class="demo-form-inline">
        <el-form-item label="表名称">
          <el-input v-model="searchForm.generateTableName" placeholder="请输入表名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="getList" size="medium">搜索</el-button>
          <el-button @click="searchForm = {}" size="medium">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div class="btn-div">
      <div>
        <el-button plain size="mini" type="danger" @click="handleDelete(multipleSelectionIds)" :disabled="multipleSelectionIds.length === 0">批量删除</el-button>
        <el-button plain size="mini" type="success" @click="handleImport">导入</el-button>
      </div>
      <div>

      </div>
    </div>
    <div>
      <el-table v-loading="loading" :data="tableList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="表名称" align="center" key="tableName" prop="tableName" :show-overflow-tooltip="true" />
        <el-table-column label="表注释" align="center" key="tableComment" prop="tableComment" :show-overflow-tooltip="true" />
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
          width="300"
        >
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handlePreview(scope.row)"
            >预览</el-button>
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
              @click="handleDelete([scope.row.genTableId])"
            >删除</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-upload"
              @click="handleGenerateCode([scope.row.genTableId])"
            >生成代码</el-button>
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

    <el-drawer
      :title="dialogGenTableDbTitle"
      :visible.sync="drawerGenTableDbVisible"
      direction="rtl"
      size="50%"
      :before-close="handleClose">
      <div class="drawer-div">
        <el-table v-loading="genDbLoading" :data="genTableDbList">
          <el-table-column label="表名称" align="center" key="tableName" prop="tableName" :show-overflow-tooltip="true" />
          <el-table-column label="表注释" align="center" key="tableComment" prop="tableComment" :show-overflow-tooltip="true" />
          <el-table-column label="创建时间" align="center" key="createTime" prop="createTime" :show-overflow-tooltip="true" width="150" />
          <el-table-column label="更新时间" align="center" key="updateTime" prop="updateTime" :show-overflow-tooltip="true" width="150"/>
          <el-table-column
            label="操作"
            align="center"
            width="100"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleImportGenTableDb([scope.row.tableName])"
              >导入</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="searchDbForm.total > 0"
          :total="searchDbForm.total"
          :page.sync="searchDbForm.pageNum"
          :limit.sync="searchDbForm.pageSize"
          @pagination="getList"
        />

      </div>

    </el-drawer>

    <el-dialog :title="dialogTitle" :visible.sync="dialogPreviewVisible" width="80%" top="5vh" append-to-body>
      <el-tabs v-model="previewTabActiveName">
        <el-tab-pane
          v-for="(value, key) in previewData"
          :label="key.substring(key.lastIndexOf('/')+1,key.indexOf('.vm'))"
          :name="key.substring(key.lastIndexOf('/')+1,key.indexOf('.vm'))"
          :key="key"
        >
          <el-link :underline="false" icon="el-icon-document-copy" v-clipboard:copy="value" v-clipboard:success="clipboardSuccess" style="float:right">复制</el-link>
          <pre><code class="hljs" v-html="highlightedCode(value, key)"></code></pre>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>

    <el-drawer
      :title="dialogGenTableDbTitle"
      :visible.sync="drawerGenTableUpdateVisible"
      direction="rtl"
      size="70%"
      :before-close="handleClose">
      <div class="drawer-div">
        <el-tabs v-model="drawerUpdateActiveName" :loading="genDbLoading">
          <el-tab-pane label="表信息" name="table">
            <el-form :model="form" label-width="80px">
              <el-row>
                <el-col :span="12">
                  <el-form-item label="表名称">
                    <el-input v-model="form.tableName"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="表注释">
                    <el-input v-model="form.tableComment"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="实体名称">
                    <el-input v-model="form.className"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="作者">
                    <el-input v-model="form.functionAuthor"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="包路径">
                    <el-input v-model="form.packageName"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="模块名">
                    <el-input v-model="form.moduleName"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
              <el-row>
                <el-col :span="12">
                  <el-form-item label="功能名">
                    <el-input v-model="form.functionName"></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="12">
                  <el-form-item label="业务名">
                    <el-input v-model="form.businessName"></el-input>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="字段信息" name="tableColumn">
            <el-table :data="form.tableColumns">
              <el-table-column label="字段名称" align="center" key="columnName" prop="columnName" :show-overflow-tooltip="true" />
              <el-table-column label="字段类型" align="center" key="columnType" prop="columnType"" :show-overflow-tooltip="true" />
              <el-table-column label="字段注释" align="center" key="columnComment" prop="columnComment" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.columnComment"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="Java字段" align="center" key="javaField" prop="javaField" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.javaField"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="Java类型" align="center" key="updateTime" prop="updateTime" :show-overflow-tooltip="true">
                <template slot-scope="scope">
                  <el-select v-model="scope.row.javaType">
                    <el-option :label="item.label" :value="item.value" v-for="(item, index) in dict.type.system_java_type" :key="index"/>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="必填">
                <template slot-scope="scope">
                  <el-checkbox true-label="1" false-label="0" v-model="scope.row.isRequire" :true-label="1" :false-label="0"></el-checkbox>
                </template>
              </el-table-column>
              <el-table-column label="主键">
                <template slot-scope="scope">
                  <el-checkbox true-label="1" false-label="0" v-model="scope.row.isPk" :true-label="1" :false-label="0"></el-checkbox>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>
        </el-tabs>
        <div class="update-btn">
          <el-button size="medium" type="primary" @click="submitUpdate" :loading="submitLoading">保存</el-button>
        </div>
      </div>
    </el-drawer>

  </div>
</template>

<script>
import {
  getGenerateTablePage,
  addGenerateTable,
  updateGenerateTable,
  deleteGenerateTable,
  getGenerateTableDbPage, importGenerateTable, previewGenerateTable, downloadGenerateTableCode, getGenerateTableInfo
} from '@/api/system/generateTable';
import hljs from 'highlight.js/lib/core'
import 'highlight.js/styles/github.css'
import clipboard from '@/directive/clipboard/index.js';
hljs.registerLanguage("java", require("highlight.js/lib/languages/java"));
hljs.registerLanguage("xml", require("highlight.js/lib/languages/xml"));
hljs.registerLanguage("html", require("highlight.js/lib/languages/xml"));
hljs.registerLanguage("vue", require("highlight.js/lib/languages/xml"));
hljs.registerLanguage("javascript", require("highlight.js/lib/languages/javascript"));
hljs.registerLanguage("sql", require("highlight.js/lib/languages/sql"));

export default {
  name: 'GenerateTable',
  dicts: ['common_status', 'system_java_type'],
  directives: {
    clipboard
  },
  data() {
    return {
      searchForm: {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      },
      searchDbForm: {
        pageNum: 1,
        pageSize: 10,
        total: 0,
      },
      form: {},
      tableList: [],
      genTableDbList: [],
      loading: false,
      submitLoading: false,
      genDbLoading: false,
      dialogVisible: false,
      dialogPreviewVisible: false,
      drawerGenTableDbVisible: false,
      drawerGenTableUpdateVisible: false,
      dialogTitle: null,
      dialogGenTableDbTitle: null,
      rules: {
        generateTableName: [
          { required: true, message: "表名称不能为空", trigger: "blur" }
        ],
        generateTableKey: [
          { required: true, message: "表字符不能为空", trigger: "blur" }
        ],
      },
      multipleSelectionIds: [],
      previewData: {},
      previewTabActiveName: "entity.java",
      drawerUpdateActiveName: "table"
    }
  },
  mounted() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      getGenerateTablePage(this.searchForm).then(res => {
        this.tableList = res.data.list;
        this.searchForm.total = parseInt(res.data.total);
        this.loading = false;
      }).catch(error => {
        this.loading = false;
      })
    },
    handleUpdate(item) {
      this.form = item;
      this.dialogGenTableDbTitle = "修改表";
      this.drawerGenTableUpdateVisible = true;
      this.genDbLoading = true;
      getGenerateTableInfo(item.genTableId).then(res => {
        this.form = res.data;
        this.genDbLoading = false;
      }).catch(error => {
        this.genDbLoading = false;
      })
    },
    handleDelete(ids) {
      if (ids === null || ids.length === 0) {
        this.$modal.msgWarning("未选中表列表");
        return;
      }
      this.$modal.confirm('是否确认删除表编号为"' + ids + '"的数据项？').then(() => {
        this.loading = true;
        return deleteGenerateTable({
          genTableIds: ids
        });
      }).then(() => {
        this.$modal.msgSuccess("删除成功");
        this.loading = false;
        this.getList();
      }).catch((error) => {
        this.loading = false;
      });
    },
    handleClose() {
      this.form = {
        menuCheckStrictly: true,
      };
      this.dialogVisible = false;
      this.drawerGenTableDbVisible = false;
      this.drawerGenTableUpdateVisible = false;
    },
    handleSelectionChange(val) {
      this.multipleSelectionIds = val.map(item => item.generateTableId);
    },
    handleImport() {
      this.dialogGenTableDbTitle = "导入数据库表";
      this.drawerGenTableDbVisible = true;
      this.getGenerateTableDbPage();
    },
    getGenerateTableDbPage() {
      this.genDbLoading = true;
      getGenerateTableDbPage(this.searchDbForm).then(res => {
        this.genTableDbList = res.data.list;
        this.searchDbForm.total = parseInt(res.data.total);
        this.genDbLoading = false;
      }).catch(error => {
        this.genDbLoading = false;
      })
    },
    handleImportGenTableDb(item) {
      this.genDbLoading = true;
      importGenerateTable({
        tableNames: item
      }).then(res => {
        this.$message.success("导入成功");
        this.drawerGenTableDbVisible = false;
        this.genDbLoading = false;
        this.getList();
      }).catch(error => {
        this.genDbLoading = false;
      })
    },
    handlePreview(item) {
      previewGenerateTable(item.genTableId).then(res => {
        this.previewData = res.data;
        this.dialogTitle = "预览代码";
        this.dialogPreviewVisible = true;
      })
    },
    clipboardSuccess() {
      this.$modal.msgSuccess("复制成功");
    },
    highlightedCode(code, key) {
      const vmName = key.substring(key.lastIndexOf("/") + 1, key.indexOf(".vm"));
      var language = vmName.substring(vmName.indexOf(".") + 1, vmName.length);
      const result = hljs.highlight(language, code || "", true);
      return result.value || '&nbsp;';
    },
    handleGenerateCode(data) {
      this.loading = true;
      downloadGenerateTableCode({
        genTableIds: data
      }).then(res => {
        this.$modal.msgSuccess("生成成功");
        this.loading = false;
      }).catch(error => {
        this.loading = false;
      })
    },
    submitUpdate() {
      this.submitLoading = true;
      updateGenerateTable(this.form).then(res => {
        this.$modal.msgSuccess("操作成功");
        this.drawerGenTableUpdateVisible = false;
        this.submitLoading = false;
        this.getList();
      }).catch(error => {
        this.submitLoading = false;
      })

    }
  }
}
</script>

<style lang="scss" scoped>
.generateTable-container {
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

.update-btn{
  padding: 10px 0;
  text-align: center;
}
</style>

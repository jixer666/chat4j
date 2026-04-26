<template>
  <div class="component-upload-image">
    <el-upload
      multiple
      :disabled="disabled"
      :action="uploadImgUrl"
      list-type="picture-card"
      :on-success="handleUploadSuccess"
      :before-upload="handleBeforeUpload"
      :data="uploadData"
      :limit="limit"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      ref="imageUpload"
      :before-remove="handleDelete"
      :show-file-list="true"
      :headers="headers"
      :file-list="fileList"
      :on-preview="handlePictureCardPreview"
      :class="{ hide: fileList.length >= limit }"
    >
      <i class="el-icon-plus avatar-uploader-icon"></i>
    </el-upload>
    <!-- 上传提示 -->
    <div class="el-upload__tip" v-if="showTip && !disabled">
      请上传
      <template v-if="fileSize">
        大小不超过 <b style="color: #f56c6c">{{ fileSize }}MB</b>
      </template>
      <template v-if="fileType">
        格式为 <b style="color: #f56c6c">{{ fileType.join("/") }}</b>
      </template>
      的文件
    </div>

    <el-dialog
      :visible.sync="dialogVisible"
      title="预览"
      width="800px"
      append-to-body
    >
      <img
        :src="dialogImageUrl"
        style="display: block; max-width: 100%; margin: 0 auto"
      />
    </el-dialog>
  </div>
</template>

<script>
import { getToken } from "@/utils/auth"
import { isExternal } from "@/utils/validate"
import Sortable from 'sortablejs'
import md5 from 'js-md5'

export default {
  name: "ImageUpload",
  props: {
    value: [String, Object, Array],
    // 上传接口地址
    action: {
      type: String,
      default: "/system/oss/upload"
    },
    // 上传携带的参数
    data: {
      type: Object
    },
    // 图片数量限制
    limit: {
      type: Number,
      default: 5
    },
    // 大小限制(MB)
    fileSize: {
      type: Number,
      default: 5
    },
    // 文件类型, 例如['png', 'jpg', 'jpeg']
    fileType: {
      type: Array,
      default: () => ["png", "jpg", "jpeg"]
    },
    // 是否显示提示
    isShowTip: {
      type: Boolean,
      default: true
    },
    // 禁用组件（仅查看图片）
    disabled: {
      type: Boolean,
      default: false
    },
    // 拖动排序
    drag: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      number: 0,
      uploadList: [],
      dialogImageUrl: "",
      dialogVisible: false,
      baseUrl: process.env.VUE_APP_BASE_API,
      uploadImgUrl: process.env.VUE_APP_BASE_API + this.action,
      headers: this.getHeaders(),
      fileList: [],
      currentFileMd5: '',
      currentFileType: ''
    }
  },
  computed: {
    uploadData() {
      return {
        ...this.data,
        fileMd5: this.currentFileMd5,
        fileType: this.currentFileType
      }
    },
    showTip() {
      return this.isShowTip && (this.fileType || this.fileSize)
    }
  },
  watch: {
    value: {
      handler(val) {
        if (val) {
          // 首先将值转为数组
          const list = Array.isArray(val) ? val : val.split(",")
          // 然后将数组转为对象数组
          this.fileList = list.map(item => {
            if (typeof item === "string") {
              if (item.indexOf(this.baseUrl) === -1 && !isExternal(item)) {
                item = { name: this.baseUrl + item, url: this.baseUrl + item }
              } else {
                item = { name: item, url: item }
              }
            }
            return item
          })
        } else {
          this.fileList = []
        }
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    getHeaders() {
      const token = getToken()
      if (token) {
        return {
          Authorization: "Bearer " + token
        }
      }
      return {}
    },

    // 计算文件的MD5
    calculateFileMd5(file) {
      return new Promise((resolve, reject) => {
        const fileReader = new FileReader()
        const chunkSize = 2097152 // 2MB
        let offset = 0
        const spark = md5.create()

        this.currentFileType = file.type

        const loadNextChunk = () => {
          const fileSlice = file.slice(offset, offset + chunkSize)
          fileReader.readAsBinaryString(fileSlice)
        }

        fileReader.onload = e => {
          spark.update(e.target.result)
          offset += chunkSize

          if (offset < file.size) {
            loadNextChunk() // 继续读取下一块
          } else {
            // 计算完成
            resolve(spark.hex())
          }
        }

        fileReader.onerror = () => {
          console.error('文件读取失败')
          reject(new Error('文件读取失败'))
        }

        // 开始读取第一块
        loadNextChunk()
      })
    },

    // 上传前loading加载和MD5计算
    async handleBeforeUpload(file) {
      // 校检文件类型
      if (this.fileType.length) {
        const fileName = file.name.split(".")
        const fileExt = fileName[fileName.length - 1]
        const isTypeOk = this.fileType.indexOf(fileExt) >= 0
        if (!isTypeOk) {
          this.$modal.msgError(
            `文件格式不正确，请上传${this.fileType.join("/")}格式文件!`
          )
          return false
        }
      }
      // 校检文件名是否包含特殊字符
      if (file.name.includes(",")) {
        this.$modal.msgError("文件名不正确，不能包含英文逗号!")
        return false
      }
      // 校检文件大小
      if (this.fileSize) {
        const isLt = file.size / 1024 / 1024 < this.fileSize
        if (!isLt) {
          this.$modal.msgError(`上传文件大小不能超过 ${this.fileSize} MB!`)
          return false
        }
      }
      // 显示加载中
      this.$modal.loading("正在计算文件MD5并上传，请稍候...")
      this.number++

      try {
        // 计算MD5
        const md5 = await this.calculateFileMd5(file)
        this.currentFileMd5 = md5
        console.log('文件MD5计算完成:', md5)
        return true  // 允许上传
      } catch (error) {
        this.$modal.closeLoading()
        this.$modal.msgError('文件MD5计算失败，请重试')
        return false  // 阻止上传
      }
    },

    // 文件个数超出
    handleExceed() {
      this.$modal.msgError(`上传文件数量不能超过 ${this.limit} 个!`)
    },

    // 上传成功回调
    handleUploadSuccess(res, file) {
      if (res.code === 200) {
        this.uploadList.push({
          name: res.data.filename,
          url: res.data.downloadUrl,
          fileId: res.data.fileId
        })
        this.uploadedSuccessfully()
      } else {
        this.number--
        this.$modal.closeLoading()
        this.$modal.msgError(res.msg)
        this.$refs.imageUpload.handleRemove(file)
        this.uploadedSuccessfully()
      }
    },

    // 删除图片
    handleDelete(file) {
      const findex = this.fileList.map(f => f.name).indexOf(file.name)
      if (findex > -1 && this.uploadList.length === this.number) {
        this.fileList.splice(findex, 1)
        this.$emit("input", this.listToString(this.fileList))
        return false
      }
    },

    // 上传结束处理
    uploadedSuccessfully() {
      if (this.number > 0 && this.uploadList.length === this.number) {
        this.fileList = this.fileList.filter(f => f.url !== undefined).concat(this.uploadList)
        this.uploadList = []
        this.number = 0
        this.$emit("input", this.listToString(this.fileList))
        this.$modal.closeLoading()
      }
    },

    // 上传失败
    handleUploadError() {
      this.$modal.msgError("上传图片失败")
      this.$modal.closeLoading()
    },

    // 预览
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url
      this.dialogVisible = true
    },

    // 对象转成指定字符串分隔
    listToString(list, separator) {
      let strs = ""
      separator = separator || ","
      for (let i in list) {
        if (undefined !== list[i].url && list[i].url.indexOf("blob:") !== 0) {
          strs += list[i].url.replace(this.baseUrl, "") + separator
        }
      }
      return strs != "" ? strs.substr(0, strs.length - 1) : ""
    },

    // 初始化拖拽排序
    initSortable() {
      if (this.drag && !this.disabled) {
        this.$nextTick(() => {
          const element = this.$refs.imageUpload?.$el?.querySelector('.el-upload-list')
          if (element) {
            Sortable.create(element, {
              onEnd: (evt) => {
                const movedItem = this.fileList.splice(evt.oldIndex, 1)[0]
                this.fileList.splice(evt.newIndex, 0, movedItem)
                this.$emit('input', this.listToString(this.fileList))
              }
            })
          }
        })
      }
    }
  },
  mounted() {
    this.initSortable()
  }
}
</script>

<style scoped lang="scss">
::v-deep .hide .el-upload--picture-card {
  display: none;
}

::v-deep .el-upload.el-upload--picture-card.is-disabled {
  display: none !important;
}
</style>

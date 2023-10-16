<template>
    <div class='article-add'>
        <div class="containner">
            <div class="title">
                发布文章
            </div>
            <el-form style="display: flex;" :rules="rules" ref="ruleForm" label-position="left" label-width="80px"
                :model="article">
                <div class="article-left">
                    <el-row :gutter="20">
                        <el-col :span="16">
                            <el-form-item label="文章标题" prop="title">
                                <el-input v-model="article.title"></el-input>
                            </el-form-item>
                            <el-form-item label="文章简介" prop="summary">
                                <el-input v-model="article.summary"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="封面图" prop="">
                                <el-upload class="avatar-uploader" :show-file-list="false" ref="upload" name="filedatas"
                                    :action="uploadPictureHost" :http-request="uploadSectionFile"
                                    :before-upload="handleUploadBefore" multiple>
                                    <img v-if="article.avatar" style="width: 100%;height: 100%;" :src="article.avatar"
                                        class="imgAvatar" />
                                    <i v-else class="el-icon-plus avatar-img-icon"></i>
                                </el-upload>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-form-item label="文章内容" prop="contentMd">
                        <mavon-editor placeholder="输入文章内容..." style="height: 500px" ref=md v-model="article.contentMd"
                            @imgDel="imgDel" @change="" @imgAdd="imgAdd" />
                    </el-form-item>
                </div>
                <div class="article-right">
                    <div class="top">
                        <el-form-item label="文章标签" prop="tagList">
                            <el-select v-model="article.tagList" :multiple-limit="3" multiple placeholder="请选择标签">
                                <el-option v-for="item in tagList" :key="item.id" :label="item.name" :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="文章分类" prop="categoryId">
                            <el-select v-model="article.categoryId" placeholder="请选择分类">
                                <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="创作类型" prop="isOriginal">
                            <el-radio v-model="article.isOriginal" :label="1">原创</el-radio>
                            <el-radio v-model="article.isOriginal" :label="0">转载</el-radio>
                        </el-form-item>
                        <el-form-item v-if="article.isOriginal == 0" label="文章地址" prop="originalUrl">
                            <el-input v-model="article.originalUrl"></el-input>
                        </el-form-item>
                        <el-form-item label="关键词">
                            <el-input v-model="article.keywords"></el-input>
                        </el-form-item>
                    </div>

                    <div class="bottom">
                        <div class="btn-tips">Are you ready</div>
                        <button v-if="$store.state.userInfo" type="button" class="btnDraft" @click="submit(3)">
                            <i class="el-icon-orange"></i> 保存草稿
                        </button>
                        <button v-if="$store.state.userInfo" type="button" class="btnSubmit" @click="submit(2)">
                            <i class="el-icon-circle-check"></i> 提交审核
                        </button>
                        <span v-else class="noBtn">
                            暂无发布权限,请先<a @click="$store.state.loginFlag = true">登录</a>
                        </span>
                    </div>
                </div>
            </el-form>
        </div>
    </div>
</template>
<script>
import { upload, featchCategory, insertArticle, updateArticle, getMyArticleInfo, fetchTagList } from '@/api'
export default {
    data() {
        return {
            uploadPictureHost: process.env.VUE_APP_BASE_API + "/file/upload",
            article: {
                avatar: ""
            },
            categoryList: [],
            loading: [],
            img: process.env.VUE_APP_IMG_API,
            articleId: this.$route.query.id,
            tagList: [],
            rules: {
                title: [
                    { required: true, message: '请输入文章名称', trigger: 'blur' },
                ],
                avatar: [
                    { required: true, message: '请上传文章封面', trigger: 'change' },
                ],
                summary: [
                    { required: true, message: '请输入文章简介', trigger: 'blur' },
                ],
                tagList: [
                    { required: true, message: '请选择文章标签', trigger: 'blur' },
                ],
                categoryId: [
                    { required: true, message: '请选择文章分类', trigger: 'blur' },
                ],
                isOriginal: [
                    { required: true, message: '请选择创作类型', trigger: 'blur' },
                ],
                originalUrl: [
                    { required: true, message: '请输入转载地址', trigger: 'blur' },
                ],
                contentMd: [
                    { required: true, message: '请输入文章内容', trigger: 'blur' },
                ],
            }
        };
    },
    created() {
        if (this.articleId) {
            getMyArticleInfo(this.articleId).then(res => {
                this.article = res.data
            })
        }
        featchCategory().then(res => {
            this.categoryList = res.data
        })
        fetchTagList().then(res => {
            this.tagList = res.data
        })
    },
    methods: {
        imgAdd: function (pos, $file) {
            var formdata = new FormData();
            formdata.append('multipartFile', $file);
            upload(formdata).then(res => {
                this.$refs.md.$img2Url(pos, res.data);
            }).catch(err => {
                console.log(err)
            })
        },
        imgDel: function (filename) {
            delBatchFile(filename[0].split(this.img)[1])
        },
        submit(type) {
            this.$refs['ruleForm'].validate((valid) => {
                if (valid) {
                    if (!this.article.contentMd) {
                        this.$notify({
                            title: '失败',
                            message: '请编写文章内容！',
                            type: 'error'
                        });
                        return;
                    }
                    this.article.isPublish = type
                    this.article.content = this.$refs.md.d_render
                    if (this.article.id) {
                        updateArticle(this.article).then(res => {
                            this.$notify({
                                title: '成功',
                                message: '修改成功，请耐心等待审核',
                                type: 'success'
                            });
                            this.$router.push({ path: "/user" })
                        })
                    } else {
                        insertArticle(this.article).then(res => {
                            this.$notify({
                                title: '成功',
                                message: '提交成功，请耐心等待审核',
                                type: 'success'
                            });
                            this.$router.push({ path: "/user" })
                        })
                    }

                } else {
                    console.log('error submit!!');
                    return false;
                }
            });
        },
        handleUploadImage(event, insertImage, files) {
            const file = files[0]
            // FormData 对象
            var formData = new FormData()
            // 文件对象
            formData.append('multipartFile', file)
            upload(formData).then(res => {
                insertImage({
                    url: res.data,
                    desc: file.name,
                    width: '100%',
                    height: '300px',
                });
            })

        },
        handleUploadBefore() {
            this.$bus.$emit('showLoading');
        },
        uploadSectionFile: function (param) {
            this.files = param.file
            // FormData 对象
            var formData = new FormData()
            // 文件对象
            formData.append('multipartFile', this.files)
            upload(formData).then(res => {
                this.article.avatar = res.data
                this.$bus.$emit('hideLoading')
            }).catch(err => {
                this.$bus.$emit('hideLoading')
            })
        },
    }
};
</script>
<style lang="scss" scoped>
.article-add {
    display: flex;
    justify-content: center;

    @media screen and (max-width: 1118px) {
        padding: 0 5px;

        .containner {
            width: 100%;
            margin-top: 80px;

        }
    }

    @media screen and (min-width: 1119px) {
        .containner {
            width: 80%;
            margin-top: 80px;
        }
    }

    .containner {
        padding: 10px;
        color: var(--text-color);

        /deep/ .article-left input {
            border-top: none !important;
            border-left: none !important;
            border-right: none !important;

            &:hover {
                border-color: var(--theme-color) !important;
            }
        }

        .title {
            margin-bottom: 15px;
            position: relative;
            padding-left: 10px;

            &::before {
                content: ' ';
                width: 5px;
                height: 100%;
                background: linear-gradient(to right, #ff00ff, #c2553a);
                position: absolute;
                bottom: 0;
                left: 0;
            }
        }

        .article-left,
        .article-right .top,
        .bottom {
            border-radius: 5px;
            padding: 10px;
        }

        .article-left {
            background-color: var(--background-color);
            margin-right: 20px;
            width: 100%;
            max-width: 80%;

            .el-textarea {
                width: 92%;
            }

            /deep/ .contentInput textarea {
                resize: none;
                background-color: var(--background-color);
            }
        }

        .article-right {
            width: 35%;
            height: 100%;
            display: flex;
            flex-direction: column;

            .top,
            .bottom {
                background-color: var(--background-color);
            }

            .bottom {
                margin-top: 20px;
                text-align: center;

                .btn-tips {
                    color: #b5b5b5;
                    font-size: 0.75rem;
                    margin-bottom: 1rem;

                    &::before {
                        content: "";
                        display: inline-block;
                        background-color: #d8d8d8;
                        width: 60px;
                        height: 1px;
                        margin: 0 12px;
                        vertical-align: middle;
                    }

                    &::after {
                        content: "";
                        display: inline-block;
                        background-color: #d8d8d8;
                        width: 60px;
                        height: 1px;
                        margin: 0 12px;
                        vertical-align: middle;
                    }
                }

                .btnDraft,
                .btnSubmit {
                    padding: 10px;
                    width: 50%;
                    border: none;
                    margin: 0 auto;
                    color: #fff;
                    font-weight: 700;
                }

                .btnDraft {
                    border-top-left-radius: 5px;
                    border-bottom-left-radius: 5px;
                    background: linear-gradient(135deg, #60e464 10%, #5cb85b 100%);
                }

                .btnSubmit {
                    border-top-right-radius: 5px;
                    border-bottom-right-radius: 5px;
                    background: linear-gradient(135deg, #59c3fb 10%, #268df7 100%);
                }

                .noBtn {
                    color: var(--text-color);

                    a {
                        color: var(--theme-color);
                    }
                }
            }

        }

    }
}

/deep/ .avatar-uploader {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: url(https://img.shiyit.com/link.cur), pointer;
    position: relative;
    overflow: hidden;
    max-width: 200px;
    height: 100px;
    text-align: center;
}

/deep/ .el-upload {
    width: 100%;
    height: 100%;
}

/deep/ .avatar-uploader:hover {
    border-color: #409EFF;
}

/deep/ .el-icon-plus {
    font-size: 28px;
    color: #8c939d;
    line-height: 100px;
}

/deep/ .avatar {
    width: 178px;
    height: 178px;
    display: block;
}
</style>
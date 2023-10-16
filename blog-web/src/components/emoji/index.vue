<template>
    <div class='emoji-container'>
        <div class="emojiBox" v-if="!type">
            <span class="emoji-item" v-for="(item, index) of emojiList" :key="index" @click="chooseEmoji(item.url, 0)">
                <img :src="item.url" class="emoji" :title="item.name" />
            </span>
        </div>

        <div class="emojiBox" v-else>
            <el-upload class="avatar-uploader" :action="uploadPictureHost" :http-request="uploadSectionFile"
                :show-file-list="false" multiple>
                <i class="el-icon-plus avatar-uploader-icon"></i>
            </el-upload>
            <ul id="collectEmoji">
                <li class="collect-emoji" v-for="(item, index) in myEmojiList" :key="index"
                    @contextmenu.prevent="openMenu($event, item, index)" @click="chooseEmoji(item.url, 1)">
                    <img v-lazy="item.url" alt="">
                </li>
            </ul>
            <!-- 自定义右键功能 -->
            <ul v-show="visible" :style="{ left: left + 'px', top: top + 'px' }" class="contextmenu">
                <li @click.stop="handleStick">
                    <div class="menuitem">
                        <i class="el-icon-upload2"></i>置顶
                    </div>
                </li>
                <li @click.stop="handleDelete">
                    <div class="menuitem">
                        <i class="el-icon-delete"></i>删除
                    </div>
                </li>
            </ul>
        </div>

        <div class="btnBox">
            <el-radio-group v-model="type" @input="handleChage">
                <el-radio-button :label="0">表情</el-radio-button>
                <el-radio-button :label="1">收藏</el-radio-button>
            </el-radio-group>
        </div>
    </div>
</template>
   
<script>
import { getEmojiList, addEmoji, deleteEmoji, stickEmoji } from '@/api/emoji'
import { upload } from '@/api'
export default {
    name: '',
    data() {
        return {
            uploadPictureHost: process.env.VUE_APP_BASE_API + "/file/upload",
            emojiList: null,
            type: 0,
            myEmojiList: [],
            files: [],
            visible: false,
            left: 0,
            top: 0,
            emoji: {}
        }
    },

    created() {
        this.emojiList = require('@/assets/emoji.json');
    },
    methods: {
        handleStick() {
            stickEmoji(this.emoji.id).then(res => {
                this.$notify({
                    title: '成功',
                    message: "置顶成功",
                    type: "success"
                });
                this.visible = false
                this.getEmojiList()
            })
        },
        handleDelete() {
            this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                deleteEmoji(this.emoji.id).then(res => {
                    this.$notify({
                        title: '成功',
                        message: "删除成功",
                        type: "success"
                    });
                    this.visible = false
                    this.getEmojiList()
                })
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        handleChage(value) {
            this.visible = false
            if (value == 1) {
                this.getEmojiList()
                this.$nextTick(() => {
                    document.getElementById("collectEmoji").oncontextmenu = new Function("event.returnValue=false");
                })
            }
        },
        getEmojiList() {
            getEmojiList().then(res => {
                this.myEmojiList = res.data
            })
        },
        handleTabClick(index) {
            let btnList = document.getElementsByClassName("itemBtn")
            for (var i = 0; i < btnList.length; i++) {
                btnList[i].className = "itemBtn"
                if (i == index) {
                    btnList[i].className = "itemBtn active"
                }
            }
        },
        chooseEmoji(url, type) {
            let emoji = {
                url: url,
                type: type
            }
            this.$emit('chooseEmoji', emoji);
        },
        //右击
        openMenu(e, item) {
            var x = e.x; //这个应该是相对于整个浏览器页面的x坐标，左上角为坐标原点（0,0）
            var y = e.y; //这个应该是相对于整个浏览器页面的y坐标，左上角为坐标原点（0,0）
            this.top = y;
            this.left = x;
            this.visible = true; //显示菜单
            this.emoji = item
        },
        uploadSectionFile: function (param) {
            this.files = param.file
            // FormData 对象
            var formData = new FormData()
            // 文件对象
            formData.append('multipartFile', this.files)
            upload(formData).then(res => {
                let emoji = {
                    url: res.data
                }
                addEmoji(emoji).then(res => {
                    this.getEmojiList()
                })
            })

        },
    },
}
</script>
   
<style lang='scss' scoped>
/deep/ .avatar-uploader {
    display: inline-block;
    margin-right: 5px;

    .el-upload {
        border: 1px dashed #d9d9d9;
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;

        &:hover {
            border-color: #409EFF;
        }
    }

    .avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 75px;
        height: 75px;
        line-height: 75px;
        text-align: center;
    }

    .avatar {
        width: 75px;
        height: 75px;
        display: block;
    }
}


/deep/ .el-radio-button__inner {
    padding: 8px 15px !important;
}

.emoji-container {
    max-width: 400px;
    background-color: var(--background-color);
    border: 1px solid var(--border-line);
    padding: 5px;
    border-radius: 5px;

    .emojiBox {
        min-height: 150px;
        max-height: 150px;
        max-width: 400px;
        min-width: 400px;
        overflow-y: scroll;

        &::-webkit-scrollbar {
            display: none;
        }

        .emoji-item {
            cursor: url(https://img.shiyit.com/link.cur), pointer;
            display: inline-block;

            .emoji {
                padding: 3px;
                border-radius: 5px;
                width: 30px;
                height: 30px;

                &:hover {
                    background-color: rgb(221, 221, 221)
                }
            }
        }

        #collectEmoji {
            list-style: none;
            display: inline-block;
        }

        .collect-emoji {
            width: 75px;
            height: 75px;
            margin-right: 5px;
            display: inline-block;
            border: 1px solid var(--border-line);

            img {
                width: 100%;
                height: 100%;
            }
        }

        .contextmenu {
            margin: 0;
            background: var(--background-color);
            z-index: 50;
            list-style: none;
            position: fixed; //关键样式设置固定定位
            color: #333;
            box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);
            border-radius: 5px;

            li {
                margin: 0;
                padding: 5px;
                width: 100px;
                cursor: url(https://img.shiyit.com/link.cur), pointer;
                color: var(--text-color);
                position: relative;

                .menuitem {
                    padding: 5px;
                    font-size: 0.8rem;
                    border-radius: 5px;

                    i {
                        margin-right: 3px;
                    }

                    &:hover {
                        background: #ddaec8;
                    }

                }
            }
        }
    }

    .btnBox {
        margin-top: 10px;
        padding-top: 5px;
        border-top: 2px solid var(--border-line);
        color: var(--text-color);
    }


}
</style>
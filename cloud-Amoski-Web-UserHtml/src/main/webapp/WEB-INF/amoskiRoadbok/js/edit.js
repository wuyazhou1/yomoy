var component1 = Vue.extend({
    props: {
        option: {
            type: Object,
            default: null
        }
    },
    data(){
        return{
            textMap: {
                update: '编辑',
                create: '新增'
            },
            temp: {
               title:'英国8天轻奢深度游',//路书名称
                ridingtime:'4天3晚',//时长
            },
        }
    },
    mounted(){

    },
    methods:{
        // getData(row) {
        //     let _this = this;
        //     return new Promise((resolve, reject) => {
        //         const data = {
        //             cmd: 'hm077',
        //             code: '',
        //             type: '1',
        //             pagemap: this.pagemap,
        //             data: {
        //                 KFF004: row.KFF004
        //             }
        //         },
        //         _this.postData(data,'AmoskiActivity/memberUser/getMobileCode').then(response => {
        //             if (response.code == 0) {
        //                 _this.temp = result.data
        //             } else {
        //                 // reject('error')
        //             }
        //         })
        //     })
        // }
        createData() {
            this.$refs['dataForm'].validate((valid) => {
                if (valid) {
                    this.$emit('addList', this.temp)
                }
            })
        },
        updateData() {
            this.$refs['dataForm'].validate((valid) => {
                if (valid) {
                    this.$emit('updateList', this.temp)
                }
            })
        }
    },
    template:'<el-dialog :title="textMap[option.dialogStatus]" :visible.sync="option.visible">   <el-form ref="dataForm"\n' +
        '             :model="temp"\n' +
        '             label-position="left"\n' +
        '             label-width="130px"\n' +
        '             style="border: 1px #B3B3B3 solid;padding: 15px;background-color: white;">\n' +
        '      <el-form-item label="路书名称"\n' +
        '                    prop="type">\n' +
        '        <el-input v-model="temp.title"  class="pInput" :disabled="!option.tinymceShow" />\n' +
        '      </el-form-item>\n' +
        '      <el-form-item label="时长"\n' +
        '                    prop="type">\n' +
        '        <el-input v-model="temp.ridingtime"  class="pInput" :disabled="!option.tinymceShow"/>\n' +
        '      </el-form-item>\n' +
        '      <el-form-item label="地点"\n' +
        '                    prop="type">\n' +
        '        <el-input v-model="temp.ridingtime"  class="pInput" :disabled="!option.tinymceShow"/>\n' +
        '      </el-form-item>\n' +
        '      <el-form-item label="解答"\n' +
        '                    prop="type" class="jieda">\n' +
        '        <tinymce :height="300" v-model="temp.KFA002" v-show="option.tinymceShow"/>\n' +
        '        <div v-show="!option.tinymceShow" style="border:1px solid #dcdfe6;border-radius:4px;line-height:28px;padding-bottom:100px;padding-left:10px;width:60%;"><el-input type="textarea" v-html="temp.KFA002"  class="pInput" :disabled="!option.tinymceShow" /></div>\n' +
        '\n' +
        '      </el-form-item>\n' +
        '\n' +
        '    </el-form>\n' +
        '    <div slot="footer"\n' +
        '         class="dialog-footer">\n' +
        '      <el-button @click="option.visible = false">取消</el-button>\n' +
        '      <el-button type="primary"\n' +
        '                 @click="option.dialogStatus===\'create\'?createData(temp):updateData(temp)">确定</el-button>\n' +
        '    </div></el-dialog>'
})
Vue.component('route-edit',component1)

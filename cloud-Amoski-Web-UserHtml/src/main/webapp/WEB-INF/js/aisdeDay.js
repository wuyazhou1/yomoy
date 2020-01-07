var myComponent = {
    data(){
        return{
            subActive:'',
            activeName: 'first',
            input:'',
            TbActivityBasicsEntity:{},
            Infortitle:'',//活动标题
            characteristic:'',//活动特色
            tablistall:[//周边/长线/涉外
                {
                    img:'../img/listImg1.png',
                    txt:'内蒙草原生机嘉年华',
                    type:"0|0|0|0|0|0|0",
                    id:1
                },
                {
                    img:'../img/listImg2.png',
                    txt:'哈尔滨冰雪重机嘉年华',
                    type:"0|1|0|0|0|0|0",
                    id:3,
                },
                {
                    img:'../img/listImg1.png',
                    txt:'内蒙草原生机嘉年华',
                    type:"0|0|0|0|0|0|0",
                    id:2,
                },
                {
                    img:'../img/listImg2.png',
                    txt:'哈尔滨冰雪重机嘉年华',
                    type:"0|1|0|0|0|0|0",
                    id:4,
                },
                {
                    img:'../img/listImg1.png',
                    txt:'内蒙草原生机嘉年华222222',
                    type:"1|0|1|0|0|0|0",
                    id:5,
                },
                {
                    img:'../img/listImg2.png',
                    txt:'哈尔滨冰雪重机嘉年华2222222222',
                    type:"0|0|0|0|0|0|0",
                    id:6,
                },
                {
                    img:'../img/listImg1.png',
                    txt:'内蒙草原生机嘉年华333',
                    type:"0|1|0|0|0|0|0",
                    id:7,

                }
            ],//图片集合
            tablistimg:'',//存放最终显示的图片
            isActive:'',
            tablistli:[
                {text:'周边',datatype:"1"},
                {text:'长线',datatype:"2"},
                {text:'涉外',datatype:"3"},
            ],
            inforList:[//此处为基本信息数据
                {
                    Infortitle:'活动标题1',
                    characteristic:'活动特色1',
                },
                {
                    Infortitle:'活动标题2',
                    characteristic:'活动特色2',
                },
                {
                    Infortitle:'活动标题3',
                    characteristic:'活动特色3',
                },
            ],
        }
    },
    mounted() {
        let that = this;
        /*左侧默认显示周边路线*/
        let type = 1;//周边
        let curArry = [];
        that.tablistall.forEach(function(cur,index,arry){
            var split = cur.type.split("|");
            if(split[type-1] === "1"){
                curArry.push(cur);
                that.tablistimg = curArry;
            }
        })
        that.setElementDayMang();
    },
    methods: {
        setElementDayMang() {
            const pathname = location.pathname;
            if (pathname.includes('/dayManage/daymanag.html') || pathname.includes('/activity/index.html')) {
                /*var Activeid =  window.sessionStorage.getItem('SessionId');
                this.subActive = Activeid;

                let sessionIndex = window.sessionStorage.getItem('sessionIndex');
                let SessType = window.sessionStorage.getItem('sessionType');
                if(sessionIndex !== null || SessType !== null)
                {
                 this.imgtab(sessionIndex,SessType);
                 console.log(sessionIndex,SessType);
                }*/
            }
        },
        handleClick(tab, event) {
        },
        imgtab(index,type){//周边、长线、涉外
            let that = this;
            that.isActive = index;
            that.tablistimg = "";
            let curArry = [];
            that.tablistall.forEach(function(cur,index,arry){
                var split = cur.type.split("|");
                if(split[type-1] === "1"){
                    curArry.push(cur);
                    that.tablistimg = curArry;
                }
            })

            window.sessionStorage.setItem("sessionIndex",index);
            window.sessionStorage.setItem("sessionType",type)

        },
        tabListInfor(index,id){//切换图片信息
            var infor = this.inforList[index];
            var b1 = JSON.stringify(infor);

            this.subActive = id;
            window.sessionStorage.setItem('tabInforArry', b1);
            window.sessionStorage.setItem('SessionId',id);
            window.sessionStorage.setItem('SessionActive',this.isActive);
            // var infor = this.inforList[index];
            // var b1 = JSON.stringify(infor);
            // window.sessionStorage.setItem('tabInforArry', b1);
          //  this.$emit('addlist',infor)
        },
    },
    template:`<div class="templateleft fl">
                <el-tabs v-model="activeName" @tab-click="handleClick" class="clearfix">
                    <el-tab-pane label="  模板中心" name="first">
                        <div class="temp">
                            <el-input v-model="input" placeholder="请输入内容" class="fl searchinput"></el-input>
                            <el-button class="searchbutton el-icon-search" size="small">搜索</el-button>
                             <ul class="tagInfor clearfix">
                                <li class="tagall"><a href="#">游玩</a></li>
                                <li><a href="#">骑行</a></li>
                                <li><a href="#">庆典</a></li>
                                <li><a href="#">开业</a></li>
                                <li><a href="#">晚宴</a></li>
                                <li><a href="#">接风</a></li>
                            </ul>
                            <ul class="tablistli clearfix">
                                <li v-for="(item,index) in tablistli"  @click="imgtab(index,item.datatype)" :class="{cur:index == isActive}">{{item.text}}</li>
                            </ul>
                            <ul class="tablistImg">
                                <li v-for="(item,index) in tablistimg" @click="tabListInfor(index,item.id)" :id="item.id" :class="{active:item.id==subActive}">
                                    <a href="javascript:void(0);">
                                        <img :src=item.img>
                                        <span>{{item.txt}}</span>
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </el-tab-pane>
                    <el-tab-pane label="我的" name="second">
                        <div>sdfsdfsdfsdfsf</div>
                    </el-tab-pane>
                </el-tabs>
    
            </div>`
    }
Vue.component('my-aisde-day',myComponent)

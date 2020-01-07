function valueStringActiveEditor(){
    if(tinyMCE.activeEditor==null){
        setTimeout(function(){
            valueStringActiveEditor();
        },1000);
    }else{
        setTimeout(function(){
            let tinvMceActiveEditor=window.top.activityCreateList[window.top.activityIndex].tbActivityScheduleEntity[window.top.scheduleIndex].introduce;
            tinyMCE.activeEditor.setContent(tinvMceActiveEditor==null?"":tinvMceActiveEditor);
        },1000);
    }
}
//页面跳转需要用到的数据加载
function onlocadWindow(){
    let url = window.location.href;
    if(url.indexOf("activity/index.html")!=-1){
        window.top.activityCreateList[window.top.activityIndex].tbActivityBasicsEntity.tbActivityRefundSettings=app._data.createObj.tbActivityRefundSettings
    }
}

var myComponent2 = Vue.extend({
    props: {
        option:{
            type: Array,
            default: null
        ,
        option2:{
                type:Object,
                default: null
            }
        }
    },
    data() {
        return{
            formDisabled:false,
            IsActiveIndex:0,
            mealList:[],
            inforshow:false,
            subActive:'',
            oldindex:0,
            navList:[
                {
                    cl:'el-icon-location navicon',
                    text:'活动',
                },
                {
                    cl:'el-icon-daty navicon',
                    text:'日程',
                },
                {
                    cl:'el-icon-rote navicon',
                    text:'线路',
                },
                {
                    cl:'el-icon-accom navicon',
                    text:'餐饮',
                },
                {
                    cl:'el-icon-rest navicon',
                    text:'住宿',
                }
            ],
            dayList:[],
            navListsub:'',
            dialogTableVisible: false,
            dialogFormVisible: false,
            form: {
                name: '',
                region: '',
                date1: '',
                date2: '',
                delivery: false,
                type:[],
                resource: '',
                desc: ''
            },
            formLabelWidth: '120px',
            cityname:'',//城市名;
            cityname2:'',
            selectedOptions: [],//存放默认值
            options:options,//存放城市数据
            ActiveList:[
                {
                    text:'基本信息',
                },
                {
                    text:'活动报名',
                },
                {
                    text:'活动简介',
                },
                {
                    text:'活动详情',
                },
                {
                    text:'费用详情',
                }

            ],
        }
    },
    mounted () {
        let that = this;
        this.setElementLight();
        let bl = null;
        if (bl == null || bl == undefined) {
            bl = window.top.lookActivity;
        }
        this.formDisabled = bl;
        setTimeout(function(){
            $(".baseInfor li").on('click', function (e) {
                e.preventDefault()
                $(".baseInfor li.active").removeClass('active')
                $(this).addClass('active');
                // tabsSwiper.slideTo($(this).index())
                //window.location.href = 'module';
                // var Indexnum = $(this).index();
                // console.log(Indexnum);
                // window.location.href = '../activity/index.html#module' + Indexnum;
            })
        },500);
    },
    methods:{
        /**
         * 文件上传
         * @returns {Object}
         * @constructor
         */
        fileUploadSuccess(res){
            console.log(">>>>>>>>>>upload success res:"+res);
        },
        setElementLight() {

            //window.sessionStorage.setItem('activityCreateList',activityCreateList);
            //window.sessionStorage.setItem('activityIndex',"0");
            //window.sessionStorage.setItem('activityCreate',activityCreateList[window.sessionStorage.getItem('activityIndex')]);
            //let activityCreate = window.sessionStorage.getItem('activityCreate');
            let activityCreate = window.top.activityCreateList[window.top.activityIndex];
            for(let i=0;i<activityCreate.tbActivityScheduleEntity.length;i++){
                if(activityCreate.tbActivityScheduleEntity[i].id==null){
                    continue;
                }
                let originarry={
                    id:i+"",
                    startorigin:activityCreate.tbActivityScheduleEntity[i].placeDeparture,
                    endorigin:activityCreate.tbActivityScheduleEntity[i].destination
                }
                this.dayList.push(originarry);
            }
            /*let originarry={
                id:"0",
                startorigin:"凤凰城",
                endorigin:"衡阳"
            }
            this.dayList.push(originarry);*/

            const　pathname　=　location.pathname;
            /*一级导航跳转判断*/
            if (pathname.includes('activity/index.html')){
                this.$refs.navinfor1.style.display = 'block';
                document.getElementById('ActiveWarp0').className = 'active';
                document.getElementById('subActive0').className="active"
            } else if (pathname.includes('dayManage/daymanag.html')){
                this.$refs.navinfor2.style.display = 'block';
                document.getElementById('ActiveWarp1').className = 'active';
                if(window.top.scheduleIndexChecked!=null){
                    this.dayListTab(window.top.scheduleIndexChecked,
                        {id:window.top.scheduleIndexChecked});
                }else{
                    this.dayListTab(0,
                        {id:"0"});
                }
            }
            else if (pathname.includes('routeManage/index.html')){
                this.$refs.navinfor3.style.display = 'block';
                document.getElementById('ActiveWarp2').className = 'active';
                if(window.top.scheduleIndexChecked!=null){
                    this.dayListLine(window.top.scheduleIndexChecked,
                        {id:window.top.scheduleIndexChecked});
                }else{
                    this.dayListLine(0,
                        {id:"0"});
                }
            }
            else if (pathname.includes('eatManage/eatManage.html')){
                this.$refs.navinfor4.style.display = 'block';
                document.getElementById('ActiveWarp3').className = 'active';
            }
            else if (pathname.includes('accomManage/index.html')){
                this.$refs.navinfor4.style.display = 'block';
                document.getElementById('ActiveWarp4').className = 'active';
            }
            //二级导航跳转判断
            //活动跳转
            else if (pathname.includes('activity/baoming.html')){
                this.$refs.navinfor1.style.display = 'block';
                document.getElementById('ActiveWarp0').className = 'active';
                document.getElementById('subActive1').className="active"
            }
            else if (pathname.includes('activity/introduction.html')){
                this.$refs.navinfor1.style.display = 'block';
                document.getElementById('ActiveWarp0').className = 'active';
                document.getElementById('subActive2').className="active"
            }
            else if (pathname.includes('activity/detail.html')){
                this.$refs.navinfor1.style.display = 'block';
                document.getElementById('ActiveWarp0').className = 'active';
                document.getElementById('subActive3').className="active"
            }
            else if (pathname.includes('activity/costdetail.html')){
                this.$refs.navinfor1.style.display = 'block';
                document.getElementById('ActiveWarp0').className = 'active';
                document.getElementById('subActive4').className="active"
            }
        },
        navListsubclick(index){
            let that = this;
            onlocadWindow();
            //活动
            if(index === 0)
            {
                this.$refs.navinfor1.style.display = 'block';
                window.location.href='../activity/index.html';
            }
            //日程
            if(index === 1)
            {
                window.location.href='../dayManage/daymanag.html';
                this.$refs.navinfor2.style.display = 'block';
            }
            //线路
            if(index === 2)
            {
                window.location.href='../routeManage/index.html';
                this.$refs.navinfor3.style.display = 'block';
            }
            //餐饮
            if(index === 3)
            {
                window.location.href='../eatManage/eatManage.html';
                this.$refs.navinfor4.style.display = 'block';
            }
            //住宿
            if(index === 4)
            {
                window.location.href = '../accomManage/index.html';
                this.$refs.navinfor4.style.display = 'block';
            }
            if(index === that.oldindex)
            {
                that.inforshow = !that.inforshow;
            }else{
                that.inforshow = true;
            }
            /* that.oldindex = index;//将当前值赋给前一个值,获取到前一个索引值.
           that.subActive = index;
           let curIndex = index+1;

            //根据左右菜单判断后侧导航显示与隐藏
           let ullenth = this.$refs.navinforall.children.length;
           for(let i=1;i<ullenth+1;i++)
           {
               this.$refs["navinfor"+i].style.display = 'none';
           }
           this.$refs["navinfor"+curIndex].style.display = 'block';
*/
            // //活动二级导航菜单
            // let baseinforLi = document.getElementsByClassName('baseInfor');
            // let li = document.querySelectorAll('.baseInfor li');
            // for (let i = 0; i < li.length; i++)
            //     li[i].onclick = function () {
            //         for (let i = 0; i < li.length; i++) li[i].className = '';
            //         this.className='active'
            //     }
        },
        adddayClick(){
            this.cityname="";
            this.cityname2="";
            this.dialogFormVisible = true;
        },
        initEatData(idx){
            this.IsActiveIndex=idx;
            window.top.scheduleIndex=idx;
            app.initEatData(idx);
        },
        handleChange(value){
            let that = this;
        },
        addatelog(){//点击确定添加日程
            if(this.cityname.length==0||this.cityname2.length==0){
                // layer.msg("请选择出发地，和目的地", {
                //     icon : 5,
                //     time : 3000
                // });
                window.parent.Msg("1","请选择出发地和目的地");
                return;
            }
            let activityCreate = window.top.activityCreate;
            let index="";
            if(activityCreate.tbActivityScheduleEntity.length-1==-1){
                let createaActivity = new TbActivityScheduleEntity();
                createaActivity.id="0";
                createaActivity.basicsId=activityCreate.id;
                activityCreate.tbActivityScheduleEntity.push(createaActivity);
                if(window.top.lineIndex==null){
                    window.top.lineIndex=0;
                }
                index=0;
            }else if(activityCreate.tbActivityScheduleEntity[activityCreate.tbActivityScheduleEntity.length-1].id==null||
                activityCreate.tbActivityScheduleEntity[activityCreate.tbActivityScheduleEntity.length-1].id==""){
                activityCreate.tbActivityScheduleEntity[activityCreate.tbActivityScheduleEntity.length-1].id="#1";
                activityCreate.tbActivityScheduleEntity[activityCreate.tbActivityScheduleEntity.length-1].basicsId=activityCreate.id;
                index=activityCreate.tbActivityScheduleEntity.length-1;
            }else{
                let subIndex = activityCreate.tbActivityScheduleEntity[activityCreate.tbActivityScheduleEntity.length-1].id.toString().indexOf("#")+1;
                let idIndex=0;
                if(subIndex!=-1){
                    idIndex=parseInt(activityCreate.tbActivityScheduleEntity[activityCreate.tbActivityScheduleEntity.length-1].id.toString().substring(subIndex));
                }
                let createaActivity = new TbActivityScheduleEntity();
                createaActivity.id="#"+(idIndex+1);
                createaActivity.basicsId=activityCreate.id;
                activityCreate.tbActivityScheduleEntity.push(createaActivity);
                index=activityCreate.tbActivityScheduleEntity.length-1;
            }
            activityCreate.tbActivityScheduleEntity[index].placeDeparture=this.cityname;
            activityCreate.tbActivityScheduleEntity[index].destination=this.cityname2;
            let math = Math.random();
            let originarry={
                id:(activityCreate.tbActivityScheduleEntity.length-1)+"",
                startorigin:activityCreate.tbActivityScheduleEntity[index].placeDeparture,
                endorigin:activityCreate.tbActivityScheduleEntity[index].destination
            }
            this.dayList.push(originarry);
            let activityCreateList = window.top.activityCreateList;
            activityCreateList[window.top.activityIndex]=activityCreate;
            window.top.activityCreateList=activityCreateList;
            window.top.activityCreate=activityCreate;
            this.dialogFormVisible = false;

            this.dayListTab(index,originarry);
            this.dayListLine(index,originarry);

            this.$emit('displaynone');
        },
        deletesch(index){
            this.dayList.splice(index,1);//删除日程
            this.option.splice(index,1);//删除当前页面胡日程下数据
            window.top.activityCreateList[window.top.activityIndex].tbActivityScheduleEntity.splice(index,1);//删除4个页面日程数据
        },
        movesch(newindex,index){
            var that = this;

            function swap(arr,i,j) {

                if(!arr instanceof Array || arr[i]==undefined || arr[j] ==undefined) {
                    return;
                }
                var temp = arr[i];
                arr[i]=arr[j];
                arr[j]=temp;
                that.$forceUpdate();
            }
            swap(that.dayList,newindex,index);
            swap(that.option, newindex,index);
            swap(window.top.activityCreateList[window.top.activityIndex].tbActivityScheduleEntity,newindex,index);
            //this.dayList.splice(index,1);//删除日程
            // this.option.splice(index,1);//删除当前页面胡日程下数据
            // window.top.activityCreateList[window.top.activityIndex].tbActivityScheduleEntity.splice(index,1);//删除4个页面日程数据
        },
        SubActiveList(index){
            if(index == 0)
            {
                window.location.href = '../activity/index.html';
            }else if(index == 1)
            {
                window.location.href = '../activity/baoming.html'

            }else if(index == 2){
                window.location.href = '../activity/introduction.html'

            }else if(index == 3){
                window.location.href = '../activity/detail.html'
            }
            else if(index == 4){
                window.location.href = '../activity/costdetail.html'
            }
        },
        dayListTab(index,nodes){
            try {
                for(let i=0;i<app._data.mealall.length;i++){
                    laydate.render({
                        elem: '#startTime'+i
                    });
                    laydate.render({
                        elem: '#endTime'+i
                    });
                }
                app.$forceUpdate();
            } catch (e) {
                console.log("初始化加载为空异常捕获menuright1");
            }

            this.IsActiveIndex=index;
            window.top.scheduleIndexChecked=index;
            //window.top.scheduleIndex=nodes.id;
            try {
                window.top.activityCreateList[window.top.activityIndex].tbActivityScheduleEntity[window.top.scheduleIndex].introduce=editor.txt.html();
            } catch (e) {
                console.log("初始化加载为空异常捕获menuright2");
            }
            window.top.scheduleIndex=index;
            try {
                editor.txt.html(window.top.activityCreateList[window.top.activityIndex].tbActivityScheduleEntity[window.top.scheduleIndex].introduce?window.top.activityCreateList[window.top.activityIndex].tbActivityScheduleEntity[window.top.scheduleIndex].introduce:"");
            } catch (e) {
                console.log("初始化加载为空异常捕获menuright3");
            }
            let Option=[];
            if(window.top.activityCreateList[window.top.activityIndex].tbActivityScheduleEntity[window.top.scheduleIndex]==null){
                this.$emit('funmeallist',Option,nodes.id);
                return;
            }
            //获取时辰安排集合数据
            let tbActivityTimeHistoryEntity=window.top.activityCreateList[window.top.activityIndex].tbActivityScheduleEntity[window.top.scheduleIndex].tbActivityTimeHistoryEntity;
            //循环集合
            for(let i=0;i<tbActivityTimeHistoryEntity.length;i++){
                let startTime="起始时间";//开始时间
                let stopTime="结束时间";//结束时间
                let introduce="";//描述
                let introduceType="";//描述类型
                let id="";
                //判断当前时辰安排id是否存在
                if(tbActivityTimeHistoryEntity[i]==null){
                    continue;
                }
                if(tbActivityTimeHistoryEntity[i].id!=null&&tbActivityTimeHistoryEntity[i].id.toString().indexOf("!")!=-1){
                    continue;
                }
                if(tbActivityTimeHistoryEntity[i].id!=null && tbActivityTimeHistoryEntity[i].id.toString()!=""){
                    startTime=tbActivityTimeHistoryEntity[i].startTime;
                    stopTime=tbActivityTimeHistoryEntity[i].stopTime;
                    introduce=tbActivityTimeHistoryEntity[i].introduce;
                    introduceType=tbActivityTimeHistoryEntity[i].introduceType;
                    id=tbActivityTimeHistoryEntity[i].id;
                }else{//建立关联关系
                    tbActivityTimeHistoryEntity[i].scheduleId=window.top.activityCreateList[window.top.activityIndex].tbActivityScheduleEntity[window.top.scheduleIndex].id.toString();
                    tbActivityTimeHistoryEntity[i].basicsId=window.top.activityCreateList[window.top.activityIndex].tbActivityScheduleEntity[window.top.scheduleIndex].basicsId;
                    tbActivityTimeHistoryEntity[i].id="#1";
                    id="#1";
                    window.top.activityCreateList[window.top.activityIndex].tbActivityScheduleEntity[window.top.scheduleIndex].tbActivityTimeHistoryEntity[i]=tbActivityTimeHistoryEntity[i];
                }
                let valueIntroduceType=window.top.activityCreateList[window.top.activityIndex].tbActivityScheduleEntity[window.top.scheduleIndex].tbActivityTimeHistoryEntity[i].introduceType;
                Option.push(
                    {
                        "id":i,
                        "startTime": startTime,
                        "endTime": stopTime,
                        "mealFood": introduce,
                        "inspectTicket":tbActivityTimeHistoryEntity[i].inspectTicket,
                        comeon:{name:'加油',value:valueIntroduceType==null?'':valueIntroduceType.indexOf('加油')==-1?'':'active'},
                        rest:{name:'休息',value:valueIntroduceType==null?'':valueIntroduceType.indexOf('休息')==-1?'':'active'},
                        riding:{name:'骑行',value:valueIntroduceType==null?'':valueIntroduceType.indexOf('骑行')==-1?'':'active'},
                        breakfast:{name:'早餐',value:valueIntroduceType==null?'':valueIntroduceType.indexOf('早餐')==-1?'':'active'},
                        lunch:{name:'午餐',value:valueIntroduceType==null?'':valueIntroduceType.indexOf('午餐')==-1?'':'active'},
                        dinner:{name:'晚餐',value:valueIntroduceType==null?'':valueIntroduceType.indexOf('晚餐')==-1?'':'active'},
                        accommodation:{name:'住宿',value:valueIntroduceType==null?'':valueIntroduceType.indexOf('住宿')==-1?'':'active'},
                        play:{name:'游玩',value:valueIntroduceType==null?'':valueIntroduceType.indexOf('游玩')==-1?'':'active'},
                    })
            }
            /*if(tinyMCE.activeEditor==null){
                setTimeout(function(){
                    valueStringActiveEditor();
                },1000);
            }else{
                let tinvMceActiveEditor=window.top.activityCreateList[window.top.activityIndex].tbActivityScheduleEntity[window.top.scheduleIndex].introduce;
                tinyMCE.activeEditor.setContent(tinvMceActiveEditor==null?"":tinvMceActiveEditor);
            }*/

            //document.getElementById("mytextarea").value=window.top.activityCreateList[window.top.activityIndex].tbActivityScheduleEntity[window.top.scheduleIndex].introduce;
            this.option[index]=Option;
            this.$emit('funmeallist',Option,nodes.id);
            try {
                laydateRender();
            } catch (e) {
                console.log(e);
            }

            //this.$emit('routelist',Option);
        },dayListLine(index,nodes){
            this.IsActiveIndex=index;
            window.top.scheduleIndexChecked=index;
            //window.top.scheduleIndex=nodes.id;
            window.top.scheduleIndex=index;
            if(window.top.activityCreateList[window.top.activityIndex].
                tbActivityScheduleEntity[window.top.scheduleIndex]==null){
                let Option = [];
                this.$emit('funmeallist',Option,nodes.id);
                return;
            }
            let dayLine = window.top.activityCreateList[window.top.activityIndex].
                tbActivityScheduleEntity[window.top.scheduleIndex].tbActivityRouteEntity;
            if(dayLine==null){
                dayLine=[];
                window.top.activityCreateList[window.top.activityIndex].
                    tbActivityScheduleEntity[window.top.scheduleIndex].tbActivityRouteEntity=[];
            }
            if(dayLine.length==0||dayLine.length==1){
                window.top.activityCreateList[window.top.activityIndex].
                    tbActivityScheduleEntity[window.top.scheduleIndex].tbActivityRouteEntity=[];
                for(let i=0;i<2;i++){
                    let tbActivityRouteEntity = new TbActivityRouteEntity();
                    tbActivityRouteEntity.id="#"+i;
                    tbActivityRouteEntity.basicsId=dayLine.basicsId;
                    tbActivityRouteEntity.scheduleId=dayLine.id;
                    tbActivityRouteEntity.orderId=i;
                    window.top.activityCreateList[window.top.activityIndex].
                        tbActivityScheduleEntity[window.top.scheduleIndex].
                    tbActivityRouteEntity.push(tbActivityRouteEntity);
                }
            }
            dayLine = window.top.activityCreateList[window.top.activityIndex].tbActivityScheduleEntity[window.top.scheduleIndex].tbActivityRouteEntity;
            let Option = [];
            for(let i=0;i<dayLine.length;i++){
                let line = dayLine[i];
                let optionId;
                let ridingImg;
                let tabImg;
                if(i==0){
                    optionId="start";
                    ridingImg="../img/timeIcon.png";
                    tabImg="出发地：";
                }else if(i==dayLine.length-1){
                    optionId="end";
                    ridingImg="累计";
                    tabImg="目的地：";
                }else{
                    optionId=i+"";
                    ridingImg="../img/timeIcon.png";
                    tabImg = line.pathPointType==null||line.pathPointType==""?"../img/LocatIcon.png":
                        line.pathPointType=="景点"?"../img/pointIcon1h.png":
                            line.pathPointType=="饭店"?"../img/pointIcon2.png":
                                line.pathPointType=="加油站"?"../img/pointIcon3.png":
                                    line.pathPointType=="酒店"?"../img/pointIcon4.png":
                                        line.pathPointType=="医院"?"../img/pointIcon5.png":
                                            line.pathPointType=="物流"?"../img/pointIcon6.png":
                                                line.pathPointType=="维修站"?"../img/pointIcon7.png":
                                                    line.pathPointType=="休息区"?"../img/pointIcon8.png":"../img/LocatIcon.png";
                }
                try {
                    Option.push({
                        id:optionId,
                        ridingImg:ridingImg,
                        tabImg:tabImg,
                        km:line.distance==null?'0km':line.distance,
                        min:line.timeRequired==null?'0min':line.timeRequired,
                        pathPointName:line.pathPointName==null?'':line.pathPointName
                    });
                }catch (ex) {
                }
            }
            this.$emit('funmeallist',Option,nodes.id,index);
        },hrefAmodule1(hrefValue){
            //window.top.document.getElementById("header").style.marginTop="60px";

            setTimeout(function(){
                location.href = hrefValue;      // firstAnchor为锚点名称
                var activity = $("#activity");
                var marginTop = (parseInt(activity.css("marginTop"))+60)+"px";
                console.log("activity外高度設置"+marginTop);
                activity.css("marginTop",marginTop);
            },200);
        }
    },
    template:`<div class="navright fr" style="width:30%">
<el-dialog title="日程" :visible.sync="dialogFormVisible"  >
  <el-form :model="form">
    <el-form-item label="出发地:" :label-width="formLabelWidth">
        <el-input 
            v-model="cityname"
            @change="formDisabled?'':handleChange()"
            :separator="'-'" style="width: 70%;">
        </el-input>
      <!--<el-cascader
            :options="options"
            v-model="cityname"
            @change="formDisabled?'':handleChange()"
            :separator="'-'" style="width: 70%;">
       </el-cascader>-->
<!--       <input type="text" class="navInput">-->
    </el-form-item>
    <el-form-item label="目的地" :label-width="formLabelWidth">
        <el-input 
            v-model="cityname2"
            @change="formDisabled?'':handleChange()"
            :separator="'-'" style="width: 70%;">
        </el-input>
<!--      <el-cascader
            :options="options"
            v-model="cityname2"
            @change="formDisabled?'':handleChange()"
            :separator="'-'" style="width: 70%;">
       </el-cascader>-->
<!--       <input type="text" class="navInput">-->
    </el-form-item>
  </el-form>
  <div slot="footer" class="dialog-footer">
    <el-button @click="dialogFormVisible = false">取 消</el-button>
    <el-button type="primary" @click="addatelog">确 定</el-button>
  </div>
</el-dialog>
            <ul class="list fl" >            
                <li v-for="(item,index) in navList" :id="'ActiveWarp'+index" @click="navListsubclick(index)">
                    <i :class="item.cl"></i>
                    <span>{{item.text}}</span>
                </li>
            </ul>
            <div ref="navinforall" class="infor fl">
                <!--<ul ref="navinfor1" class="baseInfor">-->
                    <!--<li v-for="(item,index) in ActiveList" :id="'subActive'+index"><a href="javascript:void(0)">{{item.text}}</a></li>-->
                <!--</ul>-->
                 <ul ref="navinfor1" class="baseInfor">
                    <!--<li v-for="(item,index) in ActiveList" :id="'subActive'+index"><a href="javascript:void(0)">{{item.text}}</a></li>-->
                     <li id="subActive0" @click="hrefAmodule1('#Amodule1')" ><a href="#Amodule1">基本信息</a></li>
                     <li id="subActive1" @click="hrefAmodule1('#Amodule2')" ><a href="#Amodule2">活动报名</a></li>
                     <li id="subActive2" @click="hrefAmodule1('#Amodule3')" ><a href="#Amodule3">活动简介</a></li>
                     <li id="subActive3" @click="hrefAmodule1('#Amodule4')" ><a href="#Amodule4">活动详情</a></li>
                     <li id="subActive4" @click="hrefAmodule1('#Amodule5')" ><a href="#Amodule5">费用详情</a></li>
                     <li id="subActive5" @click="hrefAmodule1('#Amodule6')" ><a href="#Amodule6">活动须知</a></li>
                </ul>
                <ul ref="navinfor2" class="schedule">
                    <li class="clearfix" v-for="(item,index) in dayList" @click="dayListTab(index,item)" :class="{active:index===IsActiveIndex}">
                        <span class="s_1">D{{index+1}}</span>
                        <div style="width: 140px;margin-left: 35px;" class="top">
                            <span class="s_2" ><a :title="item.startorigin +' > '+ item.endorigin " :alt="item.startorigin +' > '+ item.endorigin ">{{item.startorigin}}</a></span>
                            <span class="s_3">></span>
                            <span class="s_2"><a :title="item.startorigin +' > '+ item.endorigin " :alt="item.startorigin +' > '+ item.endorigin ">{{item.endorigin}}</a></span>
                        </div>
                         <div class="schedule_min" style="display: none">
                               <p @click="formDisabled?'':deletesch(index)">删除</p>
                               <p>修改</p>
                               <p @click="movesch(index-1, index)">上移</p>
                               <p @click="movesch(index+1, index)">下移</p>
                        </div>
                        <span class="s_5" id="bt_rc"  :disabled="formDisabled">
                            <img src="/AmoskiWebHtmlUser/img/@1xClose@2x.png" style="width: 20px;margin-top: -2px;">
                        </span>
                           

                    </li>
                    <li class="addday" @click="formDisabled?'':adddayClick()"  >+日程</li>
                </ul>
                <ul ref="navinfor3">
                    <li class="clearfix" v-for="(item,index) in dayList" @click="dayListLine(index,item)" :class="{active:index===IsActiveIndex}">
                        
                        <span class="s_1">D{{index+1}} </span>
                     
                            <span class="s_4" ><a :title="item.startorigin +' > '+ item.endorigin " :alt="item.startorigin +' > '+ item.endorigin ">{{item.startorigin}}</a></span>
                            <span class="s_3">></span>
                            <span class="s_4" ><a :title="item.startorigin +' > '+ item.endorigin " :alt="item.startorigin +' > '+ item.endorigin ">{{item.endorigin}}</a></span>
                        <span class="s_5" @click="formDisabled?'':deletesch(index,item)" >
                            <img src="/AmoskiWebHtmlUser/img/22.-Add@2x.png" style="width: 12px;margin-top: -2px;">
                        </span>
                        
                        
                    </li>
                    <li class="addday" @click="formDisabled?'':adddayClick()" >+日程</li>
                    <!--<li>33333333333333333</li>-->
                </ul>
                <ul ref="navinfor4" class="schedule">
                    <li class="clearfix" v-for="(item,index) in dayList" @click="initEatData(index)" :class="{active:index===IsActiveIndex}" >
                        <span class="s_1">D{{index+1}}</span>
                        <div style="width: 140px;margin-left: 35px;" class="top">
                            <span class="s_2" ><a :title="item.startorigin +' > '+ item.endorigin " :alt="item.startorigin +' > '+ item.endorigin ">{{item.startorigin}}</a></span>
                            <span class="s_3">></span>
                            <span class="s_2" ><a :title="item.startorigin +' > '+ item.endorigin " :alt="item.startorigin +' > '+ item.endorigin ">{{item.endorigin}}</a></span>
                        </div>
                        <span class="s_5" @click="formDisabled?'':deletesch(index)" >
                            <img src="/AmoskiWebHtmlUser/img/22.-Add@2x.png" style="width: 12px;margin-top: -2px;">
                        </span>
                    </li>
                    <li class="addday"  @click="formDisabled?'':adddayClick()"  >+日程</li>
                </ul>
                <!--<ul ref="navinfor4" class="schedule">
                    <li>444444444444444</li>
                </ul>
                <ul ref="navinfor5" class="schedule">
                    <li>555555555555555</li>
                </ul>-->
            </div>
        </div>`
})
Vue.component('menu-right',myComponent2)
//lookDisabled();//如果是查看需要禁用表单
var myComponent = Vue.extend({
    props: {
        option: {
            type: Object,
            default: null
        }
    },
    data(){
        return{
            startTime:''
        }
    },
    created(){
        let Request = new Object();
        Request = GetRequest();
        this.startTime = Request.activityStart;
        console.log(this.startTime);
    },
    mounted(){},
    methods: {
        submit(index){
            window.sessionStorage.setItem('tabIndexCur',JSON.stringify(index));
        }
    },
    template:`<div class="swiper-container swiperCount">
            <div class="swiper-wrapper">
                <div class="swiper-slide" v-for="(item,index) in option" @click="submit(index+1)">
                    <div class="clearfix" style="width:95%;">
                        <div class="clearfix PicTxt pt15">
                            <strong class="fl txt f15">{{item.title}}</strong>
                            <span class="fr refundImg1"><img width="100%" height="100%" :src="API_ROOT+'AmoskiRiding/appRidingGuideManage/getImg?fileUrl='+item.fileNameUrl" alt="" title=""></span>
                        </div>
                        <div class="startTime">开始时间：{{startTime}}</div>
                        <div class="peoplePicTxt clearfix mt10">
                            <span class="Img fl wh24"><img src="../img/defaulthead.png" width="100%" height="100%"></span>
                           <span class="name fl f15 color38 ml10">{{item.name}}</span>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Add Pagination -->
            <div class="swiper-pagination"></div>
        </div>`
})
Vue.component('e-tick',myComponent)

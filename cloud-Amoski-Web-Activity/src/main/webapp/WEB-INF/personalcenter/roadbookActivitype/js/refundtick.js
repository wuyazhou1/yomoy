var myComponent = Vue.extend({
    // props:{
    //     type:Object,
    //     default:null,
    // },
    props: {
        option: {
            type: Object,
            default: null
        }
    },
    data(){
       return{
           swiperdata:'',
           swiperdataLit:[
               // {
               //     title:'英国一地8天轻奢深度游经典线路小团出行',
               //     img:'../img/ActivityImg1.jpg',
               //     startTime:'2019-07-11',
               //     img2:'../img/ActivityImg1.jpg',
               //     title2:'李四',
               // },
               // {
               //     title:'222222222222222222222',
               //     img:'../img/ActivityImg1.jpg',
               //     startTime:'2019-07-11',
               //     img2:'../img/ActivityImg1.jpg',
               //     title2:'李四',
               // },
               // {
               //     title:'33333333333333333333333',
               //     img:'../img/ActivityImg1.jpg',
               //     startTime:'2019-07-11',
               //     img2:'../img/ActivityImg1.jpg',
               //     title2:'李四',
               // }
           ]
       }
    },
    created(){
    },
    mounted(){
    },
    methods: {
        submit(index){
          window.sessionStorage.setItem('tabIndexCur',JSON.stringify(index));
        }
    },
    template:`<div class="swiper-container swiperCount">
            <div class="swiper-wrapper">
                <div class="swiper-slide" v-for="(item,index) in option.list" @click="submit(index+1)">
                    <div class="clearfix" style="width:95%;">
                        <div class="clearfix PicTxt mt15">
                            <strong class="fl txt f15">{{option.title}}</strong>
                            <img class="fl refundImg1" :src="option.img" alt="" title="">
                        </div>
                        <div class="startTime">开始时间：{{getFmtTime(option.activityStart)}}</div>
                        <div class="peoplePicTxt clearfix mt10">
                            <span class="Img fl"><img src="../img/defaulthead.png"></span>
                           <span class="name fl f15 color38 ml10">{{item.name}}</span>
                        </div>
                    </div>
                </div>
            </div>
            <!-- Add Pagination -->
            <div class="swiper-pagination"></div>
        </div>`
})
Vue.component('refund-tick',myComponent)

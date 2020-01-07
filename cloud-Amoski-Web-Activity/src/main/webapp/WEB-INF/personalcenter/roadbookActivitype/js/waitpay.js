var myComponent = Vue.extend({
    props:{
        // option:{
        //     type:String,
        //     defaule:null
        // }
        option:{
            type:Object,
            defaule:null
        }
    },
    data(){
        console.log(this.option);
    },
    mounted(){
    },
    method:{},
    template:`<div class="clearfix">
        <div class="picTxt ml15 mr15 mt20 clearfix">
            <span class="color6f">订单号：{{option.code}}</span>
            <p class="clearfix">
                <strong class="color38 fl Txt">{{option.title}}</strong>
                <span class="fl picTxtImg"><img src="../img/picTxtImg1.jpg" alt="" title=""></span>
            </p>
            <p>开始时间：{{getFmtTime(option.startTime)}}</p>
            <p>地点：{{option.address}}</p>
        </div>
</div>`
})
Vue.component('wait-pay',myComponent)

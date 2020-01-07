var myComponent = Vue.extend({
    props:['totalprice'],
    data(){},
    mounted(){
    },
    template:` <footer class="orderFooter pt10 pb10 mt22 clearfix">
        <span class="f15 color38 ml15 mt10 displayinlik">合计</span>
        <strong class="ml15 color59 f18 mt10 displayinlik"><i class="f12 dollar">￥</i>{{totalprice}}</strong>
        <input class="fr mr15 nextButton" type="button" value="下一步">
    </footer>`
})
Vue.component('sub-footer',myComponent)

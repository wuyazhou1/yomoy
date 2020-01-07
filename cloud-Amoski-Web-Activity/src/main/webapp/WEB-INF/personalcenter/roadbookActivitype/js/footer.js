var myComponent = Vue.extend({
    data:{},
    methods:{
        Gosign(){
            window.location.href = '../order/index.html';
        }
    },
    template:`<footer class="clearfix footer mt15 pb10">
        <input type="button" value="立即报名" @click="Gosign" class="imBaom mb16 f15">
    </footer>`
})
Vue.component('footer-bottom',myComponent)

var myComponent = Vue.extend({
    template:`<footer class="clearfix footer mt15">
        <ul>
            <li>
                <img src="../img/icon_community.png" alt="" title="">
                <span>消息</span>
            </li>
            <li>
                <img src="../img/favourite.png" alt="" title="">
                <span>收藏</span>
            </li>
        </ul>
        <input type="button" value="立即报名" class="imBaom f15">
    </footer>`
})
Vue.component('footer-bottom',myComponent)

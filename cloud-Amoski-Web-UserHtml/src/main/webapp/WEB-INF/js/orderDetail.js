var myorder = Vue.extend({
    props:{
        option:{
            type:Object,
            default:null
        }
    },
    data(){},
    mounted(){
        debugger;
    },
    template:`
        <div class="clearfix mt20">
                <h2>订单</h2>
                <div class="orderInfor" style="border: 1px solid #EFEFF6;border-radius: 5px;padding-bottom: 20px;">
                    <div style="background: #F6F7F7;width: 100%;height: 32px;padding: 5px 5px 5px 10px;margin: -1px 0px 0px 0px;border-radius: 5px 5px 0px 0px;">
                        <h3 style="float: left;">订单状态</h3>
                    </div>
                    <div class="orderDetail clearfix" style="padding: 0px 0px 0px 12px;">
                        <div class="clearfix" style="display: grid;padding-bottom: 12px;">
                            <h3 style="padding: 10px;color: #62B297;">{{option.state}}</h3>
                            <p class="fl">
                                <span class="s_1">订单编号：</span>
                                <span class="s_2" style="padding-right: 70px;">{{option.code}}</span>
                                <span class="s_1">创建时间：</span>
                                <span class="s_2">{{option.createDate}}</span>
                            </p>
                        </div>
                        <a :href="'../orderManage/orderDatail.html?id='+option.code" class="viewOrderDetail" style="background: #62B297;border: 1px solid #f6f7f7;border-radius: 3px;padding: 5px;margin-top: 10px;">
                            查看订单详情
                        </a>
                    </div>
                </div>
            </div>
    `,
})
Vue.component('order-detail',myorder)

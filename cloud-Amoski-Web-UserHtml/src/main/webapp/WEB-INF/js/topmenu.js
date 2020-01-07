var myComponent3 = Vue.extend({
    data() {
        return{
            isActiveIndex:'',
            tabmenuList:[
                {
                    text:'活动管理',
                    //router:'../activityManage/index.html',
                    router:'../activity/index.html',
                },
                {
                    text:'人员管理',
                    router:'../registrationManage/index.html',
                },
                {
                    text:'签到管理',
                    router:'../diningManage/index.html',
                },
                {
                    text:'相册管理',
                    router:'../amoskialbum/index.html',
                }
            ],
        }
    },
    mounted () {
        try {
            this.tabmenuList[1].text=personneTitle==null?tabmenuList[1].text:personneTitle;
        } catch (e) {
            console.log(e);
        }
        if(window.top.createActivityBool){
            this.tabmenuList.splice(1,1);
            this.tabmenuList.splice(1,1);
            this.tabmenuList.splice(1,1);
            this.tabmenuList.splice(1,1);
        }
        let that = this;
        that.setElementTopMenu();
    },
    methods: {
        setElementTopMenu() {
            const pathname = location.pathname;
            /*if (pathname.includes('amoskiwebuser/activity/index.html')) {
                document.getElementById('menuActive0').className = 'active';
            }*/
            if (pathname.includes('registrationManage/index.html')||
                pathname.includes('registrationManage/personnelManag.html')
            ) {
                document.getElementById('menuActive1').className = 'active';
            }else if(pathname.includes('/diningManage/index.html')){
                document.getElementById('menuActive2').className = 'active';
            }else if(pathname.includes('/amoskialbum/index.html')){
                document.getElementById('menuActive3').className = 'active';
            }else{
                document.getElementById('menuActive0').className = 'active';
            }
        },
        /*tabListTab(index,item){
            if(index == 0)
            {
                window.location.href = '../activity/index.html';
            }
            if(index == 1)
            {
                window.location.href = '../registrationManage/index.html';
            }
            if(index == 2)
            {
                window.location.href = '../diningManage/index.html';
            }
        }*/
        tabListTab(route){
            window.location.href = route;
        }
    },
    template:`<ul class="tablist">
            <li v-for="(item,index) in tabmenuList"   :id="'menuActive'+index" @click="tabListTab(item.router)"><a href="javascript:void(0)">{{item.text}}</a></li>
        </ul>`
})
Vue.component('menu-top',myComponent3)

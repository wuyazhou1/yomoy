'use strict'
//活动基础表
class TbActivityBasicsEntity{
    constructor(id,code,orgCode,currentTraffic,dailyVisits,totalVisits,type,title,nature,state,describe,notice,createName,createData,updateName,updateDate)  {
        //活动基础id
        this.id = id;
        //活动代码
        this.code = code;
        //活动标题
        this.title = title;
        //当前访问量
        this.currentTraffic=currentTraffic;
        //当天访问量
        this.dailyVisits=dailyVisits;
        //总访问量
        this.totalVisits=totalVisits;
        //活动性质
        this.nature = nature;
        //活动状态1(保存，草稿)，2发布，3已失效，4删除)
        this.state=state;
        //活动描述
        this.describe = describe;
        //活动须知
        this.notice = notice;
        //创建人
        this.createName = createName;
        //创建时间
        this.createData = createData;
        //修改人
        this.updateName = updateName;
        //修改时间
        this.updateDate = updateDate;
        //活动类型
        this.type = type;
        //活动海报图片表
        this.tbActivityBillImageEntity = [new TbActivityBillImageEntity()];
        //活动票种表
        this.tbCtivityInvoiceEntity = [new TbCtivityInvoiceEntity()];
        //活动退款设置表
        this.tbActivityRefundSettings = [new TbActivityRefundSettingsEntity(null,null,"30","0"),new TbActivityRefundSettingsEntity(null,null,"10","0"),new TbActivityRefundSettingsEntity(null,null,"3","0"),new TbActivityRefundSettingsEntity(null,null,"1","0")];
    }
}

//活动简介表
class TbActivitySynopsisEntity{
    constructor(activityNotice,pathPoint,activityIntensity,equipmentRequirements,detailsActivities,createName,createData,updateName,updateDate,id,basicsId,type,destination) {
        //活动须知
        this.activityNotice=activityNotice;
        //途径点
        this.pathPoint=pathPoint;
        //活动强度（休闲，比赛）
        this.activityIntensity=activityIntensity;
        //装备要求
        this.equipmentRequirements=equipmentRequirements;
        //活动详情
        this.detailsActivities=detailsActivities;
        //创建人
        this.createName=createName;
        //创建时间
        this.createData=createData;
        //修改人
        this.updateName=updateName;
        //修改时间
        this.updateDate=updateDate;
        //活动简介id
        this.id=id;
        //活动基础id
        this.basicsId=basicsId;
        //活动玩法类型
        this.type=type;
        //目的地（省市区）
        this.destination=destination;

    }
}

//活动报名规则表
class TbActivityOrdinationEntity{
    constructor(id,basicsId,mandatoryField,scopeRegistration,collectionPlace,collectionTime,activityStart,activityStop,activityEnd,numberLimitation,showNumber,createName,createData,updateName,updateDate) {
        //活动报名规则id
        this.id=id;
        //活动基础id
        this.basicsId=basicsId;
        //活动报名必填字段
        this.mandatoryField=mandatoryField;
        //报名范围（团队，个人）
        this.scopeRegistration=scopeRegistration;
        //集合地
        this.collectionPlace=collectionPlace;
        //集合时间
        this.collectionTime=collectionTime;
        //活动开始时间
        this.activityStart=activityStart;
        //活动结束时间
        this.activityStop=activityStop;
        //活动截止时间
        this.activityEnd=activityEnd;
        //活动报名人数限制
        this.numberLimitation=numberLimitation;
        //人数显示
        this.showNumber=showNumber;
        //创建人
        this.createName=createName;
        //创建时间
        this.createData=createData;
        //修改人
        this.updateName=updateName;
        //修改时间
        this.updateDate=updateDate;

    }
}


//活动海报图片表
class TbActivityBillImageEntity{
    constructor(id,basicsId,projectUrl,filePathUrl,fileNameUrl) {
        //活动报名规则id
        this.id=id;
        //活动基础id
        this.basicsId=basicsId;
        //活动项目路径
        this.projectUrl=projectUrl;//     /userWeb
        //活动文件方法路径
        this.filePathUrl=filePathUrl;//    /startNetty
        //活动文件路径
        this.fileNameUrl=fileNameUrl;//    /a.img

    }
}

//活动票种表
class TbCtivityInvoiceEntity{
    constructor(id,basicsId,nameInvoice,ticketPrice,numberCount,describe,createName,createData,updateName,updateDate) {
        //发票id
        this.id=id;
        //活动基础id
        this.basicsId=basicsId;
        //发票名称
        this.nameInvoice=nameInvoice;
        //票价
        this.ticketPrice=ticketPrice;
        //数量
        this.numberCount=numberCount;
        //描述
        this.describe=describe;
        //创建人
        this.createName=createName;
        //创建时间
        this.createData=createData;
        //修改人
        this.updateName=updateName;
        //修改时间
        this.updateDate=updateDate;

    }
}


//活动日程安排表
class TbActivityScheduleEntity{
    constructor(id,basicsId,orgCode,placeDeparture,destination,introduce,daysStatistics,createName,createData,updateName,updateDate) {
        //日程id
        this.id=id;
        //活动基础id
        this.basicsId=basicsId;
        //当前天数
        this.daysStatistics=daysStatistics;
        //出发地
        this.placeDeparture=placeDeparture;
        //目的地
        this.destination=destination;
        //介绍
        this.introduce=introduce;
        //创建人
        this.createName=createName;
        //创建时间
        this.createData=createData;
        //修改人
        this.updateName=updateName;
        //修改时间
        this.updateDate=updateDate;
        //活动时辰安排表
        this.tbActivityTimeHistoryEntity=[new TbActivityTimeHistoryEntity()];
        //活动线路安排表
        this.tbActivityRouteEntity=[new TbActivityRouteEntity()];
        //活动酒店表
        this.tbActivityHotelEntity=new TbActivityHotelEntity();
        //活动酒店餐厅表
        this.tbHotelRestaurantEntity=[new TbHotelRestaurantEntity(),new TbHotelRestaurantEntity(),new TbHotelRestaurantEntity()];

    }
}



//活动时辰安排表
class TbActivityTimeHistoryEntity{
    constructor(inspectTicket,id,basicsId,scheduleId,startTime,stopTime,introduceType,introduce,createName,createData,updateName,updateDate) {
        //是否验票
        this.inspectTicket=inspectTicket;
        //活动时辰安排id
        this.id=id;
        //活动基础id
        this.basicsId=basicsId;
        //活动日程安排id
        this.scheduleId=scheduleId;
        //开始时间
        this.startTime=startTime;
        //结束时间
        this.stopTime=stopTime;
        //描述类型
        this.introduceType=introduceType;
        //描述
        this.introduce=introduce;
        //创建人
        this.createName=createName;
        //创建时间
        this.createData=createData;
        //修改人
        this.updateName=updateName;
        //修改时间
        this.updateDate=updateDate;

    }
}




//活动线路安排表
class TbActivityRouteEntity{
    constructor(address,id,basicsId,scheduleId,code,orderId,orgCode,type,pathPointType,yAxis,xAxis,pathPointName,describe,distance,timeRequired,createName,createData,updateName,updateDate) {
        //途径点地址详情
        this.address=address;
        //活动时辰安排id
        this.id=id;
        //活动基础id
        this.basicsId=basicsId;
        //活动日程安排id
        this.scheduleId=scheduleId;
        //活动线路代码
        this.code=code;
        //线路排序编码
        this.orderId=orderId;
        //null
        this.orgCode=orgCode;
        //null
        this.type=type;
        //途径点类型
        this.pathPointType=pathPointType;
        //途径点Y轴
        this.yAxis=yAxis;
        //途径点X轴
        this.xAxis=xAxis;
        //途径点名称
        this.pathPointName=pathPointName;
        //途径点描述
        this.describe=describe;
        //途径距离
        this.distance=distance;
        //所需时间（分钟）
        this.timeRequired=timeRequired;
        //创建人
        this.createName=createName;
        //创建时间
        this.createData=createData;
        //修改人
        this.updateName=updateName;
        //修改时间
        this.updateDate=updateDate;
        //活动线路图片表
        this.tbActivityRouteImageEntity=[new TbActivityRouteImageEntity(),
            new TbActivityRouteImageEntity(),
            new TbActivityRouteImageEntity(),
            new TbActivityRouteImageEntity()];
    }
}



//活动线路图片表
class TbActivityRouteImageEntity{
    constructor(id,basicsId,routeId,projectUrl,filePathUrl,fileNameUrl) {
        //活动线路图片id
        this.id=id;
        //活动基础id
        this.basicsId=basicsId;
        //线路ID
        this.routeId=routeId;
        //活动项目路径
        this.projectUrl=projectUrl;
        //活动文件方法路径
        this.filePathUrl=filePathUrl;
        //活动文件路径
        this.fileNameUrl=fileNameUrl;

    }
}
//活动酒店表
class TbActivityHotelEntity{
    constructor(starClass,hotelMatching,id,basicsId,scheduleId,yAxis,xAxis,hotelName,locationAddrName,hotelPolicy,contacts,contactsTel,createName,createData,updateName,updateDate) {
        //活动时辰安排id
        this.id=id;
        //活动基础id
        this.basicsId=basicsId;
        //活动日程安排id
        this.scheduleId=scheduleId;
        //经纬度Y轴
        this.yAxis=yAxis;
        //经纬度X轴
        this.xAxis=xAxis;
        //酒店配套
        this.hotelMatching=hotelMatching;
        //酒店名称
        this.hotelName=hotelName;
        //定位位置名称
        this.locationAddrName=locationAddrName;
        //酒店星级
        this.starClass=starClass;
        //酒店政策
        this.hotelPolicy=hotelPolicy;
        //联系人
        this.contacts=contacts;
        //联系电话
        this.contactsTel=contactsTel;
        //创建人
        this.createName=createName;
        //创建时间
        this.createData=createData;
        //修改人
        this.updateName=updateName;
        //修改时间
        this.updateDate=updateDate;
        //活动酒店餐厅图片表
        this.tbHotelRestaurantImageEntity=[new TbHotelRestaurantImageEntity()];
        //酒店房型表
        this.tbActivityHotelRoomEntity=[new TbActivityHotelRoomEntity()];
    }
}


//活动酒店餐厅图片表
class TbHotelRestaurantImageEntity{
    constructor(id,type,relationId,basicsId,projectUrl,filePathUrl,fileNameUrl) {
        //活动酒店餐厅图片id
        this.id=id;
        //关联类型（1[酒店]，2[餐厅]）
        this.type=type;
        //关联id
        this.relationId=relationId;
        //活动基础id
        this.basicsId=basicsId;
        //活动项目路径
        this.projectUrl=projectUrl;
        //活动文件方法路径
        this.filePathUrl=filePathUrl;
        //活动文件路径
        this.fileNameUrl=fileNameUrl;

    }
}



//酒店房型表
class TbActivityHotelRoomEntity{
    constructor(id,hotelId,hotelType,numberCount,price,facilities,createName,createData,updateName,updateDate) {
        //床型id
        this.id=id;
        //酒店id
        this.hotelId=hotelId;
        //型号(1[大床房]2,[中等床房],3[小床房])
        this.hotelType=hotelType;
        //数量
        this.numberCount=numberCount;
        //价格
        this.price=price;
        //设施
        this.facilities=facilities;
        //创建人
        this.createName=createName;
        //创建时间
        this.createData=createData;
        //修改人
        this.updateName=updateName;
        //修改时间
        this.updateDate=updateDate;
        //活动房型图片
        this.tbActivityRoomImageEntity=[new TbActivityRoomImageEntity()];
    }
}



//活动房型图片
class TbActivityRoomImageEntity{
    constructor(id,roomId,relationId,projectUrl,filePathUrl,fileNameUrl) {
        //活动房型图片id
        this.id=id;
        //房型id
        this.roomId=roomId;
        //null
        this.relationId=relationId;
        //活动项目路径
        this.projectUrl=projectUrl;
        //活动文件方法路径
        this.filePathUrl=filePathUrl;
        //活动文件路径
        this.fileNameUrl=fileNameUrl;

    }
}




//活动酒店餐厅表
class TbHotelRestaurantEntity{
    constructor(contactsTel,menu,cuisine,createName,createData,updateName,updateDate,id,basicsId,scheduleId,orderId,restaurantName,yAxis,xAxis,locationAddrName,characteristic,price,tableNumber,sum,contacts) {
        //电话
        this.contactsTel=contactsTel;
        //菜单
        this.menu=menu;
        //菜系
        this.cuisine=cuisine;
        //创建人
        this.createName=createName;
        //创建时间
        this.createData=createData;
        //修改人
        this.updateName=updateName;
        //修改时间
        this.updateDate=updateDate;
        //餐厅id
        this.id=id;
        //活动基础id
        this.basicsId=basicsId;
        //活动日程id
        this.scheduleId=scheduleId;
        //活动排序id（1早餐，2中餐，3晚餐）
        this.orderId=orderId;
        //餐厅名称
        this.restaurantName=restaurantName;
        //y轴
        this.yAxis=yAxis;
        //x轴
        this.xAxis=xAxis;
        //定位位置名称
        this.locationAddrName=locationAddrName;
        //特色
        this.characteristic=characteristic;
        //金额
        this.price=price;
        //桌数
        this.tableNumber=tableNumber;
        //小计
        this.sum=sum;
        //联系人
        this.contacts=contacts;
        //活动酒店餐厅图片表
        this.tbHotelRestaurantImageEntity=[new TbHotelRestaurantImageEntity()];

    }
}









//活动报名人员表
class TbActivitySignUpEntity{
    constructor(id,basicsId,headPortraitUrl,orgCode,name,sex,region,identityNumber,tel,club,orderCode,invoiceId,identitys,motorcycleLicense,vehicleLicense,verificationCode,verificationDate,invoiceName,emergencyContact,emergencyTel,drivingExperience,createName,createData,updateName,updateDate) {
        //报名id
        this.id=id;
        //活动基础id
        this.basicsId=basicsId;
        //头像路径
        this.headPortraitUrl=headPortraitUrl;
        //null
        this.orgCode=orgCode;
        //姓名TbActivitySignUpEntity
        this.name=name;
        //性别
        this.sex=sex;
        //住址区域
        this.region=region;
        //身份证号码
        this.identityNumber=identityNumber;
        //手机号码
        this.tel=tel;
        //俱乐部
        this.club=club;
        //订单编号
        this.orderCode=orderCode;
        //票号id
        this.invoiceId=invoiceId;
        //身份
        this.identitys=identitys;
        //摩托牌照
        this.motorcycleLicense=motorcycleLicense;
        //汽车牌照
        this.vehicleLicense=vehicleLicense;
        //验票数字码
        this.verificationCode=verificationCode;
        //验票时间
        this.verificationDate=verificationDate;
        //票种名称
        this.invoiceName=invoiceName;
        //紧急联系人
        this.emergencyContact=emergencyContact;
        //紧急联系电话
        this.emergencyTel=emergencyTel;
        //驾驶经验
        this.drivingExperience=drivingExperience;
        //创建人
        this.createName=createName;
        //创建时间
        this.createData=createData;
        //修改人
        this.updateName=updateName;
        //修改时间
        this.updateDate=updateDate;
    }
}


//活动报名人员接送
class TbPeopleReceiveSendEntity{
    constructor(arriveDate,id,peopleId,orgCode,type,flightDate,flightNumber,placeDeparture,destination,describe,meetGive) {
        //到达时间
        this.arriveDate=arriveDate;
        //接送机id
        this.id=id;
        //报名人员id
        this.peopleId=peopleId;
        //null
        this.orgCode=orgCode;
        //接送类型（1接，2送）
        this.type=type;
        //航班时间
        this.flightDate=flightDate;
        //航班号
        this.flightNumber=flightNumber;
        //出发地
        this.placeDeparture=placeDeparture;
        //目的地
        this.destination=destination;
        //机场及航站楼
        this.describe=describe;
        //是否接送
        this.meetGive=meetGive;
    }
}


//人员住宿表
class TbPeoplePutUpEntity{
    constructor(id,peopleId,daysOfTravel,hotelName,roomType,roomNumber){
        //住宿id
        this.id=id;
        //人员id
        this.peopleId=peopleId;
        //行程天数
        this.daysOfTravel=daysOfTravel;
        //酒店名称
        this.hotelName=hotelName;
        //房型
        this.roomType=roomType;
        //房号
        this.roomNumber=roomNumber;
    }
}


//活动订单表
class TbActivityOrderEntity{
    constructor(id,code,invoiceId,invoiceName,orderCartId,signUpId,state,primitiveMoneySum,sumCount,createName,createData,updateName,updateDate) {
        //订单id
        this.id=id;
        //订单代码
        this.code=code;
        //票种id
        this.invoiceId=invoiceId;
        //票种名称
        this.invoiceName=invoiceName;
        //购物车id
        this.orderCartId=orderCartId;
        //报名人id
        this.signUpId=signUpId;
        //1以提交，2未付款，3已付款，4审核退款，5以退款
        this.state=state;
        //订单金额
        this.primitiveMoneySum=primitiveMoneySum;
        //订单数量
        this.sumCount=sumCount;
        //创建人
        this.createName=createName;
        //创建时间
        this.createData=createData;
        //修改人
        this.updateName=updateName;
        //修改时间
        this.updateDate=updateDate;

    }
}



//活动订单详情表
class TbActivityOrderDetailsEntity{
    constructor(id,code,orderId,state,primitiveMoney,primitiveMoneySum,sumCount,applyDate,createName,createDate,updateName,updateDate,invoiceId,invoiceName,signUpId) {
        //订单id
        this.id=id;
        //订单代码
        this.code=code;
        //订单id
        this.orderId=orderId;
        //1已取消，2未付款，3已付款，4审核退款，5以退款,6.已完成
        this.state=state;
        //订单金额
        this.primitiveMoney=primitiveMoney;
        //订单总金额
        this.primitiveMoneySum=primitiveMoneySum;
        //订单数量
        this.sumCount=sumCount;
        //使用时间
        this.applyDate=applyDate;
        //创建人
        this.createName=createName;
        //创建时间
        this.createDate=createDate;
        //修改人
        this.updateName=updateName;
        //修改时间
        this.updateDate=updateDate;
        //票种id
        this.invoiceId=invoiceId;
        //票种名称
        this.invoiceName=invoiceName;
        //报名人id
        this.signUpId=signUpId;

    }
}


//活动退款设置
class TbActivityRefundSettingsEntity{
    constructor(id,basicsId,closingDay,serviceCharge) {
        //null
        this.id=id;
        //活动基础code
        this.basicsId=basicsId;
        //截止天数
        this.closingDay=closingDay;
        //收取服务费用
        this.serviceCharge=serviceCharge;

    }
}
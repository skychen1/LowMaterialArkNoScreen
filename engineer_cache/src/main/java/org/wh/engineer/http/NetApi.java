package org.wh.engineer.http;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/13 15:28
 * 描述:        TODO:
 * 包名:        high.rivamed.myapplication.http
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public interface NetApi {

    //版本检测和下载
    String URL_GET_VER = "/bdm/base/systemVersion/rmApi/getApkInfo";//获取版本信息    新
    String URL_LOGO = "/bdm/base/hospitalFile/rmApi/findByHospitalId";//LOGO    新
    //工程模式
    String URL_TEST_FINDDEVICE = "/hvc-dept/base/deviceDict/rmApi/findDevice";//根据部件类型查名字   新
    String URL_TEST_SNQUERY = "/hvc-dept/base/thing/rmApi/findEquipmentInfo";//SN码查询   新
    String URL_TEST_REGISTE = "/hvc-dept/base/thing/rmApi/save";//预注册   新
    String URL_TEST_ACTIVE = "/hvc-dept/base/thing/rmApi/active";//设备激活    新
    String URL_TEST_FIND_BRANCH = "/bdm/base/hospitalBranch/rmApi/findBranchs";//查询院区信息   新
    String URL_TEST_FIND_DEPT = "/bdm/base/dept/rmApi/findDepts";//根据院区编码查询科室信息   新
    String URL_TEST_FIND_BYDEPT = "/bdm/base/storehouse/rmApi/findStoreHouseDtoByDeptId";//根据科室查询库房情况    新

    String URL_TEST_FIND_OPERROOMS = "/bdm/base/operationRoom/rmApi/findOperRoomsByDept";//根据科室查询手术室信息   新

    String URL_HOME_BOXSIZE = "/hvc-dept/base/device/rmApi/getCabinetCount";//获取柜子信息   新

    //用户
    String URL_USER_LOGIN = "/bdm/login/validateLogin";//登录    新
    String URL_USER_LOGININFO = "/bdm/base/userFeatureInfo/findUserFeatureBind";//登录设置信息    新
    String URL_USER_FINDUSERINFO = "/bdm/base/user/findUserInfo";//查询个人信息    新`
    String URL_USER_UNNET_LOGIN = "/bdm/login/validateLoginPasswordOffline";//离线登录换Token    新
    String URL_REFRESH_TOKEN = "/bdm/login/refreshToken";//token刷新换取   新
    String URL_USER_REGISTER_FINGER = "/bdm/base/userFeatureInfo/registerFinger";//绑定指纹    新
    String URL_USER_RESET_PASSWORD = "/bdm/base/account/resetPassword";//重置密码    新
    String URL_USER_REGISTERWAIDAI = "/bdm/base/userFeatureInfo/registerWaidai";//腕带绑定    新
    String URL_USER_UNREGISTERWAIDAI = "/bdm/base/userFeatureInfo/untieWaidai";//腕带解绑    新
    String URL_USER_EMERGENCY_PWD = "/bdm/base/account/bindEmergencyPwd";//紧急登录密码修改    新

    //耗材流水
    String URL_HOME_RUNWATE = "/hvc-dept/sth/inventoryJournal/findInventoryJournal";//查询耗材流水    新

    //库存状态
    String URL_STOCKSTATUS_TOP = "/hvc-dept/sth/inventory/rmApi/expireStatistics";//库存监控 耗材效期监控     新
    String URL_STOCKSTATUS_DETAILS = "/hvc-dept/sth/inventory/rmApi/inventoryStatusAndDetails";//库存详情    新
    String URL_STOCKUNCON_RIGHT = "/hvc-dept/sth/inventory/rmApi/findNoConfirm";//未确认耗材
    String URL_STOCK_DETAIL = "/hvc-dept/sth/inventory/rmApi/detail";//查询单个耗材    新
    String URL_STOCKSTATUS_MONITORING = "/hvc-dept/sth/inventory/rmApi/inventoryControl";//库存监控    新


    //耗材操作
    String URL_OPERATE_QUERY = "/hvc-dept/sth/inventory/findInventory";//查询扫描出来的耗材和数据库耗材情况    新
    String URL_OPERATE_QUERY_LYTH = "/hvc-dept/sth/inventory/findTwooutOrBackInventory";//查询扫描出来的耗材和数据库耗材情况    领用/退回新加接口查询epc信息
    String URL_OPERATE_INOUTBOX_YES = "/hvc-dept/sth/inventory/operateInventory";//查询后入柜出柜的确认操作     新
    String URL_OPERATE_INOUTBOX_YES_LYTH = "/hvc-dept/sth/inventory/operateTwoOutOrBackInventory";//查询后入柜出柜的确认操作     领用/退回新加接口确认操作
    String URL_OPERATE_DB_YES = "/hvc-dept/rmApi/store/tCstBaseStorehouse/findAllStorehouse";//查询非本科室的库房Dialog    调拨查  暂停使用
    String URL_OPERATE_STORAGECST_COUNT = "/hvc-dept/sth/inventory/storageCstCount";//查入库统计的数据
    String URL_OPERATE_STORAGECST_ORDER  = "/hvc-dept/order/order/findInStockOrder";//查入库单的数据

    //快速开柜
    String URL_QUERY_ALL_YES = "/hvc-dept/sth/inventory/fastOperateTCstInventories";//快速开柜确认    新
    String URL_QUERY_OUTANDIN = "/hvc-dept/sth/inventory/outAndInCupboardList";//快速开柜数据查询    新

    //  实时盘点
    String URL_TIMELY_ONE = "/hvc-dept/sth/inventory/findStocktaking";//库存盘点     新
    String URL_TIMELY_PROFIT = "/hvc-dept/sth/inventory/inventoryProfit";//库存盘盈情况    新
    String URL_TIMELY_LOSSES = "/hvc-dept/sth/inventory/inventoryLosses";//库存盘亏情况    新
    String URL_TIMELY_DETAIL = "/hvc-dept/sth/inventory/inventoryCompareDetail";//耗材对比详情    新
    String URL_TIMELY_PUTSAVEPADPD = "/hvc-dept/order/checkOrder/saveCheckOrder";//盘点信息提交   新

    //患者绑定
    String URL_PATIENTS_FIND = "/bdm/patient/surgeryInfo/findAllOperationSchedulePatients";//查询本科室下24小时的手术预约患者（包含临时患者）    新
    String URL_FIND_IN_PATIENT_PAGE = "/bdm/patient/surgeryInfo/findInPatientPage";//查询所有在院患者信息    新
    String URL_FIND_TEMP_PATIENTS = "/hvc-dept/patient/patientIndex/findPickTempPatients";//查询所有的未绑定临时患者     新
    String URL_TEMP_PATIENT_CONN_PATIENT = "/hvc-dept/patient/patientIndex/tempPatientLinkInPatient";//临时患者与在院患者进行关联    新

    //查询配置项
//    String URL_THING_CONFIG_FIND = "/bdm/dict/config/rmApi/findConfig";//查询所有的配置项   新
    String URL_THING_CONFIG_FIND = "/bdm/dict/config/rmApi/findDeviceAllConfig";//查询所有的配置项   新

    //使用记录
    String URL_FIND_PATIENT = "/hvc-dept/sth/inventoryJournal/padFindPatientUseRecord";//患者的列表  新
    String URL_FIND_EPC_DETAILS = "/hvc-dept/sth/inventoryJournal/padFindCstDetailByPatient";//使用耗材的详情    新

    //医嘱请领单领用
    String URL_RECEIVEORDER_LISTALL = "/hvc-dept/order/order/listAll";//顶部医嘱单列表    新
    String URL_RECEIVEORDER_FINDBYORDERID        = "/hvc-dept/order/orderDetail/findByOrderId";//根据医嘱单ID查询单柜耗材的库存    新
    String URL_RECEIVEORDER_CONTAINORDERCST      = "/hvc-dept/order/orderDetail/containOrderCst";//根据EPC获取耗材判断是否属于请领单的耗材   新
    String URL_RECEIVEORDER_TWOOUTBYRECEIVEORDER = "/hvc-dept/order/orderDetail/twoOutByReceiveOrder";//确认领用耗材    新
    String URL_RECEIVEORDER_SAVERECEIVEORDERMSG  = "/hvc-dept/order/order/saveReceiveOrderMsg";//产生医嘱单的消息    新

    //套组领用
    String URL_CSTPLAN_LISTFORPAD = "/hvc-dept/sth/suite/getSuites";//套组列表-（本科室的套组列表）   新
    String URL_CSTPLAN_FINDCSTLIST = "/hvc-dept/sth/suiteDetail/getCstList";//查询单个套组的耗材详情    新
    String URL_CSTPLAN_FINDCSTANDCOMPARETOCSTPLAN = "/hvc-dept/sth/suiteDetail/findCstAndCompareToSuite";//根据EPC查询耗材对比   新
    String URL_CSTPLAN_OPERATETCSTINVENTORY= "/hvc-dept/sth/inventory/receiveCstBySuite";//套组领用    新


    //消息
    String URL_FIND_MESSAGE_BY_ACCOUNTID = "/hvc-dept/hvcMessage/findMessageByAccountId";//查询待办任务列表    新
    String URL_DELETE_MESSAGE_BY_ID = "/hvc-dept/hvcMessage/deleteMessageById";//删除待办任务

    //本地存储
    String URL_UNENT_GET_ALLCST = "/hvc-dept/sth/inventory/rmApi/getAllCst";//获取设备中所有的耗材
    String URL_UNENT_GET_LIST_ACCOUNT = "/bdm/base/account/rmApi/listAccount";//获取离线账户信息
    String URL_UNENT_GET_FIND_OPERATIONROOM = "/hvc-dept/base/thing/rmApi/findByThingId";//获取离线手术间
    String URL_UNENT_CST_OFFLINE = "/hvc-dept/sth/inventory/rmApi/forceOperateInventoryCst";//离线领用和强开

    String URL_ROBOT = "/hvc-dept/clrApi/base/clrRobot/callRobot";//召唤机器人


    String URL_CONNECT = "/bdm/base/account/rmApi/connect";//测试连接提示


    //人脸识别初始化:获取所有人脸
    String URL_FACE_GET_ALL="/bdm/login/faceRecognition";
    //人脸绑定
    String URL_FACE_BIND="/bdm/base/userFeatureInfo/boundFace";
    //人脸识别后userId登录
//    String URL_USER_LOGIN_BY_USER_ID = "/bdm/login/validateLoginUserId";

    //异常处理
    String URL_EXCEPTION_LEFT = "/hvc-dept/sth/inventoryUnNormal/rmApi/unNormalHandPage";//异常处理分页
    String URL_EXCEPTION_RIGHT = "/hvc-dept/sth/unnormalJournal/unNormalHandRecordPage";//处理记录分页
    String URL_EXCEPTION_OOERATEUNKNOW = "/hvc-dept/sth/inventoryUnNormal/operateUnKnow";//异常处理关联操作人
    String URL_EXCEPTION_RELEVANCE = "/hvc-dept/sth/inventoryUnNormal/operateUnNormal";//出柜关联，绑定患者，连续移除
    String URL_EXCEPTION_OPERATOR = "/bdm/base/account/listOperator";//查询要绑定的操作人信息


    String URL_LOGIN_CSTMSG = "/hvc-dept/sth/inventory/rmApi/findWaitInStoreCst";//查询待入库信息信息

    String URL_RECORDVIDEO = "/hvc-dept/video/video/rmApi/startRecordVideo";//开启录像
    String URL_STOPVIDEO   = "/hvc-dept/video/video/rmApi/stopRecordVideo";//结束录像

    String URL_ISORDER_STATUS   = "/hvc-dept/order/order/updateInStockOrderStatus";//整单入库确认是否改变单号状态

    String URL_FLOORLIST   = "/consumables/inventory/findInventoryFloorList";//库存预警低于下限查询
}

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

    /**
     * 低值无屏
     * */
    String URL_TEST_FINDDEVICE = "/lvc-dept/base/deviceDict/rmApi/findDevice";//根据部件类型查名字   新
    String URL_TEST_SNQUERY = "/lvc-dept/rmApi/base/thing/findEquipmentInfo";//数据恢复
    String URL_TEST_REGISTE = "/lvc-dept/rmApi/base/thing/preRegistration";//预注册   新
    String URL_TEST_ACTIVE = "/lvc-dept/rmApi/base/thing/active";//设备激活    新
    String URL_HOME_BOXSIZE = "/lvc-dept/rmApi/base/device/getCabinetCount";//获取柜子信息   新

    /**
     *高值
     */
   /* String URL_TEST_FINDDEVICE = "/hvc-dept/base/deviceDict/rmApi/findDevice";//根据部件类型查名字   新
    String URL_TEST_SNQUERY = "/hvc-dept/base/thing/rmApi/findEquipmentInfo";//SN码查询   新
    String URL_TEST_REGISTE = "/hvc-dept/base/thing/rmApi/save";//预注册   新
    String URL_TEST_ACTIVE = "/hvc-dept/base/thing/rmApi/active";//设备激活    新
    String URL_HOME_BOXSIZE = "/hvc-dept/base/device/rmApi/getCabinetCount";//获取柜子信息   新*/


    /**
     * 院区信息
     * */
    String URL_TEST_FIND_BRANCH = "/bdm/base/hospitalBranch/rmApi/findBranchs";//查询院区信息   新
    String URL_TEST_FIND_DEPT = "/bdm/base/dept/rmApi/findDepts";//根据院区编码查询科室信息   新
    String URL_TEST_FIND_BYDEPT = "/bdm/base/storehouse/rmApi/findStoreHouseDtoByDeptId";//根据科室查询库房情况    新
    String URL_TEST_FIND_OPERROOMS = "/bdm/base/operationRoom/rmApi/findOperRoomsByDept";//根据科室查询手术室信息   新

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


    String URL_THING_CONFIG_FIND = "/bdm/dict/config/rmApi/findDeviceAllConfig";//查询所有的配置项   新

    //本地存储
    String URL_UNENT_GET_ALLCST = "/hvc-dept/sth/inventory/rmApi/getAllCst";//获取设备中所有的耗材
    String URL_UNENT_GET_LIST_ACCOUNT = "/bdm/base/account/rmApi/listAccount";//获取离线账户信息
    String URL_UNENT_GET_FIND_OPERATIONROOM = "/hvc-dept/base/thing/rmApi/findByThingId";//获取离线手术间


    String URL_CONNECT = "/bdm/base/account/rmApi/connect";//测试连接提示

}

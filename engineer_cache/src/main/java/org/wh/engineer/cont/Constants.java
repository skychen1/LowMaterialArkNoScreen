package org.wh.engineer.cont;

/**
 * 项目名称:    Rivamed_High_2.5
 * 创建者:      DanMing
 * 创建时间:    2018/6/22 14:22
 * 描述:        系统常量
 * 包名:        high.rivamed.myapplication.cont
 * <p>
 * 更新者：     $$Author$$
 * 更新时间：   $$Date$$
 * 更新描述：   ${TODO}
 */

public class Constants {

   public static final long   DEFAULT_MILLISECONDS        = 16000;      //默认的超时时间
   public static final long   DEFAULT_CONNECTMILLISECONDS = 30000;      //默认的超时时间
   public static final long   FINISH_TIME                 = 1500;      //退出操作页面的时间
   public static final String TYPE_TIMELY                 = "timely";
   public static final String STYPE_STOCK_LEFT            = "stock_left";
   public static final String STYPE_STOCK_MIDDLE          = "stock_middle";
   public static final String STYPE_STOCK_RIGHT           = "stock_right";
   public static final String STYPE_DIALOG                = "dialog";//dialog样式的recyclerview
   public static final String STYPE_DIALOG2               = "dialog2";//dialog样式的recyclerview
   public static final String STYPE_IN                    = "in";//快速开柜拿入
   public static final String STYPE_OUT                   = "out";//快速开柜拿出
   public static final String STYPE_BING                  = "bing";//快速开柜拿出绑定患者的样式
   public static final String STYPE_FORM                  = "form";//请领单界面
   public static final String STYPE_FORM_CONF             = "form_ly";//请领单item详情界面
   public static final String STYPE_MEAL_NOBING           = "meal_nobing";//套餐未绑定
   public static final String STYPE_TIMELY_FIVE_DETAILS   = "timely_details";//实时盘点item详情
   public static final String STYPE_STOCK_DETAILS         = "stock_details";//耗材详情
   public static final String STYPE_MEAL_BING             = "meal_bing";//套餐绑定
   public static final String STYPE_LOSS_TYPE             = "stype_loss_type";//盘亏
   public static final String STYPE_PROFIT_TYPE           = "stype_profit_type";//盘盈
   public static final int    ACTIVITY                    = 1;
   public static final String ACT_TYPE_CONFIRM_HAOCAI     = "act_type_confirm_haocai";//确认耗材领用界面

   public static final String TEMP_AFTERBIND = "afterBind";//后绑定患者
   public static final String TEMP_FIRSTBIND = "firstBind";//后绑定患者

   public static final String STYPE_EXCEPTION_LEFT  = "exception_left";//异常处理
   public static final String STYPE_EXCEPTION_RIGHT = "exception_right";//异常处理-异常记录

   //配置项的code
   public static final String CONFIG_BPOW01 = "BPOW01";//是否先开柜门后绑定患者
   public static final String CONFIG_BPOW02 = "BPOW02";//是否先绑定患者后开柜门
   public static final String CONFIG_011 = "011";//是否允许快速开柜
   public static final String CONFIG_012 = "012";//是否启用绑定临时患者领用耗材
   public static final String CONFIG_013 = "013";//设备禁用
   public static final String CONFIG_014 = "014";//是否启用套餐领用
   public static final String CONFIG_015 = "015";//是否启用请领单领用
   public static final String CONFIG_017 = "017";//是否启用紧急登录
   public static final String CONFIG_BPOW03 = "BPOW03";//不绑定患者
   public static final String CONFIG_BPOW04 = "BPOW04";//启用可绑定患者或者可不绑定患者,先开柜门后绑定患者
   public static final String CONFIG_BPOW05 = "BPOW05";//启用可绑定患者或者可不绑定患者,先绑定患者后开柜门
   public static final String CONFIG_026 = "026";//启用未确认耗材
   public static final String CONFIG_034 = "034";//启用人脸识别登录
   public static final String CONFIG_051 = "HD002";//启用是否是否领用时请求计费
   public static final String CONFIG_052 = "HD001";//启用是否退回时请求退费
   public static final String CONFIG_043 = "043";//启用非x位的epc过滤
   public static final String CONFIG_044 = "044";//启用前x位epc为y的过滤
   public static final String CONFIG_045 = "045";//启用后x位epc为y的过滤
   public static final String CONFIG_046 = "046";//启用查看未入库耗材
   public static final String CONFIG_056 = "056";//启用查看手术排班患者病区床号
   public static final String CONFIG_058 = "058_ZDRK";//是否开启整单入库
   public static final String CONFIG_059 = "059_VIDEO";//是否开启视频录制
   public static final String CONFIG_060 = "060";//是否开启库存下限

   public static final String LEFT_MENU_HCCZ = "耗材操作";//耗材操作
   public static final String LEFT_MENU_HCLS = "耗材流水";//耗材流水
   public static final String LEFT_MENU_KCZT = "库存状态";//库存状态
   public static final String LEFT_MENU_SSPD = "实时盘点";//实时盘点
   public static final String LEFT_MENU_SYJL = "使用记录";//使用记录
   public static final String LEFT_MENU_YCCL = "异常处理";//使用记录

   public static final String DOWN_MENU_LYTH  = "领用退回";//领用/退回
   public static final String DOWN_MENU_LY    = "领用";//领用
   public static final String DOWN_MENU_RK    = "入库";//入库
   public static final String DOWN_MENU_YC    = "移出";//移出
   public static final String DOWN_MENU_DB    = "调拨";//调拨
   public static final String DOWN_MENU_YR    = "移入";//移入
   public static final String DOWN_MENU_THUI  = "退回";//退回
   public static final String DOWN_MENU_THUO  = "退货";//退货
   public static final String DOWN_MENU_YICHU = "移除";//移除

   //sp存入的key
   public static final String SAVE_SYSTEMTYPE          = "SAVE_SYSTEMTYPE";//存入3.0和2.1的判断
   public static final String SAVE_SEVER_IP           = "SAVE_SEVER_IPxxx";//存入服务器IP加端口
   public static final String SAVE_SEVER_IP_TEXT      = "SAVE_SEVER_IP_TEXT";//存入服务器IP
   public static final String SAVE_SEVER_CODE         = "SAVE_SEVER_CODE";//存入服务器端口
   public static final String SAVE_CONFIG_STRING      = "SAVE_CONFIG_STRING";//配置项存入
   public static final String SAVE_MENU_LEFT_TYPE     = "SAVE_MENU_LEFT_TYPE";//权限配置项存入主按钮
   public static final String SAVE_MENU_DOWN_TYPE     = "SAVE_MENU_DOWN_TYPE";//权限配置项存入耗材操作选择操作
   public static final String SAVE_MENU_DOWN_TYPE_ALL = "SAVE_MENU_DOWN_TYPE_ALL";//权限配置项存入耗材操作选择操作是否有数据
   public static final String READER_TYPE             = "2";//reader的type
   public static final String CONSUMABLE_TYPE         = "1";//主板type
   public static final String IC_TYPE                 = "4";//IC卡type
   public static final String FINGER_TYPE             = "3";//指纹仪type
   public static final String SN_NUMBER               = "sn_number";
   public static final String THING_CODE              = "thing_code";
   public static final String SAVE_REGISTE_DATE       = "registe_date";//预注册保存到本地的数据
   public static final String SAVE_ONE_REGISTE        = "oneRegiste";//第一次预注册的状态存入
   public static final String SAVE_ACTIVATION_REGISTE = "activationRegiste";//激活的状态存入
   public static final String SAVE_BRANCH_CODE        = "branchCode";//院区存入code
   public static final String SAVE_DEPT_CODE          = "deptId";//科室存入code
   public static final String SAVE_DEPT_NAME          = "deptName";//科室存入name
   public static final String SAVE_STOREHOUSE_CODE    = "storehouse_Code";//库房code
   public static final String KEY_ACCOUNT_DATA        = "key_account_data";//用户资料
   public static final String KEY_USER_NAME           = "key_user_name";//用户名字
   public static final String KEY_ACCOUNT_s_NAME      = "key_user_name_key";//用户名字登录名
   public static final String KEY_USER_ICON           = "key_user_icon";//用户头像
   public static final String KEY_ACCOUNT_NAME        = "KEY_ACCOUNT_NAME";//用户名字
   public static final String KEY_USER_SEX            = "key_user_sex";//用户性别
   public static final String KEY_ACCOUNT_ID          = "key_account_id";//用户ID
   public static final String SAVE_STOREHOUSE_NAME    = "save_storehouse_name";//库房名字
   public static final String SAVE_RECEIVE_ORDERID    = "save_receive_orderid";//医嘱单ID
   public static final String SAVE_READER_TIME        = "SAVE_READER_TIME";//READER扫描时长
   public static final String SAVE_ANIMATION_TIME     = "SAVE_ANIMATION_TIME";//READER扫描动画延时时长
   public static final String SAVE_LOGINOUT_TIME      = "SAVE_LOGINOUT_TIME";//无操作退出时间
   public static final String SAVE_CLOSSLIGHT_TIME      = "SAVE_CLOSSLIGHT_TIME";//无操作关灯
   public static final String SAVE_REMOVE_LOGFILE_TIME = "SAVE_REMOVE_LOGFILE_TIME";//删除多少天之前的日志
   public static final String SAVE_HOME_LOGINOUT_TIME = "SAVE_HOME_LOGINOUT_TIME";//首页无操作退出时间
   public static final String SAVE_NOEPC_LOGINOUT_TIME = "SAVE_NOEPC_LOGINOUT_TIME";//未扫描到操作耗材退出时间
   public static final String SAVE_VOICE_NOCLOSSDOOR_TIME = "SAVE_VOICE_NOCLOSSDOOR_TIME";//未扫描到操作耗材退出时间
   public static final String SYSTEMTYPES_3              = "HCT3.0";//高值登录的type
   public static final String SYSTEMTYPES_2              = "HCT";//高值登录的type

   public static final String BOX_SIZE_DATE           = "BOX_SIZE_DATE";//柜子的信息
   public static final String BOX_SIZE_DATE_HOME           = "BOX_SIZE_DATE_HOME";//柜子的信息,s首页用
   public static final String LOGCAT_OPEN             = "LOG_CAT_OPEN";//日志记录
   public static final String FACE_UPDATE_TIME        = "face_update_time";//人脸照最新更新时间
   public static final String PATIENT_TYPE            = "PatientType";///2,3手术，1是非手术
//   public static final String THING_MODEL             = "THING_MODEL";//设备类型
   public static final String FINGER_VERSION            = "2";//3.0传2，2.1传1
   public static final String HOME_LOGO = "HOME_LOGO";///主页的logo地址
   public static final String LOGIN_LOGO = "LOGIN_LOGO";///登录的logo地址

   public static final String ACCESS_TOKEN  = "ACCESS_TOKEN";//用户TOKEN
   public static final String REFRESH_TOKEN = "REFRESH_TOKEN";//刷新TOKEN

   public static final String READER_NAME           = "reader_name";//reader的厂家
   public static final String READER_NAME_RODINBELL = "RODINBELL";//reader的厂家罗丹贝尔
   public static final String READER_NAME_COLU      = "COLU";//reader的厂家鸿路

   public static final String ERROR_200  = "200";//请求正常
   public static final String ERROR_1010 = "1010";//系统异常
   public static final String ERROR_1001 = "1001";//刷新Token过期
   public static final String ERROR_1000 = "1000";//Token过期

   //人脸分组:高值
   public static final String  FACE_GROUP = "highValue";
   //选择原因
   public static final String[] REASON                         = {"清理库存", "耗材过期", "型号错误", "包装破损",
	   "厂家召回", "其他"};
   //异常处理：连续移除处理选择
   public static final String[] EXCEPTION_DEAL_REMOVE_JUDGE    = {"标签损坏", "取消异常标记", "出柜关联"};
   //异常处理：出柜关联
   public static final String[] EXCEPTION_DEAL_OUT_BOX_CONNECT = {"领用", "移出", "退货"};

   public static final String YI_CHU="9";//移出
   public static final String TUI_HUO="8";//退货
   public static final String CHU_GUI="7";//出柜关联
   public static final String BANGDING="3";//绑定患者、领用

   public static final String LOGIN_TYPE_PASSWORD = "1";//账号密码登录type
   public static final String LOGIN_TYPE_FINGER   = "2";//指纹登录type
   public static final String LOGIN_TYPE_IC       = "3";//腕带登录type
   public static final String LOGIN_TYPE_FACE     = "6";//人脸登录type
}

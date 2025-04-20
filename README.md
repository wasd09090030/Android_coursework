项目概览

该安卓应用主要功能包括：

用户登录/注册：用户可注册并登录，注册信息保存在 SharedPreferences，下一次启动可自动识别登录。

首页 (HomeFragment)：上传并显示课表图片，图片 URI 持久化到 SharedPreferences，切换界面或重启后继续显示。

签到提醒 (ReminderFragment)：添加课程签到提醒或作业提醒，统一跳转到 AddReminderActivity，根据类型提前 20 分钟或 2 小时触发闹钟，使用 AlarmManager 调度并通过 ReminderReceiver 弹出通知。

记事本 (NotesFragment)：输入并展示笔记，笔记保存在 SharedPreferences，界面切换或应用重启后仍然显示。

设置 (SettingsFragment)：可更换应用背景图片（从相册选择并持久化 URI），支持退出登录（清除 SharedPreferences 并跳转到登录界面）。

页面说明

1. LoginActivity

功能：提供用户名/密码注册与登录。

实现：用户注册时将用户名、密码保存到 SharedPreferences；登录时读取并校验，匹配后跳转到主界面。

2. MainActivity

功能：应用入口，包含底部导航栏，可在四个主要模块间切换。

实现：使用 Fragment + BottomNavigationView；启动时创建通知渠道(ReminderChannel)；根据菜单选项加载对应 Fragment。

模块详情

HomeFragment

功能：上传并显示课表图片。

实现：

使用 ActivityResultLauncher 调用系统相册；

选中图片后通过 ImageView.setImageURI(uri) 显示；

SharedPreferences 保存 URI，onCreateView 时读取并加载；

加载时校验 URI 可访问性，避免异常。

ReminderFragment

功能：展示已添加的提醒；提供 "添加签到提醒" 和 "课程作业提醒" 按钮。

实现：

RecyclerView + ReminderAdapter 展示提醒列表；

点击任一按钮，传入枚举类型(SIGN_IN 或 HOMEWORK) 启动 AddReminderActivity。

NotesFragment

功能：输入并展示多条笔记。

实现：

EditText 输入，点击按钮后添加到 ArrayList 并通过 NotesAdapter 更新列表；

每次添加后将 Set<String> 保存到 SharedPreferences；

onCreateView 时读取并恢复列表。

SettingsFragment

功能：更换应用背景；退出登录。

实现：

按钮打开相册选择背景图片，URI 持久化到 SharedPreferences 并在 onResume 加载；

退出登录按钮清除 SharedPreferences 中用户信息并跳转到 LoginActivity，结束当前栈。

AddReminderActivity

功能：统一界面添加提醒（签到或作业）。

实现：

接收 Intent 中的提醒类型枚举；

时间选择使用 TimePickerDialog，格式 HH:mm；

点击确认，创建 Reminder 对象并添加到单例 ReminderManager；

根据类型计算提前时间（20 分钟或 2 小时），使用 AlarmManager.setExact() 调度闹钟；

闹钟触发后由 ReminderReceiver 接收并推送通知。

ReminderReceiver / HomeworkReminderReceiver

功能：接收闹钟广播并生成通知。

实现：

使用 NotificationCompat 构建通知，渠道 ID 为 reminderChannel；

可选区分普通签到提醒和作业提醒图标或文案；

检查通知权限(areNotificationsEnabled())后再调用 notify()。

数据持久化

SharedPreferences：

用户登录信息(username/password)、课表图片 URI、笔记、背景图片 URI。

轻量级存储，适用于少量键值对数据。

单例管理器：

ReminderManager 维护全局提醒列表，内存中管理，应用运行时使用

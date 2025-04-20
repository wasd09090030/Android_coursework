├── MainActivity.java             // 主界面，课程表上传和课程列表展示
├── AddCourseActivity.java        // 添加课程提醒的界面
├── Course.java                   // Course类（数据实体）
├── CourseDao.java                // DAO接口，用于与数据库交互
├── CourseDatabase.java           // Room数据库
├── CourseRepository.java         // 数据库操作封装
├── ReminderBroadcast.java        // 广播接收器，用于处理通知提醒
└── NotificationUtils.java        // 通知工具类
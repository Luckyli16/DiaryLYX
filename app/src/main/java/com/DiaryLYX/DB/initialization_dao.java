package com.DiaryLYX.DB;
//通过创建并配置这个自定义的 Application 类，
// 我们可以确保在整个应用程序中共享同一个 dao 对象，使得数据库的访问和操作更加统一和方便。
import android.app.Application;
//为应用程序提供全局的状态和配置信息。
public class initialization_dao extends Application {
    private com.DiaryLYX.DB.dao dao;

    /**
     * 创建时调用
     * */
    @Override
    public void onCreate() {
        super.onCreate();
        dao = new dao(this);
    }

    /**
     * 后台进程终止，前台程序需要内存时调用此方法，用于释放内存
     * 用于关闭数据库连接
     * */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        dao.close();
    }

    public com.DiaryLYX.DB.dao getDao() {
        return dao;
    }
}

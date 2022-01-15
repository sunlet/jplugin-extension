//package net.jplugin.extension.dbp.core.api;
//
//import net.jplugin.extension.dbp.core.req.CommandRequest;
//
///**
// * Created with IntelliJ IDEA.
// * Description:
// *
// * @Author: yuanhongjun
// * DateTime: 2022-01-13 14:54
// */
//public abstract class AbstractCommandHandler implements ICommandHandler {
//    public void preHandleCommand(net.jplugin.extension.dbp.core.ConnectionContext connCtx, CommandRequest commandPackage) {
//
//    }
//
//    /**
//     * 处理查询服务，有如下几点需要说明：</br>
//     * 1、关于show full tables ，应该转成show tables，且需要处理columnName，可参考真正的mysql</br>
//     * 2、关于show create table 语句，如果想要对接navicat等现有数据库查看工具，需要处理columnName，可参考真正的mysql</br>
//     * 3、MYSQL本身的show tables 返回的字段类型是12（对应时间格式），会报Invalid format for type bigquery_data_partition. Value 'TIMESTAMP'
//     *
//     * @param connCtx
//     * @param commandPackage
//     */
//    public final void handleCommand(net.jplugin.extension.dbp.core.ConnectionContext connCtx, CommandRequest commandPackage) {
//        preHandleCommand(connCtx, commandPackage);
//        executeCommand(connCtx, commandPackage);
//        postHandleCommand(connCtx, commandPackage);
//    }
//
//    public void postHandleCommand(net.jplugin.extension.dbp.core.ConnectionContext connCtx, CommandRequest commandPackage) {
//
//    }
//}

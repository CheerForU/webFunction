package cn.com.xxx.web.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.com.xxx.common.common.CommonUtil;
import cn.com.xxx.web.CacheServlet;
import cn.com.xxx.web.model.CAModel;
import cn.com.xxx.web.model.ProtocolException;
import cn.com.xxx.web.model.ProxyException;
import cn.com.xxx.web.model.SystemModel;
import cn.com.xxx.web.pojo.Email;
import cn.com.xxx.web.pojo.Monitor;
import cn.com.xxx.web.pojo.Network;
import cn.com.xxx.web.pojo.ProcOper;
import cn.com.xxx.web.pojo.ProcState;
import cn.com.xxx.web.pojo.Route;
import cn.com.xxx.web.pojo.SMSConfig;
import cn.com.xxx.web.pojo.SelfBackup;
import cn.com.xxx.web.pojo.Server;
import cn.com.xxx.web.pojo.ServiceManage;
import cn.com.xxx.web.pojo.SysLog;
import cn.com.xxx.web.pojo.Sysconfig;
import cn.com.xxx.web.pojo.Trap;

@Path("/system")
@Controller
public class SystemResource extends BaseResource
{
    @Autowired
    private SystemModel systemModel;

    /**
     * 获取Trap功能配置
     * @return object
     */
    @GET
    @Path("/trap")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrapConfig()
    {
        List<Sysconfig> sysconfigs;
        final Map<String, String> res = new HashMap<>();
        try
        {
            sysconfigs = systemModel.getTrapConfig();
            for (final Sysconfig sysconfig : sysconfigs)
            {
                res.put(sysconfig.getS_key(), sysconfig.getS_value());
            }
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.ok().entity(res).build();
    }

    /**
     * 更新Trap功能配置
     * @return object
     */
    @PUT
    @Path("/trap")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setTrapConfig(final Trap trap)
    {
        final HttpSession session = req.getSession(false);
        final String username = session.getAttribute("username").toString();
        try
        {
            systemModel.setTrapConfig(trap);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        info("修改了Trap设置", 3, username);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * 获取服务器基本信息监控
     * @return object
     */
    @GET
    @Path("/monitor")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMonitorConfig()
    {
        List<Sysconfig> sysconfigs;
        final Map<String, String> res = new HashMap<>();
        try
        {
            sysconfigs = systemModel.getMonitorConfig();
            for (final Sysconfig sysconfig : sysconfigs)
            {
                res.put(sysconfig.getS_key(), sysconfig.getS_value());
            }
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.ok().entity(res).build();
    }

    /**
     * 更新服务器基本信息监控
     * @return object
     */
    @PUT
    @Path("/monitor")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setMonitorConfig(final Monitor monitor)
    {
        final HttpSession session = req.getSession(false);
        final String username = session.getAttribute("username").toString();
        try
        {
            systemModel.setMonitorConfig(monitor);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        info("修改了Monitor设置", 3, username);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * 获取SysLog设置
     * @return object
     */
    @GET
    @Path("/syslog")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSysLogConfig()
    {
        List<Sysconfig> sysconfigs;
        final Map<String, String> res = new HashMap<>();
        try
        {
            sysconfigs = systemModel.getSysLogConfig();
            for (final Sysconfig sysconfig : sysconfigs)
            {
                res.put(sysconfig.getS_key(), sysconfig.getS_value());
            }
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.ok().entity(res).build();
    }

    /**
     * 更新SysLog设置
     * @return object
     */
    @PUT
    @Path("/syslog")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setSysLogConfig(final SysLog sysLog)
    {
        final HttpSession session = req.getSession(false);
        final String username = session.getAttribute("username").toString();
        try
        {
            systemModel.setSysLogConfig(sysLog);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        info("修改了SysLog设置", 3, username);
        return Response.status(Response.Status.OK).build();
    }


    /**
     * 获取进程状态
     * @return object
     */
    @GET
    @Path("/cpst")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProcState()
    {
        final ArrayList<ProcState> stateList;
        try
        {
            stateList = systemModel.getProcState();
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.ok().entity(stateList).build();
    }

    /**
     * 操作进程状态
     * @return object
     */
    @PUT
    @Path("/cpst")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response operProcState(final ProcOper procOper)
    {
        final HttpSession session = req.getSession(false);
        final String username = session.getAttribute("username").toString();
        try
        {
            systemModel.operProcState(procOper);

        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        info("操作" + procOper.getName() + "进程状态,操作为：" + procOper.getOperator(),
                3,
                username);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * 获取自备份配置
     * @return object
     */
    @GET
    @Path("/selfbackup")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSelfBackupConfig()
    {
        final SelfBackup selfbackup;
        try
        {
            selfbackup = systemModel.getSelfBackupConfig();
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.ok().entity(selfbackup).build();
    }

    /**
     * 更新自备份配置
     * @return object
     */
    @PUT
    @Path("/selfbackup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setSelfBackupConfig(final SelfBackup selfbackup)
    {
        final HttpSession session = req.getSession(false);
        final String username = session.getAttribute("username").toString();
        try
        {
            final boolean res = systemModel.setSelfBackupConfig(selfbackup);
            if (!res)
            {
                return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity(toMap("exec操作失败"))
                        .build();
            }
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        info("修改了自备份配置", 3, username);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * 获取服务状态
     * @return object
     */
    @GET
    @Path("/service")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServiceConfig()
    {
        final ArrayList<ServiceManage> servicelist;
        try
        {
            servicelist = new SystemModel().getServiceConfig();
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.ok().entity(servicelist).build();
    }

    /**
     * 操作服务状态
     * @return object
     */
    @PUT
    @Path("/service")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setServiceConfig(final ServiceManage servManage)
    {
        final HttpSession session = req.getSession(false);
        final String username = session.getAttribute("username").toString();
        try
        {
            final boolean res = new SystemModel().setServiceConfig(servManage);
            if (!res)
            {
                return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity(toMap("exec操作失败"))
                        .build();
            }
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        info("操作" + servManage.getName() + "服务状态,操作为：" + servManage.getOper(),
                3,
                username);
        return Response.status(Response.Status.OK).build();
    }

}
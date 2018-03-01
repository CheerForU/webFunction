package cn.com.unary.web.controller;

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

import cn.com.unary.common.common.CommonUtil;
import cn.com.unary.web.CacheServlet;
import cn.com.unary.web.model.CAModel;
import cn.com.unary.web.model.ProtocolException;
import cn.com.unary.web.model.ProxyException;
import cn.com.unary.web.model.SystemModel;
import cn.com.unary.web.pojo.Email;
import cn.com.unary.web.pojo.Monitor;
import cn.com.unary.web.pojo.Network;
import cn.com.unary.web.pojo.ProcOper;
import cn.com.unary.web.pojo.ProcState;
import cn.com.unary.web.pojo.Route;
import cn.com.unary.web.pojo.SMSConfig;
import cn.com.unary.web.pojo.SelfBackup;
import cn.com.unary.web.pojo.Server;
import cn.com.unary.web.pojo.ServiceManage;
import cn.com.unary.web.pojo.SysLog;
import cn.com.unary.web.pojo.Sysconfig;
import cn.com.unary.web.pojo.Trap;

@Path("/system")
@Controller
public class SystemResource extends BaseResource
{
    @Autowired
    private SystemModel systemModel;

    /**
     * 获取 发送邮件设置
     * @return object
     */
    @GET
    @Path("/email")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEmailConfig()
    {
        List<Sysconfig> sysconfigs;
        final Map<String, String> res = new HashMap<>();
        try
        {
            sysconfigs = systemModel.getEmailConfig();
            for (final Sysconfig sysconfig : sysconfigs)
            {
                res.put(sysconfig.getS_key(), sysconfig.getS_value());
            }
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.ok().entity(res).build();
    }

    /**
     * 更新 发送邮件设置
     * @return object
     */
    @PUT
    @Path("/email")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setEmailConfig(final Email email)
    {
        final HttpSession session = req.getSession(false);
        final String username = session.getAttribute("username").toString();
        try
        {
            systemModel.setEmailConfig(email);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        info("修改了报警信息", 3, username);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * 测试 发送邮件设置
     * @return object
     */
    @PUT
    @Path("/email/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testEmailConfig(@PathParam("email") final String email,
            final Email e_info)
    {
        try
        {
            systemModel.testEmailConfig(email, e_info);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/network")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNetworkConfig()
    {
        List<Network> networkList;
        try
        {
            networkList = systemModel.getNetworkList();
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.ok().entity(networkList).build();
    }

    @GET
    @Path("/network/{eth}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNetworkInfo(@PathParam("eth") final String eth)
    {
        Network network;
        try
        {
            network = systemModel.getNetworkInfo(eth);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.ok().entity(network).build();
    }

    /**
     * 更新 网络设置
     * @return object
     */
    @PUT
    @Path("/network")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setNetworkConfig(final Network network)
    {
        final HttpSession session = req.getSession(false);
        final String username = session.getAttribute("username").toString();
        try
        {
            systemModel.setNetworkConfig(network);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        info("修改了网络设置", 3, username);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * 创建子网卡
     * @return Response
     */
    @POST
    @Path("/network/subdev")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSubDev(final Network network)
    {
        final HttpSession session = req.getSession(false);
        final String username = session.getAttribute("username").toString();
        try
        {
            systemModel.createSubDev(network);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }

        info("创建了[" + network.getEth() + "]的子网卡", 3, username);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * 编辑子网卡
     * @return Response
     */
    @PUT
    @Path("/network/subdev")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modifySubDev(final Network network)
    {
        final HttpSession session = req.getSession(false);
        final String username = session.getAttribute("username").toString();
        try
        {
            systemModel.setNetworkConfig(network);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }

        info("修改了子网卡[" + network.getEth() + "]", 3, username);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * 删除子网卡
     * @return Response
     */
    @DELETE
    @Path("/network/subdev/{eth}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeSubDev(@PathParam("eth") final String eth)
    {
        final HttpSession session = req.getSession(false);
        final String username = session.getAttribute("username").toString();
        try
        {
            systemModel.removeSubDev(eth);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }

        info("删除了子网卡[" + eth + "]", 3, username);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * 获取路由列表
     * @return Response
     */
    @GET
    @Path("/network/route")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoute()
    {
        List<Route> routeList;
        try
        {
            routeList = systemModel.getRouteList();
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }

        return Response.ok().entity(routeList).build();
    }

    /**
     * 获取单个路由信息
     * @return Response
     */
    @GET
    @Path("/network/route/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoute(@PathParam("id") final String id)
    {
        Route route;
        try
        {
            route = systemModel.getRoute(id);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }

        return Response.ok().entity(route).build();
    }

    /**
     * 创建路由
     * @return Response
     */
    @POST
    @Path("/network/route")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createRoute(final Route route)
    {
        final HttpSession session = req.getSession(false);
        final String username = session.getAttribute("username").toString();
        try
        {
            systemModel.createRoute(route);
        }
        catch (final ProtocolException e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }

        info("创建了路由", 3, username);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * 修改路由
     * @return Response
     */
    @PUT
    @Path("/network/route")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRoute(final Route route)
    {
        final HttpSession session = req.getSession(false);
        final String username = session.getAttribute("username").toString();
        try
        {
            systemModel.updateRoute(route);
        }
        catch (final ProtocolException e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }

        info("修改了路由", 3, username);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * 删除路由
     * @return Response
     */
    @DELETE
    @Path("/network/route/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeRoute(@PathParam("id") final String id)
    {
        final HttpSession session = req.getSession(false);
        final String username = session.getAttribute("username").toString();
        try
        {
            systemModel.removeRoute(id);
        }
        catch (final ProtocolException e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }

        info("删除了路由", 3, username);
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/server")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getServerConfig()
    {
        Server server;
        try
        {
            server = systemModel.getServerConfig();
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.ok().entity(server).build();
    }

    /**
     * 更新 服务器设置
     * @return object
     */
    @PUT
    @Path("/server")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setServerConfig(final Server server)
    {
        final HttpSession session = req.getSession(false);
        final String username = session.getAttribute("username").toString();
        try
        {
            systemModel.setServerConfig(server);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        info("修改了服务器设置", 3, username);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * 更新 服务器设置
     * @return object
     */
    @PUT
    @Path("/serverTime")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setServerTime(final String date)
    {
        final HttpSession session = req.getSession(false);
        final String username = session.getAttribute("username").toString();
        try
        {
            systemModel.setServerTime(date);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        info("修改了服务器时间", 3, username);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * 更新 服务器名
     * @return object
     */
    @PUT
    @Path("/serverName")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setServerName(final String name)
    {
        final HttpSession session = req.getSession(false);
        final String username = session.getAttribute("username").toString();
        try
        {
            systemModel.setServerName(name);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        info("修改了服务器名", 3, username);
        return Response.status(Response.Status.OK).build();
    }

    /**
     * 导出故障诊断信息
     * @return
     */
    @GET
    @Path("/diagnose")
    @Produces("application/zip")
    public Response getDiagnose()
    {
        String filePath = "";
        try
        {
            filePath = systemModel.getDiagnosePath();

            final File file = new File(filePath);
            if (file.exists())
            {
                final String mt = new MimetypesFileTypeMap().getContentType(file);

                return Response.ok(file, mt)
                        .header("Content-disposition",
                                "attachment;filename=" + file.getName())
                        .build();
            }
            else
            {
                log.error("File is not exist.");
                throw new ProxyException("File is not exist.");
            }
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }

    }

    @GET
    @Path("/sessionTime")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeSessionTime()
    {
        return Response.status(Response.Status.OK)
                .entity(CacheServlet.getSess_overTime())
                .build();

    }

    @PUT
    @Path("/sessionTime")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeOverTime(@QueryParam("overTime") final String overTime)
    {
        try
        {
            systemModel.changeOverTime(overTime);
        }
        catch (final Exception e)
        {

        }
        return Response.status(Response.Status.OK).build();

    }

    @PUT
    @Path("/reboot")
    @Produces(MediaType.APPLICATION_JSON)
    public Response reboot()
    {
        try
        {
            systemModel.reboot();
            return Response.status(Response.Status.OK).build();
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/shutdown")
    @Produces(MediaType.APPLICATION_JSON)
    public Response shutdown()
    {
        try
        {
            systemModel.shutdown();
            return Response.status(Response.Status.OK).build();
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
    }

    @GET
    @Path("/blackcubeVer")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBlackCubeVer()
    {
        final Map<String, String> map = new HashMap<String, String>();
        map.put("version", CacheServlet.getBlackcubeVer());
        return Response.status(Response.Status.OK).entity(map).build();
    }

    @GET
    @Path("/passExpireCycle")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPassExpireCycle()
    {
        return Response.status(Response.Status.OK)
                .entity(systemModel.getPassExpireCycle())
                .build();

    }

    @PUT
    @Path("/passExpireCycle")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassExpireCycle(@QueryParam("cycle") final String cycle)
    {
        try
        {
            systemModel.setPassExpireCycle(cycle);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.status(Response.Status.OK).build();

    }

    @GET
    @Path("/passMinLength")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPassMinLength()
    {
        return Response.status(Response.Status.OK)
                .entity(systemModel.getPassMinLength())
                .build();
    }

    @PUT
    @Path("/passMinLength")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setPassMinLength(@QueryParam("length") final String length)
    {
        try
        {
            systemModel.setPassMinLength(length);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/loginLimitChance")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLoginLimitChance()
    {
        return Response.status(Response.Status.OK)
                .entity(systemModel.getLoginLimitChance())
                .build();
    }

    @PUT
    @Path("/loginLimitChance")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setLoginLimitChance(@QueryParam("chance") final String chance)
    {
        try
        {
            systemModel.setLoginLimitChance(chance);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/loginLimitTime")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLoginLimitTime()
    {
        return Response.status(Response.Status.OK)
                .entity(systemModel.getLoginLimitTime())
                .build();
    }

    @PUT
    @Path("/loginLimitTime")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setLoginLimitTime(@QueryParam("limitTime") final String limitTime)
    {
        try
        {
            systemModel.setLoginLimitTime(limitTime);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            e.printStackTrace();
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/bkset/checkInterval")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBksetCheckInterval()
    {
        final Sysconfig config = new Sysconfig();
        config.setS_key("bksetcheck_interval");
        return Response.status(Response.Status.OK)
                .entity(systemModel.getSysconfig(config))
                .build();
    }

    @PUT
    @Path("/bkset/checkInterval")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setBksetCheckInterval(@QueryParam("interval") final String interval)
    {
        final Sysconfig config = new Sysconfig();
        config.setS_key("bksetcheck_interval");
        config.setS_value(interval);
        try
        {
            systemModel.setSysconfigValue(config);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/bkset/checkSpeed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBksetCheckSpeed()
    {
        final Sysconfig config = new Sysconfig();
        config.setS_key("bksetcheck_speed");
        return Response.status(Response.Status.OK)
                .entity(systemModel.getSysconfig(config))
                .build();
    }

    @PUT
    @Path("/bkset/checkSpeed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setBksetCheckSpeed(@QueryParam("speed") final String speed)
    {
        final Sysconfig config = new Sysconfig();
        config.setS_key("bksetcheck_speed");
        config.setS_value(speed);
        try
        {
            systemModel.setSysconfigValue(config);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/bkset/checkStartTm")
    public Response getBksetCheckStartTm()
    {
        final Sysconfig config = new Sysconfig();
        config.setS_key("bksetcheck_starttime");
        return Response.status(Response.Status.OK)
                .entity(systemModel.getSysconfig(config))
                .build();
    }

    @PUT
    @Path("/bkset/checkStartTm")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setBksetCheckStartTm(@QueryParam("time") final String time)
    {
        try
        {
            systemModel.setBksetCheckTime(time);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.status(Response.Status.OK).build();
    }

    /**
     * 获取短信报警配置
     * @param platform 短信平台/发送方式
     * @return
     */
    @GET
    @Path("/sms/{platform}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSMSConfig(@PathParam("platform") final String platform)
    {
        List<SMSConfig> list = new ArrayList<>();
        final String modem = "modem";
        try
        {
            // 获取的是短信猫配置
            if (modem.equals(platform))
            {
                final String key = "message_center";
                final Sysconfig condition = new Sysconfig();
                condition.setS_key(key);
                final String center = systemModel.getSysconfig(condition);
                final SMSConfig config = new SMSConfig();
                config.setPlatform(platform);
                config.setAttr(key);
                config.setValue(center);
                list.add(config);
                list.addAll(systemModel.getSMSConfigs(platform));
            }
            // 获取的是其他短信平台的配置
            else
            {
                list = systemModel.getSMSConfigs(platform);
            }
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.status(Response.Status.OK).entity(list).build();
    }

    /**
     * 设置短信报警参数
     * @param platform 短信平台/发送方式
     * @param configs 短信参数配置
     * @return
     */
    @PUT
    @Path("/sms/{platform}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response setSMSConfig(@PathParam("platform") final String platform,
            final List<SMSConfig> configs)
    {
        try
        {
            if (!CommonUtil.isEmpty(configs))
            {
                // 短信猫配置修改
                if ("modem".equals(platform))
                {
                    for (final SMSConfig config : configs)
                    {
                        // 修改的是短信猫启用状态
                        if ("onoff".equals(config.getAttr()))
                        {
                            systemModel.setSMSConfigs(platform, configs);
                        }
                        // 修改的是短信中心号码等配置
                        else
                        {
                            final Sysconfig sysconfig = new Sysconfig();
                            sysconfig.setS_key(config.getAttr());
                            sysconfig.setS_value(config.getValue());
                            systemModel.setSysconfigValue(sysconfig);
                        }
                    }

                }
                // 短信平台配置修改
                else
                {
                    systemModel.setSMSConfigs(platform, configs);
                }
            }
            return Response.status(Response.Status.OK).build();
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
    }

    /**
     * 测试 发送短信设置
     * @return object
     */
    @PUT
    @Path("/sms/{platform}/test")
    @Produces(MediaType.APPLICATION_JSON)
    public Response sendTestSMS(@PathParam("platform") final String platform,
            final List<SMSConfig> configs)
    {
        try
        {
            systemModel.sendTestSMS(platform, configs);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        return Response.status(Response.Status.OK).build();
    }

    @GET
    @Path("/port")
    public Response getServerPort()
    {
        final int port = req.getLocalPort();
        return Response.status(Response.Status.OK).entity(String.valueOf(port)).build();
    }

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
     * 导入ca证书
     * @param name
     * @param request
     * @return
     * @throws IOException
     * @throws ProtocolException
     */
    @POST
    @Path("/savecafile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveCaFile(@Context final HttpServletRequest request)
            throws Exception
    {
        BufferedReader br = null;
        final CAModel caModel = new CAModel();
        final HttpSession session = req.getSession(false);
        final String username = session.getAttribute("username").toString();
        try
        {
            br = new BufferedReader(new InputStreamReader(request.getInputStream()));
            if (request.getContentLength() > 10000 || request.getContentLength() < 1000)
            {
                throw new Exception("该文件不是CA安全证书");
            }
            String line = null;
            final StringBuffer sb = new StringBuffer();
            boolean lite = false;
            while ((line = br.readLine()) != null)
            {
                if (line.indexOf("-----BEGIN CERTIFICATE----") != -1)
                {
                    lite = true;
                }
                if (lite)
                {
                    sb.append(line + "\n");
                }
                if (line.indexOf("-----END CERTIFICATE-----") != -1)
                {
                    break;
                }
            }
            final byte[] in_b = sb.toString().getBytes();
            caModel.saveCafile(in_b);

        }
        catch (final Exception e)
        {
            error("证书导入失败", 2, username);
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
        finally
        {
            if (br != null)
            {
                br.close();
            }
        }
        info("证书导入成功", 3, username);
        return Response.status(Status.OK).build();
    }

    @Path("/cainfo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCAInfo()
    {
        try
        {
            final CAModel caModel = new CAModel();
            if (caModel.getCAInfo() == null)
            {
                return Response.status(Status.OK).entity(new ArrayList<>()).build();
            }
            return Response.ok(caModel.getCAInfo()).build();
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                    .entity(toMap(e.getMessage()))
                    .build();
        }
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
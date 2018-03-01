package cn.com.xxx.web.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.core.Application;

import org.apache.ibatis.session.SqlSession;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.com.xxx.common.common.CommonUtil;
import cn.com.xxx.common.msg.MessageConstructor;
import cn.com.xxx.common.msg.RequestNode;
import cn.com.xxx.common.msg.XMLNode;
import cn.com.xxx.mdp.MdpUtil;
import cn.com.xxx.mdp.model.Message;
import cn.com.xxx.web.ApplicationServlet;
import cn.com.xxx.web.CacheServlet;
import cn.com.xxx.web.pojo.Monitor;
import cn.com.xxx.web.pojo.SelfBackup;
import cn.com.xxx.web.pojo.Server;
import cn.com.xxx.web.pojo.ServiceManage;
import cn.com.xxx.web.pojo.SysLog;
import cn.com.xxx.web.pojo.Sysconfig;
import cn.com.xxx.web.pojo.Trap;

@org.springframework.stereotype.Service
public class SystemModel extends BaseModel
{
    private static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
    private static final String DB_NS = "cn.com.xxx.web.mapper.SysconfigMapper";
    private static final String DB_GET_SYSCONFIG = DB_NS + ".getSysconfig";
    private static final String DB_SET_SYSCONFIG_VALUE = DB_NS + ".setSysconfigValue";
 
    private static final String DB_GET_SNMP_CONFIG = DB_NS + ".getTrapConfig";
    private static final String DB_GET_SNMP_MONITOR_CONFIG = DB_NS + ".getMonitorConfig";
    private static final String DB_GET_SYSLOG_CONFIG = DB_NS + ".getSysLogConfig";

    private String mtsId = ApplicationServlet.getMtsid();

    /**
     * 获取Trap功能配置
     * @return
     * @throws Exception
     */
    public List<Sysconfig> getTrapConfig() throws Exception
    {
        List<Sysconfig> sysconfigs = new ArrayList<>();
        try (SqlSession sess = ApplicationServlet.sqlSessionfactory.openSession())
        {
            sysconfigs = sess.selectList(DB_GET_SNMP_CONFIG);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
        }
        return sysconfigs;
    }

    /**
     * 更新Trap功能配置
     * @return
     * @throws Exception
     */
    public void setTrapConfig(final Trap trap) throws Exception
    {
        try
        {
            final RequestNode node = new RequestNode("updateconfig");
            final XMLNode enable = new XMLNode("enable", trap.getEnable());
            node.addChild(enable);
            final XMLNode host = new XMLNode("host", trap.getHost());
            node.addChild(host);
            final XMLNode port = new XMLNode("port", trap.getPort());
            node.addChild(port);
            final XMLNode charset = new XMLNode("charset", trap.getCharset());
            node.addChild(charset);
            final XMLNode protocol = new XMLNode("protocol");
            protocol.addAttr("value", trap.getProtocol());
            node.addChild(protocol);
            final XMLNode type = new XMLNode("type", trap.getType());
            final XMLNode community = new XMLNode("community", trap.getCommunity());
            final XMLNode level = new XMLNode("level", trap.getLevel());
            final XMLNode username = new XMLNode("username", trap.getUsername());
            final XMLNode digest = new XMLNode("digest", trap.getDigest());
            final XMLNode password = new XMLNode("password", trap.getPassword());
            final XMLNode encrypt = new XMLNode("encrypt", trap.getEncrypt());
            final XMLNode priv = new XMLNode("priv", trap.getPriv());
            final XMLNode context = new XMLNode("context", trap.getContext());
            protocol.addChild(type);
            protocol.addChild(community);
            protocol.addChild(level);
            protocol.addChild(username);
            protocol.addChild(digest);
            protocol.addChild(password);
            protocol.addChild(encrypt);
            protocol.addChild(priv);
            protocol.addChild(context);
            MdpUtil.request("logserver",
                    "logserver.snmpconfig.update",
                    MessageConstructor.constructMsg(node));
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            throw new Exception(e);
        }
    }

    /**
     * 获取服务器基本信息监控
     * @return
     * @throws Exception
     */
    public List<Sysconfig> getMonitorConfig() throws Exception
    {
        List<Sysconfig> sysconfigs = new ArrayList<>();
        try (SqlSession sess = ApplicationServlet.sqlSessionfactory.openSession())
        {
            sysconfigs = sess.selectList(DB_GET_SNMP_MONITOR_CONFIG);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
        }
        return sysconfigs;
    }

    /**
     * 更新服务器基本信息监控
     * @return
     * @throws Exception
     */
    public void setMonitorConfig(final Monitor monitor) throws Exception
    {
        try
        {
            final RequestNode node = new RequestNode("updateconfig");
            final XMLNode enable = new XMLNode("enable", monitor.getEnable());
            node.addChild(enable);
            final XMLNode source = new XMLNode("source", monitor.getSource());
            node.addChild(source);
            final XMLNode snmpV1 = new XMLNode("snmpV1");
            snmpV1.addAttr("value", monitor.getSnmpV1());
            node.addChild(snmpV1);
            final XMLNode v1community = new XMLNode("community", monitor.getV1community());
            snmpV1.addChild(v1community);
            final XMLNode snmpV2c = new XMLNode("snmpV2c");
            snmpV2c.addAttr("value", monitor.getSnmpV2c());
            node.addChild(snmpV2c);
            final XMLNode v2community = new XMLNode("community", monitor.getV2community());
            snmpV2c.addChild(v2community);
            final XMLNode snmpV3 = new XMLNode("snmpV3");
            snmpV3.addAttr("value", monitor.getSnmpV3());
            node.addChild(snmpV3);
            final XMLNode level = new XMLNode("level", monitor.getLevel());
            final XMLNode username = new XMLNode("username", monitor.getUsername());
            final XMLNode digest = new XMLNode("digest", monitor.getDigest());
            final XMLNode password = new XMLNode("password", monitor.getPassword());
            final XMLNode encrypt = new XMLNode("encrypt", monitor.getEncrypt());
            final XMLNode priv = new XMLNode("priv", monitor.getPriv());
            final XMLNode context = new XMLNode("context", monitor.getContext());
            snmpV3.addChild(level);
            snmpV3.addChild(username);
            snmpV3.addChild(digest);
            snmpV3.addChild(password);
            snmpV3.addChild(encrypt);
            snmpV3.addChild(priv);
            snmpV3.addChild(context);
            MdpUtil.request("logserver",
                    "logserver.sysconfig.update",
                    MessageConstructor.constructMsg(node));
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            throw new Exception(e);
        }
    }

    /**
     * 获取SysLog设置
     * @return
     * @throws Exception
     */
    public List<Sysconfig> getSysLogConfig() throws Exception
    {
        List<Sysconfig> sysconfigs = new ArrayList<>();
        try (SqlSession sess = ApplicationServlet.sqlSessionfactory.openSession())
        {
            sysconfigs = sess.selectList(DB_GET_SYSLOG_CONFIG);
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
        }
        return sysconfigs;
    }

    /**
     * 更新SysLog设置
     * @return
     * @throws Exception
     */
    public void setSysLogConfig(final SysLog syslog) throws Exception
    {
        try
        {
            final RequestNode node = new RequestNode("updateconfig");
            final XMLNode enable = new XMLNode("enable", syslog.getEnable());
            final XMLNode protocol = new XMLNode("protocol", syslog.getProtocol());
            final XMLNode host = new XMLNode("host", syslog.getHost());
            final XMLNode port = new XMLNode("port", syslog.getPort());
            final XMLNode charset = new XMLNode("charset", syslog.getCharset());
            node.addChild(enable);
            node.addChild(protocol);
            node.addChild(host);
            node.addChild(port);
            node.addChild(charset);
            MdpUtil.request("logserver",
                    "logserver.syslogconfig.update",
                    MessageConstructor.constructMsg(node));
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            throw new Exception(e);
        }
    }

    /**
     * 获取进程状态
     * @return
     * @throws Exception
     */
    public ArrayList<ProcState> getProcState() throws Exception
    {
        final ArrayList<ProcState> stateList = new ArrayList<>();
        try
        {
            final XMLNode node = new XMLNode("qurey");
            final Message m = MdpUtil.request("cpst",
                    "cpst.process.query",
                    MessageConstructor.constructMsg(node));
            final String content = (String) m.getContent();
            final Document document = DocumentHelper.parseText(content);
            final Element query = document.getRootElement();
            final List<Element> list = query.elements("process");
            for (int i = 0; i < list.size(); i++)
            {
                final Element e = list.get(i);
                final ProcState state = new ProcState(e.element("name").getText(),
                        e.element("state").getText());
                stateList.add(state);
            }
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
        }
        return stateList;
    }

    /**
     * 操作进程状态
     * @return
     * @throws Exception
     */
    public void operProcState(final ProcOper procOper) throws Exception
    {
        try
        {
            final XMLNode oper = new XMLNode("oper");
            final XMLNode process = new XMLNode("process");
            oper.addChild(process);
            final XMLNode name = new XMLNode("name", procOper.getName());
            final XMLNode operator = new XMLNode("operator", procOper.getOperator());
            process.addChild(name);
            process.addChild(operator);
            final Message m = MdpUtil.request("cpst",
                    "cpst.process.oper",
                    MessageConstructor.constructMsg(oper),
                    120000);
            final String content = (String) m.getContent();
            final Document document = DocumentHelper.parseText(content);
            final Element root = document.getRootElement();
            final String result = root.attribute("result").getText();

            if (result.equals("failure"))
            {
                final String errorInfo = root.element("errdesc").getText();
                throw new Exception("操作进程失败,errorInfo:" + errorInfo);
            }
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            throw new Exception(e);
        }
    }

    /**
     * 获取自备份配置
     * @return
     * @throws Exception
     */
    public SelfBackup getSelfBackupConfig() throws Exception
    {
        SelfBackup selfbackup = null;
        InputStream is = null;
        BufferedReader br = null;
        try
        {
            is = new FileInputStream("/var/spool/cron/root");
            br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            String allStr = null;
            while ((line = br.readLine()) != null)
            {
                final int a = line.lastIndexOf("bkdata.sh");
                if (a != -1)
                {
                    allStr = line;
                    break;
                }
            }
            if (allStr == null)
            {
                selfbackup = new SelfBackup(0, null, null, null, null, null, false,
                        false, false, false);
                return selfbackup;
            }
            final String str = allStr.trim();
            final String[] s = str.split(" ");
            final String month = trans2Num("month", s[3]);
            final String day = trans2Num("day", s[2]);
            final String week = trans2Num("week", s[4]);
            final String hour = trans2Num("hour", s[1]);
            final String minute = trans2Num("minute", s[0]);

            selfbackup = new SelfBackup(1, month, day, week, hour, minute, ifExist(str,
                    "-b"), ifExist(str, "-m"), ifExist(str, "-i"), ifExist(str, "-u"));
            return selfbackup;
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            throw new Exception(e);
        }
        finally
        {
            try
            {
                if (br != null)
                {
                    br.close();
                }
                if (is != null)
                {
                    is.close();
                }
            }
            catch (final Exception e)
            {
                log.error(e.getLocalizedMessage(), e);
                throw new Exception(e);
            }
        }
    }

    private String trans2Num(final String name, final String string)
    {
        if (string.equals("*"))
        {
            return "-1";
        }
        else if (!(name.equals("week")) && string.length() == 1)
        {
            return "0" + string;
        }
        else
        {
            return string;
        }
    }

    private boolean ifExist(final String string, final String param)
    {
        final int a = string.lastIndexOf(param);
        if (a != -1)
        {
            return true;
        }
        return false;
    }

    /**
     * 更新自备份配置
     * @return
     * @throws Exception
     */
    public synchronized boolean setSelfBackupConfig(final SelfBackup selfbackup)
            throws Exception
    {
        try
        {
            check(selfbackup);
            final String[] delCommand = { "/bin/sh", "-c",
                    "sed -i '/bkdata.sh/d' /var/spool/cron/root" };
            final Process delProc = Runtime.getRuntime().exec(delCommand);
            final String delRes = String.valueOf(delProc.waitFor());
            if (!(delRes.equals("0")))
            {
                return false;
            }
            final String enable = String.valueOf(selfbackup.getEnable());
            if (enable.equals("0"))
            {
                return true;
            }
            final String month = trans2Str(selfbackup.getMonth());
            final String day = trans2Str(selfbackup.getDay());
            final String week = trans2Str(selfbackup.getWeek());
            final String hour = trans2Str(selfbackup.getHour());
            final String minute = trans2Str(selfbackup.getMinute());
            final boolean mysql = selfbackup.isMysql();
            final boolean mds_mongo = selfbackup.isMds_mongo();
            final boolean index_mongo = selfbackup.isIndex_mongo();
            final boolean dedup_mongo = selfbackup.isDedup_mongo();

            String addStr = "sed -i '/sh/a\\{} {} {} {} {} /xxx/unabackup/bin/bkdata.sh{}' /var/spool/cron/root";
            addStr = replace(addStr,
                    minute,
                    hour,
                    day,
                    month,
                    week,
                    params(mysql, mds_mongo, index_mongo, dedup_mongo));

            final String[] addCommand = { "/bin/sh", "-c", addStr };

            final Process addProc = Runtime.getRuntime().exec(addCommand);
            final String addRes = String.valueOf(addProc.waitFor());

            if (!(addRes.equals("0")))
            {
                return false;
            }
            return true;
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            throw new Exception(e);
        }
    }

    private void check(final SelfBackup selfbackup) throws Exception
    {
        final SimpleDateFormat format = new SimpleDateFormat("MM/dd HH:mm");
        final String date = Math.abs(Integer.parseInt(selfbackup.getMonth())) + "/"
                + Math.abs(Integer.parseInt(selfbackup.getDay())) + " "
                + Math.abs(Integer.parseInt(selfbackup.getHour())) + ":"
                + Math.abs(Integer.parseInt(selfbackup.getMinute()));
        final int week = Math.abs(Integer.parseInt(selfbackup.getWeek()));
        if (week > 7 || week < 1)
        {
            log.error("时间设置非法");
            throw new Exception("时间设置非法");
        }
        try
        {
            format.setLenient(false);
            format.parse(date);
        }
        catch (final Exception e)
        {
            log.error("时间设置非法");
            throw new Exception("时间设置非法");
        }
    }

    private String trans2Str(final String num)
    {
        if (num.equals("-1"))
        {
            return "*";
        }
        else if (num.length() == 2 && num.substring(0, 1).equals("0"))
        {
            return num.substring(1, 2);
        }
        else
        {
            return num;
        }
    }

    private String params(final boolean mysql, final boolean mds_mongo,
            final boolean index_mongo, final boolean dedup_mongo)
    {
        final StringBuffer buffer = new StringBuffer();

        if (mysql)
        {
            buffer.append(" -b");
        }
        if (mds_mongo)
        {
            buffer.append(" -m");
        }
        if (index_mongo)
        {
            buffer.append(" -i");
        }
        if (dedup_mongo)
        {
            buffer.append(" -u");
        }
        final String str = buffer.toString();
        return str;
    }

    private String replace(final String addStr, final String... args)
    {
        int i = 0;
        final StringBuffer addBuffer = new StringBuffer(addStr);
        int index = -1;
        while (args != null && i < args.length
                && ((index = addBuffer.indexOf("{}")) != -1))
        {
            addBuffer.replace(index, index + 2, args[i]);
            i++;
        }
        return addBuffer.toString();
    }

    /**
     * 获取服务状态
     * @return
     * @throws Exception
     */
    public ArrayList<ServiceManage> getServiceConfig() throws Exception
    {
        final ArrayList<ServiceManage> servicelist = new ArrayList<>();
        final File file = new File("/xxx/unabackup/conf/serviceName.xml");
        final SAXReader reader = new SAXReader();
        final Document document = reader.read(file);
        final Element root = document.getRootElement();
        final List list = root.elements("service");
        for (int i = 0; i < list.size(); i++)
        {
            final Element service = (Element) list.get(i);
            final ServiceManage servManage = new ServiceManage(service.getText(), null,
                    null);
            servManage.setState(servManage.checkService(servManage.getName()));
            servicelist.add(servManage);
        }
        return servicelist;
    }

    /**
     * 操作服务状态
     * @return
     * @throws Exception
     */
    public boolean setServiceConfig(final ServiceManage servManage) throws Exception
    {
        try
        {
            boolean res;
            if (servManage.getName().equals("iptables"))
            {
                res = servManage.operate("iptables", servManage.getOper());
            }
            else
            {
                throw new Exception("no such service");
            }
            return res;
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            throw new Exception(e);
        }
    }
}

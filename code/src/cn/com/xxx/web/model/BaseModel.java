package cn.com.xxx.web.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.ws.rs.core.Application;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.net.www.protocol.https.Handler;
import cn.com.xxx.common.common.CommonUtil;
import cn.com.xxx.common.msg.ResponseNode;
import cn.com.xxx.mdp.MdpUtil;
import cn.com.xxx.mdp.api.MessageListener;
import cn.com.xxx.mdp.model.Message;
import cn.com.xxx.web.ApplicationServlet;
import cn.com.xxx.web.pojo.HttpResponse;
import cn.com.xxx.web.pojo.LogItem;
import cn.com.xxx.web.pojo.LogLevel;

import com.xxx.common.utils.HexCoding;

public class BaseModel
{

    private static final int DEFAULT_TIMEOUT = 30000;

    private static final String MSG_LOG_ADD = "logrecorder.write";

    private static final String VERSION = "1";

    protected static final Logger log = LoggerFactory.getLogger(BaseModel.class);

    protected Logger getLogger()
    {
        return LoggerFactory.getLogger(this.getClass());
    }

    /**
     * 基于dom的消息发送
     * @param destId
     * @param msgName
     * @param doc
     * @return Document
     * @throws ProtocolException
     */
    public Document requestMsg(final String destId, final String msgName,
            final Document doc) throws ProtocolException
    {
        return requestMsg(destId, msgName, doc, DEFAULT_TIMEOUT);
    }

    /**
     * 基于dom的消息发送
     * @param destId
     * @param msgName
     * @param doc
     * @param timeout
     * @return Document
     * @throws ProtocolException
     */
    public Document requestMsg(final String destId, final String msgName,
            final Document doc, final int timeout) throws ProtocolException
    {
        return requestMsg(destId, msgName, doc.asXML(), timeout);
    }

    /**
     * 基于Serializable的消息发送
     * @param destId
     * @param msgName
     * @param msgContent
     * @return Document
     * @throws ProtocolException
     */
    public Document requestMsg(final String destId, final String msgName,
            final Serializable msgContent) throws ProtocolException
    {
        return requestMsg(destId, msgName, msgContent, DEFAULT_TIMEOUT);
    }

    /**
     * 基于Serializable的消息发送
     * @param destId
     * @param msgName
     * @param msgContent
     * @param timeout
     * @return Document
     * @throws ProtocolException
     */
    public Document requestMsg(final String destId, final String msgName,
            final Serializable msgContent, final int timeout) throws ProtocolException
    {
        if (!ApplicationServlet.mdpStarted)
        {
            getLogger().warn("MDP is not started, trying to start.");
            try
            {
                MdpUtil.start("../conf/web.properties");
                ApplicationServlet.mdpStarted = true;
                getLogger().info("MDP is started.");
            }
            catch (final Exception e)
            {
                getLogger().error("MDP error.");
                getLogger().error(e.getLocalizedMessage(), e);
                throw new ProtocolException("MDP未开启！");
            }
        }

        final Message response = MdpUtil.request(destId, msgName, msgContent, timeout);
        if (null == response)
        {
            getLogger().warn("Response null ({}, {}). Timeout.", destId, msgName);
            throw new ProtocolException("请求超时");
        }

        String respStr = null;
        try
        {
            respStr = (String) response.getContent();
        }
        catch (final Exception e)
        {
            getLogger().warn("Get response content error.");
            getLogger().error(e.getLocalizedMessage(), e);
            throw new ProtocolException("获得响应内容错误！");
        }
        getLogger().debug("Server response: {}", respStr);

        Document respDoc = null;
        try
        {
            respDoc = DocumentHelper.parseText(respStr);
        }
        catch (final DocumentException e)
        {
            getLogger().warn("Convert response error.");
            getLogger().error(e.getLocalizedMessage(), e);
            throw new ProtocolException("转换响应出错！");
        }

        return respDoc;
    }

    /**
     * 基于dom的消息发送 异步
     * @param destId String
     * @param msgName String
     * @param doc Document
     * @param messageListener messageListener
     * @throws ProtocolException
     */
    public void requestMsgAsync(final String destId, final String msgName,
            final Document doc, final MessageListener messageListener)
            throws ProtocolException
    {

        requestMsgAsync(destId, msgName, doc, messageListener, DEFAULT_TIMEOUT);
    }

    /**
     * 基于dom的消息发送 异步
     * @param destId String
     * @param msgName String
     * @param doc Document
     * @param messageListener messageListener
     * @param timeout timeout
     * @throws ProtocolException
     */
    public void requestMsgAsync(final String destId, final String msgName,
            final Document doc, final MessageListener messageListener, final int timeout)
            throws ProtocolException
    {
        requestMsgAsync(destId, msgName, doc.asXML(), messageListener, timeout);
    }

    /**
     * 基于dom的消息发送 异步
     * @param destId String
     * @param msgName String
     * @param msgContent Serializable
     * @param messageListener messageListener
     * @throws ProtocolException
     */
    public void requestMsgAsync(final String destId, final String msgName,
            final Serializable msgContent, final MessageListener messageListener)
            throws ProtocolException
    {
        requestMsgAsync(destId, msgName, msgContent, messageListener, DEFAULT_TIMEOUT);
    }

    /**
     * 基于dom的消息发送 异步
     * @param destId String
     * @param msgName String
     * @param msgContent Serializable
     * @param messageListener messageListener
     * @param timeout timeout
     * @throws ProtocolException
     */
    public void requestMsgAsync(final String destId, final String msgName,
            final Serializable msgContent, final MessageListener messageListener,
            final int timeout) throws ProtocolException
    {
        if (!ApplicationServlet.mdpStarted)
        {
            getLogger().warn("MDP is not started, trying to start.");
            try
            {
                MdpUtil.start("../conf/web.properties");
                ApplicationServlet.mdpStarted = true;
                getLogger().info("MDP is started.");
            }
            catch (final Exception e)
            {
                getLogger().error("MDP error.");
                getLogger().error(e.getLocalizedMessage(), e);
                throw new ProtocolException("MDP未开启！");
            }
        }

        MdpUtil.request(destId, msgName, msgContent, messageListener, timeout);
    }

    public void sendMsg(final String destId, final String msgName, final Document doc,
            final boolean persistent) throws ProtocolException
    {
        sendMsg(destId, msgName, doc.asXML(), persistent);
    }

    public void sendMsg(final String destId, final String msgName,
            final Serializable msgContent, final boolean persistent)
            throws ProtocolException
    {
        if (!ApplicationServlet.mdpStarted)
        {
            getLogger().warn("MDP is not started, trying to start.");
            try
            {
                MdpUtil.start("../conf/web.properties");
                ApplicationServlet.mdpStarted = true;
                getLogger().info("MDP is started.");
            }
            catch (final Exception e)
            {
                getLogger().error("MDP error.");
                getLogger().error(e.getLocalizedMessage(), e);
                throw new ProtocolException("MDP未开启！");
            }
        }

        MdpUtil.inform(destId, msgName, msgContent, persistent);
    }

    /**
     * 检查xml消息响应状态
     * @param doc Document
     * @throws ProtocolException
     */
    @SuppressWarnings("unchecked")
    public void checkResponseResult(final Document doc) throws ProtocolException
    {
        final List<Attribute> attr = doc.selectNodes("/*/@result");
        if (CommonUtil.isEmpty(attr))
        {
            getLogger().warn("Unknown result status.");
            throw new ProtocolException("未知的结果状态");
        }

        if (ResponseNode.FAILURE.equalsIgnoreCase(attr.get(0).getValue()))
        {
            // error
            final List<Element> els = doc.selectNodes("/*/errdesc");
            final org.dom4j.Node errno = doc.selectSingleNode("/*/errno");
            final String errornum = (errno == null ? "" : errno.getText());
            if (CommonUtil.isEmpty(els) && CommonUtil.isEmpty(errornum))
            {
                getLogger().warn("Response error, but no content.");
                throw new ProtocolException("响应错误，但是没有错误说明！");
            }

            String errorinfo = this.readProperties(errornum);
            if (CommonUtil.isEmpty(errorinfo))
            {
                errorinfo = els.get(0).getText();
            }

            getLogger().warn("Server responsed: {}", errorinfo);
            throw new ProtocolException(errorinfo);
        }

        getLogger().debug("Response OK.");
    }

    // 从配置文件中读取属性
    public String readPropertiesValue(final String path, final String key)
    {
        String value = "";
        if (CommonUtil.isEmpty(key))
        {
            return value;
        }

        final Properties prop = new Properties();

        try
        {
            final FileInputStream in = new FileInputStream(path);
            prop.load(in);
            final String temp = prop.getProperty(key);
            if (null != temp)
            {
                value = temp.trim();
            }
        }
        catch (final Exception e)
        {
            getLogger().error(e.getLocalizedMessage(), e);
        }

        return value;
    }

    // 从配置文件中读取错误信息
    public String readProperties(final String errornum)
    {
        String errorinfo = "";
        if (CommonUtil.isEmpty(errornum))
        {
            return errorinfo;
        }

        final String mybatisConfig = new File(Application.class.getResource("/")
                .getPath()).getParent();
        final Properties prop = new Properties();

        final File file = new File(mybatisConfig);
        final File[] tempList = file.listFiles();
        // 分步读取.properties文件
        if (tempList != null)
        {
            for (final File f : tempList)
            {
                final String filepath = f.toString();
                if (filepath.endsWith("info.properties"))
                {
                    try (FileInputStream in = new FileInputStream(filepath))
                    {
                        prop.load(in);
                    }
                    catch (final IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        try
        {
            // 获取错误码解释，并转为中文
            errorinfo = prop.getProperty(errornum).trim();
            errorinfo = new String(errorinfo.getBytes("ISO-8859-1"), "utf-8");
        }
        catch (final Exception ex)
        {
            ex.printStackTrace();
        }
        return errorinfo;
    }

    public void error(final String desc, final int mainType, final String user,
            final String ip) throws ProtocolException
    {
        addLog(webLog(desc, LogLevel.ERROR, mainType, user, ip));
    }

    public void warn(final String desc, final int mainType, final String user,
            final String ip) throws ProtocolException
    {
        addLog(webLog(desc, LogLevel.WARN, mainType, user, ip));
    }

    public void info(final String desc, final int mainType, final String user,
            final String ip) throws ProtocolException
    {
        addLog(webLog(desc, LogLevel.INFO, mainType, user, ip));
    }

    private LogItem webLog(final String desc, final int lv, final int mainType,
            final String user, final String ip)
    {
        final LogItem log = new LogItem();
        log.description = desc;
        log.level = String.valueOf(lv);
        log.type = String.valueOf(mainType);
        log.user = user;
        log.ip = ip;
        log.subType = "0";

        log.time = System.currentTimeMillis();
        log.componentName = "WEB";
        log.componentId = "WEB";
        return log;
    }

    /**
     * 记录日志
     * @param log LogItem
     * @throws ProtocolException
     */
    protected void addLog(final LogItem log) throws ProtocolException
    {
        final Document reqDoc = DocumentHelper.createDocument();
        final Element root = reqDoc.addElement("auditlog");
        root.addElement("ver").setText(VERSION);
        root.addElement("tm").setText(String.valueOf(log.time));
        root.addElement("level").setText(String.valueOf(log.level));
        final Element type = root.addElement("type");
        type.addAttribute("main", String.valueOf(log.type));
        type.addAttribute("sub", String.valueOf(log.subType));
        root.addElement("user").setText(log.user);
        root.addElement("ip").setText(log.ip);
        final Element comp = root.addElement("component");
        comp.addElement("id").setText(log.componentId);
        comp.addElement("name").setText(log.componentName);
        root.addElement("desc").setText(log.description);
        sendMsg("logrecorder", MSG_LOG_ADD, reqDoc, true);
    }

    /**
     * 加密
     * @param in
     * @return
     * @throws Exception
     */
    @SuppressWarnings("static-access")
    public static String encrypt(final String in) throws Exception
    {
        final HexCoding hex = new HexCoding();
        final String key = hex.C_STR_TAG2 + hex.C_STR_TAG3 + hex.C_STR_TAG4;
        try
        {
            return hex.string2HexWithEncrypt(in, key.getBytes("utf-8"));
        }
        catch (final UnsupportedEncodingException e)
        {
            log.error(e.getLocalizedMessage(), e);
            throw e;
        }
    }

    /**
     * 解密
     * @param in
     * @return
     * @throws Exception
     */
    @SuppressWarnings("static-access")
    public static String decrypt(final String in) throws Exception
    {
        final HexCoding hex = new HexCoding();
        final String key = hex.C_STR_TAG2 + hex.C_STR_TAG3 + hex.C_STR_TAG4;
        try
        {
            return hex.hex2StringWithDecrypt(in, key.getBytes("utf-8"));
        }
        catch (final UnsupportedEncodingException e)
        {
            log.error(e.getLocalizedMessage(), e);
            throw e;
        }
    }

    /**
     * 发送HTTP请求
     * @param requestMedthod
     * @param urlStr
     * @param headerParams
     * @param queryParams
     * @param body
     * @param timeout
     * @return
     */
    public HttpResponse sendHttpRequest(final String requestMedthod, final String urlStr,
            final Map<String, String> headerParams,
            final Map<String, String> queryParams, String body, final Integer timeout)
    {
        // 创建连接
        URL url;
        HttpURLConnection connection;
        StringBuffer sbuffer = null;
        final HttpResponse result = new HttpResponse(false);
        if (null == body)
        {
            body = "";
        }

        // 构建请求参数
        final StringBuffer qParams = new StringBuffer();
        if (queryParams != null && queryParams.size() > 0)
        {
            for (final Entry<String, String> entry : queryParams.entrySet())
            {
                qParams.append(entry.getKey());
                qParams.append("=");
                qParams.append(entry.getValue());
                qParams.append("&");
            }
        }

        try
        {
            if (null != qParams && qParams.length() > 0)
            {
                url = new URL(urlStr + "?" + qParams.substring(0, qParams.length() - 1));
            }
            else
            {
                url = new URL(urlStr);
            }

            // 添加 请求内容
            connection = (HttpURLConnection) url.openConnection();
            // 设置http连接属性
            connection.setDoOutput(true);// http正文内，因此需要设为true, 默认情况下是false;
            connection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
            connection.setRequestMethod(requestMedthod); // 可以根据需要
                                                         // 提交GET、POST、DELETE、PUT等http提供的功能
            if ("get".equalsIgnoreCase(requestMedthod))
            {
                connection.setUseCaches(true);// 设置缓存，注意设置请求方法为post不能用缓存
            }

            connection.setRequestProperty("Content-Type", "application/json");// 设定 请求格式

            // 设置自定义的header参数
            if (!CommonUtil.isEmpty(headerParams))
            {
                for (final Entry<String, String> entry : headerParams.entrySet())
                {
                    connection.setRequestProperty(entry.getKey(),
                            URLEncoder.encode(entry.getValue(), "utf-8"));
                }
            }

            // 设置连接超时时间
            connection.setConnectTimeout((null == timeout || timeout == 0)
                    ? DEFAULT_TIMEOUT : timeout);
            connection.connect();

            if (!CommonUtil.isEmpty(body))
            {
                final OutputStream out = connection.getOutputStream();// 向对象输出流写出数据，这些数据将存到内存缓冲区中
                out.write(body.toString().getBytes("utf-8"));

                // 刷新对象输出流，将任何字节都写入潜在的流中
                out.flush();
                // 关闭流对象,此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中
                out.close();
                // 读取响应
            }

            if (connection.getResponseCode() == 200)
            {
                // 从服务器获得一个输入流
                final InputStreamReader inputStream = new InputStreamReader(
                        connection.getInputStream());// 调用HttpURLConnection连接对象的getInputStream()函数,
                                                     // 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
                final BufferedReader reader = new BufferedReader(inputStream);
                String lines;
                sbuffer = new StringBuffer("");
                while ((lines = reader.readLine()) != null)
                {
                    lines = new String(lines.getBytes(), "utf-8");
                    sbuffer.append(lines);
                }
                reader.close();
                result.setSuccess(true);
                result.setResCode(connection.getResponseCode());
                result.setResMessage(sbuffer.toString());
            }
            else
            {
                result.setSuccess(false);
                result.setResCode(connection.getResponseCode());
                result.setResMessage(connection.getResponseMessage());
            }

            // 断开连接
            connection.disconnect();
        }
        catch (final IOException e)
        {
            e.printStackTrace();
            log.error(e.getLocalizedMessage(), e);
            return result;
        }
        return result;
    }

    /**
     * 发送HTTPS请求
     * @param requestMedthod
     * @param urlStr
     * @param headerParams
     * @param queryParams
     * @param body
     * @param timeout
     * @return
     */
    public static HttpResponse sendHttpsRequest(final String requestMedthod,
            final String urlStr, final Map<String, String> headerParams,
            final Map<String, String> queryParams, String body, final Integer timeout)
    {
        // 创建连接
        URL url;
        final HttpResponse result = new HttpResponse(false);
        try
        {
            final SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null,
                    new TrustManager[] { new TrustAnyTrustManager() },
                    new java.security.SecureRandom());
            StringBuffer sbuffer = null;

            if (null == body)
            {
                body = "";
            }

            // 构建请求参数
            final StringBuffer qParams = new StringBuffer();
            if (queryParams != null && queryParams.size() > 0)
            {
                for (final Entry<String, String> entry : queryParams.entrySet())
                {
                    qParams.append(entry.getKey());
                    qParams.append("=");
                    qParams.append(entry.getValue());
                    qParams.append("&");
                }
            }
            if (null != qParams && qParams.length() > 0)
            {
                if (urlStr.indexOf("?") == -1)
                {
                    url = new URL(null, urlStr + "?"
                            + qParams.substring(0, qParams.length() - 1), new Handler());
                }
                else
                {
                    url = new URL(null, urlStr + "&"
                            + qParams.substring(0, qParams.length() - 1), new Handler());
                }
            }
            else
            {
                url = new URL(null, urlStr, new Handler());
            }

            // 添加 请求内容
            HttpsURLConnection connection;

            connection = (HttpsURLConnection) url.openConnection();

            // 设置https相关属性
            connection.setSSLSocketFactory(sc.getSocketFactory());
            connection.setHostnameVerifier(new TrustAnyHostnameVerifier());

            // 设置连接属性
            connection.setDoOutput(true);// https正文内，因此需要设为true, 默认情况下是false;
            connection.setDoInput(true);// 设置是否从httpsUrlConnection读入，默认情况下是true;
            connection.setRequestMethod(requestMedthod); // 可以根据需要
                                                         // 提交GET、POST、DELETE、PUT等http提供的功能
            if ("get".equalsIgnoreCase(requestMedthod))
            {
                connection.setUseCaches(true);// 设置缓存，注意设置请求方法为post不能用缓存
            }

            connection.setRequestProperty("Content-Type", "application/json");// 设定 请求格式

            // 设置自定义的header参数
            if (!CommonUtil.isEmpty(headerParams))
            {
                for (final Entry<String, String> entry : headerParams.entrySet())
                {
                    connection.setRequestProperty(entry.getKey(),
                            URLEncoder.encode(entry.getValue(), "utf-8"));
                }
            }

            // 设置连接超时时间
            connection.setConnectTimeout((null == timeout || timeout == 0) ? 30000
                    : timeout);

            try
            {
                connection.connect();

                if (!CommonUtil.isEmpty(body))
                {
                    final OutputStream out = connection.getOutputStream();// 向对象输出流写出数据，这些数据将存到内存缓冲区中
                    out.write(body.toString().getBytes("utf-8"));

                    // 刷新对象输出流，将任何字节都写入潜在的流中
                    out.flush();
                    // 关闭流对象,此时，不能再向对象输出流写入任何数据，先前写入的数据存在于内存缓冲区中
                    out.close();
                    // 读取响应
                }

                if (connection.getResponseCode() == 200)
                {
                    // 从服务器获得一个输入流
                    final InputStreamReader inputStream = new InputStreamReader(
                            connection.getInputStream());// 调用HttpsURLConnection连接对象的getInputStream()函数,
                                                         // 将内存缓冲区中封装好的完整的HTTP请求电文发送到服务端。
                    final BufferedReader reader = new BufferedReader(inputStream);
                    String lines;
                    sbuffer = new StringBuffer("");
                    while ((lines = reader.readLine()) != null)
                    {
                        lines = new String(lines.getBytes(), "utf-8");
                        sbuffer.append(lines);
                    }
                    reader.close();
                    result.setSuccess(true);
                    result.setResCode(connection.getResponseCode());
                    result.setResMessage(sbuffer.toString());
                }
                else
                {
                    result.setSuccess(false);
                    result.setResCode(connection.getResponseCode());
                    result.setResMessage(connection.getResponseMessage());
                    log.warn(result.getResCode() + " " + urlStr + " "
                            + result.getResMessage());
                }

                // 断开连接
                connection.disconnect();
            }
            catch (final ConnectException e)
            {
                log.debug("Request failed.Cause:{}. requestMedthod={},urlStr={}",
                        e.getMessage(),
                        requestMedthod,
                        urlStr);
            }
            catch (final SocketTimeoutException e)
            {
                log.debug("connect timed out. requestMedthod={},urlStr={}",
                        requestMedthod,
                        urlStr);
                return result;
            }
        }
        catch (final Exception e)
        {
            log.error(e.getLocalizedMessage(), e);
            return result;
        }
        return result;
    }

    private static class TrustAnyTrustManager implements X509TrustManager
    {

        @Override
        public void checkClientTrusted(final X509Certificate[] chain,
                final String authType) throws CertificateException
        {
        }

        @Override
        public void checkServerTrusted(final X509Certificate[] chain,
                final String authType) throws CertificateException
        {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers()
        {
            return new X509Certificate[] {};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier
    {
        @Override
        public boolean verify(final String hostname, final SSLSession session)
        {
            return true;
        }
    }
}

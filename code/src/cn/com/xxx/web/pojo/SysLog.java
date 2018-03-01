package cn.com.xxx.web.pojo;

public class SysLog
{
    private String enable;
    private String protocol;
    private String host;
    private String port;
    private String charset;

    public SysLog()
    {
    }
	
	public SysLog(final String enable, final String protocol, final String host,
            final String port, final String charset)
    {
        this.enable = enable;
        this.protocol = protocol;
        this.host = host;
        this.port = port;
        this.charset = charset;
    }

    public String getEnable()
    {
        return enable;
    }

    public void setEnable(final String enable)
    {
        this.enable = enable;
    }

    public String getProtocol()
    {
        return protocol;
    }

    public void setProtocol(final String protocol)
    {
        this.protocol = protocol;
    }

    public String getHost()
    {
        return host;
    }

    public void setHost(final String host)
    {
        this.host = host;
    }

    public String getPort()
    {
        return port;
    }

    public void setPort(final String port)
    {
        this.port = port;
    }

    public String getCharset()
    {
        return charset;
    }

    public void setCharset(final String charset)
    {
        this.charset = charset;
    }

}

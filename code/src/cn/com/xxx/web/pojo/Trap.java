package cn.com.xxx.web.pojo;

public class Trap
{
    private int enable;
    private String host;
    private String port;
    private String charset;
    private int protocol;
    private int type;
    private String community;
    private int level;
    private String username;
    private int digest;
    private String password;
    private int encrypt;
    private String priv;
    private String context;

    public Trap()
    {
    }
	
	public Trap(final int enable, final String host, final String port,
            final String charset, final int protocol, final int type,
            final String community, final int level, final String username,
            final int digest, final String password, final int encrypt,
            final String priv, final String context)
    {
        this.enable = enable;
        this.host = host;
        this.port = port;
        this.charset = charset;
        this.protocol = protocol;
        this.type = type;
        this.community = community;
        this.level = level;
        this.username = username;
        this.digest = digest;
        this.password = password;
        this.encrypt = encrypt;
        this.priv = priv;
        this.context = context;
    }

    public int getEnable()
    {
        return enable;
    }

    public void setEnable(final int enable)
    {
        this.enable = enable;
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

    public int getProtocol()
    {
        return protocol;
    }

    public void setProtocol(final int protocol)
    {
        this.protocol = protocol;
    }

    public int getType()
    {
        return type;
    }

    public void setType(final int type)
    {
        this.type = type;
    }

    public String getCommunity()
    {
        return community;
    }

    public void setCommunity(final String community)
    {
        this.community = community;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(final int level)
    {
        this.level = level;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(final String username)
    {
        this.username = username;
    }

    public int getDigest()
    {
        return digest;
    }

    public void setDigest(final int digest)
    {
        this.digest = digest;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(final String password)
    {
        this.password = password;
    }

    public int getEncrypt()
    {
        return encrypt;
    }

    public void setEncrypt(final int encrypt)
    {
        this.encrypt = encrypt;
    }

    public String getPriv()
    {
        return priv;
    }

    public void setPriv(final String priv)
    {
        this.priv = priv;
    }

    public String getContext()
    {
        return context;
    }

    public void setContext(final String context)
    {
        this.context = context;
    }

}

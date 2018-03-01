package cn.com.xxx.web.pojo;

public class Monitor
{
    private int enable;
    private String source;
    private String snmpV1;
    private String v1community;
    private String snmpV2c;
    private String v2community;
    private String snmpV3;
    private int level;
    private String username;
    private int digest;
    private String password;
    private int encrypt;
    private String priv;
    private String context;

    public Monitor()
    {
    }

    public Monitor(final int enable, final String source, final String snmpV1,
            final String v1community, final String snmpV2c, final String v2community,
            final String snmpV3, final int level, final String username,
            final int digest, final String password, final int encrypt,
            final String priv, final String context)
    {
        this.enable = enable;
        this.source = source;
        this.snmpV1 = snmpV1;
        this.v1community = v1community;
        this.snmpV2c = snmpV2c;
        this.v2community = v2community;
        this.snmpV3 = snmpV3;
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

    public String getSource()
    {
        return source;
    }

    public void setSource(final String source)
    {
        this.source = source;
    }

    public String getSnmpV1()
    {
        return snmpV1;
    }

    public void setSnmpV1(final String snmpV1)
    {
        this.snmpV1 = snmpV1;
    }

    public String getV1community()
    {
        return v1community;
    }

    public void setV1community(final String v1community)
    {
        this.v1community = v1community;
    }

    public String getSnmpV2c()
    {
        return snmpV2c;
    }

    public void setSnmpV2c(final String snmpV2c)
    {
        this.snmpV2c = snmpV2c;
    }

    public String getV2community()
    {
        return v2community;
    }

    public void setV2community(final String v2community)
    {
        this.v2community = v2community;
    }

    public String getSnmpV3()
    {
        return snmpV3;
    }

    public void setSnmpV3(final String snmpV3)
    {
        this.snmpV3 = snmpV3;
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

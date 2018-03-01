package cn.com.xxx.web.pojo;

import cn.com.xxx.common.cmd.ExecuteResult;
import cn.com.xxx.common.cmd.NativeCallTool;
import cn.com.xxx.common.cmd.ResultParser;
import cn.com.xxx.web.CMD;

public class ServiceManage
{
    private String name;
    private String state;
    private String oper;

    public ServiceManage()
    {
    }

    public ServiceManage(final String name, final String state, final String oper)
    {
        this.name = name;
        this.state = state;
        this.oper = oper;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public String getState()
    {
        return state;
    }

    public void setState(final String state)
    {
        this.state = state;
    }

    public String getOper()
    {
        return oper;
    }

    public void setOper(final String oper)
    {
        this.oper = oper;
    }

    @Override
    public String toString()
    {
        return "ServiceManage [name=" + name + ", state=" + state + ", oper=" + oper
                + "]";
    }

    ResultParser<ExecuteResult> parser = new ResultParser<ExecuteResult>()
        {

            @Override
            public ExecuteResult parse(final ExecuteResult paramExecuteResult)
            {
                return paramExecuteResult;
            }
        };

    public boolean operate(final String name, final String oper) throws Exception
    {
        switch (getOper())
        {
            case "0":
                if (checkService(name).equals("running"))
                {
                    final ExecuteResult res = NativeCallTool.call(CMD.IPTABLES_STOP,
                            parser);
                    if (res.getExitValue() != 0)
                    {
                        return false;
                    }
                    return true;
                }
                else
                {
                    return true;
                }
            case "1":
                if (checkService(name).equals("stopping"))
                {
                    final ExecuteResult res = NativeCallTool.call(CMD.IPTABLES_START,
                            parser);
                    if (res.getExitValue() != 0)
                    {
                        return false;
                    }
                    return true;
                }
                else
                {
                    return true;
                }
            default:
                throw new Exception("no such oper");
        }
    }

    public String checkService(final String service) throws Exception
    {
        if (service.equals("iptables"))
        {
            final ExecuteResult res = NativeCallTool.call(CMD.IPTABLES_GET_STATUS, parser);
            if (res.getExitValue() == 0)
            {
                return "running";
            }
            if (res.getExitValue() == 3)
            {
                return "stopping";
            }
            else
            {
                throw new Exception("exec执行结果：" + res);
            }
        }
        else
        {
            throw new Exception("no such service");
        }

    }
}

package rob.test.activemq.model;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

public class MessagePayload implements Serializable
{
    private static final long serialVersionUID = 1L;

    @NotNull
    private Date sent;

    private String content;

    public Date getSent()
    {
        return sent;
    }

    public void setSent(Date sent)
    {
        this.sent = sent;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}

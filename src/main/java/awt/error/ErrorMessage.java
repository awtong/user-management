package awt.error;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.NONE)
public class ErrorMessage {
    @XmlElement(name = "field")
    private String field;

    @XmlElement(name = "message")
    private String message;

    public String getField() {
	return this.field;
    }

    public void setField(final String field) {
	this.field = field;
    }

    public String getMessage() {
	return this.message;
    }

    public void setMessage(final String message) {
	this.message = message;
    }

    @Override
    public String toString() {
	return "ErrorMessage [field=" + this.field + ", message=" + this.message + "]";
    }
}

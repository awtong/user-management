package awt.error;

import java.util.*;

import javax.xml.bind.annotation.*;

import org.slf4j.MDC;

import awt.diagnostics.DiagnosticsFilter;

@XmlRootElement(name = "errors")
@XmlAccessorType(XmlAccessType.NONE)
public class ErrorMessages {
    @XmlElement(name = "id")
    private final String logId;

    @XmlElement(name = "error")
    private final Collection<ErrorMessage> errors = new HashSet<>();

    public ErrorMessages() {
	this.logId = MDC.get(DiagnosticsFilter.LOGGING_ID);
    }

    public String getLogId() {
	return this.logId;
    }

    public boolean addError(final ErrorMessage error) {
	return this.errors.add(error);
    }

    public Collection<ErrorMessage> getErrors() {
	return this.errors;
    }
}

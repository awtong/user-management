package awt.util;

import java.util.UUID;

import javax.xml.bind.annotation.adapters.XmlAdapter;

// Only needed because there is a bug in MOXY which prevents UUID from being handled correctly
// There is a patch in place to fix this in the core. Once that is fixed, this class will no longer be needed.
public class UUIDAdapter extends XmlAdapter<String, UUID> {

    @Override
    public String marshal(final UUID id) throws Exception {
	return id == null ? null : id.toString();
    }

    @Override
    public UUID unmarshal(final String id) throws Exception {
	return id == null ? null : UUID.fromString(id);
    }
}

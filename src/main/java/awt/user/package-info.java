@XmlJavaTypeAdapters({
	@XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
})
package awt.user;

import javax.xml.bind.annotation.adapters.*;

import awt.util.OffsetDateTimeAdapter;

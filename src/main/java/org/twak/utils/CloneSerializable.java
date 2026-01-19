package org.twak.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedOutputStream;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.binary.BinaryStreamDriver;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 *
 * @author twak
 */
public class CloneSerializable
{
    /**
     * Configures XStream with a secure whitelist of allowed types.
     * Uses single-level wildcards to allow only direct classes in specific packages,
     * not arbitrary subpackages, balancing security with maintainability.
     */
    private static void configureXStreamSecurity(XStream xstream) {
        xstream.allowTypesByWildcard(new String[] {
            // Core camp classes (direct package only, not subpackages)
            "org.twak.camp.*",
            // Camp UI classes
            "org.twak.camp.ui.*",
            // Camp offset classes
            "org.twak.camp.offset.*",
            // Utility classes
            "org.twak.utils.*",
            // Utility collections
            "org.twak.utils.collections.*",
            // Utility geometry
            "org.twak.utils.geom.*",
            // javax.vecmath for Point3d, Vector3d, etc.
            "javax.vecmath.*"
        });
    }

    public static Object clone( Object orig )
    {
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream( baos );
            oos.writeObject( orig );
            oos.flush();
            oos.close();

            ObjectInputStream in = new ObjectInputStream( new ByteArrayInputStream( baos.toByteArray() ) );
            return in.readObject();
        } catch ( Throwable th )
        {
            th.printStackTrace();
            return null;
        }
    }
    
    public static Object xClone (Object orig) {
    	
    	XStream x = new XStream(new BinaryStreamDriver());
    	configureXStreamSecurity(x);

    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	x.toXML( orig, bos );
    	
    	return x.fromXML( new ByteArrayInputStream( bos.toByteArray() ) );
    	
//        return XSTREAM.fromXML(XSTREAM.toXML(orig));
    }
}

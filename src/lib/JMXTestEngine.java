import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import net.sf.dozer.util.mapping.DozerBeanMapper;
import net.sf.dozer.util.mapping.MapperIF;
import net.sf.dozer.util.mapping.jmx.DozerAdminController;
import net.sf.dozer.util.mapping.jmx.DozerStatisticsController;
import net.sf.dozer.util.mapping.util.MapperConstants;
import net.sf.dozer.util.mapping.util.TestDataFactory;
import net.sf.dozer.util.mapping.vo.SimpleObj;
import net.sf.dozer.util.mapping.vo.SimpleObjPrime2;
import net.sf.dozer.util.mapping.vo.TestObject;
import net.sf.dozer.util.mapping.vo.TestObjectPrime;

public class JMXTestEngine {
  
  public static void main(String[] args) throws Exception {
    System.setProperty(MapperConstants.CONFIG_FILE_SYS_PROP, "samplecustomdozer.properties");
  	MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
    ObjectName name = new ObjectName("com.example.mbeans:type=DozerStatisticsController");
    ObjectName name2 = new ObjectName("com.example.mbeans:type=DozerAdminController");
    DozerStatisticsController mbean = new DozerStatisticsController();
    DozerAdminController mbean2 = new DozerAdminController();

    mbs.registerMBean(mbean, name);
    mbs.registerMBean(mbean2, name2);
    performSomeMappings();

    System.out.println("Waiting forever...");
	  Thread.sleep(Long.MAX_VALUE);
  }
  
  private static void performSomeMappings() {
    List mappingFiles = new ArrayList();
    mappingFiles.add("dozerBeanMapping.xml");
    MapperIF mapper = new DozerBeanMapper(mappingFiles);

    try {
      mapper.map(new String("yo"), new String("y"));
    } catch (Throwable t) {
    }
    
    try {
      mapper.map(null, null);
    } catch (Throwable t) {
    }
    try {
      mapper.map(new String(),null);
    } catch (Throwable t) {
    }
    
    TestObject to = TestDataFactory.getInputGeneralMappingTestObject();
    TestObjectPrime prime = (TestObjectPrime) mapper.map(to, TestObjectPrime.class);
    TestObject source = (TestObject) mapper.map(prime, TestObject.class);
    mapper.map(source, TestObjectPrime.class);
    
    int numIters = 4000;
    for (int i=0;i<numIters;i++) {
      SimpleObj src = (SimpleObj) TestDataFactory.getSimpleObj();
      mapper.map(src, SimpleObjPrime2.class);
    }
  }
}

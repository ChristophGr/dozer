/*
 * Copyright 2005-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.dozer.util.mapping;

import net.sf.dozer.util.mapping.util.TestDataFactory;
import net.sf.dozer.util.mapping.vo.SimpleObj;
import net.sf.dozer.util.mapping.vo.SimpleObjPrime;
import net.sf.dozer.util.mapping.vo.SimpleObjPrime2;
import net.sf.dozer.util.mapping.vo.TestObject;
import net.sf.dozer.util.mapping.vo.TestObjectPrime;
import net.sf.dozer.util.mapping.vo.deep.DestDeepObj;
import net.sf.dozer.util.mapping.vo.deep.SrcDeepObj;
import net.sf.dozer.util.mapping.vo.inheritance.AnotherSubClass;
import net.sf.dozer.util.mapping.vo.inheritance.AnotherSubClassPrime;
import net.sf.dozer.util.mapping.vo.perf.MyClassA;
import net.sf.dozer.util.mapping.vo.perf.MyClassB;

import org.apache.commons.lang.time.StopWatch;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author garsombke.franz
 * @author sullins.ben
 * @author tierney.matt
 * 
 */
public class PerformanceTest extends DozerTestBase {

  private static Log log = LogFactory.getLog(PerformanceTest.class);

  private int numIters = 10;
  
  private static MapperIF mapper;

  protected void setUp() throws Exception {
    super.setUp();
    if (mapper == null) {
      mapper = getNewMapper(new String[] { "dozerBeanMapping.xml" });
    }
  }

  /*
   * Baseline Performance Numbers. Established with Release 2.0 - Jan 2006
   * 
   * All performance results based on 25000 numIters and jdk 1.4.2
   * 
   * TEST TOTAL TIME(ms) - 1/1/06
   * 
   * testMapping1 19562 
   * testMapping2 2859 
   * testMapping3 2782 
   * testMapping4 8391 
   * testMapping5 4985
   * 
   * MHT Computer - 8/12/06 
   * #1  17015
   * #2  1954
   * #3  1890
   * #4  5985
   * #5  4062
   * 
   * MHT Computer - 9/26/06 - After 2.3 release 
   * #1  19578
   * #2  1937
   * #3  1953
   * #4  6734
   * #5  4641
   *
   * MHT Computer - 10/1/06 - 2.4 release.  After removing Reflection caching to resolve general CGLIB/Proxy issues 
   * #1  29313
   * #2  2985
   * #3  3130
   * #4  9891
   * #5  7656
   * 
   * MHT Computer - 12/20/06 - 2.5 release.  After adding code submitted for fixing recursive mapping infinite loop problem.  Due to maintaining mappedFields variable in MappingProcessor.  Is this ok or better way to do it?  Not sure 
   *
   * #1  31422
   * #2  3485
   * #3  3547
   * #4  11656
   * #5  8281
   * 
   * MHT Computer - 1/26/07 - 2.5 release.  After making misc perf improvements to improve test case #6 
   *
   * #1  26047
   * #2  3375
   * #3  3469
   * #4  11672
   * #5  7516
   * #6  45850
   * 
   * MHT Computer - 2/1/07 - 2.5 release.  Just prior to release of 2.5 
   *
   * #1  26266
   * #2  3094
   * #3  3203
   * #4  11297
   * #5  7453
   * #6  42312
   * 
   */

  public void testMapping1() throws Exception {
    // TestObject --> TestObjectPrime
    log.debug("Begin timings...");
    TestObject src = TestDataFactory.getInputGeneralMappingTestObject();
    runGeneric(src, TestObjectPrime.class, 35000);
  }

  
  public void testMapping2() throws Exception {
    // SimpleObject --> SimpleObjectPrime
    log.debug("Begin timings...");
    SimpleObj src = (SimpleObj) TestDataFactory.getSimpleObj();
    runGeneric(src, SimpleObjPrime.class, 3600);
  }

  public void testMapping3() throws Exception {
    // SimpleObject --> SimpleObjectPrime2
    log.debug("Begin timings...");
    SimpleObj src = (SimpleObj) TestDataFactory.getSimpleObj();
    runGeneric(src, SimpleObjPrime2.class, 3700);
  }

  public void testMapping4() throws Exception {
    // AnotherSubClass --> AnotherSubClassPrime (Inheritance)
    log.debug("Begin timings...");
    AnotherSubClass src = TestDataFactory.getAnotherSubClass();
    runGeneric(src, AnotherSubClassPrime.class, 12000);
  }

  public void testMapping5() throws Exception {
    // SrcDeepObj --> DestDeepObj (Field Deep)
    log.debug("Begin timings...");
    SrcDeepObj src = TestDataFactory.getSrcDeepObj();
    runGeneric(src, DestDeepObj.class, 9000);
  }
  
  //1-2007: Test Case submitted by Dave B.
  public void testMapping6() throws Exception {
    // MyClassA --> MyClassB.  Src object contains List with 500 String elements.
    MyClassA src = TestDataFactory.getRandomMyClassA();
    runGeneric(src, MyClassB.class, 50000);
  }
  

  private void runGeneric(Object src, Class destClass, long maxTimeAllowed) throws Exception {
    // warm up the mapper
    mapper.map(src, destClass);

    // perform x number of additional mappings
    StopWatch timer = new StopWatch();
    timer.start();
    for (int i = 0; i < numIters; i++) {
      mapper.map(src, destClass);
    }
    timer.stop();
    log.debug("Total time for additional " + numIters + " mappings: " + timer.getTime() + " milliseconds");
    log.debug("avg time for " + numIters + " mappings: " + (timer.getTime() / numIters) + " milliseconds");

    if (timer.getTime() > maxTimeAllowed) {
      log.error("Elapsed time exceeded max allowed: " + maxTimeAllowed + " Actual time: " + timer.getTime());
    }
  }
  

  public static void main(String[] args) {
    junit.textui.TestRunner.run(PerformanceTest.class);
  }

  /*
  public static void main(String[] args) throws Exception {
    PerformanceTest pt = new PerformanceTest();
    pt.setUp();
    pt.testMapping6_Dozer();
  }
  */
  
}

/*
 * Copyright DataStax, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datastax.oss.driver.osgi;

import com.datastax.oss.driver.api.core.Version;
import com.datastax.oss.driver.api.testinfra.DseRequirement;
import com.datastax.oss.driver.api.testinfra.ccm.CustomCcmRule;
import com.datastax.oss.driver.categories.IsolatedTests;
import com.datastax.oss.driver.osgi.support.BundleOptions;
import com.datastax.oss.driver.osgi.support.OsgiGraphTests;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.CoreOptions;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
@Category(IsolatedTests.class)
@DseRequirement(min = "5.0", description = "Requires Graph")
public class OsgiGraphIT implements OsgiGraphTests {

  @ClassRule
  public static final CustomCcmRule CCM_RULE =
      CustomCcmRule.builder().withNodes(1).withDseWorkloads("graph").build();

  @Configuration
  public Option[] config() {
    return CoreOptions.options(
        BundleOptions.driverCoreBundle(),
        BundleOptions.driverQueryBuilderBundle(),
        BundleOptions.baseOptions(),
        BundleOptions.jacksonBundles(),
        BundleOptions.tinkerpopBundles());
  }

  @Test
  public void should_connect_and_query_without_graph() {
    connectAndQuerySimple();
  }

  @Test
  public void should_connect_and_query_with_graph() {
    connectAndQueryGraph();
  }

  @Override
  public Version getDseVersion() {
    return CCM_RULE.getDseVersion().orElseThrow(IllegalStateException::new);
  }
}

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
package net.sf.dozer.util.mapping.config;

import net.sf.dozer.util.mapping.util.MapperConstants;

/**
 * @author tierney.matt
 */
public class Settings {
  
  private boolean statisticsEnabled = MapperConstants.DEFAULT_STATISTICS_ENABLED;
  private long converterByDestTypeCacheMaxSize = MapperConstants.DEFAULT_CONVERTER_BY_DEST_TYPE_CACHE_MAX_SIZE;
  private long superTypesCacheMaxSize = MapperConstants.DEFAULT_SUPER_TYPE_CHECK_CACHE_MAX_SIZE;

  public boolean isStatisticsEnabled() {
    return statisticsEnabled;
  }

  public void setStatisticsEnabled(boolean statisticsEnabled) {
    this.statisticsEnabled = statisticsEnabled;
  }

  public long getConverterByDestTypeCacheMaxSize() {
    return converterByDestTypeCacheMaxSize;
  }

  public void setConverterByDestTypeCacheMaxSize(long converterByDestTypeCacheMaxSize) {
    this.converterByDestTypeCacheMaxSize = converterByDestTypeCacheMaxSize;
  }

  public long getSuperTypesCacheMaxSize() {
    return superTypesCacheMaxSize;
  }

  public void setSuperTypesCacheMaxSize(long superTypesCacheMaxSize) {
    this.superTypesCacheMaxSize = superTypesCacheMaxSize;
  }
}

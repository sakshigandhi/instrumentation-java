/*
 * Copyright 2017, Google Inc.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.instrumentation.trace;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.testing.EqualsTester;
import com.google.instrumentation.common.Timestamp;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link StartSpanOptions}. */
@RunWith(JUnit4.class)
public class StartSpanOptionsTest {
  private final List<Span> singleParentList = Arrays.asList(BlankSpan.INSTANCE);

  @Test
  public void defaultOptions() {
    StartSpanOptions defaultOptions = new StartSpanOptions();
    assertThat(defaultOptions.getStartTime()).isNull();
    assertThat(defaultOptions.getSampler()).isNull();
    assertThat(defaultOptions.getParentLinks().isEmpty()).isTrue();
    assertThat(defaultOptions.getRecordEvents()).isNull();
  }

  @Test
  public void setStartTime() {
    StartSpanOptions options = new StartSpanOptions();
    options.setStartTime(Timestamp.fromMillis(1234567L));
    assertThat(options.getStartTime()).isEqualTo(Timestamp.fromMillis(1234567L));
    assertThat(options.getSampler()).isNull();
    assertThat(options.getParentLinks().isEmpty()).isTrue();
    assertThat(options.getRecordEvents()).isNull();
  }

  @Test
  public void setSampler() {
    StartSpanOptions options = new StartSpanOptions();
    options.setSampler(Samplers.neverSample());
    assertThat(options.getStartTime()).isNull();
    assertThat(options.getSampler()).isEqualTo(Samplers.neverSample());
    assertThat(options.getParentLinks().isEmpty()).isTrue();
    assertThat(options.getRecordEvents()).isNull();
  }

  @Test
  public void setParentLinks() {
    StartSpanOptions options = new StartSpanOptions();
    options.setParentLinks(singleParentList);
    assertThat(options.getStartTime()).isNull();
    assertThat(options.getSampler()).isNull();
    assertThat(options.getParentLinks()).isEqualTo(singleParentList);
    assertThat(options.getRecordEvents()).isNull();
  }

  @Test
  public void setParentLinks_EmptyList() {
    StartSpanOptions options = new StartSpanOptions();
    options.setParentLinks(new LinkedList<Span>());
    assertThat(options.getStartTime()).isNull();
    assertThat(options.getSampler()).isNull();
    assertThat(options.getParentLinks().size()).isEqualTo(0);
    assertThat(options.getRecordEvents()).isNull();
  }

  @Test
  public void setParentLinks_MultipleParents() {
    StartSpanOptions options = new StartSpanOptions();
    options.setParentLinks(Arrays.asList(BlankSpan.INSTANCE, BlankSpan.INSTANCE));
    assertThat(options.getStartTime()).isNull();
    assertThat(options.getSampler()).isNull();
    assertThat(options.getParentLinks().size()).isEqualTo(2);
    assertThat(options.getRecordEvents()).isNull();
  }

  @Test
  public void setRecordEvents() {
    StartSpanOptions options = new StartSpanOptions();
    options.setRecordEvents(true);
    assertThat(options.getStartTime()).isNull();
    assertThat(options.getSampler()).isNull();
    assertThat(options.getParentLinks().isEmpty()).isTrue();
    assertThat(options.getRecordEvents()).isTrue();
  }

  @Test
  public void setAllProperties() {
    StartSpanOptions options = new StartSpanOptions();
    options.setStartTime(Timestamp.fromMillis(1234567L));
    options.setSampler(Samplers.neverSample());
    options.setSampler(Samplers.alwaysSample()); // second SetSampler should apply
    options.setRecordEvents(true);
    options.setParentLinks(singleParentList);
    assertThat(options.getStartTime()).isEqualTo(Timestamp.fromMillis(1234567L));
    assertThat(options.getSampler()).isEqualTo(Samplers.alwaysSample());
    assertThat(options.getParentLinks()).isEqualTo(singleParentList);
    assertThat(options.getRecordEvents()).isTrue();
  }

  @Test
  public void startSpanOptions_EqualsAndHashCode() {
    EqualsTester tester = new EqualsTester();
    StartSpanOptions optionsWithStartTime1 = new StartSpanOptions();
    optionsWithStartTime1.setStartTime(Timestamp.fromMillis(1234567L));
    StartSpanOptions optionsWithStartTime2 = new StartSpanOptions();
    optionsWithStartTime2.setStartTime(Timestamp.fromMillis(1234567L));
    tester.addEqualityGroup(optionsWithStartTime1, optionsWithStartTime2);
    StartSpanOptions optionsWithAlwaysSampler = new StartSpanOptions();
    optionsWithAlwaysSampler.setSampler(Samplers.alwaysSample());
    tester.addEqualityGroup(optionsWithAlwaysSampler);
    StartSpanOptions optionsWithNeverSampler = new StartSpanOptions();
    optionsWithNeverSampler.setSampler(Samplers.neverSample());
    tester.addEqualityGroup(optionsWithNeverSampler);
    tester.addEqualityGroup(new StartSpanOptions());
    tester.testEquals();
  }
}

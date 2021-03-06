/*
 * Copyright 2016 Square Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package flow;

import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;

/**
 * wrapper context to enhance
 */
final class FlowContextWrapper extends ContextWrapper {
  static final String SYSTEM_SERVICE = "flow_services_context_wrapper";

  // get itself
  static FlowContextWrapper get(Context context) {
    @SuppressWarnings("WrongConstant") FlowContextWrapper wrapper =
        (FlowContextWrapper) context.getSystemService(SYSTEM_SERVICE);
    return wrapper;
  }

  final Services services;

  private LayoutInflater inflater;

  FlowContextWrapper(Services services, Context baseContext) {
    super(baseContext);
    this.services = services;
  }

  /**
   * enhance to get self
   * @param name
     * @return
     */
  @Override public Object getSystemService(String name) {
    if (SYSTEM_SERVICE.equals(name)) {
      return this;
    }
    //return a new layoutInflater associated with a new context
    if (LAYOUT_INFLATER_SERVICE.equals(name)) {
      if (inflater == null) {
        inflater = LayoutInflater.from(getBaseContext()).cloneInContext(this);
      }
      return inflater;
    }
    return super.getSystemService(name);
  }
}

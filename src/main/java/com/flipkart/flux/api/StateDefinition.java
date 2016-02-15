/*
 * Copyright 2012-2015, the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flipkart.flux.api;

import java.util.Set;

/**
 * @understands API model that a user can use to define an action and its list of dependencies.
 * For flux, this is equivalent to a "State" in the state machine
 */
public class StateDefinition {
    Long version;
   	String name;
   	String task; // Assume, for now, that this is the URI that would be called in order to execute a task.
	Long retryCount;
	Long timeout;
    Set<EventDefinition> dependencies;
}
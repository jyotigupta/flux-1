/*
 * Copyright 2012-2016, the original author or authors.
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

package com.flipkart.flux.deploymentunit;

import com.flipkart.flux.client.intercept.TaskInterceptor;
import com.flipkart.flux.client.model.Task;
import com.flipkart.flux.client.registry.ExecutableImpl;
import com.flipkart.flux.client.registry.ExecutableRegistry;
import com.flipkart.flux.domain.FluxError;
import com.flipkart.polyguice.core.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author shyam.akirala
 */
@Singleton
public class ExecutableRegistryPopulator implements Initializable {

    /** Logger for this class*/
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutableRegistryPopulator.class);

    private DeploymentUnitUtil deploymentUnitUtil;

    private ExecutableRegistry executableRegistry;

    private Set<String> routerNames;

    //todo: included this to call generateTaskIdentifier(), it's better to move that method to separate class like util class
    private TaskInterceptor taskInterceptor;

    @Inject
    public ExecutableRegistryPopulator(DeploymentUnitUtil deploymentUnitUtil, ExecutableRegistry executableRegistry, TaskInterceptor taskInterceptor) {
        System.out.println("Test logger========================================");
        this.deploymentUnitUtil = deploymentUnitUtil;
        this.executableRegistry = executableRegistry;
        this.taskInterceptor = taskInterceptor;
    }

    @Override
    public void initialize() {
        routerNames = new HashSet<String>();
        List<String> deploymentUnitNames = DeploymentUnitUtil.getAllDeploymentUnits();
        for(String deploymentUnitName : deploymentUnitNames) {
            try {
                URLClassLoader classLoader = deploymentUnitUtil.getClassLoader(deploymentUnitName);
                Class TaskClass = classLoader.loadClass(Task.class.getCanonicalName());
                Set<Method> taskMethods = deploymentUnitUtil.getTaskMethods(classLoader);
                for(Method method : taskMethods) {
                    String taskIdentifier = taskInterceptor.generateTaskIdentifier(method);
                    Annotation taskAnnotation = method.getAnnotationsByType(TaskClass)[0];
                    Class<? extends Annotation> annotationType = taskAnnotation.annotationType();
                    long timeout = 60;
                    for (Method annotationMethod : annotationType.getDeclaredMethods()) {
                        Object value = annotationMethod.invoke(taskAnnotation, (Object[])null);
                        if(annotationMethod.getName().equals("timeout")) { //todo: find a way get Task.timeout() name
                            timeout = (Long) value;
                        }
                    }
                    executableRegistry.registerTask(taskIdentifier, new ExecutableImpl(method.getDeclaringClass().newInstance(), method, timeout, classLoader)); //todo: singletonMethodOwner -- cases: classes which carry state, abstract classes ?
                    //TODO - add workflow routers, need to decide how they will be used
                    routerNames.add(method.getDeclaringClass().getName() + "_" + method.getName());
                }
            } catch (Exception e) {
                LOGGER.error("Unable to populate Executable Registry. Exception: "+e.getMessage());
                throw new FluxError(FluxError.ErrorType.runtime, "Unable to populate Executable Registry", e);
            }
        }

    }

    public Set<String> getDeploymentUnitRouterNames() {
        return routerNames;
    }

}

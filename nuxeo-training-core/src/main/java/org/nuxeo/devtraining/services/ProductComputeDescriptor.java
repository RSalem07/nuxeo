/*
 * (C) Copyright 2019 Nuxeo (http://nuxeo.com/) and others.
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
 *
 * Contributors:
 *     Salem Aouana
 */

package org.nuxeo.devtraining.services;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * The type Product compute descriptor.
 *
 * @since 10.10
 */
@XObject("plugin")
public class ProductComputeDescriptor implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * The Provider class.
     */
    @XNode("@class")
    protected Class<? extends ProductCompute> providerClass;

    /**
     * The Origin.
     */
    @XNode("@origin")
    protected String origin;

    /**
     * Gets provider class.
     *
     * @return the provider class
     */
    public Class<? extends ProductCompute> getProviderClass() {
        return providerClass;
    }

    /**
     * Sets provider class.
     *
     * @param providerClass the provider class
     */
    public void setProviderClass(Class<? extends ProductCompute> providerClass) {
        this.providerClass = providerClass;
    }

    /**
     * Gets origin.
     *
     * @return the origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Sets origin.
     *
     * @param origin the origin
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("providerClass", providerClass).append("origin", origin).toString();
    }
}

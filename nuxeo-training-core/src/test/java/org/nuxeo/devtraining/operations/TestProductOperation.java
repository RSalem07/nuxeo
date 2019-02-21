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

package org.nuxeo.devtraining.operations;

import static org.junit.Assert.assertThat;

import javax.inject.Inject;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.devtraining.commons.ProductTestUtils;
import org.nuxeo.devtraining.features.ProductFeature;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

/**
 * The type Test product operation.
 *
 * @since 10.10
 */
@RunWith(FeaturesRunner.class)
@Features(ProductFeature.class)
public class TestProductOperation {

    /**
     * The Product operation.
     */
    @Inject
    protected ProductOperation productOperation;

    /**
     * The Core session.
     */
    @Inject
    protected CoreSession coreSession;

    /**
     * Is up.
     */
    @Test
    public void isUP() {
        Assert.assertNotNull(productOperation);
    }

    /**
     * Test compute price operation.
     */
    @Test
    public void testComputePriceOperation() {

        DocumentModel product = ProductTestUtils.createAndSaveProduct(coreSession);

        assertThat("Created product should be have an ID", product.getId(), Matchers.notNullValue());

        productOperation.run(product);
    }
}

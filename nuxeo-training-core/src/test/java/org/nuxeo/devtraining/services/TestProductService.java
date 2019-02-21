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

import static org.junit.Assert.*;
import static org.nuxeo.devtraining.commons.ProductTestUtils.PropertyValue;
import static org.nuxeo.devtraining.commons.ProductTestUtils.PropertyValueData;

import java.time.Instant;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.devtraining.adapters.ProductAdapter;
import org.nuxeo.devtraining.commons.ProductTestUtils;
import org.nuxeo.devtraining.features.ProductFeature;
import org.nuxeo.ecm.collections.api.CollectionManager;
import org.nuxeo.ecm.collections.core.adapter.Collection;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

/**
 * The type Test product service.
 *
 * @since 10.10
 */
@RunWith(FeaturesRunner.class)
@Features(ProductFeature.class)
public class TestProductService {
    @Inject
    private CoreSession coreSession;

    /**
     * The Productservice.
     */
    @Inject
    protected ProductService productservice;

    /**
     * The Collection manager.
     */
    @Inject
    CollectionManager collectionManager;

    /**
     * Is up.
     */
    @Test
    public void isUp() {
        Assert.assertNotNull("Session cannot be null !", coreSession);
        Assert.assertNotNull("Product Service cannot be null !", productservice);
    }

    /**
     * Test create product without exception.
     */
    @Test
    public void testCreateProductWithoutException() {
        DocumentModel created_product = ProductTestUtils.createAndSaveProduct(coreSession);

        assertThat("Created product should be have an ID", created_product.getId(), Matchers.notNullValue());

        // FIXME Ask how to flush or anything else to force retrieving from D.B

        DocumentModel product = coreSession.getDocument(created_product.getRef());

        assertTrue("Product should be type of Collection !", product.hasFacet("Collection"));

        for (PropertyValueData pvd : PropertyValueData.values()) {
            PropertyValue propertyValue = pvd.getPropertyValue();
            if (pvd == PropertyValueData.DELIVERY_TIME) {
                Instant expected = ((Date) propertyValue.getValue()).toInstant();
                Instant actual = ((GregorianCalendar) product.getPropertyValue(propertyValue.getXpath())).toInstant();

                assertThat("Delivery time should be the same !", actual, Matchers.equalTo(expected));
            } else {
                assertThat(String.format("Values should be the same for %s", pvd),
                        product.getPropertyValue(propertyValue.getXpath()), Matchers.equalTo(propertyValue.getValue()));
            }
        }
    }

    /**
     * Test retrieve product adapter without exception.
     */
    @Test
    public void testRetrieveProductAdapterWithoutException() {
        DocumentModel product = ProductTestUtils.createAndSaveProduct(coreSession);

        ProductAdapter adapter = product.getAdapter(ProductAdapter.class);

        assertThat("Adapter title must be the same then the saved document", adapter.getTitle(),
                Matchers.equalTo(PropertyValueData.TITLE.getPropertyValue().getValue()));

        assertThat("Adapter description must be the same then the saved document", adapter.getDescription(),
                Matchers.equalTo(PropertyValueData.DESCRIPTION.getPropertyValue().getValue()));

        assertThat("Adapter origin must be the same then the saved document", adapter.getOrigin(),
                Matchers.equalTo(PropertyValueData.ORIGIN.getPropertyValue().getValue()));

        assertThat("Adapter price must be the same then the saved document", adapter.getPrice(),
                Matchers.equalTo(PropertyValueData.PRICE.getPropertyValue().getValue()));

        assertThat("Adapter delivery time must be the same then the saved document", adapter.getDeliveryTime(),
                Matchers.equalTo(PropertyValueData.DELIVERY_TIME.getPropertyValue().getValue()));

        ProductAdapter.Distributor distributor = adapter.getDistributor();

        Map expectedDistributor = (Map) PropertyValueData.DISTRIBUTOR.getPropertyValue().getValue();
        assertThat("Adapter distributor name must be the same then the saved document", distributor.getName(),
                Matchers.equalTo(expectedDistributor.get("name")));
        assertThat("Adapter distributor location must be the same then the saved document", distributor.getLocation(),
                Matchers.equalTo(expectedDistributor.get("location")));

    }

    /**
     * Test create product using adapter without exception.
     */
    @Test
    @Ignore
    public void testCreateProductUsingAdapterWithoutException() {
        ProductAdapter product = ProductTestUtils.createAndSaveProductByAdapter(coreSession);
        DocumentModel document = coreSession.getDocument(new IdRef(product.getId()));

        // FIXME ask if there are or not same references. Same Id but the references are !=
        assertThat("Retrieved document and document product must be the same", product.hasDocument(document),
                Matchers.equalTo(Boolean.TRUE));

    }

    /**
     * Test create visuals for product.
     */
    @Test
    public void testCreateVisualsForProduct() {
        ProductTestUtils.ProductWrapper wrapper = ProductTestUtils.createAndSaveProductWithVisuals(coreSession,
                collectionManager);

        Collection collectionAdapter = wrapper.getProduct().getAdapter(Collection.class);
        assertEquals(wrapper.getVisuals().size(), collectionAdapter.getCollectedDocumentIds().size());
        assertThat("Randomly accessed visual must be linked to the product collection",
                wrapper.getVisuals().get(new Random().nextInt(3)).getId(),
                Matchers.isIn(collectionAdapter.getCollectedDocumentIds()));

    }
}

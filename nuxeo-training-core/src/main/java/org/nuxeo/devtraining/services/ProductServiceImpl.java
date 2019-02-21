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

import static org.nuxeo.devtraining.enums.ProductField.PRICE;

import java.io.Serializable;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.devtraining.enums.ProductField;
import org.nuxeo.devtraining.extensions.ItalyProductCompute;
import org.nuxeo.ecm.core.api.*;
import org.nuxeo.runtime.model.ComponentContext;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

/**
 * The type Product service.
 *
 * @since 10.10
 */
public class ProductServiceImpl extends DefaultComponent implements ProductService {
    private static final Logger LOGGER = LogManager.getLogger(ItalyProductCompute.class);

    private final Map<String, ProductCompute> productComputes = new HashMap<>();

    @Override
    public void activate(ComponentContext context) {
        super.activate(context);
    }

    @Override
    public void deactivate(ComponentContext context) {
        super.deactivate(context);
    }

    @Override
    public void registerContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        if (contribution instanceof ProductComputeDescriptor) {
            ProductComputeDescriptor productComputeDescriptor = (ProductComputeDescriptor) contribution;
            registerProductCompute(productComputeDescriptor);
        }
    }

    @Override
    public void unregisterContribution(Object contribution, String extensionPoint, ComponentInstance contributor) {
        if (contribution instanceof ProductComputeDescriptor) {
            ProductComputeDescriptor productComputeDescriptor = (ProductComputeDescriptor) contribution;
            unregisterContribution(productComputeDescriptor);
        }

    }

    @Override
    public double computePrice(DocumentModel document) {
        LOGGER.info("Compute price for document={}", document);
        Objects.requireNonNull(document, "Document/Product cannot be null !");

        // By Origin: country
        Serializable value = document.getPropertyValue(ProductField.ORIGIN.getXpath());
        String origin = Optional.ofNullable(value).map(String.class::cast).orElse(null);

        ProductCompute plugin = getPluginByOrigin(origin);

        double price;
        if (plugin == null) {
            LOGGER.warn("No product compute for document origin = {}. Use the default one", origin);
            price = computeDefaultPrice();

        } else {
            price = plugin.computePrice(document);
        }

        document.setPropertyValue(PRICE.getXpath(), price);
        document.getCoreSession().saveDocument(document);
        document.getCoreSession().save();

        return price;

    }

    private double computeDefaultPrice() {
        return new Random().nextDouble() * 50;
    }

    private void registerProductCompute(ProductComputeDescriptor contribution) {
        try {

            ProductCompute productCompute = contribution.getProviderClass().newInstance();

            productComputes.put(contribution.getOrigin(), productCompute);

        } catch (ReflectiveOperationException e) {
            LOGGER.error("Error registering contribution for Product Compute", e);
            throw new NuxeoException(e);
        }
    }

    private void unregisterContribution(ProductComputeDescriptor productComputeDescriptor) {
        productComputes.remove(productComputeDescriptor.getOrigin());
    }

    private ProductCompute getPluginByOrigin(String origin) {
        if (StringUtils.isNoneEmpty(origin)) {
            return productComputes.get(origin.toLowerCase());
        }
        return null;
    }

    @Override
    public double computePrice(CoreSession coreSession, String docId) {
        Objects.requireNonNull(coreSession, "Core Session cannot be null !");

        if (StringUtils.isEmpty(docId)) {
            throw new IllegalArgumentException("Document Id is required !");
        }

        DocumentModel documentModel = coreSession.getDocument(new IdRef(docId));

        return computePrice(documentModel);
    }

    @Override
    public DocumentModelList getProducts(CoreSession coreSession) {
        Objects.requireNonNull(coreSession, "Core Session cannot be null !");

        return coreSession.query("Select * From product_v", 10);

    }
}

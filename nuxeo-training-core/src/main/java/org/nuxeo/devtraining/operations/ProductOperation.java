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

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.devtraining.extensions.ItalyProductCompute;
import org.nuxeo.devtraining.services.ProductService;
import org.nuxeo.ecm.automation.core.annotations.Operation;
import org.nuxeo.ecm.automation.core.annotations.OperationMethod;
import org.nuxeo.ecm.automation.core.collectors.DocumentModelCollector;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.runtime.api.Framework;

/**
 * The type Product operation.
 *
 * @since 10.10
 */
@Operation(id = ProductOperation.ID, category = "Product", label = "computePrices", description = "Compute Prices for Product(s)")
public class ProductOperation {
    private static final Logger LOGGER = LogManager.getLogger(ItalyProductCompute.class);

    /**
     * The constant ID.
     */
    public static final String ID = "Product.ComputePrices";

    /**
     * Run document model.
     *
     * @param docModel the doc model
     * @return the document model
     */
    @OperationMethod(collector = DocumentModelCollector.class)
    public DocumentModel run(DocumentModel docModel) {
        LOGGER.info("run product operation on document = {}", docModel);
        Objects.requireNonNull(docModel, "Document cannot be null !");

        Framework.getService(ProductService.class).computePrice(docModel);

        return docModel;
    }

}

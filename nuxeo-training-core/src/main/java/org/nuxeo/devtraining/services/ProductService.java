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

import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;

/**
 * The interface Product service.
 *
 * @since 10.10
 */
public interface ProductService {
    /**
     * Compute price double.
     *
     * @param document the document
     * @return the double
     */
    double computePrice(DocumentModel document);

    /**
     * Compute price double.
     *
     * @param coreSession the core session
     * @param id the id
     * @return the double
     */
    double computePrice(CoreSession coreSession, String id);

    /**
     * Gets products.
     *
     * @param coreSession the core session
     * @return the products
     */
    DocumentModelList getProducts(CoreSession coreSession);
}

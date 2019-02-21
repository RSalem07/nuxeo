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

package org.nuxeo.devtraining.enums;

import java.util.Objects;

import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * The enum Document type.
 *
 * @since 10.10
 */
public enum DocumentType {
    /**
     * Product document type.
     */
    PRODUCT("product_v"),
    /**
     * Collection document type.
     */
    COLLECTION("Collection");

    DocumentType(String name) {
        this.name = name;
    }

    private final String name;

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Is product boolean.
     *
     * @param doc the doc
     * @return the boolean
     */
    public static boolean isProduct(DocumentModel doc) {
        if (!Objects.isNull(doc)) {
            return PRODUCT.getName().equals(doc.getType()) && doc.hasFacet(COLLECTION.getName());
        }
        return false;
    }
}

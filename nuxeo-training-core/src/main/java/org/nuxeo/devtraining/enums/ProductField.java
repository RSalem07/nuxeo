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

/**
 * The enum Product field.
 *
 * @since 10.10
 */
public enum ProductField {
    /**
     * Price product field.
     */
    PRICE("product_common:price"), //
    /**
     * Size product field.
     */
    SIZE("product_common:size"), //
    /**
     * Origin product field.
     */
    ORIGIN("product_common:origin"), //
    /**
     * Name product field.
     */
    NAME("product_common:name"), //
    /**
     * Delivery time product field.
     */
    DELIVERY_TIME("product_common:delivery_time"), //
    /**
     * Distributor product field.
     */
    DISTRIBUTOR("product_common:distributor"), //
    /**
     * Title product field.
     */
    TITLE("dc:title"), //
    /**
     * Description product field.
     */
    DESCRIPTION("dc:description");//

    ProductField(String xpath) {
        this.xpath = xpath;
    }

    private final String xpath;

    /**
     * Gets xpath.
     *
     * @return the xpath
     */
    public String getXpath() {
        return xpath;
    }
}


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

package org.nuxeo.devtraining.adapters;

import static org.nuxeo.devtraining.enums.ProductField.*;

import java.io.Serializable;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * The type Product adapter.
 *
 * @since 10.10
 */
public class ProductAdapter {
    private static final Logger LOGGER = LogManager.getLogger(ProductAdapter.class);

    /**
     * The Doc.
     */
    protected DocumentModel doc;

    /**
     * Instantiates a new Product adapter.
     *
     * @param doc the doc
     */
    public ProductAdapter(DocumentModel doc) {
        this.doc = doc;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return doc.getId();
    }

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return doc.getTitle();
    }

    /**
     * Sets title.
     *
     * @param value the value
     */
    public void setTitle(String value) {
        doc.setPropertyValue(TITLE.getXpath(), value);
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return (String) doc.getPropertyValue(DESCRIPTION.getXpath());
    }

    /**
     * Sets description.
     *
     * @param value the value
     */
    public void setDescription(String value) {
        doc.setPropertyValue(DESCRIPTION.getXpath(), value);
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public Double getPrice() {
        return (Double) doc.getPropertyValue(PRICE.getXpath());
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(Double price) {
        doc.setPropertyValue(PRICE.getXpath(), price);
    }

    /**
     * Gets origin.
     *
     * @return the origin
     */
    public String getOrigin() {
        return (String) doc.getPropertyValue(ORIGIN.getXpath());
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return (String) doc.getPropertyValue(NAME.getXpath());
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        doc.setPropertyValue(NAME.getXpath(), name);
    }

    /**
     * Gets delivery time.
     *
     * @return the delivery time
     */
    public Date getDeliveryTime() {
        GregorianCalendar deliveryTime = (GregorianCalendar) doc.getPropertyValue(DELIVERY_TIME.getXpath());

        if (!Objects.isNull(deliveryTime)) {
            return Date.from(deliveryTime.toInstant());
        }

        return null;
    }

    /**
     * Sets delivery time.
     *
     * @param delivery_time the delivery time
     */
    public void setDeliveryTime(Date delivery_time) {
        doc.setPropertyValue(DELIVERY_TIME.getXpath(), delivery_time);
    }

    /**
     * Gets distributor.
     *
     * @return the distributor
     */
    public Distributor getDistributor() {
        Serializable propertyValue = doc.getPropertyValue(DISTRIBUTOR.getXpath());
        if (propertyValue instanceof Map) {
            Map map = (Map) propertyValue;

            return new Distributor((String) map.get("name"), (String) map.get("location"));
        } else if (propertyValue != null) {
            LOGGER.warn("Unexpected type of distributor data. Expected = {}, found = {} !", Map.class,
                    propertyValue.getClass());
        }

        return null;

    }

    /**
     * Sets distributor.
     *
     * @param distributor the distributor
     */
    public void setDistributor(Distributor distributor) {
        if (distributor != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", distributor.getName());
            map.put("location", distributor.getLocation());

            doc.setPropertyValue(DISTRIBUTOR.getXpath(), (Serializable) map);

        } else {
            doc.setPropertyValue(DISTRIBUTOR.getXpath(), null);
        }
    }

    /**
     * Has document boolean.
     *
     * @param input the input
     * @return the boolean
     */
    public boolean hasDocument(DocumentModel input) {
        return input == this.doc && input.getId().equals(this.getId());
    }

    /**
     * Save.
     *
     * @param coreSession the core session
     */
    public void save(CoreSession coreSession) {
        Objects.requireNonNull(this.doc, "Document cannot be null !");

        // Getting ID linked to this document
        this.doc = coreSession.createDocument(this.doc);
        coreSession.saveDocument(this.doc);
    }

    /**
     * The type Distributor.
     */
    public static class Distributor implements Serializable {
        private final String name;

        private final String location;

        /**
         * Gets name.
         *
         * @return the name
         */
        public String getName() {
            return name;
        }

        /**
         * Gets location.
         *
         * @return the location
         */
        public String getLocation() {
            return location;
        }

        /**
         * Instantiates a new Distributor.
         *
         * @param name the name
         * @param location the location
         */
        public Distributor(String name, String location) {
            super();
            this.name = name;
            this.location = location;
        }
    }
}

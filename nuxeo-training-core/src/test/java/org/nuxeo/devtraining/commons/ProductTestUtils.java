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

package org.nuxeo.devtraining.commons;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.nuxeo.devtraining.adapters.ProductAdapter;
import org.nuxeo.devtraining.enums.ProductField;
import org.nuxeo.ecm.collections.api.CollectionManager;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * The type Product test utils.
 *
 * @since 10.10
 */
public class ProductTestUtils {
    /**
     * Create and save product document model.
     *
     * @param coreSession the core session
     * @return the document model
     */
    public static DocumentModel createAndSaveProduct(CoreSession coreSession) {
        DocumentModel product = coreSession.createDocumentModel("product_v");

        for (PropertyValueData pvd : PropertyValueData.values()) {
            product.setPropertyValue(pvd.getPropertyValue().getXpath(),
                    (Serializable) pvd.getPropertyValue().getValue());
        }

        // Creation and id
        product = coreSession.createDocument(product);

        // Save
        coreSession.save();

        return product;
    }

    /**
     * Create and save visual document model.
     *
     * @param coreSession the core session
     * @return the document model
     */
    public static DocumentModel createAndSaveVisual(CoreSession coreSession) {
        // Create Parent of all Visual
        createAnyDocument(coreSession, "/default-domain/workspaces", "Visuals", "Folder");

        // Create the visual under All Visuals Folder
        DocumentModel visual = coreSession.createDocumentModel("/default-domain/workspaces/Visuals", "anyVisual",
                "visual");

        // Just copy the same data as used in studio
        for (PropertyValueData pvd : PropertyValueData.values()) {
            visual.setPropertyValue(pvd.getPropertyValue().getXpath(),
                    (Serializable) pvd.getPropertyValue().getValue());
        }

        // Creation and id
        visual = coreSession.createDocument(visual);

        // Save
        coreSession.save();

        return visual;
    }

    /**
     * Create and save product with visuals product wrapper.
     *
     * @param coreSession the core session
     * @param collectionManager the collection manager
     * @return the product wrapper
     */
    public static ProductWrapper createAndSaveProductWithVisuals(CoreSession coreSession,
            CollectionManager collectionManager) {
        // create the Product Type
        DocumentModel product = ProductTestUtils.createAndSaveProduct(coreSession);

        // Create the Visuals linked to the created product
        List<DocumentModel> visuals = Arrays.asList(createAndSaveVisual(coreSession),
                ProductTestUtils.createAndSaveVisual(coreSession), ProductTestUtils.createAndSaveVisual(coreSession),
                ProductTestUtils.createAndSaveVisual(coreSession));

        collectionManager.addToCollection(product, visuals, coreSession);

        return new ProductWrapper(product, visuals);
    }

    /**
     * Create and save product by adapter product adapter.
     *
     * @param coreSession the core session
     * @return the product adapter
     */
    public static ProductAdapter createAndSaveProductByAdapter(CoreSession coreSession) {
        // Creation Model
        DocumentModel product = coreSession.createDocumentModel("product_v");

        // Create product document and save it
        ProductAdapter productAdapter = new ProductAdapter(product);
        productAdapter.save(coreSession);

        productAdapter.setDeliveryTime(new Date());
        productAdapter.setDescription("anyDescription by Adapter");
        productAdapter.setDistributor(new ProductAdapter.Distributor("Any Distributor name by adapter",
                "Any Distributor location by adapter"));
        productAdapter.setTitle("Any title By Adapter");

        coreSession.save();

        return productAdapter;
    }

    /**
     * Create any document document model.
     *
     * @param coreSession the core session
     * @param parentPath the parent path
     * @param name the name
     * @param typeName the type name
     * @return the document model
     */
    public static DocumentModel createAnyDocument(CoreSession coreSession, String parentPath, String name,
            String typeName) {
        DocumentModel document = coreSession.createDocumentModel(parentPath, name, typeName);
        document = coreSession.createDocument(document);
        coreSession.saveDocument(document);
        coreSession.save();

        return document;
    }

    /**
     * The enum Property value data.
     */
    public enum PropertyValueData {
        /**
         * The Title.
         */
        TITLE {
            public PropertyValue getPropertyValue() {
                return new PropertyValue(ProductField.TITLE.getXpath(), "anyTitle");
            }

        },
        /**
         * The Description.
         */
        DESCRIPTION {
            public PropertyValue getPropertyValue() {
                return new PropertyValue(ProductField.DESCRIPTION.getXpath(), "anyDescription");
            }

        },
        /**
         * The Size.
         */
        SIZE {
            @Override
            public PropertyValue getPropertyValue() {
                return new PropertyValue(ProductField.SIZE.getXpath(), 42l);
            }
        },
        /**
         * The Price.
         */
        PRICE {
            @Override
            public PropertyValue getPropertyValue() {
                return new PropertyValue(ProductField.PRICE.getXpath(), 234.50d);
            }
        },
        /**
         * The Origin.
         */
        ORIGIN {
            @Override
            public PropertyValue getPropertyValue() {
                return new PropertyValue(ProductField.ORIGIN.getXpath(), "France");
            }
        },
        /**
         * The Name.
         */
        NAME {
            @Override
            public PropertyValue getPropertyValue() {
                return new PropertyValue(ProductField.NAME.getXpath(), "AnyName");
            }
        },
        /**
         * The Distributor.
         */
        DISTRIBUTOR {
            @Override
            public PropertyValue getPropertyValue() {
                Map<String, String> distributor = new HashMap<>();
                distributor.put("name", "anyDistributorName");
                distributor.put("location", "anyDistributorLocation");
                return new PropertyValue(ProductField.DISTRIBUTOR.getXpath(), distributor);
            }
        },
        /**
         * The Delivery time.
         */
        DELIVERY_TIME {
            @Override
            public PropertyValue getPropertyValue() {
                LocalDateTime localDateTime = LocalDateTime.parse("2019-02-08T12:30:00",
                        DateTimeFormatter.ISO_DATE_TIME);

                return new PropertyValue("product_common:delivery_time",
                        Date.from(localDateTime.toInstant(ZoneOffset.UTC)));
            }
        };

        /**
         * Gets property value.
         *
         * @return the property value
         */
        public abstract PropertyValue getPropertyValue();

    }

    /**
     * The type Property value.
     */
    public static class PropertyValue {
        private final String xpath;

        private final Object value;

        private PropertyValue(String xpath, Object value) {
            this.xpath = xpath;
            this.value = value;
        }

        /**
         * Gets xpath.
         *
         * @return the xpath
         */
        public String getXpath() {
            return xpath;
        }

        /**
         * Gets value.
         *
         * @return the value
         */
        public Object getValue() {
            return value;
        }
    }

    /**
     * The type Product wrapper.
     */
    public static class ProductWrapper {
        private final DocumentModel product;

        private final List<DocumentModel> visuals;

        private ProductWrapper(DocumentModel product, List<DocumentModel> visuals) {
            this.product = product;
            this.visuals = visuals;
        }

        /**
         * Gets product.
         *
         * @return the product
         */
        public DocumentModel getProduct() {
            return product;
        }

        /**
         * Gets visuals.
         *
         * @return the visuals
         */
        public List<DocumentModel> getVisuals() {
            return visuals;
        }
    }
}

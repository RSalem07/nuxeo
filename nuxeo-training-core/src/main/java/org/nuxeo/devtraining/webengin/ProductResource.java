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

package org.nuxeo.devtraining.webengin;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.nuxeo.devtraining.services.ProductService;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.webengine.WebEngine;
import org.nuxeo.ecm.webengine.model.WebObject;
import org.nuxeo.ecm.webengine.model.impl.ModuleRoot;
import org.nuxeo.runtime.api.Framework;

/**
 * The type Product resource.
 *
 * @since 10.10
 */
@WebObject(type = "product")
@Path("/product")
public class ProductResource extends ModuleRoot {
    /**
     * The Product service.
     */
    protected ProductService productService;

    /**
     * Instantiates a new Product resource.
     */
    public ProductResource() {
        super();
        this.productService = Framework.getService(ProductService.class);
    }

    /**
     * Gets all.
     *
     * @return the all
     */
    @GET
    public Object getAll() {
        DocumentModelList products = productService.getProducts(WebEngine.getActiveContext().getCoreSession());

        return getTemplate("products.ftl").arg("products", products);
    }

    /**
     * Gets price by id.
     *
     * @param id the id
     * @return the price by id
     */
    @GET
    @Path("{id}/price")
    public Object getPriceById(@PathParam("id") String id) {
        double price = productService.computePrice(WebEngine.getActiveContext().getCoreSession(), id);

        return getTemplate("product.ftl").arg("documentId", id).arg("price", price);
    }

}

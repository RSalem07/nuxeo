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

package org.nuxeo.devtraining.extensions;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.devtraining.services.ProductCompute;
import org.nuxeo.ecm.core.api.DocumentModel;

/**
 * The type Italy product compute.
 *
 * @since 10.10
 */
public class ItalyProductCompute implements ProductCompute {
    private static final Logger LOGGER = LogManager.getLogger(ItalyProductCompute.class);

    @Override
    public double computePrice(DocumentModel document) {
        LOGGER.info("Compute product price for Italy origin for doc = {}", document);

        return new Random().nextDouble() * 80;

    }
}

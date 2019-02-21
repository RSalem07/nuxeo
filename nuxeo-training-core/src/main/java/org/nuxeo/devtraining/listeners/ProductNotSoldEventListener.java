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

package org.nuxeo.devtraining.listeners;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.nuxeo.devtraining.enums.DocumentType;
import org.nuxeo.ecm.collections.core.adapter.Collection;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentRef;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventContext;
import org.nuxeo.ecm.core.event.EventListener;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;

import avro.shaded.com.google.common.base.Preconditions;

/**
 * The type Product not sold event listener.
 *
 * @since 10.10
 */
public class ProductNotSoldEventListener implements EventListener {
    private static final Logger LOGGER = LogManager.getLogger(ProductNotSoldEventListener.class);

    @Override
    public void handleEvent(Event event) {
        LOGGER.debug("Handle new event = {} for product not sold anymore !", event);

        EventContext ctx = event.getContext();
        if (!(ctx instanceof DocumentEventContext)) {
            return;
        }
        DocumentModel doc = ((DocumentEventContext) ctx).getSourceDocument();
        if (doc == null) {
            return;
        }
        if (DocumentType.isProduct(doc)) {
            process(ctx);
        } else {
            LOGGER.warn("Document = {} not handled by this listener !", doc);
        }
    }

    private void process(EventContext ctx) {
        DocumentModel product = ((DocumentEventContext) ctx).getSourceDocument();

        LOGGER.debug("Process document = {}", product);
        CoreSession coreSession = product.getCoreSession();

        Collection collectionAdapter = product.getAdapter(Collection.class);
        List<String> visualIds = collectionAdapter.getCollectedDocumentIds();

        if (CollectionUtils.isNotEmpty(visualIds)) {
            Object destination = ctx.getProperties().get("destination");
            validateDestination(destination);

            LOGGER.debug("Move visuals {} from {} to {}", visualIds, product.getPath(), destination);
            List<DocumentRef> visualIdsRefs = visualIds.stream().map(IdRef::new).collect(Collectors.toList());

            coreSession.move(visualIdsRefs, ((DocumentModel) destination).getRef());

        } else {
            LOGGER.warn("No visuals available to be moved for product = {}.", product);
        }

    }

    private void validateDestination(Object destination) {
        Objects.requireNonNull(destination, "Destination to move visuals cannot be null !");
        Preconditions.checkArgument(destination instanceof DocumentModel,
                "The new destination of visuals must be type of Document Model");
    }
}

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

import javax.inject.Inject;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nuxeo.devtraining.commons.ProductTestUtils;
import org.nuxeo.devtraining.features.ProductFeature;
import org.nuxeo.ecm.collections.api.CollectionManager;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.event.Event;
import org.nuxeo.ecm.core.event.EventProducer;
import org.nuxeo.ecm.core.event.impl.DocumentEventContext;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.test.runner.Features;
import org.nuxeo.runtime.test.runner.FeaturesRunner;

/**
 * The type Test product listener.
 */
@RunWith(FeaturesRunner.class)
@Features(ProductFeature.class)
public class TestProductListener {
    /**
     * The Core session.
     */
    @Inject
    protected CoreSession coreSession;

    /**
     * The Collection manager.
     */
    @Inject
    CollectionManager collectionManager;

    /**
     * Is up.
     */
    @Test
    public void isUP() {
        Assert.assertNotNull(coreSession);
    }

    /**
     * Test dispatch not sold anymore event.
     */
    @Test
    public void testDispatchNotSoldAnymoreEvent() {
        ProductTestUtils.ProductWrapper wrapper = ProductTestUtils.createAndSaveProductWithVisuals(coreSession,
                collectionManager);
        Assert.assertThat("At least one visual must be created !", wrapper.getVisuals(),
                Matchers.not(Matchers.empty()));

        EventProducer eventProducer = Framework.getService(EventProducer.class);
        DocumentEventContext ctx = new DocumentEventContext(coreSession, coreSession.getPrincipal(),
                wrapper.getProduct());

        DocumentModel hiddenVisualsDestination = ProductTestUtils.createAnyDocument(coreSession,
                "/default-domain/workspaces", "HiddenVisual", "Folder");

        Event event = ctx.newEvent("productNotSoldAnymore");
        ctx.setProperty("destination", hiddenVisualsDestination);

        // Dispatch the Event
        eventProducer.fireEvent(event);

        // Reload
        for (DocumentModel visual : wrapper.getVisuals()) {
            Assert.assertThat(String.format("Visual of Id %s must be in the Hidden folder", visual.getId()),
                    coreSession.getDocument(visual.getRef()).getParentRef(),
                    Matchers.equalTo(hiddenVisualsDestination.getRef()));
        }

    }
}

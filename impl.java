package org.ncsecu.aem.site.core.models.impl;

import com.adobe.acs.commons.genericlists.GenericList;
import com.adobe.acs.commons.genericlists.GenericList.Item;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.ncsecu.aem.site.core.models.NcsecuTrueCarMake;
import org.ncsecu.aem.site.core.models.NcsecuTrueCarSearch;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Model(
    adaptables = Resource.class,
    adapters = NcsecuTrueCarSearch.class,
    resourceType = "ncsecu/components/truecar-search",
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class NcsecuTrueCarSearchImpl implements NcsecuTrueCarSearch {

    @ValueMapValue
    private String makeListPath;

    @SlingObject
    private ResourceResolver resourceResolver;

    private List<NcsecuTrueCarMake> makes;

    @PostConstruct
    protected void init() {

        makes = new ArrayList<>();

        if (StringUtils.isBlank(makeListPath) || resourceResolver == null) {
            return;
        }

        Resource listResource = resourceResolver.getResource(makeListPath);

        if (listResource == null) {
            return;
        }

        GenericList genericList = listResource.adaptTo(GenericList.class);

        if (genericList == null) {
            return;
        }

        for (Item item : genericList.getItems()) {

            if (item == null) {
                continue;
            }

            String title = item.getTitle();
            String value = item.getValue();

            if (StringUtils.isNotBlank(title)
                    && StringUtils.isNotBlank(value)) {

                makes.add(new NcsecuTrueCarMake(title, value));
            }
        }
    }

    @Override
    public List<NcsecuTrueCarMake> getMakes() {
        return makes == null ? Collections.emptyList() : makes;
    }

    @Override
    public String getMakeListPath() {
        return makeListPath;
    }



@PostConstruct
protected void init() {
    makes = new ArrayList<>();

    if (StringUtils.isBlank(makeListPath) || resourceResolver == null) {
        return;
    }

    Resource listResource = resourceResolver.getResource(makeListPath + "/jcr:content/list");

    if (listResource == null) {
        return;
    }

    for (Resource itemResource : listResource.getChildren()) {
        String title = itemResource.getValueMap().get("jcr:title", String.class);
        String value = itemResource.getValueMap().get("value", String.class);

        if (StringUtils.isNotBlank(title) && StringUtils.isNotBlank(value)) {
            makes.add(new NcsecuTrueCarMake(title, value));
        }
    }
}
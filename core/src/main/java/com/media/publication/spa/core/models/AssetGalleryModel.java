package com.media.publication.spa.core.models;

import javax.annotation.PostConstruct;
import javax.jcr.RepositoryException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;

@Model(adaptables = SlingHttpServletRequest.class, resourceType = AssetGalleryModel.RESOURCE_TYPE, adapters = {
		AssetGalleryModel.class,
		ComponentExporter.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class AssetGalleryModel implements ComponentExporter {

	protected static final String RESOURCE_TYPE = "media_publication_spa/components/asset-gallery";

	@ValueMapValue
	@Default(values = "Asset Gallery")
	private String title;

	@PostConstruct
	protected void init() throws RepositoryException {

	}

	public String getTitle() {
		return title;
	}

	@Override
	public String getExportedType() {
		return RESOURCE_TYPE;
	}

}

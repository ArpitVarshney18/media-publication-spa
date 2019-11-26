package com.media.publication.spa.core.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.media.pubication.spa.core.services.SearchService;
import com.media.publication.spa.core.pojo.AssetGalleryData;
import com.media.publication.spa.core.utility.IAMUtil;

@Model(adaptables = SlingHttpServletRequest.class, resourceType = AssetGalleryModel.RESOURCE_TYPE, adapters = {
		AssetGalleryModel.class,
		ComponentExporter.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class AssetGalleryModel implements ComponentExporter {

	protected static final String RESOURCE_TYPE = "media_publication_spa/components/asset-gallery";
	private static final String NOT_SPECIFIED = "Not Specified";

	@ValueMapValue
	@Default(values = "Asset Gallery")
	private String title;
	
	@OSGiService
	private SearchService service;

	@SlingObject
	private ResourceResolver resourceResolver;

	@ScriptVariable
	private Page currentPage;
	
	@ValueMapValue
	@Default(values = "/content/dam/media-publication")
	private String assetGalleryDamPath;

	private List<AssetGalleryData> assetDataList;

	@PostConstruct
	protected void init() throws RepositoryException {
		Session session = resourceResolver.adaptTo(Session.class);
		SearchResult result = service.getResult(getSearchPredicateMap(assetGalleryDamPath), session);
		populateDataFromResult(result);
	}
	
	private void populateDataFromResult(SearchResult result) throws RepositoryException {
		assetDataList = new ArrayList<>();
		if (null != result && result.getTotalMatches() > 0) {
			for (final Hit hit : result.getHits()) {
				final Resource resource = resourceResolver.getResource(hit.getPath());
				if (null != resource) {
					final Asset asset = resource.adaptTo(Asset.class);
					if (null != asset) {
						AssetGalleryData data = new AssetGalleryData();
						data.setFileName(StringUtils.substringBefore(asset.getName(), "."));
						data.setThumnailPath(IAMUtil.getThumbNailPath(asset, "cq5dam.thumbnail.319.319.png"));
						data.setAssetType(getAssetType(asset));
						data.setAssetImage(IAMUtil.getThumbNailPath(asset, "cq5dam.web.1280.1280.jpeg"));
						data.setAssetDownloadLink(StringUtils.defaultIfBlank(asset.getPath(), NOT_SPECIFIED));
						data.setAssetOwnerName(getValue(asset, "photoshop:AuthorsPosition"));
						data.setAssetOwnerEmail(getValue(asset, "Iptc4xmpCore:CiEmailWork"));
						assetDataList.add(data);
					}
				}
			}
		}
	}
	
	private Map<String, String> getSearchPredicateMap(final String assetPath) {
		final Map<String, String> predicateMap = new LinkedHashMap<>();
		predicateMap.put("path", assetPath);
		predicateMap.put("type", "dam:Asset");
		predicateMap.put("path.flat", "true");
		return predicateMap;
	}
	
	private String getAssetType(final Asset asset) {
		if (null != asset.getMetadata("dc:format")) {
			String format = asset.getMetadata("dc:format").toString();
			if (format.contains("video"))
				return "Video";
			else if (format.contains("pdf"))
				return "PDF";
			else if (format.contains("image"))
				return "Image";
			else
				return NOT_SPECIFIED;
		}
		return NOT_SPECIFIED;

	}
	
	private String getValue(Asset asset, String property) {
		if (null != asset.getMetadata(property)) {
			return asset.getMetadata(property).toString();
		}
		return NOT_SPECIFIED;
	}

	public String getTitle() {
		return title;
	}
	
	public List<AssetGalleryData> getAssetDataList() {
		return assetDataList;
	}

	@Override
	public String getExportedType() {
		return RESOURCE_TYPE;
	}

}

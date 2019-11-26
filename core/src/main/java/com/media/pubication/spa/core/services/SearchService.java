package com.media.pubication.spa.core.services;

import java.util.Map;

import javax.jcr.Session;

import com.day.cq.search.result.SearchResult;

public interface SearchService {
	SearchResult getResult(Map<String, String> predicateMap, Session session);
}

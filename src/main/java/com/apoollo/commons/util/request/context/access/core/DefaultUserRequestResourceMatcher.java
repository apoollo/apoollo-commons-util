/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.AntPathMatcher;

import com.apoollo.commons.util.request.context.access.RequestResource;
import com.apoollo.commons.util.request.context.access.UserRequestResourceMatcher;

/**
 * @author liuyulong
 * @since 2025-06-12
 */
public class DefaultUserRequestResourceMatcher implements UserRequestResourceMatcher {

	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	@Override
	public boolean maches(UserMatchesRequestResourceCondition condition, RequestResource requestResource) {

		return !matches(condition.getExcludeRoles(), condition.getExcludeRequestResourcePins(),
				condition.getExcludeAntPathPatterns(), requestResource)//
				&& //
				matches(condition.getIncludeRoles(), condition.getIncludeRequestResourcePins(),
						condition.getIncludeAntPathPatterns(), requestResource);
	}

	private boolean matches(List<String> roles, List<String> requestResourcePins, List<String> antPathPatterns,
			RequestResource requestResource) {
		boolean matches = false;
		if (CollectionUtils.isNotEmpty(roles) && CollectionUtils.isNotEmpty(requestResource.getRoles())) {
			matches = roles.stream().filter(requestResource.getRoles()::contains).findAny().isPresent();
		}
		if (!matches) {
			if (CollectionUtils.isNotEmpty(requestResourcePins)) {
				matches = requestResourcePins.stream()
						.filter(resourcePin -> resourcePin.equals(requestResource.getResourcePin())).findAny()
						.isPresent();
			}
			if (!matches) {
				if (CollectionUtils.isNotEmpty(antPathPatterns)) {
					matches = antPathPatterns.stream()
							.filter(patter -> ANT_PATH_MATCHER.match(patter, requestResource.getRequestMappingPath()))
							.findAny().isPresent();
				}
			}
		}
		return matches;
	}

}

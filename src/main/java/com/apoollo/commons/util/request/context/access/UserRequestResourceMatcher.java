/**
 * 
 */
package com.apoollo.commons.util.request.context.access;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 * @since 2025-06-12
 */
public interface UserRequestResourceMatcher {

	public boolean maches(UserMatchesRequestResourceCondition condition , RequestResource requestResource);
	
	
	@Getter
	@Setter
	public static class UserMatchesRequestResourceCondition {
		
		private List<String> excludeRoles;
		private List<String> excludeAntPathPatterns;
		private List<String> excludeRequestResourcePins;

		private List<String> includeRoles;
		private List<String> includeAntPathPatterns;
		private List<String> includeRequestResourcePins;
	}
}

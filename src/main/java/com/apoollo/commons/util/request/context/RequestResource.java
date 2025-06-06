/**
 * 
 */
package com.apoollo.commons.util.request.context;

import java.util.List;

import com.apoollo.commons.util.request.context.core.AccessStrategy;

/**
 * @author liuyulong
 * @since 2023年9月25日
 */
public interface RequestResource extends CapacitySupport {

	public Boolean getEnable();

	public String getName();

	public String getRequestMappingPath();

	public AccessStrategy getAccessStrategy();

	public List<String> getRoles();
	
}

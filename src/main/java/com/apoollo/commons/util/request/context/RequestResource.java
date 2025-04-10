/**
 * 
 */
package com.apoollo.commons.util.request.context;

/**
 * @author liuyulong
 * @since 2023年9月25日
 */
public interface RequestResource {

	public Boolean getEnable();

	public String getResourcePin();

	public String getName();

	public String getRequestMappingPath();

	public String getAccessStrategy();

	public Long getLimtUserQps();

	public Long getLimtPlatformQps();

	public String[] getRoles();

	public Boolean getEnableSync();

}

/**
 * 
 */
package com.apoollo.commons.util.request.context.access;

/**
 * @author liuyulong
 * @since 2023年9月25日
 */
public interface RequestResourceManager {

    public static final String CACHE_NAME = "RequestResource";

    public RequestResource getRequestResource(String requestMappingPath);

    public void setRequestResource(RequestResource requestResource);

    public void deleteRequestResource(String requestMappingPath);
}

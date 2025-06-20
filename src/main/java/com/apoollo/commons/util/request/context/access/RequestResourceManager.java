/**
 * 
 */
package com.apoollo.commons.util.request.context.access;

import com.apoollo.commons.util.request.context.access.core.DefaultRequestResource.SerializableRequestResource;

/**
 * @author liuyulong
 * @since 2023年9月25日
 */
public interface RequestResourceManager {

    public RequestResource getRequestResource(String requestMappingPath);

    public void setRequestResource(SerializableRequestResource requestResource);

    public void deleteRequestResource(String requestMappingPath);
}

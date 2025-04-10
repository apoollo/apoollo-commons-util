/**
 * 
 */
package com.apoollo.commons.util.request.context;

/**
 * @author liuyulong
 * @since 2023年8月22日
 */
public interface RequestContextDataBus {

	public void transport(RequestContext requestContext);

}

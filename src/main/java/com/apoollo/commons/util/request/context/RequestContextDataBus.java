/**
 * 
 */
package com.apoollo.commons.util.request.context;

import org.springframework.core.Ordered;

/**
 * @author liuyulong
 * @since 2023年8月22日
 */
public interface RequestContextDataBus extends Ordered {

	public void transport(RequestContext requestContext);

}

/**
 * 
 */
package com.apoollo.commons.util.request.context.access;

import com.apoollo.commons.util.JwtUtils.JwtToken;

/**
 * @author liuyulong
 */
public interface AuthorizationJwtTokenDecoder {

    public JwtToken decode(String authorizationJwtToken);

}

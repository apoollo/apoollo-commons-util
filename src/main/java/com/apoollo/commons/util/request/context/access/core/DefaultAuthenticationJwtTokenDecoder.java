/**
 * 
 */
package com.apoollo.commons.util.request.context.access.core;

import org.apache.commons.lang3.StringUtils;

import com.apoollo.commons.util.JwtUtils;
import com.apoollo.commons.util.JwtUtils.JwtToken;
import com.apoollo.commons.util.exception.AppForbbidenException;
import com.apoollo.commons.util.request.context.access.AuthorizationJwtTokenDecoder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @author liuyulong
 */
public class DefaultAuthenticationJwtTokenDecoder implements AuthorizationJwtTokenDecoder {
    

    @Override
    public JwtToken decode(String authorizationJwtToken) {
        if (StringUtils.isBlank(authorizationJwtToken)) {
            throw new AppForbbidenException("authorizationJwtToken must not be blank");
        }

        String jwtToken = JwtUtils.extractJwtToken(authorizationJwtToken);
        if (StringUtils.isBlank(jwtToken)) {
            throw new AppForbbidenException("authorizationJwtToken illegal");
        }

        DecodedJWT decodedJwtToken = null;
        try {
            decodedJwtToken = JWT.decode(jwtToken);
        } catch (JWTDecodeException e) {
            throw new AppForbbidenException("jwtToken illegal");
        }

        return new JwtToken(jwtToken, decodedJwtToken);
    }

}

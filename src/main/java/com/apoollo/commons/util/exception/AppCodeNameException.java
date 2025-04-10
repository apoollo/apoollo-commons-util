/**
 * 
 */
package com.apoollo.commons.util.exception;

import com.apoollo.commons.util.LangUtils;
import com.apoollo.commons.util.request.context.CodeName;

/**
 * @author liuyulong
 */
public class AppCodeNameException extends AppException {

    /**
     * 
     */
    private static final long serialVersionUID = -1218749175108957511L;

    private CodeName<String, String> codeName;
    private String message;

    /**
     * @param codeName
     * @param message
     */
    public AppCodeNameException(CodeName<String, String> codeName, String message) {
        super("AppCodeNameException Error: ");
        this.codeName = codeName;
        this.message = message;
    }

    public String getDefaultMessage() {
        return LangUtils.join(LangUtils.getStream(codeName.getName(), message), ":", "", "");
    }

    public CodeName<String, String> getCodeName() {
        return codeName;
    }

    public String getMessage() {
        return message;
    }

}

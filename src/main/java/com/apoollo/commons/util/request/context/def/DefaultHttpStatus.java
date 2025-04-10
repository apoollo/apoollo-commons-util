/**
 * 
 */
package com.apoollo.commons.util.request.context.def;

import com.apoollo.commons.util.request.context.CodeName;
import com.apoollo.commons.util.request.context.HttpCodeName;

import lombok.Getter;

/**
 * @author liuyulong
 */
@Getter
public enum DefaultHttpStatus implements HttpCodeName<String, String> {

    OK(200, "Ok", "通过"), //
    FORBIDDEN(200, "Forbidden", "访问无权限"), //
    NO_HANDLER_FOUND(404, "NoHandlerFound", "没有找到处理资源"), //
    ILLEGAL_ARGUMENT(200, "IllegalArgument", "非法入参"), //
    REQUEST_TIMOUT_LIMIT(200, "RequestTimeoutLimit", "请求超时限制"), //
    EXCEEDING_DAILY_MAXIMUM_USE_TIMES_LIMIT(200, "ExceedingDailyMaximumUseTimesLimit", "超过每日最大使用次数限制"), //
    SERVER_OVERLOADED(200, "ServerOverloaded", "服务超过负载"), //
    MaxUploadSizeExceceded(200, "MaxUploadSizeExceeded", "超过最大上传大小"), //
    SERVER_ERROR(200, "ServerError", "服务异常") //
    ;

    private Integer httpCode;
    private String code;
    private String name;

    private DefaultHttpStatus(Integer httpCode, String code, String name) {
        this.httpCode = httpCode;
        this.code = code;
        this.name = name;
    }

    public static boolean is(String code, DefaultHttpStatus instance) {
        return CodeName.is(code, instance);
    }

    public static boolean contains(String code) {
        return CodeName.contains(code, DefaultHttpStatus.values());
    }

}

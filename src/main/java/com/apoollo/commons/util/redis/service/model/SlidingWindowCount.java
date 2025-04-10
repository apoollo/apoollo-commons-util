/**
 * 
 */
package com.apoollo.commons.util.redis.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liuyulong
 * @since 2024-11-20
 */
@Getter
@Setter
@AllArgsConstructor
public class SlidingWindowCount {

	private Long window;
	private Long count;
}

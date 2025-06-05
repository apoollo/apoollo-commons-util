local redisCountKey = KEYS[1]
local expireAt  = tonumber(ARGV[1])
local limitCount  = tonumber(ARGV[2])


local currentCount = redis.call("INCR", redisCountKey)    		-- 增加键的计数器并返回当前计数值

if currentCount == 1 then
    redis.call("EXPIREAT", redisCountKey, expireAt)    			-- 在第一次调用时设置键的过期时间
end

local result = nil
if currentCount > limitCount then
    redis.call("DECR", redisCountKey) 							-- 调用次数回退
    result = { accessed = false, currentCount = currentCount }	-- 超过调用次数限制
else
    result = { accessed = true, currentCount = currentCount }   -- 在调用次数限制范围内
end

return cjson.encode(result)
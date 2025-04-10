local redisCountKey = KEYS[1]
local expireAt = tonumber(ARGV[1])
local currentCount = redis.call("INCR", redisCountKey) -- 增加键的计数器并返回当前计数值

if currentCount == 1 then
    redis.call("EXPIREAT", redisCountKey, expireAt) -- 在第一次调用时设置键的过期时间
end

return currentCount
local key = KEYS[1]
local currentTimeMillis  = tonumber(ARGV[1])                                    -- 时间戳
local windowTimeMillis  = tonumber(ARGV[2])                                     -- 每个时间窗口毫秒数，取值 100 ，200， 500，1000

local windowStart, _ = math.modf(currentTimeMillis / 1000)                      -- 第一个窗口

windowStart = windowStart * 1000 

local appendDuration = currentTimeMillis - windowStart                          -- 毫秒内发生时长
local windowOfNumber, _ = math.modf(appendDuration / windowTimeMillis)          -- 所属窗口个数
local window = windowStart + windowTimeMillis * windowOfNumber                  -- 窗口

-- 清理掉旧窗口
local allMembers = redis.call("ZRANGE", key, 0, -1)
for _, member in ipairs(allMembers) do
    if( tonumber(member) < window ) then
        redis.call("ZREM", key, member)
    end
end

-- 增加新窗口，并设置过期时间
local windowCount = redis.call('ZINCRBY', key, '1', window)
redis.call("EXPIRE", key, '1')

return {window, windowCount}


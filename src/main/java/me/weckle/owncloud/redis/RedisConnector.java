package me.weckle.owncloud.redis;

import me.weckle.owncloud.redis.pub.RedisPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisConnector {

    private JedisPool jedisPool;
    private RedisPubSub redisPubSub;

    public RedisConnector(JedisPool jedisPool, RedisPubSub redisPubSub) {
        this.jedisPool = jedisPool;
        this.redisPubSub = redisPubSub;
        setupSubscriber();
    }

    private final void setupSubscriber() {
        new Thread(() -> {
            for(;;) {
                try(Jedis jedis = jedisPool.getResource()) {
                    jedis.subscribe(redisPubSub, "cloud");
                } catch (Exception ex) {
                    redisPubSub.unsubscribe();
                }
            }
        }).start();
    }

    public JedisPool getJedisPool() {
        return jedisPool;
    }

    public void sendMessage(String message) {
        try(Jedis jedis = jedisPool.getResource()) {
            jedis.publish("cloud", message);
        }
    }

}

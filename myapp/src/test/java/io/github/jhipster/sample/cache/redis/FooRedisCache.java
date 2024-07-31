package io.github.jhipster.sample.cache.redis;

import jakarta.inject.Singleton;

@Singleton
public class FooRedisCache extends RedisCache<Foo> {

    public FooRedisCache() {
        super("Foo:", Foo.class);
    }
}

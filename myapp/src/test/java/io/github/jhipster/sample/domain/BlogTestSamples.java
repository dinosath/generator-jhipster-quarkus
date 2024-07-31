package io.github.jhipster.sample.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BlogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Blog getBlogSample1() {
        Blog blog = new Blog();
        blog.id = 1L;
        blog.name = "name1";
        blog.handle = "handle1";
        return blog;
    }

    public static Blog getBlogSample2() {
        Blog blog = new Blog();
        blog.id = 2L;
        blog.name = "name2";
        blog.handle = "handle2";
        return blog;
    }

    public static Blog getBlogRandomSampleGenerator() {
        Blog blog = new Blog();
        blog.id = longCount.incrementAndGet();
        blog.name = UUID.randomUUID().toString();
        blog.handle = UUID.randomUUID().toString();
        return blog;
    }
}

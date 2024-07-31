package io.github.jhipster.sample.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PostTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Post getPostSample1() {
        Post post = new Post();
        post.id = 1L;
        post.title = "title1";
        return post;
    }

    public static Post getPostSample2() {
        Post post = new Post();
        post.id = 2L;
        post.title = "title2";
        return post;
    }

    public static Post getPostRandomSampleGenerator() {
        Post post = new Post();
        post.id = longCount.incrementAndGet();
        post.title = UUID.randomUUID().toString();
        return post;
    }
}

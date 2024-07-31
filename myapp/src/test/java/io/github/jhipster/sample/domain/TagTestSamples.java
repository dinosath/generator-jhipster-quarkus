package io.github.jhipster.sample.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TagTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Tag getTagSample1() {
        Tag tag = new Tag();
        tag.id = 1L;
        tag.name = "name1";
        return tag;
    }

    public static Tag getTagSample2() {
        Tag tag = new Tag();
        tag.id = 2L;
        tag.name = "name2";
        return tag;
    }

    public static Tag getTagRandomSampleGenerator() {
        Tag tag = new Tag();
        tag.id = longCount.incrementAndGet();
        tag.name = UUID.randomUUID().toString();
        return tag;
    }
}

package io.github.jhipster.sample.domain;

import static io.github.jhipster.sample.domain.BlogTestSamples.*;
import static io.github.jhipster.sample.domain.PostTestSamples.*;
import static io.github.jhipster.sample.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.jhipster.sample.TestUtil;
import org.junit.jupiter.api.Test;

class PostTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Post.class);
        Post post1 = getPostSample1();
        Post post2 = new Post();
        assertThat(post1).isNotEqualTo(post2);

        post2.id = post1.id;
        assertThat(post1).isEqualTo(post2);

        post2 = getPostSample2();
        assertThat(post1).isNotEqualTo(post2);
    }
}
